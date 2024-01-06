package cyy.greenblue.controller;

import  cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.dto.OrderProductDto;
import cyy.greenblue.service.OrderProductService;
import cyy.greenblue.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderProductService orderProductService;
    private final PointService pointService;

    @PostMapping
    public List<OrderProductDto> order(@RequestBody List<OrderProduct> orderProducts, Authentication authentication) {
        return orderProductService.add(orderProducts, authentication);
    }

    @PostMapping("/cancels")
    public String orderCancels(@RequestBody List<Long> orderProductIdList) {
        orderProductService.cancel(orderProductIdList);
        return "ok";
    }

    @PostMapping("/confirms")
    public String purchaseConfirms(@RequestBody List<OrderProductDto> orderProductDtoList) {
        orderProductService.purchaseConfirm(orderProductDtoList);
        for (OrderProductDto orderProductDto : orderProductDtoList) {
            pointService.addPurchaseConfirmPoint(orderProductDto.getId());
        }
        return "ok";
    }

    @GetMapping
    public List<OrderProductDto> list(Authentication authentication) {
        return  orderProductService.findAllByAuthentication(authentication);
    }
}
