package ru.hse.coursework.berth.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Order;
import ru.hse.coursework.berth.database.repository.OrderRepository;
import ru.hse.coursework.berth.service.event.EventPublisher;
import ru.hse.coursework.berth.service.payment.tinkoff.NotificationReq;
import ru.hse.coursework.berth.service.payment.tinkoff.PaymentStatus;
import ru.hse.coursework.berth.service.payment.tinkoff.TinkoffClient;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final TinkoffClient tinkoffClient;
    private final EventPublisher eventPublisher;

    @Transactional
    public void completePayment(NotificationReq notification) {
        if (tinkoffClient.checkNotificationValidity(notification)
                && PaymentStatus.CONFIRMED == notification.getStatus()) {

            Order order = orderRepository.findById(UUID.fromString(notification.getOrderId())).orElseThrow(NotFoundException::new)
                    .setCompleted(true)
                    .setDateTime(LocalDateTime.now());

            eventPublisher.payBooking(order.getPayload().getBookingId());
        }
    }
}
