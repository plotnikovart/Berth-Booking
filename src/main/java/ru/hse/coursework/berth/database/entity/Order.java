package ru.hse.coursework.berth.database.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.hse.coursework.berth.service.payment.OrderKind;
import ru.hse.coursework.berth.service.payment.OrderPayloadConverter;
import ru.hse.coursework.berth.service.payment.PaymentServiceKind;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"order\"")
public class Order {

    @Id
    private UUID id;

    @Column(nullable = false)
    @Convert(converter = PaymentServiceKind.Converter.class)
    private PaymentServiceKind serviceKind;

    @Column(nullable = false)
    @Convert(converter = OrderKind.Converter.class)
    private OrderKind orderKind;

    @Column(nullable = false)
    @Convert(converter = OrderPayloadConverter.class)
    private Payload payload;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private Boolean completed;

    @Column(nullable = false)
    private Double price;


    @Data
    public static class Payload {

        private Long accountId;
        private Long bookingId;
    }
}
