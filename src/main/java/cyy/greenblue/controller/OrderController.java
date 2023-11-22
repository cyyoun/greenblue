package cyy.greenblue.controller;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.dto.OrderProductDto;
import cyy.greenblue.dto.OrderRequestDto;
import cyy.greenblue.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Object> order(@RequestBody OrderRequestDto requestDto) {
        String paymentResult = requestDto.getPaymentResult();
        List<OrderProduct> orderProducts = requestDto.getOrderProducts();

        if (paymentResult.equals("success")) {
            OrderSheet orderSheet = orderService.add(orderProducts);
            List<OrderProductDto> list = orderService.findAllByOrderSheet(orderSheet).stream()
                    .map(orderProduct -> modelMapper.map(orderProduct, OrderProductDto.class))
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(list);
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


}
