package ru.hse.coursework.berth.service.payment.tinkoff;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.hse.coursework.berth.common.AbstractHttpClient;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;
import ru.hse.coursework.berth.service.payment.OrderDto;

import static one.util.streamex.StreamEx.of;
import static ru.hse.coursework.berth.common.SMessageSource.message;

@Slf4j
@Component
public class TinkoffClient extends AbstractHttpClient {

    public static final String INIT_URL = "https://securepay.tinkoff.ru/v2/Init";

    private final ObjectMapper mapper;
    private final RestTemplate client;

    private final String terminalKey;
    private final String notificationUrl;
    private final String successUrl;
    private final String failureUrl;

    public TinkoffClient(ObjectMapper mapper,
                         RestTemplate client,
                         @Value("${tinkoff.terminalKey}") String terminalKey,
                         @Value("${back.url}") String backUrl,
                         @Value("${front.url}") String frontUrl) {
        this.mapper = mapper;
        this.client = client;

        this.terminalKey = terminalKey;
        this.notificationUrl = backUrl + "/api/payments/tinkoff/notifications";

        this.successUrl = frontUrl + "/successPayment";
        this.failureUrl = frontUrl + "/failurePayment";
    }

    @Override
    protected ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    protected RestTemplate getClient() {
        return client;
    }


    public String createPaymentLink(OrderDto order) {
        var req = new InitReq()
                .setTerminalKey(terminalKey)
                .setAmount(of(order.getItems()).mapToInt(OrderDto.Item::getPrice).sum())
                .setOrderId(order.getId())
                .setData(new InitReq.Data().setEmail(order.getEmail()))
                .setReceipt(new InitReq.Receipt()
                        .setEmail(order.getEmail())
                        .setItems(of(order.getItems())
                                .map(it -> new InitReq.Receipt.Item()
                                        .setName(it.getName())
                                        .setPrice(it.getPrice())
                                        .setAmount(it.getPrice())
                                        .setQuantity(1)
                                )
                                .toList()
                        )
                )
                .setNotificationUrl(notificationUrl)
                .setSuccessUrl(successUrl)
                .setFailUrl(failureUrl);

        var resp = sendRequest(INIT_URL, HttpMethod.POST, req, InitResp.class);

        if (!resp.getSuccess()) {
            var message = message("payments.url.error", resp.getMessage(), resp.getDetails());
            log.error(message);
            throw new ServiceException(message);
        }

        return resp.getPaymentURL();
    }


    public boolean checkNotificationValidity(NotificationReq notification) {
        return notification.getTerminalKey().equals(terminalKey);
    }
}
