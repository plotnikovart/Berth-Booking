package ru.hse.coursework.berth.service.booking.fsm;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;
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
                .source(PAYED).target(CANCELLED).event(CANCEL);

        return builder.build();
    }


    Action<BookingStatus, BookingEvent> rejectOtherApplications() {
        return createAction((booking, ctx) -> {
            // Для других бронирований на пересекающиеся даты устанавливаем статус - отклонено
//            booking.getBerthPlace().getBookingList().stream()
//                    .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
//                    .filter(b -> DateHelper.isIntersect(b.getStartDate(), b.getEndDate(), booking.getStartDate(), booking.getEndDate()))
//                    .forEach(b -> b.setStatus(BookingStatus.REJECTED));

            // todo
            log.info("Other was rejected");
        });
    }

    public Guard<BookingStatus, BookingEvent> checkOtherNotApproved() {
        return createGuard((booking, ctx) -> {
            // todo
            log.info("check was completed");
            return false;
        });
    }
}
