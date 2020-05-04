package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.support.TransactionTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public abstract class ApplicationTest {

    @Autowired
    protected TransactionTemplate tpl;

    @Autowired
    protected ObjectMapper mapper;

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