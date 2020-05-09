package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.Review;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByBerth(Berth berth);

    @Query("select r from Review r where r in (?1)")
    @EntityGraph(attributePaths = "userInfo.account")
    List<Review> loadUserInfo(Collection<Review> reviews);
}
