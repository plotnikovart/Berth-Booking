package app.database.repository;

import app.common.exception.ServiceException;
import app.database.entity.Berth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BerthSearchRepository {

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Pair<Double, Berth>> findByCoordinates(Double lat, Double lng, Double rad) {
        try {
            FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
            QueryBuilder queryBuilder = em.getSearchFactory().buildQueryBuilder().forEntity(Berth.class).get();

            Query luceneQuery = queryBuilder
                    .spatial()
                    .onField(Berth.SPATIAL_FIELD)
                    .within(rad, Unit.KM)
                    .ofLatitude(lat)
                    .andLongitude(lng)
                    .createQuery();

            FullTextQuery hibQuery = em.createFullTextQuery(luceneQuery, Berth.class);
            hibQuery.setProjection(FullTextQuery.SPATIAL_DISTANCE, FullTextQuery.THIS);
            hibQuery.setSpatialParameters(lat, lng, Berth.SPATIAL_FIELD);

            List<Pair<Double, Berth>> results = new LinkedList<>();
            for (var o : hibQuery.getResultList()) {
                Object[] objects = (Object[]) o;
                results.add(Pair.of((Double) objects[0], (Berth) objects[1]));
            }

            return results;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }
}
