package ru.hse.coursework.berth.web;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.coursework.berth.service.berth.dto.BerthDto;
import ru.hse.coursework.berth.service.booking.BookingFacade;
import ru.hse.coursework.berth.service.booking.BookingSearchService;
import ru.hse.coursework.berth.service.booking.dto.BookingDto;
import ru.hse.coursework.berth.service.booking.dto.BookingPayLinkResp;
import ru.hse.coursework.berth.service.booking.dto.BookingSearchReq;
import ru.hse.coursework.berth.service.booking.dto.BookingStatusResp;
import ru.hse.coursework.berth.web.dto.resp.ListResp;
import ru.hse.coursework.berth.web.dto.resp.ObjectResp;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingFacade bookingFacade;
    private final BookingSearchService bookingSearchService;

    @PostMapping
    public ObjectResp<BookingDto.RespRenter> openBooking(@RequestBody BookingDto.Req bookingRequest) {
        var resp = bookingFacade.openBooking(bookingRequest);
        return new ObjectResp<>(resp);
    }

    @GetMapping
    public ListResp<BookingDto.RespRenter> getAllBookings() {
        var resp = bookingFacade.getMyBookings();
        return new ListResp<>(resp);
    }

    @GetMapping("berths/{berthId}")
    public ListResp<BookingDto.RespOwner> getBookingsForBerth(@PathVariable Long berthId) {
        var resp = bookingFacade.getBookingsForBerth(berthId);
        return new ListResp<>(resp);
    }

    @PutMapping("{id}/reject")
    public ObjectResp<BookingStatusResp> rejectBooking(@PathVariable Long id) {
        var resp = bookingFacade.rejectBooking(id);
        return new ObjectResp<>(resp);
    }

    @PutMapping("{id}/approve")
    public ObjectResp<BookingStatusResp> approveBooking(@PathVariable Long id) {
        var resp = bookingFacade.approveBooking(id);
        return new ObjectResp<>(resp);
    }

    @PutMapping("{id}/cancel")
    public ObjectResp<BookingStatusResp> cancelBooking(@PathVariable Long id) {
        var resp = bookingFacade.cancelBooking(id);
        return new ObjectResp<>(resp);
    }

    @PutMapping("{id}/pay")
    public ObjectResp<BookingPayLinkResp> payBooking(@PathVariable Long id) {
        var resp = bookingFacade.payBooking(id);
        return new ObjectResp<>(resp);
    }

    @PostMapping("search")
    public List<BerthDto.Resp.Search> search(@RequestBody BookingSearchReq req) {
        return bookingSearchService.searchPlaces(req);
    }
}
