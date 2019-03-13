package app.database.dao;

import app.database.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDao extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
