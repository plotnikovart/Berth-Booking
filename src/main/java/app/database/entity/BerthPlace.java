package app.database.entity;

import app.common.EntityWithOwner;
import app.web.dto.BerthPlaceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class BerthPlace implements EntityWithOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "berth_id")
    private Berth berth;

    private Double length;

    private Double draft;

    private Double width;

    private Double price;

    public BerthPlace(Berth berth, BerthPlaceDto dto) {
        this.berth = berth;
        setDto(dto);
    }

    public void setDto(BerthPlaceDto dto) {
        length = dto.getLength();
        draft = dto.getDraft();
        width = dto.getWidth();
        price = dto.getPrice();
    }

    public BerthPlaceDto.WithId getDto() {
        return (BerthPlaceDto.WithId) new BerthPlaceDto.WithId()
                .setId(id)
                .setLength(length)
                .setDraft(draft)
                .setWidth(width)
                .setPrice(price);
    }


    @Override
    public Long getOwnerId() {
        return berth.getUserInfo().getAccountId();
    }
}
