package app.util;

import app.service.berth.dto.BerthApplicationDto;
import app.service.berth.dto.BerthDto;
import app.service.berth.dto.BerthPlaceDto;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;

public final class CompareHelper {

    public static boolean areEqual(BerthApplicationDto.Resp expected, BerthApplicationDto.Resp actual) {
        return equals(expected.getStatus(), actual.getStatus()) &&
                equals(expected.getTitle(), actual.getTitle()) &&
                equals(expected.getAttachments(), actual.getAttachments()) &&
                equals(expected.getDescription(), actual.getDescription()) &&
                equals(expected.getDecision(), actual.getDecision()) &&
                equalsId(expected.getId(), actual.getId()) &&
                equalsId(expected.getApplicantId(), actual.getApplicantId()) &&
                equalsId(expected.getBerthId(), actual.getBerthId()) &&
                equalsId(expected.getModeratorId(), actual.getModeratorId());
    }

    public static boolean areEqualBerths(List<BerthDto.Resp> e, List<BerthDto.Resp> a) {
        e = ofNullable(e).orElseGet(List::of);
        a = ofNullable(a).orElseGet(List::of);

        if (e.size() != a.size()) {
            return false;
        }

        return StreamEx.zip(e, a, Pair::of).allMatch(pair -> areEqual(pair.getLeft(), pair.getRight()));
    }

    public static boolean areEqual(BerthDto.Resp e, BerthDto.Resp a) {
        return equalsId(e.getId(), a.getId()) &&
                equals(e.getIsConfirmed(), a.getIsConfirmed()) &&
                equals(e.getRating(), a.getRating()) &&
                equals(e.getName(), a.getName()) &&
                equals(e.getDescription(), a.getDescription()) &&
                equals(e.getLat(), a.getLat()) &&
                equals(e.getLng(), a.getLng()) &&
                equals(e.getRadio(), a.getRadio()) &&
                equals(e.getSite(), a.getSite()) &&
                equals(e.getPhCode(), a.getPhCode()) &&
                equals(e.getPhNumber(), a.getPhNumber()) &&
                equals(e.getPhNumber(), a.getPhNumber()) &&
                areEqualPlaces(e.getPlaces(), a.getPlaces()) &&
                equals(e.getAmenities(), a.getAmenities()) &&
                equals(e.getPhotos(), a.getPhotos());
    }

    public static boolean areEqualPlaces(List<BerthPlaceDto> e, List<BerthPlaceDto> a) {
        e = ofNullable(e).orElseGet(List::of);
        a = ofNullable(a).orElseGet(List::of);

        if (e.size() != a.size()) {
            return false;
        }

        return StreamEx.zip(e, a, Pair::of).allMatch(pair -> areEqual(pair.getLeft(), pair.getRight()));
    }

    public static boolean areEqual(BerthPlaceDto e, BerthPlaceDto a) {
        return equalsId(e.getId(), a.getId()) &&
                equals(e.getName(), a.getName()) &&
                equals(e.getLength(), a.getLength()) &&
                equals(e.getWidth(), a.getWidth()) &&
                equals(e.getDraft(), a.getDraft()) &&
                equals(e.getPrice(), a.getPrice()) &&
                equals(e.getXCoord(), a.getXCoord()) &&
                equals(e.getYCoord(), a.getYCoord()) &&
                equals(e.getRotate(), a.getRotate()) &&
                equals(e.getColor(), a.getColor());
    }


    private static boolean equals(Object o1, Object o2) {
        return Objects.equals(o1, o2) ;
    }


    private static boolean equalsId(Object expected, Object actual) {
        boolean idNotPresented = expected == null && actual != null;
        return idNotPresented || Objects.equals(expected, actual);
    }
}
