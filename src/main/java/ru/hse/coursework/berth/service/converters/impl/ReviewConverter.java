package ru.hse.coursework.berth.service.converters.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.Review;
import ru.hse.coursework.berth.service.converters.AbstractConverter;
import ru.hse.coursework.berth.service.review.dto.ReviewDto;
import ru.hse.coursework.berth.service.review.dto.ReviewerDto;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewConverter extends AbstractConverter<Review, ReviewDto.Resp, ReviewDto> {

    @Override
    public ReviewDto.Resp toDto(ReviewDto.Resp dto, Review e) {
        ReviewerDto reviewerDto = Optional.ofNullable(dto.getReviewer()).orElseGet(ReviewerDto::new)
                .setAccountId(e.getReviewer().getId());

        return (ReviewDto.Resp) dto
                .setId(e.getId())
                .setReviewer(reviewerDto)
                .setDateTime(e.getCreatedAt().toLocalDate())
                .setRating(e.getRating())
                .setText(e.getText());
    }

    @Override
    public Review toEntity(Review entity, ReviewDto dto) {
        return entity
                .setRating(dto.getRating())
                .setText(dto.getText());
    }
}
