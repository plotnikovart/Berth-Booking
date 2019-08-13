package app.database.entity;

import app.common.EntityWithOwner;
import app.web.dto.BerthDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.Spatial;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.*;

@NamedEntityGraph(name = "berth_eg", attributeNodes = @NamedAttributeNode("berthPlaces"))
@Getter
@Setter
@Indexed
@Spatial(name = Berth.SPATIAL_FIELD)
@Entity
@NoArgsConstructor
public class Berth implements EntityWithOwner {

    public static final String SPATIAL_FIELD = "location";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private UserInfo userInfo;

    private String name;

    private String description;

    @Latitude(of = Berth.SPATIAL_FIELD)
    private Double lat;

    @Longitude(of = Berth.SPATIAL_FIELD)
    private Double lng;

    private Double standardPrice;

    @OneToMany(mappedBy = "berth", cascade = {PERSIST, DETACH, MERGE}, orphanRemoval = true)
    @OrderBy("id")
    private List<BerthPlace> berthPlaces = new ArrayList<>();

    @OneToMany(mappedBy = "pk.berth", cascade = {PERSIST, DETACH, MERGE}, orphanRemoval = true)
    @OrderBy("pk.num")
    private List<BerthPhoto> photos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "berth_to_convenience",
            joinColumns = @JoinColumn(name = "berth_id"),
            inverseJoinColumns = @JoinColumn(name = "convenience_id"))
    @OrderBy("id")
    private List<Convenience> conveniences = new ArrayList<>();


    public Berth(UserInfo userInfo, BerthDto dto) {
        this.userInfo = userInfo;
        setDto(dto);
    }

    public void setDto(BerthDto dto) {
        name = dto.getName();
        description = dto.getDescription();
        lat = dto.getLat();
        lng = dto.getLng();
        standardPrice = dto.getStandardPrice();
        if (dto.getPhotoList() != null) {
            var i = new AtomicInteger();
            var newPhotos = dto.getPhotoList().stream()
                    .map(photoName -> new BerthPhoto(this, i.getAndIncrement(), photoName))
                    .collect(Collectors.toList());

            photos.clear();
            photos.addAll(newPhotos);
        }
    }

    public BerthDto.WithId getDto() {
        return (BerthDto.WithId) new BerthDto.WithId()
                .setId(id)
                .setName(name)
                .setDescription(description)
                .setLat(lat)
                .setLng(lng)
                .setStandardPrice(standardPrice)
                .setPhotoList(photos.stream().map(BerthPhoto::getFileName).collect(Collectors.toList()));
    }

    public void setBerthPlaces(List<BerthPlace> newPlaces) {
        berthPlaces.clear();
        berthPlaces.addAll(newPlaces);
    }

    public void setConveniences(List<Convenience> newConveniences) {
        conveniences.clear();
        conveniences.addAll(newConveniences);
    }

    public void setPhotos(List<BerthPhoto> newPhotos) {
        photos.clear();
        photos.addAll(newPhotos);
    }

    @Override
    public Long getOwnerId() {
        return userInfo.getAccountId();
    }
}