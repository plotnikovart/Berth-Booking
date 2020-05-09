package ru.hse.coursework.berth.service.berth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.BerthApplication;
import ru.hse.coursework.berth.database.entity.enums.BerthApplicationStatus;
import ru.hse.coursework.berth.database.repository.AccountRepository;
import ru.hse.coursework.berth.database.repository.BerthApplicationRepository;
import ru.hse.coursework.berth.service.berth.dto.BerthApplicationDto;
import ru.hse.coursework.berth.service.berth.dto.BerthApplicationFilter;
import ru.hse.coursework.berth.service.berth.dto.management.ChangeApplicationStatusResp;
import ru.hse.coursework.berth.service.berth.dto.management.StartApplicationResp;
import ru.hse.coursework.berth.service.converters.impl.BerthApplicationConverter;
import ru.hse.coursework.berth.web.dto.response.ListCount;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

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
        BerthApplication application = modifyApplication(id, BerthApplicationStatus.IN_PROGRESS, null);
        application.getBerth().setIsConfirmed(false);

        // todo создание чата, присоединение к чату
        return (StartApplicationResp) new StartApplicationResp()
                .setChatId(-1L)
                .setNewStatus(BerthApplicationStatus.IN_PROGRESS);
    }

    @Transactional
    public ChangeApplicationStatusResp rejectApplication(long id, String decision) {
        BerthApplication application = modifyApplication(id, BerthApplicationStatus.REJECTED, decision);
        application.getBerth().setIsConfirmed(false);

        return new ChangeApplicationStatusResp()
                .setNewStatus(BerthApplicationStatus.REJECTED);
    }

    @Transactional
    public ChangeApplicationStatusResp approveApplication(long id, String decision) {
        BerthApplication application = modifyApplication(id, BerthApplicationStatus.APPROVED, decision);
        application.getBerth().setIsConfirmed(true);

        return new ChangeApplicationStatusResp()
                .setNewStatus(BerthApplicationStatus.APPROVED);
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
