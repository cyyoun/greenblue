package cyy.greenblue.controller;

import cyy.greenblue.domain.CartItem;
import cyy.greenblue.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartItem add(@RequestBody CartItem cartItem) {
        return cartService.save(cartItem);
    }

    @DeleteMapping("{cartItemId}")
    public void delete(@PathVariable long cartItemId) {
        cartService.delete(cartItemId);
    }

    @PostMapping("{cartItemId}")
    public CartItem edit(@PathVariable long cartItemId, @RequestBody CartItem cartItem) {
        CartItem item = cartService.changeQuantity(cartItem, cartItemId);
        return item;
    }
}
