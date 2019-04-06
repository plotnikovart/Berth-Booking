package app.database.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class ShipPhoto {

    @EmbeddedId
    private ShipPhotoPK id = new ShipPhotoPK();
    @Lob
    @Column
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] photo;

    public ShipPhoto(Ship ship, int num, byte[] photo) {
        id.ship = ship;
        id.num = num;
        this.photo = photo;
    }

    @Embeddable
    @Getter
    static class ShipPhotoPK implements Serializable {
        @ManyToOne
        @JoinColumn(name = "ship_id")
        private Ship ship;
        @Column
        @NotNull
        private Integer num;
    }
}
