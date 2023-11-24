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

    public OrderSheet add(List<OrderProduct> orderProducts) {
        OrderSheet orderSheet = orderSheetService.save(); //주문서 저장
        saveAll(orderProducts, orderSheet); //주문상품 저장(주문서 외래키 일괄주입)

        //회원, 장바구니 정보 가져오기
        Member member = orderProducts.stream().findAny().get().getMember();
        List<Cart> carts = cartService.findByMember(member);

        delOrderQuantityByProduct(orderProducts); //재고관리
        editOrderQuantityByCart(orderProducts, carts); //장바구니 관리
        return orderSheet;
    }

    public void saveAll(List<OrderProduct> orderProducts, OrderSheet orderSheet) {
        orderProducts.forEach(orderProduct -> orderProduct.updateOrderSheet(orderSheet));
        orderProductRepository.saveAll(orderProducts);
    }

    private void editOrderQuantityByProduct(List<OrderProduct> orderProducts, int multiplier) {
        orderProducts.forEach(orderProduct ->
                productService.editQuantity(orderProduct.getProduct(),
                        orderProduct.getQuantity() * multiplier));
    }

    public void delOrderQuantityByProduct(List<OrderProduct> orderProducts) {
        editOrderQuantityByProduct(orderProducts, -1); //주문 들어옴 → 재고 감소
    }
    public void addOrderQuantityByProduct(List<OrderProduct> orderProducts) {
        editOrderQuantityByProduct(orderProducts, 1); //주문 취소됨 → 재고 증가
    }

    private void editOrderQuantityByCart(List<OrderProduct> orderProducts, List<Cart> carts) {
        for (OrderProduct orderProduct : orderProducts) {
            for (Cart cart : carts) {
                if (cart.getProduct() == orderProduct.getProduct()) {
                    cartService.editQuantity(cart, orderProduct.getQuantity());
                    break;
                }
            }
        }
    }

    public List<OrderProduct> findAllByOrderSheet(OrderSheet orderSheet) {
        return orderProductRepository.findByOrderSheet(orderSheet);
    }

    public List<OrderProduct> findAllByMember(long memberId) {
        return orderProductRepository.findByMember(memberId);
    }
}
