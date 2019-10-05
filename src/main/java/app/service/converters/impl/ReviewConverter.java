package app.service.converters.impl;

import app.database.entity.Berth;
import app.database.entity.Review;
import app.service.converters.AbstractConverter;
import app.web.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class ReviewConverter extends AbstractConverter<Review, ReviewDto.Resp, ReviewDto.Req> {

    private final UserInfoConverter userInfoConverter;
    private final EntityManager em;

    @Override
    public ReviewDto.Resp toDto(ReviewDto.Resp dto, Review e) {
        var userInfo = userInfoConverter.toDto(e.getUserInfo())
                .setAccountId(null)
                .setEmail(null)
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
}
