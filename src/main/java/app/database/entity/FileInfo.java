package app.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@Entity
@Immutable
public class FileInfo {

    @Id
    private UUID id;

    @Column(nullable = false, length = 16)
    private String code;

    @Column(nullable = false, length = 512)
    private String name;

    @Column(length = 16)
    private String ext;

    public String getExtWithDot() {
        return ext != null ? "." + ext : "";
    }
}
