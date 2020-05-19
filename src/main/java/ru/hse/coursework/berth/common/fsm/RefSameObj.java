package ru.hse.coursework.berth.common.fsm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor(staticName = "of")
public class RefSameObj<T> {

    @NonNull
    private final T value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefSameObj)) return false;

        RefSameObj<?> that = (RefSameObj<?>) o;

        // reference the SAME object!!!
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
