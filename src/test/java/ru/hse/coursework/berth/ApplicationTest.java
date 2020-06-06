package ru.hse.coursework.berth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.support.TransactionTemplate;
import ru.hse.coursework.berth.service.account.facebook.FacebookAuthClient;
import ru.hse.coursework.berth.service.email.EmailSender;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public abstract class ApplicationTest {

    @Autowired
    protected TransactionTemplate tpl;

    @Autowired
    protected ObjectMapper mapper;

    @MockBean
    private Clock clock;

    @MockBean
    protected FacebookAuthClient facebookAuthClientMock;
    @MockBean
    protected EmailSender emailSenderMock;

    @BeforeEach
    public void setUp() {
        LocalDate today = LocalDate.of(2020, 6, 21);
        Clock fixedClock = Clock.fixed(today.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }

    protected void commitAndStartNewTransaction() {
        commitTransaction();
        startTransaction();
    }

    protected void startTransaction() {
        if (!TestTransaction.isActive()) {
            TestTransaction.start();
        }
    }

    protected void commitTransaction() {
        if (TestTransaction.isActive()) {
            TestTransaction.flagForCommit();
            TestTransaction.end();
        }
    }
}