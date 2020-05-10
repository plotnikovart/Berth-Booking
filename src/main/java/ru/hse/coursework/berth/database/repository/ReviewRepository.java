package ru.hse.coursework.berth.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByBerth(Berth berth, Pageable pageable);
}
