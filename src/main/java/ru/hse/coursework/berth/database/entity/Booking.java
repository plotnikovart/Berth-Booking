package ru.hse.coursework.berth.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import ru.hse.coursework.berth.common.fsm.StateField;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;

import javax.persistence.*;
import java.time.LocalDate;

import static ru.hse.coursework.berth.config.DBConfig.NOT_DELETED;

@Getter
@Setter
@Entity
@Where(clause = NOT_DELETED)
public class Booking extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", nullable = false)
    private Account renter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "berth_place_id", nullable = false)
    private BerthPlace berthPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id", nullable = false)
    private Ship ship;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @StateField
    @Column(nullable = false)
    @Convert(converter = BookingStatus.Converter.class)
    private BookingStatus status = BookingStatus.NEW;

    @Column(nullable = false)
    private Double totalPrice;

    @Deprecated // use state machine
    public Booking setStatus(BookingStatus status) {
        this.status = status;
        return this;
    }
}
