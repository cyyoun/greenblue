package cyy.greenblue.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Test
    public void pointSchedulerTest() {
        pointService.orderPointAdd();
    }

    @Test
    void pointSchedulerTest2() {
        pointService.orderPointDel();

    }
}
