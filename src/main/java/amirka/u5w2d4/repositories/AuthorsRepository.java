package amirka.u5w2d4.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthorsRepository extends JpaRepository<amirka.u5w2d4.entities.Author, UUID> {
    boolean existsByEmail(String email);
}
