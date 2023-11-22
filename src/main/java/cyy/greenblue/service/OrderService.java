package cyy.greenblue.service;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.OrderSheet;
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

    public String add(List<OrderProduct> orderProducts, OrderSheet orderSheet) {
        /**
         * 결제 확인 로직 필요
         */
        if (orderSheet.getStatus().equals("success")) {
            orderSheetRepository.save(orderSheet);

            orderProducts.forEach(orderProduct -> orderProduct.updateOrderSheet(orderSheet));
            orderProductRepository.saveAll(orderProducts);
            return "success";
        }
        return "failure";
    }

    public OrderProduct findOrderProduct(long orderProductId) {
        return orderProductRepository.findById(orderProductId).orElseThrow();
    }


    public List<OrderProduct> findAllByOrderSheet(OrderSheet orderSheet) {
        return orderProductRepository.findByOrderSheet(orderSheet);
    }

    public OrderSheet findOrderSheet(long orderSheetId) {
        return orderSheetRepository.findById(orderSheetId).orElseThrow();
    }

}
