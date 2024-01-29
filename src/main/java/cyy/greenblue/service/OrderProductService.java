package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.domain.status.OrderStatus;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import cyy.greenblue.dto.OrderProductDto;
import cyy.greenblue.dto.OrderProductInputDto;
import cyy.greenblue.exception.PurchaseConfirmException;
import cyy.greenblue.repository.OrderProductRepository;
import cyy.greenblue.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumSet;
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
        return orderProducts.stream().map(this::toDto).toList();
    }

    public OrderProduct findOne(long orderProductId) {
        return orderProductRepository.findById(orderProductId).orElseThrow();
    }

    public List<OrderProductDto> add(List<OrderProductInputDto> orderProductInputDtoList,
                                     Authentication authentication) {
        OrderSheet orderSheet = orderSheetService.save(); //주문서 저장
        Member member = findMemberByAuthentication(authentication);
        List<OrderProduct> orderProducts = orderProductInputDtoList.stream().map(orderProductInputDto -> {
            Product product = productService.findOne(orderProductInputDto.getProductId());
            OrderProduct orderProduct = toEntity(orderProductInputDto, orderSheet, member, product);
            orderProductRepository.save(orderProduct);

            cartService.editQuantity(orderProduct);
            editQuantityByProduct(orderProduct, -1); //주문 들어옴 → 재고 감소
            return orderProduct;
        }).toList();

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
                throw new PurchaseConfirmException("구매확정 상품이 존재합니다.");
            }
        }
    }

    public void purchaseConfirm(List<Long> orderProductIdList) {
        List<OrderProduct> orderProducts = orderProductIdList.stream().map(this::findOne).toList();

        for (OrderProduct orderProduct : orderProducts) {
            PurchaseStatus purchaseStatus = orderProduct.getPurchaseStatus();
            if (purchaseStatus == PurchaseStatus.PURCHASE_UNCONFIRM &&
                    orderProduct.getOrderSheet().getOrderStatus() == OrderStatus.ORDER_COMPLETE) {
                editPurchaseStatus(orderProduct, PurchaseStatus.PURCHASE_CONFIRM);

            } else if (EnumSet.of(PurchaseStatus.PURCHASE_CONFIRM, PurchaseStatus.ACCRUAL,
                    PurchaseStatus.NON_ACCRUAL).contains(purchaseStatus)) {
                throw new PurchaseConfirmException("이미 구매확정된 상품입니다.");

            } else if (purchaseStatus == PurchaseStatus.PURCHASE_CANCEL){
                throw new PurchaseConfirmException("취소한 상품입니다.");

            } else {
                throw new PurchaseConfirmException("구매확정 실패");
            }
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
        Collections.reverse(orderProducts);
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

    public OrderProduct toEntity(OrderProductInputDto inputDto, OrderSheet orderSheet, Member member, Product product) {
        return OrderProduct.builder()
                .quantity(inputDto.getQuantity())
                .orderSheet(orderSheet)
                .member(member)
                .product(product)
                .reviewStatus(ReviewStatus.UNWRITTEN)
                .purchaseStatus(PurchaseStatus.PURCHASE_UNCONFIRM)
                .purchaseDate(LocalDateTime.now())
                .build();
    }

    public OrderProductDto toDto(OrderProduct orderProduct) {
        long productId = orderProduct.getProduct().getId();
        String mainImgFilename = productService.findProductDtoById(productId).getMainImgDto().getFilename();


        return OrderProductDto.builder()
                .id(orderProduct.getId())
                .productName(orderProduct.getProduct().getName())
                .productPrice(orderProduct.getProduct().getPrice())
                .regDate(orderProduct.getOrderSheet().getRegDate())
                .reviewStatus(orderProduct.getReviewStatus())
                .purchaseStatus(orderProduct.getPurchaseStatus())
                .quantity(orderProduct.getQuantity())
                .productId(productId)
                .mainImgFilename(mainImgFilename)
                .orderSheetId(orderProduct.getOrderSheet().getId())
                .build();
    }
}