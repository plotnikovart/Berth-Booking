package ru.hse.coursework.berth.common.fsm;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.lang.reflect.Field;

@AllArgsConstructor
public class FSMChangeStatusListener<ENTITY,
        STATE extends Enum<STATE> & StateEnum,
        EVENT extends Enum<EVENT> & EventEnum> extends StateMachineListenerAdapter<STATE, EVENT> {

    private final ENTITY obj;
    private final Field stateField;

    @Override
    @SneakyThrows
    public void stateChanged(State<STATE, EVENT> from, State<STATE, EVENT> to) {
        stateField.set(obj, to.getId());
    }
}


