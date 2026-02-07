package org.example.katalog.repository;

import java.util.Optional;
import org.example.katalog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  boolean existByUsername(String username);

  boolean existsByEmail(String email);
}
