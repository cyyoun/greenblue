package cyy.greenblue.controller;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.dto.OrderProductDto;
import cyy.greenblue.dto.OrderRequestDto;
import cyy.greenblue.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    private List<OrderProductDto> changeDto(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(orderProduct -> new OrderProductDto(
                        orderProduct.getId(),
                        orderProduct.getQuantity(),
                        orderProduct.getMember().getId(),
                        orderProduct.getProduct().getId(),
                        orderProduct.getOrderSheet().getId()))
                .collect(Collectors.toList());

    }

    @PostMapping
    public ResponseEntity<Object> order(@RequestBody OrderRequestDto requestDto) {
        String paymentResult = requestDto.getPaymentResult();
        List<OrderProduct> orderProducts = requestDto.getOrderProducts();

        if (paymentResult.equals("success")) { //결제 성공인 경우
            OrderSheet orderSheet = orderService.add(orderProducts);
            List<OrderProduct> getOrderProducts = orderService.findAllByOrderSheet(orderSheet);
            List<OrderProductDto> orderProductDtos = changeDto(getOrderProducts);
            return ResponseEntity.status(HttpStatus.OK).body(orderProductDtos);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 실패");
    }

    @PostMapping("/{orderSheetId}")
    public ResponseEntity<Object> cancel(@PathVariable long orderSheetId, @RequestBody OrderRequestDto requestDto) {
        String paymentResult = requestDto.getPaymentResult();

        if (paymentResult.equals("success")) {
            orderService.cancel(orderSheetId);
            return ResponseEntity.status(HttpStatus.OK).body("취소 성공");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("취소 실패");
    }

    @GetMapping("/member/{memberId}")
    public List<OrderProductDto> list(@PathVariable long memberId) {
        List<OrderProduct> orderProducts = orderService.findAllByMember(memberId);
        List<OrderProductDto> orderProductDtos = changeDto(orderProducts);
        return orderProductDtos;
    }

}
