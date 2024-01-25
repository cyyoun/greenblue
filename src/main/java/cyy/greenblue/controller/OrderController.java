package cyy.greenblue.controller;

import cyy.greenblue.dto.OrderProductDto;
import cyy.greenblue.dto.OrderProductInputDto;
import cyy.greenblue.service.OrderProductService;
import cyy.greenblue.service.PointService;
import cyy.greenblue.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderProductService orderProductService;
    private final PointService pointService;

    @PostMapping
    public List<OrderProductDto> order(@Valid @RequestBody List<OrderProductInputDto> orderProductInputDtoList,
                                       BindingResult bindingResult,
                                       Authentication authentication) {
        ValidationUtil.chkBindingResult(bindingResult);
        return orderProductService.add(orderProductInputDtoList, authentication);
    }

    @PostMapping("/cancels")
    public String orderCancels(@RequestBody List<Long> orderProductIdList) {
        orderProductService.cancel(orderProductIdList);
        return "ok";
    }

    @PostMapping("/confirms")
    public String purchaseConfirms(@RequestBody List<Long> orderProductIdList) {
        orderProductService.purchaseConfirm(orderProductIdList);
        pointService.addPurchaseConfirmPoint(orderProductIdList);
        return "ok";
    }

    @GetMapping
    public List<OrderProductDto> list(Authentication authentication) {
        return  orderProductService.findAllByAuthentication(authentication);
    }
}
