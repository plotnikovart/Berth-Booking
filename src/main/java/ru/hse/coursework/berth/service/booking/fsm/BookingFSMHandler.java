package ru.hse.coursework.berth.service.booking.fsm;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.common.DateHelper;
import ru.hse.coursework.berth.common.fsm.EntityFSMHandler;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;

import java.util.EnumSet;

import static ru.hse.coursework.berth.database.entity.enums.BookingStatus.*;
import static ru.hse.coursework.berth.service.booking.fsm.BookingEvent.*;

@Slf4j
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
                .source(NEW).target(APPROVED).event(APPROVE).guard(checkOtherNotApproved())
                .and()
                .withExternal()
                .source(NEW).target(CANCELLED).event(CANCEL)
                .and()
                .withExternal()
                .source(APPROVED).target(REJECTED).event(REJECT)
                .and()
                .withExternal()
                .source(APPROVED).target(APPROVED).event(PAY_PREPARE)
                .and()
                .withExternal()
                .source(APPROVED).target(PAYED).event(PAY).action(rejectOtherApplications())
                .and()
                .withExternal()
                .source(APPROVED).target(CANCELLED).event(CANCEL)
                .and()
                .withExternal()
                .source(PAYED).target(CANCELLED).event(CANCEL)

                // other booking was payed
                .and()
                .withExternal()
                .source(NEW).target(REJECTED).event(OTHER_PAY)
                .and()
                .withExternal()
                .source(REJECTED).target(REJECTED).event(OTHER_PAY)
                .and()
                .withExternal()
                .source(CANCELLED).target(CANCELLED).event(OTHER_PAY);

        return builder.build();
    }


    private Action<BookingStatus, BookingEvent> rejectOtherApplications() {
        return createAction((booking, ctx) -> {
            // другие бронирования на текущее место на пересекающиеся даты отклоняем
            booking.getBerthPlace().getBookingList().stream()
                    .filter(it -> !it.getId().equals(booking.getId()))
                    .filter(b -> DateHelper.isIntersect(b.getStartDate(), b.getEndDate(), booking.getStartDate(), booking.getEndDate()))
                    .forEach(b -> sendEvent(b, OTHER_PAY));
        });
    }


    private Guard<BookingStatus, BookingEvent> checkOtherNotApproved() {
        return createGuard((booking, ctx) -> {
            // для перевода в статус approved нужно, чтобы в этом статусе не было других заявок
            return booking.getBerthPlace().getBookingList().stream()
                    .filter(it -> !it.getId().equals(booking.getId()))
                    .filter(b -> DateHelper.isIntersect(b.getStartDate(), b.getEndDate(), booking.getStartDate(), booking.getEndDate()))
                    .noneMatch(b -> b.getStatus() == APPROVED);
        });
    }
}
