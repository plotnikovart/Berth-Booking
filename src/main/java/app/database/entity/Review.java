package app.database.entity;

import app.common.EntityWithOwner;
import app.web.dto.ReviewDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Review implements EntityWithOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "berth_id")
    private Berth berth;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private UserInfo userInfo;

    private Integer rating;

    private String text;

    @CreationTimestamp
    private LocalDateTime dateTime;

    public Review(Berth berth, UserInfo userInfo, ReviewDto reviewDto) {
        this.berth = berth;
        this.userInfo = userInfo;
        setDto(reviewDto);
    }

    public Review setDto(ReviewDto dto) {
        setRating(dto.getRating());
        setText(dto.getText());
        return this;
    }

    public ReviewDto.WithId getDto() {
        var userInfo = getUserInfo().getDto()
                .setAccountId(null)
                .setPhCode(null)
                .setPhNumber(null)
                .setEmail(null);

        return (ReviewDto.WithId) new ReviewDto.WithId()
                .setId(getId())
                .setUserInfo(userInfo)
                .setRating(getRating())
                .setText(getText())
                .setDateTime(getDateTime());
    }

    @Override
    public Long getOwnerId() {
        return getUserInfo().getAccountId();
    }
}
