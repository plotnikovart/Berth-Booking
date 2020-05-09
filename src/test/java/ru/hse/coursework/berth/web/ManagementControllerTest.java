package ru.hse.coursework.berth.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.BerthApplication;
import ru.hse.coursework.berth.database.entity.enums.BerthApplicationStatus;
import ru.hse.coursework.berth.database.repository.AbstractAccountTest;
import ru.hse.coursework.berth.database.repository.BerthApplicationRepository;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.service.berth.dto.BerthApplicationFilter;
import ru.hse.coursework.berth.service.berth.dto.management.BerthApplicationDecision;

import java.time.LocalDateTime;

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
        Long application0 = createApplication("0", BerthApplicationStatus.APPROVED);
        Long application1 = createApplication("1", BerthApplicationStatus.NEW);
        Long application2 = createApplication("2", BerthApplicationStatus.NEW);
        Long application3 = createApplication("3", BerthApplicationStatus.IN_PROGRESS);

        OperationContext.accountId(moderatorAccount.getId());

        var filter = new BerthApplicationFilter()
                .setPageNum(0)
                .setPageSize(3);

        var resp = managementController.getApplications(filter).getData().getItems();
        Assertions.assertEquals(3, resp.size());
        Assertions.assertEquals(application3, resp.get(0).getId());


        filter
                .setStatus(BerthApplicationStatus.IN_PROGRESS);
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

    @Test
    void changeStatus() {
        Long applicationId = createApplication("Причал", BerthApplicationStatus.NEW);
        Long berthId = berth.getId();

        BerthApplication application;

        OperationContext.accountId(moderatorAccount.getId());

        managementController.startWorkWithApplication(applicationId);
        application = berthApplicationRepository.findById(applicationId).orElseThrow();
        berth = berthRepository.findById(berthId).orElseThrow();

        Assertions.assertEquals(BerthApplicationStatus.IN_PROGRESS, application.getStatus());
        Assertions.assertEquals(moderatorAccount.getId(), application.getModerator().getId());
        Assertions.assertNull(application.getDecision());
        Assertions.assertFalse(berth.getIsConfirmed());

        var decision = new BerthApplicationDecision().setDecision("Плохая заявка");

        managementController.rejectApplication(applicationId, decision);
        application = berthApplicationRepository.findById(applicationId).orElseThrow();
        berth = berthRepository.findById(berthId).orElseThrow();

        Assertions.assertEquals(BerthApplicationStatus.REJECTED, application.getStatus());
        Assertions.assertEquals(moderatorAccount.getId(), application.getModerator().getId());
        Assertions.assertEquals(decision.getDecision(), application.getDecision());
        Assertions.assertFalse(berth.getIsConfirmed());


        managementController.approveApplication(applicationId, decision);
        application = berthApplicationRepository.findById(applicationId).orElseThrow();
        berth = berthRepository.findById(berthId).orElseThrow();

        Assertions.assertEquals(BerthApplicationStatus.APPROVED, application.getStatus());
        Assertions.assertEquals(moderatorAccount.getId(), application.getModerator().getId());
        Assertions.assertEquals(decision.getDecision(), application.getDecision());
        Assertions.assertTrue(berth.getIsConfirmed());

        managementController.startWorkWithApplication(applicationId);
        application = berthApplicationRepository.findById(applicationId).orElseThrow();
        berth = berthRepository.findById(berthId).orElseThrow();

        Assertions.assertEquals(BerthApplicationStatus.IN_PROGRESS, application.getStatus());
        Assertions.assertEquals(moderatorAccount.getId(), application.getModerator().getId());
        Assertions.assertNull(application.getDecision());
        Assertions.assertFalse(berth.getIsConfirmed());
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
