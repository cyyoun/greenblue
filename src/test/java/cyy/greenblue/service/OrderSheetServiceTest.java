package cyy.greenblue.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderSheetServiceTest {
    @Autowired
    private OrderSheetService orderSheetService;

    @Test
    public void scheduleTest() {
        orderSheetService.updateAutoPurchaseStatus();
    }
}
