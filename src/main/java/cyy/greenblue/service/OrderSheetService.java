package cyy.greenblue.service;

import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.domain.status.OrderStatus;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.repository.OrderSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderSheetService {

    private static final int AMOUNT_SUBTRACT_7DAY = 168;
    private final OrderSheetRepository orderSheetRepository;

    @Scheduled(cron = "0 0 10 * * ?")
    public void updateAutoPurchaseStatus() { //7일 뒤 자동 구매확정
        List<OrderSheet> orderSheets = findAfterTimeAndStatus(AMOUNT_SUBTRACT_7DAY, OrderStatus.SUCCESS);
        orderSheets.forEach(orderSheet -> updatePurchaseStatus(orderSheet));
    }

    private static void updatePurchaseStatus(OrderSheet orderSheet) {
        orderSheet.updatePurchaseStatus(PurchaseStatus.SUCCESS);
    }

    public void updateMemberPurchaseStatus(long orderSheetId) {
        OrderSheet orderSheet = findOne(orderSheetId);
        updatePurchaseStatus(orderSheet);
    }


    public OrderSheet findOne(long orderSheetId) {
        return orderSheetRepository.findById(orderSheetId).orElseThrow();
    }

    public OrderSheet save() {
        OrderSheet orderSheet = new OrderSheet();
        orderSheetRepository.save(orderSheet);
        return findOne(orderSheet.getId());
    }

    public OrderSheet cancel(long orderSheetId) {
        OrderSheet orderSheet = findOne(orderSheetId);
        orderSheet.updateOrderStatus(OrderStatus.CANCEL);
        return orderSheet;
    }

    public List<OrderSheet> findBeforeTimeAndStatus(int amountToSubtract, OrderStatus orderStatus) {
        /**
         * ex)
         * 현재 시각 ~ amountToSubtract(3시간) 전 시각에서
         * 상태가 orderStatus(SUCCESS)인 OrderSheet 목록 가져오기
         */
        LocalDateTime hoursAgo = getHoursAgo(amountToSubtract);
        return orderSheetRepository.findBeforeTimeAndStatus(hoursAgo, orderStatus);
    }
    public List<OrderSheet> findAfterTimeAndStatus(int amountToSubtract, OrderStatus orderStatus) {
        LocalDateTime hoursAgo = getHoursAgo(amountToSubtract);
        return orderSheetRepository.findAfterTimeAndStatus(hoursAgo, orderStatus);
    }

    private LocalDateTime getHoursAgo(int amountToSubtract) {
        LocalDateTime now = LocalDateTime.now(); //현재 시각
        LocalDateTime hoursAgo = now.minus(amountToSubtract, ChronoUnit.HOURS); //now - amountToSubtract 시각
        return hoursAgo;
    }
}
