package app.util;

import app.service.berth.dto.BerthApplicationDto;

import java.util.Objects;

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


    private static boolean equals(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }


    private static boolean equalsId(Object expected, Object actual) {
        boolean idNotPresented = expected == null && actual != null;
        return idNotPresented || Objects.equals(expected, actual);
    }
}
