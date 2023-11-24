package cyy.greenblue.service;

import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.domain.OrderStatus;
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

    private final OrderSheetRepository orderSheetRepository;

    public OrderSheet findOne(long orderSheetId) {
        return orderSheetRepository.findById(orderSheetId).orElseThrow();
    }

    public OrderSheet save() {
        OrderSheet orderSheet = new OrderSheet(OrderStatus.SUCCESS);
        orderSheetRepository.save(orderSheet);
        return orderSheet;
    }

    public OrderSheet cancel(long orderSheetId) {
        OrderSheet orderSheet = findOne(orderSheetId);
        orderSheet.updateStatus(OrderStatus.CANCEL);
        return orderSheet;
    }

    public List<OrderSheet> findAllByRegDate(int amountToSubtract, OrderStatus orderStatus) {
        /**
         * 현재 시각 ~ amountTOSubtract(3시간) 전 시각에서
         * 상태가 orderStatus(success)인 OrderSheet 목록 가져오기
         */
        LocalDateTime now = LocalDateTime.now(); //현재 시각
        LocalDateTime hoursAgo = now.minus(amountToSubtract, ChronoUnit.HOURS); //now - amountToSubtract 시각
        return orderSheetRepository.findByRegDateAndStatus(hoursAgo, orderStatus);
    }
}
