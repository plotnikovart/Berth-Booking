package app.database.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class ShipPhoto {

    @EmbeddedId
    private PK pk;

    private String fileName;

    public ShipPhoto(Ship ship, int num, String fileName) {
        pk = new PK();
        pk.setShip(ship);
        pk.setNum(num);
        this.fileName = fileName;
    }

    @Data
    @Embeddable
    public static class PK implements Serializable {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "ship_id")
        private Ship ship;

        private Integer num;
    }
}
