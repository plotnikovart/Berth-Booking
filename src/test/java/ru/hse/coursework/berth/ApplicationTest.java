package ru.hse.coursework.berth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.support.TransactionTemplate;

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