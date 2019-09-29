package app.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@Entity
@Immutable
public class Convenience {

    @Id
    private Integer id;

    private String code;

    private String name;
}
