package cyy.greenblue.service;

import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.domain.status.DeliveryStatus;
import cyy.greenblue.domain.status.OrderStatus;
import cyy.greenblue.repository.OrderSheetRepository;
import lombok.RequiredArgsConstructor;
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

    public List<OrderSheet> findAllByOrderStatus() { //7일 뒤 자동 구매확정
        return findAfterTimeAndStatus(AMOUNT_SUBTRACT_7DAY, OrderStatus.ORDER_COMPLETE);
    }

    public OrderSheet findOne(long orderSheetId) {
        return orderSheetRepository.findById(orderSheetId).orElseThrow();
    }

    public OrderSheet save() {
        OrderSheet orderSheet = new OrderSheet();
        orderSheetRepository.save(orderSheet);
        return findOne(orderSheet.getId());
    }

    public static boolean cancelConfirm(OrderSheet orderSheet) {
        OrderStatus orderStatus = orderSheet.getOrderStatus();
        DeliveryStatus deliveryStatus = orderSheet.getDeliveryStatus();

        if (orderStatus == OrderStatus.ORDER_COMPLETE && deliveryStatus == DeliveryStatus.DELIVERY_ACCEPT) {
            return true;
        } else if (deliveryStatus == DeliveryStatus.DELIVERY_START) {
            throw new RuntimeException("배송 상태에서는 취소가 불가합니다.");
        } else {
            throw new RuntimeException("알 수 없는 오류입니다.");
        }
    }

    public List<OrderSheet> findAllByTimeRange(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        return orderSheetRepository.findByTimeRange(fromDateTime, toDateTime, OrderStatus.ORDER_COMPLETE);
    }

    public List<OrderSheet> findAfterTimeAndStatus(int amountToSubtract, OrderStatus orderStatus) {
        LocalDateTime hoursAgo = getHoursAgo(amountToSubtract);
        return orderSheetRepository.findAfterTimeAndStatus(hoursAgo, orderStatus);
    }

    private LocalDateTime getHoursAgo(int amountToSubtract) {
        LocalDateTime now = LocalDateTime.now(); //현재 시각
        return now.minus(amountToSubtract, ChronoUnit.HOURS); //now - amountToSubtract 시각
    }
}
