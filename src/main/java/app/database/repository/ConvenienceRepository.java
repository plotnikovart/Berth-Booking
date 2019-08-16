package app.database.repository;

import app.database.entity.Convenience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvenienceRepository extends JpaRepository<Convenience, Integer> {
}
