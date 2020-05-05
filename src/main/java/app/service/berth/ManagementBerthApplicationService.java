package app.service.berth;

import app.config.security.OperationContext;
import app.database.entity.BerthApplication;
import app.database.repository.BerthApplicationRepository;
import app.service.berth.dto.BerthApplicationDto;
import app.service.berth.dto.BerthApplicationFilter;
import app.service.converters.impl.BerthApplicationConverter;
import app.web.dto.response.ListCount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementBerthApplicationService {

    private final BerthApplicationConverter berthApplicationConverter;
    private final BerthApplicationRepository berthApplicationRepository;

    public ListCount<BerthApplicationDto.Resp> getApplications(BerthApplicationFilter filter) {
        Specification<BerthApplication> predicate = createPredicate(filter);
        Pageable pageable = createPageable(filter);

        Page<BerthApplication> applications = berthApplicationRepository.findAll(predicate, pageable);

        List<BerthApplicationDto.Resp> dtos = berthApplicationConverter.toDtos(applications.getContent());
        return ListCount.of(dtos, applications.getTotalElements());
    }

    private Specification<BerthApplication> createPredicate(BerthApplicationFilter filter) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if (filter.getOnlyMy() != null && filter.getOnlyMy()) {
                predicates.add(cb.equal(root.get("moderator").get("id"), OperationContext.accountId()));
            }

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getDateFrom() != null) {
                predicates.add(cb.greaterThan(root.get("creationDate"), filter.getDateFrom()));
            }

            if (filter.getDateTo() != null) {
                predicates.add(cb.lessThan(root.get("creationDate"), filter.getDateTo().plusDays(1)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable createPageable(BerthApplicationFilter filter) {
        var sort = new Sort(Sort.Direction.DESC, "creationDate");
        return PageRequest.of(filter.getPageNum(), filter.getPageSize(), sort);
    }
}
