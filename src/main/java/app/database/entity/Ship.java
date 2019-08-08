package app.database.entity;

import app.common.EntityWithOwner;
import app.web.dto.ShipDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "pk.ship", fetch = FetchType.EAGER, cascade = {PERSIST, DETACH, MERGE}, orphanRemoval = true)
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
        var dto = new ShipDto.WithId();
        dto.setId(id);
        dto.setName(name);
        dto.setLength(length);
        dto.setDraft(draft);
        dto.setWidth(width);
        dto.setPhotoList(getPhotos().stream().map(ShipPhoto::getFileName).collect(Collectors.toList()));
        return dto;
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
