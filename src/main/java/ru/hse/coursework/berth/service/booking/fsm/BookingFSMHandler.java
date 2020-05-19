package ru.hse.coursework.berth.service.booking.fsm;

import lombok.SneakyThrows;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.common.fsm.EntityFSMHandler;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;

import java.util.EnumSet;

import static ru.hse.coursework.berth.database.entity.enums.BookingStatus.*;
import static ru.hse.coursework.berth.service.booking.fsm.BookingEvent.*;

@Component
public class BookingFSMHandler extends EntityFSMHandler<Booking, BookingStatus, BookingEvent> {

    @Override
    @SneakyThrows
    protected StateMachine<BookingStatus, BookingEvent> buildStateMachine(BookingStatus baseState) {
        Builder<BookingStatus, BookingEvent> builder = StateMachineBuilder.builder();

        builder.configureStates()
                .withStates()
                .initial(baseState)
                .states(EnumSet.allOf(BookingStatus.class));

        builder.configureTransitions()
                .withExternal()
                .source(NEW).target(REJECTED).event(REJECT)
                .and()
                .withExternal()
                .source(NEW).target(APPROVED).event(APPROVE)
                .and()
                .withExternal()
                .source(APPROVED).target(PAYED).event(PAY)
                .and()
                .withExternal()
                .source(APPROVED).target(CANCELLED).event(CANCEL)
                .and()
                .withExternal()
                .source(PAYED).target(CANCELLED).event(CANCEL);

        return builder.build();
    }
}
