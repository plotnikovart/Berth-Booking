package ru.hse.coursework.berth.service.booking.fsm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.ApplicationTest;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;

class BookingFSMHandlerTest extends ApplicationTest {

    @Autowired
    BookingFSMHandler bookingFSMHandler;

    @Test
    @SuppressWarnings("deprecation")
    void test() {
        var booking = new Booking()
                .setId(1L);

        bookingFSMHandler.sendEvent(booking, BookingEvent.REJECT);

        Assertions.assertEquals(BookingStatus.REJECTED, booking.getStatus());
        Assertions.assertThrows(ServiceException.class, () -> bookingFSMHandler.sendEvent(booking, BookingEvent.APPROVE));
        Assertions.assertThrows(ServiceException.class, () -> bookingFSMHandler.sendEvent(booking, BookingEvent.PAY));
        Assertions.assertThrows(ServiceException.class, () -> bookingFSMHandler.sendEvent(booking, BookingEvent.CANCEL));

        booking.setStatus(BookingStatus.APPROVED);  // state mismatch exception
        Assertions.assertThrows(ServiceException.class, () -> bookingFSMHandler.sendEvent(booking, BookingEvent.PAY));


        var booking2 = new Booking()
                .setId(2L)
                .setStatus(BookingStatus.APPROVED)
                .setBerthPlace(new BerthPlace());

        bookingFSMHandler.sendEvent(booking2, BookingEvent.PAY);
        Assertions.assertEquals(BookingStatus.PAYED, booking2.getStatus());

        bookingFSMHandler.sendEvent(booking2, BookingEvent.CANCEL);
        Assertions.assertEquals(BookingStatus.CANCELLED, booking2.getStatus());

        Assertions.assertThrows(ServiceException.class, () -> bookingFSMHandler.sendEvent(booking2, BookingEvent.PAY));


        var booking3 = new Booking()
                .setId(3L)
                .setStatus(BookingStatus.APPROVED);

        bookingFSMHandler.sendEvent(booking3, BookingEvent.CANCEL);
        Assertions.assertEquals(BookingStatus.CANCELLED, booking3.getStatus());

        Assertions.assertThrows(ServiceException.class, () -> bookingFSMHandler.sendEvent(booking3, BookingEvent.PAY));
        Assertions.assertThrows(ServiceException.class, () -> bookingFSMHandler.sendEvent(booking3, BookingEvent.REJECT));
    }
}