package cyy.greenblue.service;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class AutoScheduleService {
    private final OrderSheetService orderSheetService;
    private final OrderProductService orderProductService;

    @Scheduled(cron = "0 0 10 * * ?")
    public void autoPurchaseConfirm() {
        //7일 전 시간이면서 orderStatus = ORDER_COMPLETE 인 값 가져오기
        for (OrderSheet orderSheet : orderSheetService.findAllByOrderStatus()) {
            for (OrderProduct orderProduct : orderProductService.findAllByOrderSheet(orderSheet)) {
                if (orderProduct.getPurchaseStatus() == PurchaseStatus.PURCHASE_UNCONFIRM) {
                    orderProductService.editPurchaseStatus(orderProduct, PurchaseStatus.NON_ACCRUAL);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public void endOfReview() {
        //구매확정 후 14일이 경과되면 리뷰 작성 종료
        //1.오늘 날짜 기준으로 구매확정 날짜가 14일 이상이면서 리뷰 상태가 UNWRITTEN 인 orderProduct 값 가져오기
        LocalDateTime before14Days = LocalDateTime.now().minusDays(14);
        List<OrderProduct> orderProducts = orderProductService.findAllByTimeAndReviewStatus(before14Days, ReviewStatus.UNWRITTEN);

        //2.리뷰적립금 지급하지 않음 상태로 변경 non_accrual
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.updateReviewStatus(ReviewStatus.NON_ACCRUAL);
        }
    }
}
