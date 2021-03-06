package ru.hse.coursework.berth.database.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;
import ru.hse.coursework.berth.database.entity.Berth;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BerthSearchRepository {

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public Map<Berth, Double> findByCoordinates(Double lat, Double lng, Double rad) {
        try {
            FullTextEntityManager em = Search.getFullTextEntityManager(entityManager);
            QueryBuilder queryBuilder = em.getSearchFactory().buildQueryBuilder().forEntity(Berth.class).get();

            Query luceneQuery = queryBuilder
                    .spatial()
                    .onField(Berth.SPATIAL_FIELD)
                    .within(rad, Unit.KM)
                    .ofLatitude(lat)
                    .andLongitude(lng)
                    .createQuery();

            FullTextQuery hibQuery = em.createFullTextQuery(luceneQuery, Berth.class)
                    .setProjection("id")
                    .setProjection(FullTextQuery.SPATIAL_DISTANCE, FullTextQuery.THIS)
                    .setSpatialParameters(lat, lng, Berth.SPATIAL_FIELD);

            Map<Berth, Double> results = new HashMap<>();
            for (var o : hibQuery.getResultList()) {
                Object[] objects = (Object[]) o;
                Berth b = (Berth) objects[1];
                Double d = (Double) objects[0];

                if (b != null) {    // full text entity manager can't delete all entites from index????
                    results.put(b, d);
                }
            }

            return results;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }
}
