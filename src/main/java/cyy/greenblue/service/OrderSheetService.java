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

    public void allCancel(OrderSheet orderSheet) {
        OrderStatus orderStatus = orderSheet.getOrderStatus();
        DeliveryStatus deliveryStatus = orderSheet.getDeliveryStatus();

        if (orderStatus == OrderStatus.ORDER_COMPLETE &&
                deliveryStatus == DeliveryStatus.DELIVERY_ACCEPT) {
            editOrderStatus(orderSheet, OrderStatus.ALL_CANCEL);
        } else if (deliveryStatus != DeliveryStatus.DELIVERY_ACCEPT) {
            throw new IllegalArgumentException("배송 상태에서는 취소가 불가합니다.");
        } else {
            throw new IllegalArgumentException("이미 취소가 된 주문 건입니다.");
        }
    }

    public void oneCancel(OrderSheet orderSheet) {
        OrderStatus orderStatus = orderSheet.getOrderStatus();
        DeliveryStatus deliveryStatus = orderSheet.getDeliveryStatus();

        if (orderStatus.equals(OrderStatus.ORDER_COMPLETE) && !deliveryStatus.equals(DeliveryStatus.DELIVERY_START)) {
            editOrderStatus(orderSheet, OrderStatus.PART_CANCEL);
        } else if (deliveryStatus == DeliveryStatus.DELIVERY_START) {
            throw new IllegalArgumentException("배송 상태에서는 취소가 불가합니다.");
        } else {
            throw new IllegalArgumentException("이미 취소가 된 주문 건입니다.");
        }
    }

    public void editOrderStatus(OrderSheet orderSheet, OrderStatus orderStatus) {
        orderSheet.updateOrderStatus(orderStatus);
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
        return now.minus(amountToSubtract, ChronoUnit.HOURS); //now - amountToSubtract 시각
    }
}
