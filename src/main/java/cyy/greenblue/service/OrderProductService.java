package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderProductService {
    private final OrderProductRepository orderProductRepository;
    private final OrderSheetService orderSheetService;
    private final ProductService productService;
    private final CartService cartService;

    public List<OrderProduct> add(List<OrderProduct> orderProducts) {
        OrderSheet orderSheet = orderSheetService.save(); //주문서 저장
        for (OrderProduct orderProduct : orderProducts) {
            save(orderProduct, orderSheet); //주문상품 저장(주문서 외래키 일괄주입)

            List<Cart> carts = cartService.findByMember(orderProduct.getMember());
            editQuantityByCart(orderProduct, carts); //장바구니 관리
            editQuantityByProduct(orderProduct, -1); //주문 들어옴 → 재고 감소
        }
        return orderProducts;
    }

    public void save(OrderProduct orderProduct, OrderSheet orderSheet) {
        orderProduct.updateOrderSheet(orderSheet);
        editPointStatus(orderProduct, PointStatus.ORDER_SUCCESS);
        orderProductRepository.save(orderProduct);
    }

    public void cancel(OrderSheet orderSheet) {
        List<OrderProduct> orderProducts = findAllByOrderSheet(orderSheet);
        for (OrderProduct orderProduct : orderProducts) {
            editQuantityByProduct(orderProduct, 1); //주문 취소됨 → 재고 증가
            editPointStatus(orderProduct, PointStatus.ORDER_CANCEL);
        }
    }

    public void editPointStatus(OrderProduct orderProduct, PointStatus pointStatus) {
        orderProduct.updatePointStatus(pointStatus);
    }

    private void editQuantityByProduct(OrderProduct orderProduct, int multiplier) {
        productService.editQuantity(orderProduct.getProduct(), orderProduct.getQuantity() * multiplier);
    }

    private void editQuantityByCart(OrderProduct orderProduct, List<Cart> carts) {
        for (Cart cart : carts) {
            if (cart.getProduct() == orderProduct.getProduct()) {
                cartService.editQuantity(cart, orderProduct.getQuantity());
                break;
            }
        }
    }

    public List<OrderProduct> findAllByOrderSheet(OrderSheet orderSheet) {
        return orderProductRepository.findByOrderSheet(orderSheet);
    }

    public List<OrderProduct> findAllByMember(long memberId) {
        return orderProductRepository.findByMember(memberId);
    }

    public List<OrderProduct> findAllByPointStatus(PointStatus pointStatus) {
        return orderProductRepository.findByPointStatus(pointStatus);
    }

}
