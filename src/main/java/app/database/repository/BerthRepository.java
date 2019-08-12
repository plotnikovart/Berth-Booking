package app.database.repository;

import app.database.entity.Berth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BerthRepository extends JpaRepository<Berth, Long> {
}
