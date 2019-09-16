package app.database.entity;

import app.common.EntityWithOwner;
import app.service.file.ImageKind;
import app.web.dto.ShipDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Ship implements EntityWithOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private UserInfo userInfo;

    private String name;

    private Double length;

    private Double draft;

    private Double width;

    @OneToMany(mappedBy = "pk.ship", cascade = {PERSIST, DETACH, MERGE}, orphanRemoval = true)
    @OrderBy("pk.num")
    private List<ShipPhoto> photos = new ArrayList<>();


    public Ship(UserInfo userInfo, ShipDto dto) {
        this.userInfo = userInfo;
        setDto(dto);
    }

    public void setDto(ShipDto dto) {
        name = dto.getName();
        length = dto.getLength();
        draft = dto.getDraft();
        width = dto.getWidth();
        var i = new AtomicInteger(0);
        var newPhotos = dto.getPhotoList().stream().map(fileName -> new ShipPhoto(this, i.getAndIncrement(), fileName)).collect(Collectors.toList());

        photos.clear();
        photos.addAll(newPhotos);
    }

    public ShipDto.WithId getDto() {
        List<String> photoList = getPhotos().stream()
                .map(photo -> MessageFormat.format("/api/images/{0}/{1}/{2}", ImageKind.SHIP.name().toLowerCase(), getOwnerId(), photo.getFileName()))
                .collect(Collectors.toList());

        return (ShipDto.WithId) new ShipDto.WithId()
                .setId(getId())
                .setName(getName())
                .setLength(getLength())
                .setDraft(getDraft())
                .setWidth(getWidth())
                .setPhotoList(photoList);
    }

    public void setPhotos(List<ShipPhoto> newPhotos) {
        photos.clear();
        photos.addAll(newPhotos);
    }

    @Override
    public Long getOwnerId() {
        return userInfo.getAccountId();
    }
}
