package ru.hse.coursework.berth.database.repository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BerthRepositoryCustomImpl implements BerthRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Triple<Integer, Integer, Double>> calcMonthRevenue(Berth berth, LocalDate sinceDate, LocalDate tillDate) {
        List results = em.createQuery(
                "select YEAR(boo.startDate), MONTH(boo.startDate), sum(boo.totalPrice) " +
                        "from Booking boo " +
                        "join BerthPlace bp on boo.berthPlace = bp " +
                        "where bp.berth = :berth and boo.status = :status " +
                        "and boo.startDate >= :startDate and boo.startDate <= :endDate " +
                        "group by YEAR(boo.startDate), MONTH(boo.startDate)")
                .setParameter("berth", berth)
                .setParameter("startDate", sinceDate)
                .setParameter("endDate", tillDate)
                .setParameter("status", BookingStatus.PAYED)
                .getResultList();

        int startYear = sinceDate.getYear();
        int startMonth = sinceDate.getMonthValue();

        int endYear = tillDate.getYear();
        int endMonth = tillDate.getMonthValue();

        List<Triple<Integer, Integer, Double>> result = new LinkedList<>();
        for (int i = 0; ; i++) {
            int targetYear = startYear + (startMonth + i) / 12;
            int targetMonth = (startMonth + i) % 12;

            if (startMonth + i % 12 == 0) {
                targetYear--;
                targetMonth++;
            }

            Double revenue = findRevenue(results, targetYear, targetMonth);
            result.add(Triple.of(targetYear, targetMonth, revenue));

            if (targetYear == endYear && targetMonth == endMonth) {
                break;
            }
        }

        return result;
    }

    @Nullable
    private Double findRevenue(List results, int targetYear, int targetMonth) {
        for (var it : results) {
            int year = (int) ((Object[]) it)[0];
            int month = (int) ((Object[]) it)[1];

            if (year == targetYear && month == targetMonth) {
                return (double) ((Object[]) it)[2];
            }
        }

        return null;
    }
}
