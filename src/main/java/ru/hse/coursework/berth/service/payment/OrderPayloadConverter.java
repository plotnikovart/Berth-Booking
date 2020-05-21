package ru.hse.coursework.berth.service.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.Order;

import javax.persistence.AttributeConverter;

@Component
@RequiredArgsConstructor
public class OrderPayloadConverter implements AttributeConverter<Order.Payload, String> {

    private final ObjectMapper mapper;

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(Order.Payload p) {
        return mapper.writeValueAsString(p);
    }

    @Override
    @SneakyThrows
    public Order.Payload convertToEntityAttribute(String dbData) {
        return mapper.readValue(dbData, Order.Payload.class);
    }
}
