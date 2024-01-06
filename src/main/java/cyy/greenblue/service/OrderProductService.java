package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.domain.status.OrderStatus;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import cyy.greenblue.dto.OrderProductDto;
import cyy.greenblue.repository.OrderProductRepository;
import cyy.greenblue.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    public List<OrderProductDto> convertDtoList(List<OrderProduct> orderProducts) {
        return orderProducts.stream().map(orderProduct -> new OrderProductDto().toDto(orderProduct)).toList();
    }

    public OrderProduct findOne(long orderProductId) {
        return orderProductRepository.findById(orderProductId).orElseThrow();
    }

    public List<OrderProductDto> add(List<OrderProduct> orderProducts, Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);

        OrderSheet orderSheet = orderSheetService.save(); //주문서 저장
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.updateOrderSheet(orderSheet);  //주문상품 저장(주문서 외래키 일괄주입)
            orderProduct.updateMember(member);
            orderProductRepository.save(orderProduct);

            cartService.editQuantity(orderProduct);
            editQuantityByProduct(orderProduct, -1); //주문 들어옴 → 재고 감소
        }
        return convertDtoList(orderProducts);
    }

    public void cancel(List<Long> orderProductIdList) {
        List<OrderProduct> orderProducts = orderProductIdList.stream().map(this::findOne).toList();

        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.getPurchaseStatus() == PurchaseStatus.PURCHASE_UNCONFIRM &&
                    OrderSheetService.cancelConfirm(orderProduct.getOrderSheet())) {
                editPurchaseStatus(orderProduct, PurchaseStatus.PURCHASE_CANCEL);
                editQuantityByProduct(orderProduct, 1); //주문 취소됨 → 재고 증가
            } else {
                throw new IllegalArgumentException("구매확정 상품이 존재합니다.");
            }
        }
    }

    public void purchaseConfirm(List<OrderProductDto> orderProductDtoList) {
        for (OrderProductDto orderProductDto : orderProductDtoList) {
            OrderSheet orderSheet = orderSheetService.findOne(orderProductDto.getOrderSheetId());
            OrderStatus orderStatus = orderSheet.getOrderStatus();
            PurchaseStatus purchaseStatus = orderProductDto.getPurchaseStatus();

            if (purchaseStatus == PurchaseStatus.PURCHASE_UNCONFIRM && orderStatus == OrderStatus.ORDER_COMPLETE) {
                OrderProduct orderProduct = findOne(orderProductDto.getId());
                editPurchaseStatus(orderProduct, PurchaseStatus.PURCHASE_CONFIRM);
            } else if (purchaseStatus != PurchaseStatus.PURCHASE_UNCONFIRM)
                throw new IllegalArgumentException("이미 구매확정된 상품입니다.");
            else
                throw new IllegalArgumentException("취소된 상품입니다.");
        }
    }

    public void editPurchaseStatus(OrderProduct orderProduct, PurchaseStatus purchaseStatus) {
        orderProduct.updatePurchaseStatus(purchaseStatus);
    }

    public void editReviewStatus(OrderProduct orderProduct, ReviewStatus reviewStatus) {
        orderProduct.updateReviewStatus(reviewStatus);
    }

    public void editQuantityByProduct(OrderProduct orderProduct, int multiplier) {
        productService.editQuantity(orderProduct.getProduct(), orderProduct.getQuantity() * multiplier);
    }

    public List<OrderProduct> findAllByOrderSheet(OrderSheet orderSheet) {
        return orderProductRepository.findByOrderSheet(orderSheet);
    }

    public List<OrderProductDto> findAllByAuthentication(Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);
        List<OrderProduct> orderProducts = orderProductRepository.findByMember(member);
        return convertDtoList(orderProducts);
    }

    private Member findMemberByAuthentication(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return principal.getMember();
    }

    public List<OrderProduct> findAllByProductIdAndReviewStatus(long productId, List<ReviewStatus> reviewStatuses) {
        return orderProductRepository.findByProductId(productId, reviewStatuses);
    }

    public List<OrderProduct> findAllByTimeAndReviewStatus(LocalDateTime before14Days, ReviewStatus reviewStatus) {
        return orderProductRepository.findByTimeAndReviewStatus(before14Days, reviewStatus);
    }
}