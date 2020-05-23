package ru.hse.coursework.berth.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.Review;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByBerth(Berth berth, Pageable pageable);

    @Nullable
    @Query("select avg(r.rating) from Review r where r.berth = ?1 and r.createdAt between ?1 and ?2")
    Double calcRating(Berth berth, LocalDateTime from, LocalDateTime to);
}
