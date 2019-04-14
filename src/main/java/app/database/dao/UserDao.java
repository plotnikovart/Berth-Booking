package app.database.dao;

import app.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    @Transactional
    Optional<User> findByEmail(String email);
}
