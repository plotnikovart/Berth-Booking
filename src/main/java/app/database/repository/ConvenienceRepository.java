package app.database.repository;

import app.database.entity.Convenience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConvenienceRepository extends JpaRepository<Convenience, Integer> {

    @Query("select c from Convenience c where c.id in (?1)")
    List<Convenience> findAllByIds(List<Integer> ids);
}
