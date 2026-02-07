package org.example.katalog.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.example.katalog.model.Item;
import org.example.katalog.model.Movie;
import org.example.katalog.model.Series;
import org.example.katalog.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/items")
public class ItemResource {

  private final ItemService itemService;

  @Autowired
  public ItemResource(ItemService itemService) {
    this.itemService = itemService;
  }

  private String getAuthenticatedUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName();
  }

  @GetMapping
  public List<Item> getAllItems(@RequestParam(required = false) String username) {
    String currentUser = getAuthenticatedUsername();

    // If username parameter is provided, check authorization
    if (username != null && !username.isEmpty()) {
      // Users can only see their own items (or implement admin role check here)
      if (!username.equals(currentUser)) {
        throw new RuntimeException("You can only view your own items");
      }
      return itemService.findByUsername(username);
    }

    // Return only current user's items by default
    return itemService.findByUsername(currentUser);
  }

  @PostMapping
  public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
    // Set username from authenticated user (ignore any username in request body)
    String authenticatedUsername = getAuthenticatedUsername();
    item.setUsername(authenticatedUsername);

    Item saved = itemService.save(item);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(saved.getId())
            .toUri();
    return ResponseEntity.created(location).body(saved);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Item> getItemById(@PathVariable Long id) {
    String currentUser = getAuthenticatedUsername();

    return itemService
        .findById(id)
        .map(
            item -> {
              // Verify user owns this item
              if (!item.getUsername().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<Item>build();
              }
              return ResponseEntity.ok(item);
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateItem(@PathVariable Long id, @Valid @RequestBody Item updatedItem) {
    String currentUser = getAuthenticatedUsername();
    Optional<Item> optionalItem = itemService.findById(id);

    if (optionalItem.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
    }

    Item existingItem = optionalItem.get();

    // Verify user owns this item
    if (!existingItem.getUsername().equals(currentUser)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own items");
    }

    // Type check FIRST — before mutating anything
    if (!existingItem.getClass().equals(updatedItem.getClass())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              "Type mismatch: cannot update "
                  + existingItem.getClass().getSimpleName()
                  + " with "
                  + updatedItem.getClass().getSimpleName()
                  + " data.");
    }

    // Update common fields
    existingItem.setTitle(updatedItem.getTitle());
    existingItem.setYear(updatedItem.getYear());
    existingItem.setGenre(updatedItem.getGenre());
    // Username should NOT be updated - keep original owner

    // Update subclass-specific fields
    if (existingItem instanceof Movie existingMovie && updatedItem instanceof Movie updatedMovie) {
      existingMovie.setDirector(updatedMovie.getDirector());
      existingMovie.setDuration(updatedMovie.getDuration());
      itemService.save(existingMovie);
      return ResponseEntity.ok("Movie updated successfully");

    } else if (existingItem instanceof Series existingSeries
        && updatedItem instanceof Series updatedSeries) {
      existingSeries.setSeasons(updatedSeries.getSeasons());
      existingSeries.setEpisodes(updatedSeries.getEpisodes());
      itemService.save(existingSeries);
      return ResponseEntity.ok("Series updated successfully");
    }

    // Shouldn't reach here, but just in case
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Unexpected error during update");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
    String currentUser = getAuthenticatedUsername();

    Optional<Item> item = itemService.findById(id);
    if (item.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    // Verify user owns this item
    if (!item.get().getUsername().equals(currentUser)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    boolean deleted = itemService.deleteById(id);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
