package ru.hse.coursework.berth.util;

import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.BerthPlace;

public final class ConstructHelper {

    public static BerthPlace buildBerthPlace(Berth berth, String name, Double price, Double length, Double draft, Double width) {
        return new BerthPlace()
                .setBerth(berth)
                .setName(name)
                .setPrice(price)
                .setLength(length)
                .setDraft(draft)
                .setWidth(width)
                .setColor("")
                .setRotate(0.0)
                .setXCoord(0.0)
                .setYCoord(0.0);
    }
}
