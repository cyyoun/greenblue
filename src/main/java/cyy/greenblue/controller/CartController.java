package cyy.greenblue.controller;

import cyy.greenblue.domain.Cart;
import cyy.greenblue.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public Cart save(@RequestBody Cart cart) {
        return cartService.add(cart);
    }

    @PostMapping("/delete")
    public String delete(@RequestBody List<Cart> carts) {
        cartService.deleteAll(carts);
        return "삭제되었습니다.";
    }

    @PostMapping("/edit")
    public Cart edit(@RequestBody Cart cart) {
        return cartService.edit(cart);
    }
}
