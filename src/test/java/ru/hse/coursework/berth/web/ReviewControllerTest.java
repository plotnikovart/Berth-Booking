package ru.hse.coursework.berth.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.repository.AbstractAccountTest;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.ReviewRepository;
import ru.hse.coursework.berth.service.dto.ListCount;
import ru.hse.coursework.berth.service.review.dto.ReviewDto;
import ru.hse.coursework.berth.service.review.dto.ReviewFilter;
import ru.hse.coursework.berth.service.review.dto.ReviewerDto;

import java.time.LocalDate;

public class ReviewControllerTest extends AbstractAccountTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BerthRepository berthRepository;
    @Autowired
    private BerthController berthController;

    private Berth berth;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        reviewRepository.deleteAll();
        berthRepository.deleteAll();

        OperationContext.accountId(user1Account.getId());
        berth = new Berth()
                .setName("berth")
                .setOwner(user1Account)
                .setLat(1.0)
                .setLng(1.0)
                .setIsConfirmed(true);
        berthRepository.save(berth);
    }

    @Test
    void test() throws InterruptedException {
        OperationContext.accountId(user1Account.getId());

        var reviewDto = new ReviewDto()
                .setRating(50)
                .setText("adasd");

        // CREATE
        ReviewDto.Resp actual = berthController.publishReview(berth.getId(), reviewDto).getData();

        var reviewerDto = new ReviewerDto()
                .setAccountId(user1Info.getId())
                .setFirstName(user1Info.getFirstName())
                .setLastName(user1Info.getLastName())
                .setPhotoLink(user1Info.getPhotoLink());    // todo

        ReviewDto.Resp expected = (ReviewDto.Resp) new ReviewDto.Resp()
                .setId(actual.getId())
                .setDateTime(LocalDate.now())
                .setReviewer(reviewerDto)
                .setRating(reviewDto.getRating())
                .setText(reviewDto.getText());

        Assertions.assertEquals(expected, actual);

        // CREATE 2, 3
        var reviewDto2 = new ReviewDto().setRating(0);
        var reviewDto3 = new ReviewDto().setRating(100);
        ReviewDto.Resp actual2 = berthController.publishReview(berth.getId(), reviewDto2).getData();
        ReviewDto.Resp actual3 = berthController.publishReview(berth.getId(), reviewDto3).getData();

        // GET ALL
        var filter = (ReviewFilter) new ReviewFilter().setPageNum(0).setPageSize(2);

        ListCount<ReviewDto.Resp> allActual = berthController.getReviews(berth.getId(), filter).getData();
        Assertions.assertEquals(2, allActual.getItems().size());
        Assertions.assertEquals(3, allActual.getTotalCount());
        Assertions.assertEquals(actual3.getId(), allActual.getItems().get(0).getId());
        Assertions.assertEquals(actual2.getId(), allActual.getItems().get(1).getId());

        // DELETE
        berthController.deleteReview(berth.getId(), actual3.getId());
        allActual = berthController.getReviews(berth.getId(), filter).getData();
        Assertions.assertEquals(2, allActual.getItems().size());
        Assertions.assertEquals(2, allActual.getTotalCount());
        Assertions.assertEquals(actual2.getId(), allActual.getItems().get(0).getId());
        Assertions.assertEquals(actual.getId(), allActual.getItems().get(1).getId());


        Thread.sleep(100);
        berth = berthRepository.findById(berth.getId()).orElseThrow();
        Assertions.assertEquals((actual.getRating() + actual2.getRating()) / 2.0, berth.getAvgRating(), 0.1);
    }
}
