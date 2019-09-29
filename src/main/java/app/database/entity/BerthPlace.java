package app.database.entity;

import app.common.EntityWithOwner;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class BerthPlace implements EntityWithOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "berth_id")
    private Berth berth;

    private Double length;

    private Double draft;

    private Double width;

    private Double price;

    @OneToMany(mappedBy = "berthPlace", cascade = CascadeType.DETACH)
    private List<Booking> bookingList;

    public Double getFactPrice() {
        return getPrice() == null ? getBerth().getStandardPrice() : getPrice();
    }

    @Override
    public Long getOwnerId() {
        return berth.getUserInfo().getAccountId();
    }
}
