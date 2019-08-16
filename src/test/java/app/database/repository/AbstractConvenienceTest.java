package app.database.repository;

import app.database.entity.Convenience;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class AbstractConvenienceTest extends AbstractAccountTest {

    protected Convenience conv1;
    protected Convenience conv2;
    protected Convenience conv3;

    @Autowired
    ConvenienceRepository convenienceRepository;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        convenienceRepository.deleteAll();

        conv1 = new Convenience()
                .setId(1)
                .setCode("1")
                .setName("1");

        conv2 = new Convenience()
                .setId(2)
                .setCode("2")
                .setName("2");

        conv3 = new Convenience()
                .setId(3)
                .setCode("3")
                .setName("3");

        convenienceRepository.saveAll(List.of(conv1, conv2, conv3));
        commitAndStartNewTransaction();
    }
}
