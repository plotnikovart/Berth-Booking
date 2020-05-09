package ru.hse.coursework.berth.database.entity;

import com.google.api.client.repackaged.com.google.common.base.Splitter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.Spatial;
import ru.hse.coursework.berth.common.EntityWithOwner;
import ru.hse.coursework.berth.config.DBConfig;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.PERSIST;

@Getter
@Setter
@Indexed
@Spatial(name = Berth.SPATIAL_FIELD)
@Entity
@Where(clause = DBConfig.NOT_DELETED)
public class Berth extends AuditEntity implements EntityWithOwner {

    public static final String SPATIAL_FIELD = "location";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Account owner;

    @Column(nullable = false)
    private String name;

    private String description;

    @Latitude(of = Berth.SPATIAL_FIELD)
    @Column(nullable = false)
    private Double lat;

    @Longitude(of = Berth.SPATIAL_FIELD)
    @Column(nullable = false)
    private Double lng;

    private String site;
    private String radio;
    private String phCode;
    private String phNumber;

    private String photos;

    @Column(nullable = false)
    private Boolean isConfirmed;

    @OneToMany(mappedBy = "berth", cascade = {PERSIST, DETACH}, orphanRemoval = true)
    @OrderBy("id")
    private List<BerthPlace> berthPlaces = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "berth_to_amenity",
            joinColumns = @JoinColumn(name = "berth_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_key"))
    private Set<DictAmenity> amenities = new HashSet<>();

//    @Formula("(select avg(r.rating) from review as r WHERE r.berth_id = id)")   // todo lazy loading
//    private Integer rating;


    public Berth setBerthPlaces(Collection<BerthPlace> newPlaces) {
        getBerthPlaces().clear();
        getBerthPlaces().addAll(newPlaces);
        return this;
    }

    public Berth setAmenities(Collection<DictAmenity> newAmenities) {
        getAmenities().clear();
        getAmenities().addAll(newAmenities);
        return this;
    }

    public Berth setPhotos(List<UUID> photos) {
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
