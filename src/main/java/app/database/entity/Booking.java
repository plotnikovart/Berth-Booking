package app.database.entity;

import app.database.entity.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserInfo owner;

    @ManyToOne
    @JoinColumn(name = "renter_id")
    private UserInfo renter;

    @ManyToOne
    @JoinColumn(name = "berth_place_id")
    private BerthPlace berthPlace;

    @ManyToOne
    @JoinColumn(name = "ship_id")
    private Ship ship;

    private LocalDate startDate;

    private LocalDate endDate;

    @Convert(converter = BookingStatus.Converter.class)
    private BookingStatus status;
}
