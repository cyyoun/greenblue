package cyy.greenblue.controller;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.dto.OrderProductDto;
import cyy.greenblue.dto.OrderRequestDto;
import cyy.greenblue.service.OrderProductService;
import cyy.greenblue.service.OrderSheetService;
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

    private final OrderProductService orderProductService;

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
            List<OrderProduct> savedOrderProducts = orderProductService.add(orderProducts);
            List<OrderProductDto> orderProductDtos = changeDto(savedOrderProducts);
            return ResponseEntity.status(HttpStatus.OK).body(orderProductDtos);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문에 실패하였습니다.");
    }

    @PostMapping("/{orderSheetId}/cancel") //주문 취소
    public ResponseEntity<Object> allCancel(@PathVariable long orderSheetId) {
        orderProductService.allCancel(orderSheetId);
        return ResponseEntity.status(HttpStatus.OK).body("주문을 취소하였습니다.");
    }

    @PostMapping("/{orderProductId}/cancel")
    public ResponseEntity<Object> oneCancel(@PathVariable long orderProductId) {
        orderProductService.oneCancel(orderProductId);
        return ResponseEntity.status(HttpStatus.OK).body("주문을 취소하였습니다.");
    }
    @PostMapping("/{orderSheetId}/confirm") //구매 상품 전부 구매 확정
    public ResponseEntity<Object> allPurchaseConfirm(@PathVariable long orderSheetId) {
        orderProductService.allPurchaseConfirm(orderSheetId);
        return ResponseEntity.status(HttpStatus.OK).body("구매확정 되었습니다.");
    }

    @PostMapping("/{orderProductId}/confirm") //구매 확정
    public ResponseEntity<Object> onePurchaseConfirm(@PathVariable long orderProductId) {
        orderProductService.onePurchaseConfirm(orderProductId);
        return ResponseEntity.status(HttpStatus.OK).body("구매확정 되었습니다.");
    }

    @GetMapping("/member/{memberId}")
    public List<OrderProductDto> list(@PathVariable long memberId) {
        List<OrderProduct> orderProducts = orderProductService.findAllByMember(memberId);
        List<OrderProductDto> orderProductDtos = changeDto(orderProducts);
        return orderProductDtos;
    }

}
