package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.domain.status.OrderStatus;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import cyy.greenblue.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderProductService {
    private final OrderProductRepository orderProductRepository;
    private final OrderSheetService orderSheetService;
    private final ProductService productService;
    private final CartService cartService;

    @Scheduled(cron = "0 0 10 * * ?")
    public void autoPurchaseConfirm() {
        //7일 전 시간이면서 orderStatus = ORDER_COMPLETE 인 값 가져오기
        for (OrderSheet orderSheet : orderSheetService.findAllByOrderStatus()) {
            for (OrderProduct orderProduct : findAllByOrderSheet(orderSheet)) {
                if (orderProduct.getPurchaseStatus() == PurchaseStatus.PURCHASE_UNCONFIRM) {
                    orderProduct.updatePurchaseStatus(PurchaseStatus.NON_ACCRUAL);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public void endOfReview() {
        //구매확정 후 14일이 경과되면 리뷰 작성 종료
        //1.오늘 날짜 기준으로 구매확정 날짜가 14일 이상이면서 리뷰 상태가 UNWRITTEN 인 orderProduct 값 가져오기
        LocalDateTime before14Days = LocalDateTime.now().minusDays(14);
        List<OrderProduct> orderProducts = findAllByTimeAndReviewStatus(before14Days, ReviewStatus.UNWRITTEN);

        //2.리뷰적립금 지급하지 않음 상태로 변경 non_accrual
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.updateReviewStatus(ReviewStatus.NON_ACCRUAL);
        }
    }

    public void allPurchaseConfirm(long orderSheetId) {
        OrderSheet orderSheet = orderSheetService.findOne(orderSheetId);
        List<OrderProduct> orderProducts = findAllByOrderSheet(orderSheet);

        //sheet 단위로 한 번에 구매확정하기 위함
        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.getPurchaseStatus() != PurchaseStatus.PURCHASE_UNCONFIRM)
                throw new IllegalArgumentException("구매확정된 상품이 있습니다.");
            if (orderProduct.getOrderSheet().getOrderStatus() != OrderStatus.ORDER_COMPLETE)
               throw new IllegalArgumentException("취소된 상품이 있습니다.");
        }
        for (OrderProduct orderProduct : orderProducts) {
            editPurchaseConfirm(orderProduct);
        }
    }

    public void onePurchaseConfirm(long orderProductId) {
        OrderProduct orderProduct = findOne(orderProductId);
        OrderStatus orderStatus = orderProduct.getOrderSheet().getOrderStatus();
        PurchaseStatus purchaseStatus = orderProduct.getPurchaseStatus();

        if (purchaseStatus == PurchaseStatus.PURCHASE_UNCONFIRM && orderStatus == OrderStatus.ORDER_COMPLETE)
            editPurchaseConfirm(orderProduct);
        else if (purchaseStatus != PurchaseStatus.PURCHASE_UNCONFIRM)
            throw new IllegalArgumentException("이미 구매확정된 상품입니다.");
        else
            throw new IllegalArgumentException("취소된 상품입니다.");
    }
    public void editPurchaseConfirm(OrderProduct orderProduct) {
        orderProduct.updatePurchaseConfirm(PurchaseStatus.PURCHASE_CONFIRM, LocalDateTime.now());
    }

    public OrderProduct findOne(long orderProductId) {
        return orderProductRepository.findById(orderProductId).orElseThrow();
    }

    public List<OrderProduct> add(List<OrderProduct> orderProducts) {
        OrderSheet orderSheet = orderSheetService.save(); //주문서 저장
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.updateOrderSheet(orderSheet);  //주문상품 저장(주문서 외래키 일괄주입)
            orderProductRepository.save(orderProduct);

            List<Cart> carts = cartService.findByMember(orderProduct.getMember());
            editQuantityByCart(orderProduct, carts); //장바구니 관리
            editQuantityByProduct(orderProduct, -1); //주문 들어옴 → 재고 감소
        }
        return orderProducts;
    }

    public void editQuantityByCart(OrderProduct orderProduct, List<Cart> carts) {
        for (Cart cart : carts) {
            if (cart.getProduct() == orderProduct.getProduct()) {
                cartService.editQuantity(cart, orderProduct.getQuantity());
                break;
            }
        }
    }

    public void editQuantityByProduct(OrderProduct orderProduct, int multiplier) {
        productService.editQuantity(orderProduct.getProduct(), orderProduct.getQuantity() * multiplier);
    }

    public void allCancel(long orderSheetId) {
        OrderSheet orderSheet = orderSheetService.findOne(orderSheetId);
        List<OrderProduct> orderProducts = findAllByOrderSheet(orderSheet);

        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.getPurchaseStatus() == PurchaseStatus.PURCHASE_UNCONFIRM) {
                editPurchaseStatus(orderProduct, PurchaseStatus.ALL_CANCEL);
                editQuantityByProduct(orderProduct, 1); //주문 취소됨 → 재고 증가
            } else {
                throw new IllegalArgumentException("구매확정된 상품이 존재합니다.");
            }
        }
        orderSheetService.allCancel(orderSheet);
    }

    public void oneCancel(long orderProductId) {
        OrderProduct orderProduct = findOne(orderProductId);
        PurchaseStatus purchaseStatus = orderProduct.getPurchaseStatus();

        if (purchaseStatus == PurchaseStatus.PURCHASE_UNCONFIRM) {
            editPurchaseStatus(orderProduct, PurchaseStatus.PART_CANCEL);

            OrderSheet orderSheet = orderProduct.getOrderSheet();
            orderSheetService.oneCancel(orderSheet);
        } else {
            throw new IllegalArgumentException("구매확정으로 취소가 불가합니다.");
        }
    }

    public void editPurchaseStatus(OrderProduct orderProduct, PurchaseStatus purchaseStatus) {
        orderProduct.updatePurchaseStatus(purchaseStatus);
    }

    public List<OrderProduct> findAllByOrderSheet(OrderSheet orderSheet) {
        return orderProductRepository.findByOrderSheet(orderSheet);
    }

    public List<OrderProduct> findAllByMemberId(long memberId) {
        return orderProductRepository.findByMemberId(memberId);
    }

    public List<OrderProduct> findAllByProductIdAndReviewStatus(long productId, List<ReviewStatus> reviewStatuses) {
        return orderProductRepository.fidByProductId(productId, reviewStatuses);
    }

    public List<OrderProduct> findAllByTimeAndReviewStatus(LocalDateTime before14Days, ReviewStatus reviewStatus) {
        return orderProductRepository.findByTimeAndReviewStatus(before14Days, reviewStatus);
    }

}