package ru.hse.coursework.berth.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.service.email.dto.BookingInfo;
import ru.hse.coursework.berth.service.email.dto.ReviewInfo;

import java.time.format.DateTimeFormatter;

import static ru.hse.coursework.berth.common.SMessageSource.message;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    public final static String CONFIRM_CODE_PARAM = "code";
    public final static String EMAIL_PARAM = "email";
    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Value("${back.url}")
    private String backUrl;
    @Value("${front.url}")
    private String frontUrl;

    private final EmailSender emailSender;


    public void sendEmailConfirmation(String to, String code) {
        String url = backUrl + "/api/auth/registration/confirm" +
                "?" + CONFIRM_CODE_PARAM + "=" + code +
                "&" + EMAIL_PARAM + "=" + to;
        String text = message("account.confirm")
                .replaceAll("\\{0}", url)
                .replaceAll("\\{1}", to);

        emailSender.sendMessage(to, "Account Confirmation", text);
    }

    public void sendPasswordRecovery(String to, String recoveryCode) {
        String text = message("account.password.recovery")
                .replaceAll("\\{0}", recoveryCode);

        emailSender.sendMessage(to, "Password recovery", text);
    }

    public void sendNewReview(String to, ReviewInfo reviewInfo) {
        String berthUri = frontUrl + "/marina/" + reviewInfo.getBerthId();

        String text = message("review.publish")
                .replaceAll("\\{0}", reviewInfo.getTo())
                .replaceAll("\\{1}", reviewInfo.getStars().toString())
                .replaceAll("\\{2}", reviewInfo.getBerthName())
                .replaceAll("\\{3}", reviewInfo.getFrom())
                .replaceAll("\\{4}", berthUri);

        emailSender.sendMessage(to, "New Review to your Marina", text);
    }


    public void sendBookingOpen(String to, BookingInfo booking) {
        String bookingUri = frontUrl + "/bookings";

        String text = message("booking.open")
                .replaceAll("\\{0}", booking.getOwner())
                .replaceAll("\\{1}", booking.getPlaceName())
                .replaceAll("\\{2}", booking.getBerthName())
                .replaceAll("\\{3}", booking.getRenter())
                .replaceAll("\\{4}", booking.getFromDate().format(DATE_FORMATTER))
                .replaceAll("\\{5}", booking.getToDate().format(DATE_FORMATTER))
                .replaceAll("\\{6}", bookingUri);

        emailSender.sendMessage(to, "New booking", text);
    }

    public void sendBookingCancel(String to, BookingInfo booking) {
        String bookingUri = frontUrl + "/bookings";

        String text = message("booking.cancel")
                .replaceAll("\\{0}", booking.getOwner())
                .replaceAll("\\{1}", booking.getPlaceName())
                .replaceAll("\\{2}", booking.getBerthName())
                .replaceAll("\\{3}", booking.getRenter())
                .replaceAll("\\{4}", booking.getFromDate().format(DATE_FORMATTER))
                .replaceAll("\\{5}", booking.getToDate().format(DATE_FORMATTER))
                .replaceAll("\\{6}", bookingUri);

        emailSender.sendMessage(to, "Booking was cancelled", text);
    }

    public void sendBookingPay(String to, BookingInfo booking) {
        String bookingUri = frontUrl + "/bookings";

        String text = message("booking.pay")
                .replaceAll("\\{0}", booking.getOwner())
                .replaceAll("\\{1}", booking.getPlaceName())
                .replaceAll("\\{2}", booking.getBerthName())
                .replaceAll("\\{3}", booking.getRenter())
                .replaceAll("\\{4}", booking.getFromDate().format(DATE_FORMATTER))
                .replaceAll("\\{5}", booking.getToDate().format(DATE_FORMATTER))
                .replaceAll("\\{6}", bookingUri);

        emailSender.sendMessage(to, "Booking was paid", text);
    }


    public void sendBookingApprove(String to, BookingInfo booking) {
        String bookingUri = frontUrl + "/trips";

        String text = message("booking.approve")
                .replaceAll("\\{0}", booking.getRenter())
                .replaceAll("\\{1}", booking.getBerthName())
                .replaceAll("\\{2}", booking.getFromDate().format(DATE_FORMATTER))
                .replaceAll("\\{3}", booking.getToDate().format(DATE_FORMATTER))
                .replaceAll("\\{4}", String.format("%.2f", booking.getPrice() + booking.getServiceFee()))
                .replaceAll("\\{5}", String.format("%.2f", booking.getServiceFee()))
                .replaceAll("\\{6}", bookingUri);

        emailSender.sendMessage(to, "Booking was confirmed", text);
    }

    public void sendBookingReject(String to, BookingInfo booking) {
        String bookingUri = frontUrl + "/trips";

        String text = message("booking.reject")
                .replaceAll("\\{0}", booking.getRenter())
                .replaceAll("\\{1}", booking.getBerthName())
                .replaceAll("\\{2}", booking.getFromDate().format(DATE_FORMATTER))
                .replaceAll("\\{3}", booking.getToDate().format(DATE_FORMATTER))
                .replaceAll("\\{4}", bookingUri);

        emailSender.sendMessage(to, "Booking was rejected", text);
    }
}
