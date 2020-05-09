package ru.hse.coursework.berth.web;

//
//@RestController
//@RequestMapping("/api/bookings")
//@RequiredArgsConstructor
//public class BookingController {
//
//    private final BookingFacade bookingFacade;
//    private final BookingSearchService bookingSearchService;
//
//    @PostMapping
//    public IdResponse<Long> createBooking(@RequestBody BookingDto.Req bookingRequest) {
//        ValidationUtils.validateEntity(bookingRequest);
//        Long id = bookingFacade.createBooking(bookingRequest);
//        return new IdResponse<>(id);
//    }
//
//    @GetMapping
//    public List<BookingDto.Resp> getAllBookings() {
//        return bookingFacade.getAllBookings();
//    }
//
//    @GetMapping("{id}")
//    public BookingDto.Resp getBookingById(@PathVariable Long id) {
//        return bookingFacade.getBookingById(id);
//    }
//
//    @GetMapping("berths/{berthId}")
//    public List<BookingDto.Resp> getAllBookingsForBerth(@PathVariable Long berthId) {
//        return bookingFacade.getAllBookingsByBerth(berthId);
//    }
//
//    @PutMapping("{id}/reject")
//    public void rejectBooking(@PathVariable Long id) {
//        bookingFacade.rejectBooking(id);
//    }
//
//    @PutMapping("{id}/approve")
//    public void approveBooking(@PathVariable Long id) {
//        bookingFacade.approveBooking(id);
//    }
//
//    @PutMapping("{id}/cancel")
//    public void cancelBooking(@PathVariable Long id) {
//        bookingFacade.cancelBooking(id);
//    }
//
//    @PostMapping("search")
//    public List<BerthDto.Resp.Search> search(@RequestBody BookingSearchRequest req) {
//        ValidationUtils.validateEntity(req);
//        return bookingSearchService.searchPlaces(req);
//    }
//}
