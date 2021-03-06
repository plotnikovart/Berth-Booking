package ru.hse.coursework.berth.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import ru.hse.coursework.berth.common.EntityWithOwner;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static ru.hse.coursework.berth.config.DBConfig.NOT_DELETED;

@Getter
@Setter
@Entity
@Where(clause = NOT_DELETED)
public class BerthPlace extends AuditEntity implements EntityWithOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "berth_id", nullable = false)
    private Berth berth;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double length;
    @Column(nullable = false)
    private Double draft;
    @Column(nullable = false)
    private Double width;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double xCoord;
    @Column(nullable = false)
    private Double yCoord;
    @Column(nullable = false)
    private Double rotate;
    @Column(nullable = false)
    private String color;

    @OneToMany(mappedBy = "berthPlace", cascade = CascadeType.DETACH)
    private List<Booking> bookingList = new ArrayList<>();

    @Override
    public Long getOwnerId() {
        return berth.getOwnerId();
    }
}
