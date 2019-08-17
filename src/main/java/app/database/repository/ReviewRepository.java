package app.database.repository;

import app.database.entity.Berth;
import app.database.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByBerth(Berth berth);
}
