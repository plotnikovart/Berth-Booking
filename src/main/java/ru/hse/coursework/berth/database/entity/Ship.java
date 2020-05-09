package ru.hse.coursework.berth.database.entity;

import com.google.api.client.repackaged.com.google.common.base.Splitter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import ru.hse.coursework.berth.common.EntityWithOwner;
import ru.hse.coursework.berth.database.entity.enums.ShipType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Setter
@Getter
@Entity
public class Ship extends AuditEntity implements EntityWithOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Account owner;

    @Column(nullable = false)
    private String name;

    @Convert(converter = ShipType.Converter.class)
    private ShipType type;

    @Column(nullable = false)
    private Double length;

    @Column(nullable = false)
    private Double draft;

    @Column(nullable = false)
    private Double width;

    private String producer;

    private String model;

    private Integer year;

    private String registrationNumber;

    private LocalDate registrationExpire;

    private UUID registrationFile;

    private String insuranceCompany;

    private String insuranceNumber;

    private LocalDate insuranceExpire;

    private UUID insuranceFile;

    private String photos;

    public Ship setPhotos(List<UUID> photos) {
        this.photos = photos == null ? null : photos.stream().map(UUID::toString).collect(Collectors.joining());
        return this;
    }

    public List<UUID> getPhotos() {
        if (StringUtils.isBlank(photos)) {
            return List.of();
        }
        Iterable<String> split = Splitter.fixedLength(36).split(this.photos);
        return StreamSupport.stream(split.spliterator(), false)
                .map(UUID::fromString)
                .collect(Collectors.toList());
    }

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }
}
