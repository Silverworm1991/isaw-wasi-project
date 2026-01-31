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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/items")
public class ItemController {

  private final ItemService itemService;

  @Autowired
  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping
  public List<Item> getAllItems(@RequestParam(required = false) String username) {
    if (username != null && !username.isEmpty()) {
      return itemService.findByUsername(username);
    }
    return itemService.findAll();
  }

  @PostMapping
  public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
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
    return itemService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateItem(@PathVariable Long id, @Valid @RequestBody Item updatedItem) {
    Optional<Item> optionalItem = itemService.findById(id);

    if (optionalItem.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
    }

    Item existingItem = optionalItem.get();

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
    boolean deleted = itemService.deleteById(id);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
