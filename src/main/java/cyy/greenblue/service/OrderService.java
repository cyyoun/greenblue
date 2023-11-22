package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.repository.OrderProductRepository;
import cyy.greenblue.repository.OrderSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderSheetRepository orderSheetRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductService productService;
    private final CartService cartService;

    public String add(List<OrderProduct> orderProducts, OrderSheet orderSheet) {
        /**
         * 결제 확인 로직 필요
         */
        if (orderSheet.getStatus().equals("success")) {
            orderSheetRepository.save(orderSheet);

            Member member = orderProducts.stream().findAny().get().getMember();
            List<Cart> carts = cartService.findByMember(member);

            /**
             * 재고관리 및 외래키 값 주입
             */
            for (OrderProduct orderProduct : orderProducts) {
                Product product = orderProduct.getProduct();
                productService.editQuantity(product, orderProduct.getQuantity()); //상품재고관리
                orderProduct.updateOrderSheet(orderSheet); //주문서 외래키 일괄 주입

                for (Cart cart : carts) { //장바구니 재고관리
                    if (cart.getProduct().getId() == product.getId()) {
                        cartService.editQuantity(cart, orderProduct.getQuantity());
                        break;
                    }
                }
            }
            orderProductRepository.saveAll(orderProducts);
            return "success";
        }
        return "failure";
    }

    public List<OrderProduct> findAllByOrderSheet(OrderSheet orderSheet) {
        return orderProductRepository.findByOrderSheet(orderSheet);
    }

}
