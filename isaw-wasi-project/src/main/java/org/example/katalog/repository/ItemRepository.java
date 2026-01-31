package org.example.katalog.repository;

import java.util.List;
import org.example.katalog.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

  List<Item> findByUsername(String username);
}
