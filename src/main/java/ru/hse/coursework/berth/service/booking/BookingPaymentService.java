package ru.hse.coursework.berth.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.AccessException;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.Order;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.database.repository.OrderRepository;
import ru.hse.coursework.berth.service.account.AccountService;
import ru.hse.coursework.berth.service.booking.dto.BookingPayLinkResp;
import ru.hse.coursework.berth.service.booking.fsm.BookingEvent;
import ru.hse.coursework.berth.service.booking.fsm.BookingFSMHandler;
import ru.hse.coursework.berth.service.event.booking.PayedBookingEvent;
import ru.hse.coursework.berth.service.payment.ExchangeRateService;
import ru.hse.coursework.berth.service.payment.OrderDto;
import ru.hse.coursework.berth.service.payment.OrderKind;
import ru.hse.coursework.berth.service.payment.PaymentServiceKind;
import ru.hse.coursework.berth.service.payment.tinkoff.TinkoffClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingPaymentService {

    private final AccountService accountService;
    private final BookingRepository bookingRepository;
    private final BookingFSMHandler bookingFSMHandler;
    private final TinkoffClient tinkoffClient;
    private final OrderRepository orderRepository;
    private final ExchangeRateService exchangeRateService;

    @Transactional
    public BookingPayLinkResp formPaymentLink(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);
        checkAccess(booking);

        // check state
        bookingFSMHandler.sendEvent(booking, BookingEvent.PAY_PREPARE);

        Order order = createOrder(booking);
        OrderDto orderDto = orderToDto(order);

        String link = tinkoffClient.createPaymentLink(orderDto);
        return BookingPayLinkResp.of(link);
    }


    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void bookingPayed(PayedBookingEvent payedBookingEvent) {
        Booking booking = bookingRepository.findById(payedBookingEvent.getBookingId()).orElseThrow(NotFoundException::new);
        bookingFSMHandler.sendEvent(booking, BookingEvent.PAY);
    }


    private Order createOrder(Booking booking) {
        var order = new Order()
                .setId(UUID.randomUUID())
                .setServiceKind(PaymentServiceKind.TINKOFF)
                .setOrderKind(OrderKind.BOOKING_SERVICE_FEE)
                .setDateTime(LocalDateTime.now())
                .setPrice(booking.getServiceFee())
                .setCompleted(false)
                .setPayload(new Order.Payload()
                        .setAccountId(OperationContext.accountId())
                        .setBookingId(booking.getId())
                );

        return orderRepository.save(order);
    }

    private OrderDto orderToDto(Order order) {
        String email = accountService.getAccountInfo().getEmail();

        var item = new OrderDto.Item()
                .setName("Booking #" + order.getPayload().getBookingId())
                .setPrice((int) (order.getPrice() * 100 * exchangeRateService.getEuroRub()));   // цена в копейках

        return new OrderDto()
                .setId(order.getId().toString())
                .setEmail(email)
                .setItems(List.of(item));
    }

    private void checkAccess(Booking booking) {
        if (!booking.getRenter().getId().equals(OperationContext.accountId())) {
            throw new AccessException();
        }
    }
}
