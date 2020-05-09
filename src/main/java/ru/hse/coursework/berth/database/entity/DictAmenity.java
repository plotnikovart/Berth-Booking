package ru.hse.coursework.berth.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@Entity
@Immutable
public class DictAmenity {

    @Id
    private String key;

    @Column(nullable = false)
    private String value;
}
