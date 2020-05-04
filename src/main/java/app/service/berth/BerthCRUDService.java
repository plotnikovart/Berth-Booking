package app.service.berth;

import app.config.security.OperationContext;
import app.database.entity.Account;
import app.database.entity.Berth;
import app.database.repository.BerthRepository;
import app.service.berth.dto.BerthDto;
import app.service.converters.impl.BerthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class BerthCRUDService {

    private final EntityManager em;
    private final BerthConverter berthConverter;
    private final BerthRepository berthRepository;

    public Long create(BerthDto dto) {
        var berth = new Berth()
                .setOwner(em.getReference(Account.class, OperationContext.accountId()))
                .setIsConfirmed(false);

        berthConverter.toEntity(berth, dto);
        return berthRepository.save(berth).getId();
    }
}
