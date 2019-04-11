package app.database.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.PERSIST;

@Getter
@Setter
@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
    @Column
    @Length(max = 40, message = "Ограничение на длину названия 30 символов")
    @NotNull(message = "Название судна является обязательным параметром")
    private String name;
    @Column
    @NotNull(message = "Длина судна является обязательным параметром")
    private Double length;
    @Column
    @NotNull(message = "Оснастка судна является обязательным параметром")
    private Double draft;
    @Column
    private Double width;
    @OneToMany(mappedBy = "id.ship", cascade = {PERSIST, DETACH}, orphanRemoval = true)
    @OrderBy("id.num")
    private List<ShipPhoto> photos = new ArrayList<>();
}
