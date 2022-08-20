package tech.razymov.catharsis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.razymov.catharsis.entity.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
}