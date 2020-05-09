package ru.hse.coursework.berth.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hse.coursework.berth.common.EntityWithOwner;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public Ship setPhotos(List<ShipPhoto> newPhotos) {
        photos.clear();
        photos.addAll(newPhotos);
        return this;
    }

    @Override
    public Long getOwnerId() {
        return userInfo.getAccountId();
    }
}
