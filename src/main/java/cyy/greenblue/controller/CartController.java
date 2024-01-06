package cyy.greenblue.controller;

import cyy.greenblue.domain.Cart;
import cyy.greenblue.dto.CartDto;
import cyy.greenblue.dto.ProductDto;
import cyy.greenblue.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartDto save(@RequestBody Cart cart, Authentication authentication) {
        return cartService.add(cart, authentication);
    }

    @PostMapping("/delete")
    public String delete(@RequestBody List<Long> cartIdList, Authentication authentication) {
        cartService.deleteAll(cartIdList, authentication);
        return "ok";
    }

    @PostMapping("/edit")
    public CartDto edit(@RequestBody Cart cart, Authentication authentication) {
        return cartService.edit(cart, authentication);
    }

    @GetMapping
    public String cartList(Authentication authentication) {
        return "ok";
    }
}
