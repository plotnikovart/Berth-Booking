package app.service.berth;

import app.config.exception.impl.NotFoundException;
import app.config.security.OperationContext;
import app.database.entity.BerthApplication;
import app.database.entity.enums.BerthApplicationStatus;
import app.database.repository.AccountRepository;
import app.database.repository.BerthApplicationRepository;
import app.service.berth.dto.BerthApplicationDto;
import app.service.berth.dto.BerthApplicationFilter;
import app.service.berth.dto.management.ChangeApplicationStatusResp;
import app.service.berth.dto.management.StartApplicationResp;
import app.service.converters.impl.BerthApplicationConverter;
import app.web.dto.response.ListCount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

import static app.database.entity.enums.BerthApplicationStatus.*;

@Service
@RequiredArgsConstructor
public class ManagementBerthApplicationService {

    private final AccountRepository accountRepository;
    private final BerthApplicationConverter berthApplicationConverter;
    private final BerthApplicationRepository berthApplicationRepository;

    public ListCount<BerthApplicationDto.Resp> getApplications(BerthApplicationFilter filter) {
        Specification<BerthApplication> predicate = createPredicate(filter);
        Pageable pageable = createPageable(filter);

        Page<BerthApplication> applications = berthApplicationRepository.findAll(predicate, pageable);

        List<BerthApplicationDto.Resp> dtos = berthApplicationConverter.toDtos(applications.getContent());
        return ListCount.of(dtos, applications.getTotalElements());
    }

    @Transactional
    public StartApplicationResp startApplication(long id) {
        BerthApplication application = modifyApplication(id, IN_PROGRESS, null);
        application.getBerth().setIsConfirmed(false);

        // todo создание чата, присоединение к чату
        return (StartApplicationResp) new StartApplicationResp()
                .setChatId(-1L)
                .setNewStatus(IN_PROGRESS);
    }

    @Transactional
    public ChangeApplicationStatusResp rejectApplication(long id, String decision) {
        BerthApplication application = modifyApplication(id, REJECTED, decision);
        application.getBerth().setIsConfirmed(false);

        return new ChangeApplicationStatusResp()
                .setNewStatus(REJECTED);
    }

    @Transactional
    public ChangeApplicationStatusResp approveApplication(long id, String decision) {
        BerthApplication application = modifyApplication(id, APPROVED, decision);
        application.getBerth().setIsConfirmed(true);

        return new ChangeApplicationStatusResp()
                .setNewStatus(APPROVED);
    }


    private BerthApplication modifyApplication(long id, BerthApplicationStatus status, String decision) {
        return berthApplicationRepository.findById(id).orElseThrow(NotFoundException::new)
                .setModerator(accountRepository.findCurrent())
                .setStatus(status)
                .setDecision(decision);
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
