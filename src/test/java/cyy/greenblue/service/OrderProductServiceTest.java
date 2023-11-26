package cyy.greenblue.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderProductServiceTest {
    @Autowired
    private OrderProductService orderProductService;

    @Test
    public void scheduleTest() {
        orderProductService.autoPurchaseConfirm();
    }
}
