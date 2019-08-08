package app.database.repository;

import app.database.entity.Berth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BerthRepository extends JpaRepository<Berth, Long> {
}
