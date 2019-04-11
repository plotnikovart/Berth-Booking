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
    @Column
    @NotNull
    private String fileName;

    public ShipPhoto(Ship ship, int num, String fileName) {
        id.ship = ship;
        id.num = num;
        this.fileName = fileName;
    }

    @Embeddable
    @Getter
    public static class ShipPhotoPK implements Serializable {
        @ManyToOne
        @JoinColumn(name = "ship_id")
        private Ship ship;
        @Column
        @NotNull
        private Integer num;
    }
}
