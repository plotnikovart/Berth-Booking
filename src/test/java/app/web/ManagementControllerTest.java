package app.web;

import app.config.security.OperationContext;
import app.database.entity.Berth;
import app.database.entity.BerthApplication;
import app.database.entity.enums.BerthApplicationStatus;
import app.database.repository.AbstractAccountTest;
import app.database.repository.BerthApplicationRepository;
import app.database.repository.BerthRepository;
import app.service.berth.dto.BerthApplicationFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static app.database.entity.enums.BerthApplicationStatus.*;

class ManagementControllerTest extends AbstractAccountTest {

    @Autowired
    BerthRepository berthRepository;
    @Autowired
    BerthApplicationRepository berthApplicationRepository;
    @Autowired
    ManagementController managementController;

    private Berth berth;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        berthApplicationRepository.deleteAll();
        berthRepository.deleteAll();

        OperationContext.accountId(userAccount.getId());

        berth = new Berth()
                .setOwner(userAccount)
                .setIsConfirmed(false)
                .setLng(0.0)
                .setLat(0.0)
                .setName("Причал");

        berth = berthRepository.save(berth);
    }

    @Test
    void getApplications() {
        Long application0 = createApplication("0", APPROVED);
        Long application1 = createApplication("1", NEW);
        Long application2 = createApplication("2", NEW);
        Long application3 = createApplication("3", IN_PROGRESS);

        OperationContext.accountId(moderatorAccount.getId());

        var filter = new BerthApplicationFilter()
                .setPageNum(0)
                .setPageSize(3);

        var resp = managementController.getApplications(filter).getData().getItems();
        Assertions.assertEquals(3, resp.size());
        Assertions.assertEquals(application3, resp.get(0).getId());


        filter
                .setStatus(IN_PROGRESS);
        resp = managementController.getApplications(filter).getData().getItems();
        Assertions.assertEquals(1, resp.size());
        Assertions.assertEquals(application3, resp.get(0).getId());


        filter
                .setDateFrom(LocalDateTime.now().minusMinutes(1))
                .setDateTo(LocalDateTime.now().plusMinutes(1));
        resp = managementController.getApplications(filter).getData().getItems();
        Assertions.assertEquals(1, resp.size());
        Assertions.assertEquals(application3, resp.get(0).getId());

        BerthApplication app = berthApplicationRepository.findById(application1).orElseThrow();
        app.setModerator(moderatorAccount);
        berthApplicationRepository.save(app);

        filter
                .setOnlyMy(true);
        resp = managementController.getApplications(filter).getData().getItems();
        Assertions.assertEquals(0, resp.size());

        filter
                .setStatus(null);
        resp = managementController.getApplications(filter).getData().getItems();
        Assertions.assertEquals(1, resp.size());
        Assertions.assertEquals(application1, resp.get(0).getId());
    }


    private Long createApplication(String title, BerthApplicationStatus status) {
        var application = new BerthApplication()
                .setBerth(berth)
                .setApplicant(userAccount)
                .setStatus(status)
                .setTitle(title);

        return berthApplicationRepository.save(application).getId();
    }
}
