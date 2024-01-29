package cyy.greenblue.controller;

import cyy.greenblue.dto.CartInputDto;
import cyy.greenblue.dto.CartOutputDto;
import cyy.greenblue.service.CartService;
import cyy.greenblue.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartOutputDto save(@Valid @RequestBody CartInputDto cartInputDto,
                              BindingResult bindingResult,
                              Authentication authentication) {
        ValidationUtil.chkBindingResult(bindingResult);
        return cartService.add(cartInputDto, authentication);
    }

    @PostMapping("/delete")
    public String delete(@RequestBody List<Long> cartIdList, Authentication authentication) {
        cartService.deleteAll(cartIdList, authentication);
        return "ok";
    }

    @PostMapping("/edit")
    public CartOutputDto edit(@RequestBody CartInputDto cartInputDto, Authentication authentication) {
        return cartService.edit(cartInputDto, authentication);
    }

    @GetMapping
    public List<CartOutputDto> cartList(Authentication authentication) {
        return cartService.findAllByAuthentication(authentication);
    }
}
