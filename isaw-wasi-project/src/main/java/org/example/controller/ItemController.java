package org.example.controller;

import org.example.model.Item;
import org.example.model.Movie;
import org.example.model.Series;
import org.example.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item saved = itemService.save(item);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        boolean deleted = itemService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /*
    Early Update method before the need to updade the subclasses properties

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item updatedItem) {
        return itemService.findById(id)
                .map(item -> {
                    item.setTitle(updatedItem.getTitle());
                    item.setYear(updatedItem.getYear());
                    Item savedItem = itemService.save(item);
                    return ResponseEntity.ok(savedItem);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    */

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody Item updatedItem) {
        Optional<Item> optionalItem = itemService.findById(id);

        if (optionalItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        }

        Item existingItem = optionalItem.get();

        //Update common fields
        existingItem.setTitle(updatedItem.getTitle());
        existingItem.setYear(updatedItem.getYear());
        existingItem.setGenre(updatedItem.getGenre());

        //Update subclasses fields
        if (existingItem instanceof Movie && updatedItem instanceof Movie) {
            Movie existingMovie = (Movie) existingItem;
            Movie updatedMovie = (Movie) updatedItem;

            existingMovie.setDirector(updatedMovie.getDirector());
            existingMovie.setDuration(updatedMovie.getDuration());

            itemService.save(existingMovie);
            return ResponseEntity.ok("Movie updated successfully");

        } else if (existingItem instanceof Series && updatedItem instanceof Series) {
            Series existingSeries = (Series) existingItem;
            Series updatedSeries = (Series) updatedItem;

            existingSeries.setSeasons(updatedSeries.getSeasons());
            existingSeries.setEpisodes(updatedSeries.getEpisodes());

            itemService.save(existingSeries);
            return ResponseEntity.ok("Series updated successfully");

        } else {
            //Types don't match, cannot update (ex: updating Movie with Series fields, vice versa
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Type mismatch: cannot update item with different subclass type.");
        }


    }
}
