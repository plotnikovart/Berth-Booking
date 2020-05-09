package ru.hse.coursework.berth.service.berth;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.BerthApplication;
import ru.hse.coursework.berth.database.entity.enums.BerthApplicationStatus;
import ru.hse.coursework.berth.database.repository.BerthApplicationRepository;
import ru.hse.coursework.berth.service.berth.dto.BerthApplicationDto;
import ru.hse.coursework.berth.service.converters.impl.BerthApplicationConverter;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBerthApplicationService {

    private final BerthCRUDService berthCRUDService;
    private final BerthApplicationRepository berthApplicationRepository;
    private final EntityManager em;
    private final BerthApplicationConverter berthApplicationConverter;

    @Transactional
    public long create(BerthApplicationDto.Req dto) {
        Long berthId = berthCRUDService.create(dto.getBerth());
        Berth berth = em.getReference(Berth.class, berthId);

        var berthApplication = new BerthApplication()
                .setBerth(berth)
                .setTitle(berth.getName())
                .setApplicant(em.getReference(Account.class, OperationContext.accountId()))
                .setStatus(BerthApplicationStatus.NEW);

        berthApplicationConverter.toEntity(berthApplication, dto);

        return berthApplicationRepository.save(berthApplication).getId();
    }

    public BerthApplicationDto.Resp get(long id) {
        BerthApplication application = berthApplicationRepository.findById(id).orElseThrow(NotFoundException::new);
        return berthApplicationConverter.toDto(application);
    }

    public List<BerthApplicationDto.Resp> get() {
        List<BerthApplication> applications = berthApplicationRepository.findAllByApplicant(em.getReference(Account.class, OperationContext.accountId()));
        return StreamEx.of(applications)
                .sortedBy(BerthApplication::getCreationDate).reverseSorted()
                .map(berthApplicationConverter::toDto)
                .toList();
    }
}
