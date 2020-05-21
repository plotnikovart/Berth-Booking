package ru.hse.coursework.berth.service.payment.tinkoff;

public enum PaymentStatus {

    AUTHORIZED,
    CONFIRMED,
    REVERSED,
    REFUNDED,
    PARTIAL_REFUNDED,
    REJECTED
}
