package app.database.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class BerthPhoto {

    @EmbeddedId
    private PK pk;

    private String fileName;

    public BerthPhoto(Berth berth, int num, String fileName) {
        pk = new PK();
        pk.setBerth(berth);
        pk.setNum(num);
        this.fileName = fileName;
    }

    @Data
    @Embeddable
    public static class PK implements Serializable {

        @ManyToOne
        @JoinColumn(name = "berth_id")
        private Berth berth;

        private Integer num;
    }
}
