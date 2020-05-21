package ru.hse.coursework.berth.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.coursework.berth.service.payment.PaymentService;
import ru.hse.coursework.berth.service.payment.tinkoff.NotificationReq;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(value = "tinkoff/notification", produces = {"text/html; charset=utf-8"})
    @ResponseBody
    String tinkoffNotification(@RequestBody NotificationReq req) {
        paymentService.completePayment(req);
        return "OK";
    }
}
