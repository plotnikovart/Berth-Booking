package ru.hse.coursework.berth.service.berth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.config.exception.impl.AccessException;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.repository.BerthRepository;

@Service
@RequiredArgsConstructor
public class BerthAccessService {

    private final BerthRepository berthRepository;

    public void checkAccess(Long berthId) throws AccessException {
        Berth berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);

        if (!berth.getOwnerId().equals(OperationContext.accountId())) {
            throw new AccessException();
        }
    }
}
