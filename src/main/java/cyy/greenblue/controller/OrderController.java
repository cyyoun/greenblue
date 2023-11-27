package cyy.greenblue.controller;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.dto.OrderProductDto;
import cyy.greenblue.dto.OrderRequestDto;
import cyy.greenblue.service.OrderProductService;
import cyy.greenblue.service.OrderSheetService;
import cyy.greenblue.service.PointService;
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
    private final PointService pointService;
    private final OrderSheetService orderSheetService;

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

    @PostMapping("/{orderSheetId}/cancels") //주문 취소
    public ResponseEntity<Object> allCancel(@PathVariable long orderSheetId) {
        orderProductService.allCancel(orderSheetId);
        return ResponseEntity.status(HttpStatus.OK).body("주문을 취소하였습니다.");
    }

    @PostMapping("/{orderProductId}/cancel")
    public ResponseEntity<Object> oneCancel(@PathVariable long orderProductId) {
        orderProductService.oneCancel(orderProductId);
        return ResponseEntity.status(HttpStatus.OK).body("주문을 취소하였습니다.");
    }
    @PostMapping("/{orderSheetId}/confirms") //구매 상품 전부 구매 확정
    public ResponseEntity<Object> allPurchaseConfirm(@PathVariable long orderSheetId) {
        orderProductService.allPurchaseConfirm(orderSheetId);
        OrderSheet orderSheet = orderSheetService.findOne(orderSheetId);
        List<OrderProduct> orderProducts = orderProductService.findAllByOrderSheet(orderSheet);
        for (OrderProduct orderProduct : orderProducts) {
            pointService.add(orderProduct.getId());
        }
        return ResponseEntity.status(HttpStatus.OK).body("구매확정 되었습니다.");
    }

    @PostMapping("/{orderProductId}/confirm") //구매 확정
    public ResponseEntity<Object> onePurchaseConfirm(@PathVariable long orderProductId) {
        orderProductService.onePurchaseConfirm(orderProductId);
        pointService.add(orderProductId);
        return ResponseEntity.status(HttpStatus.OK).body("구매확정 되었습니다.");
    }

    @GetMapping("/member/{memberId}")
    public List<OrderProductDto> list(@PathVariable long memberId) {
        List<OrderProduct> orderProducts = orderProductService.findAllByMemberId(memberId);
        List<OrderProductDto> orderProductDtos = changeDto(orderProducts);
        return orderProductDtos;
    }

}
