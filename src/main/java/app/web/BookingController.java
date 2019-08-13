package app.web;

import app.common.ValidationUtils;
import app.service.facade.BookingFacade;
import app.web.dto.BookingDto;
import app.web.dto.request.BookingRequest;
import app.web.dto.response.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingFacade bookingFacade;

    @PostMapping
    public IdResponse<Long> createBooking(@RequestBody BookingRequest bookingRequest) {
        ValidationUtils.validateEntity(bookingRequest);
        Long id = bookingFacade.createBooking(bookingRequest);
        return new IdResponse<>(id);
    }

    @GetMapping
    public List<BookingDto.WithId> getAllBookings() {
        return bookingFacade.getAllBookings();
    }

    @GetMapping("{id}")
    public BookingDto.WithId getBookingById(@PathVariable Long id) {
        return bookingFacade.getBookingById(id);
    }

    @GetMapping("berths/{berthId}")
    public List<BookingDto.WithId> getAllBookingsForBerth(@PathVariable Long berthId) {
        return bookingFacade.getAllBookingsByBerth(berthId);
    }

    @PutMapping("{id}/reject")
    public void rejectBooking(@PathVariable Long id) {
        bookingFacade.rejectBooking(id);
    }

    @PutMapping("{id}/approve")
    public void approveBooking(@PathVariable Long id) {
        bookingFacade.approveBooking(id);
    }

    @PutMapping("{id}/cancel")
    public void cancelBooking(@PathVariable Long id) {
        bookingFacade.cancelBooking(id);
    }
}
