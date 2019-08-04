package app.database.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class ShipPhoto {

    @EmbeddedId
    private PK pk;

    @Column
    @NotNull
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

        @ManyToOne
        @JoinColumn(name = "ship_id")
        private Ship ship;

        @Column
        @NotNull
        private Integer num;
    }
}
