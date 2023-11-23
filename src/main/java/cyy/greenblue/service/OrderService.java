package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.repository.OrderProductRepository;
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
public class OrderService {

    private final OrderSheetRepository orderSheetRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductService productService;
    private final CartService cartService;

    public OrderSheet add(List<OrderProduct> orderProducts) {

        //주문서 저장
        OrderSheet orderSheet = saveOrderSheet();

        //주문상품 저장(주문서 외래키 일괄주입)
        saveOrderProducts(orderProducts, orderSheet);

        //회원, 장바구니 정보 가져오기
        Member member = orderProducts.stream().findAny().get().getMember();
        List<Cart> carts = cartService.findByMember(member);

        //재고관리
        delStock(orderProducts, carts);
        return orderSheet;
    }

    private OrderSheet saveOrderSheet() {
        OrderSheet orderSheet = new OrderSheet(OrderStatus.SUCCESS);
        orderSheetRepository.save(orderSheet);
        return orderSheet;
    }

    private void saveOrderProducts(List<OrderProduct> orderProducts, OrderSheet orderSheet) {
        orderProducts.forEach(orderProduct -> orderProduct.updateOrderSheet(orderSheet));
        orderProductRepository.saveAll(orderProducts);
    }

    private void delStock(List<OrderProduct> orderProducts, List<Cart> carts) {
        for (OrderProduct orderProduct : orderProducts) {
            Product product = orderProduct.getProduct();
            int quantity = -orderProduct.getQuantity(); //음수로 변경

            //상품 재고관리 (빼기)
            productService.editQuantity(product, quantity);

            //장바구니 재고관리
            for (Cart cart : carts) {
                if (cart.getProduct().getId() == product.getId()) {
                    cartService.editQuantity(cart, quantity);
                    break;
                }
            }
        }
    }

    public void cancel(long orderSheetId) {
        OrderSheet orderSheet = findOrderSheet(orderSheetId);
        orderSheet.updateStatus(OrderStatus.CANCEL);

        List<OrderProduct> orderProducts = findAllByOrderSheet(orderSheet);
        addStock(orderProducts);
    }

    private void addStock(List<OrderProduct> orderProducts) {
        //상품 재고관리 (더하기)
        orderProducts.forEach(orderProduct ->
                productService.editQuantity(orderProduct.getProduct(), orderProduct.getQuantity()));
    }

    public OrderSheet findOrderSheet(long orderSheetId) {
        return orderSheetRepository.findById(orderSheetId).orElseThrow();
    }
    public List<OrderProduct> findAllByOrderSheet(OrderSheet orderSheet) {
        return orderProductRepository.findByOrderSheet(orderSheet);
    }

    public List<OrderProduct> findAllByMember(long memberId) {
        return orderProductRepository.findByMember(memberId);
    }

    public List<OrderSheet> findAllByRegDate(int amountToSubtract, OrderStatus orderStatus) {
        LocalDateTime now = LocalDateTime.now(); //현재 시각
        LocalDateTime hoursAgo = now.minus(amountToSubtract, ChronoUnit.HOURS); //now - amountToSubtract 시각
        return orderSheetRepository.findByRegDateAndStatus(hoursAgo, orderStatus);
    }

    public int calcPoint(OrderProduct orderProduct) {
        double percent = (double) orderProduct.getMember().getGrade().getPercent() / 100;
        int calcPrice = orderProduct.getProduct().getPrice() * orderProduct.getQuantity();
        return (int) (percent * calcPrice);
    }
}
