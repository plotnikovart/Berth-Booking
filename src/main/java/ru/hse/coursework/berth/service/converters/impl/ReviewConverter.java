package ru.hse.coursework.berth.service.converters.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.Review;
import ru.hse.coursework.berth.database.repository.ReviewRepository;
import ru.hse.coursework.berth.service.converters.AbstractConverter;
import ru.hse.coursework.berth.web.dto.ReviewDto;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewConverter extends AbstractConverter<Review, ReviewDto.Resp, ReviewDto.Req> {

    private final UserInfoConverter userInfoConverter;
    private final EntityManager em;
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewDto.Resp toDto(ReviewDto.Resp dto, Review e) {
        var userInfo = userInfoConverter.toDto(e.getUserInfo())
                .setAccountId(null)
//                .setEmail(null)
                .setPhNumber(null)
                .setPhCode(null);

        return (ReviewDto.Resp) dto
                .setId(e.getId())
                .setUserInfo(userInfo)
                .setDateTime(e.getDateTime())
                .setRating(e.getRating())
                .setText(e.getText());
    }

    @Override
    public Review toEntity(Review entity, ReviewDto.Req dto) {
        Berth berth = em.getReference(Berth.class, dto.getBerthId());

        return entity
                .setBerth(berth)
                .setRating(dto.getRating())
                .setText(dto.getText());
    }

    @Override
    public List<ReviewDto.Resp> toDtos(Collection<Review> entities) {
        if (!entities.isEmpty()) {
            reviewRepository.loadUserInfo(entities);
        }
        return super.toDtos(entities);
    }
}
