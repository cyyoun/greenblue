package cyy.greenblue.service;

import cyy.greenblue.domain.Cart;
import cyy.greenblue.domain.Member;
import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.Product;
import cyy.greenblue.dto.BasicProductDto;
import cyy.greenblue.dto.CartDto;
import cyy.greenblue.dto.ProductMainImgDto;
import cyy.greenblue.repository.CartRepository;
import cyy.greenblue.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ProductMainImgService productMainImgService;

    public Member findMemberByAuthentication(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return principal.getMember();
    }

    public CartDto add(Cart cart, Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);
        //이미 상품이 장바구니에 있으면 수량만 변경
        Cart oriCart = findByMemberAndProduct(member, cart);
        if (oriCart == null) {
            cartRepository.save(cart);
            Cart findCart = findOne(cart.getId());
            return createCartDto(findCart);
        }
        oriCart.updateQuantity(oriCart.getQuantity() + cart.getQuantity());
        edit(oriCart, authentication);
        return createCartDto(oriCart);
    }

    public CartDto createCartDto(Cart cart) {
        Product product = productService.findOne(cart.getProduct().getId());
        ProductMainImgDto mainImgDto = productMainImgService.findDtoByProduct(product);
        BasicProductDto basicProductDto = new BasicProductDto().toDto(product, mainImgDto);
        return new CartDto().toDto(cart, basicProductDto);
    }

    public void deleteAll(List<Long> cartIdList, Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);
        for (Long id : cartIdList) {
            if (findOne(id).getMember().equals(member)) {
                cartRepository.deleteById(id);
            }
        }
    }

    public CartDto edit(Cart cart, Authentication authentication) { //변경할 cart 값
        Member member = findMemberByAuthentication(authentication);
        Cart oriCart = findOne(cart.getId()); //원래 cart 객체
        if (!oriCart.getMember().equals(member)) {
            throw new RuntimeException("인증 이슈");
        }
        oriCart.updateQuantity(cart.getQuantity());
        return createCartDto(oriCart);
    }

    public Cart findByMemberAndProduct(Member member, Cart cart) {
        return cartRepository.findByMemberAndProduct(member, cart.getProduct());
    }

    public List<Cart> findByMember(Member member) {
        return cartRepository.findByMember(member);
    }

    public Cart findOne(long cartItemId) {
        return cartRepository.findById(cartItemId).orElse(null);
    }

    public void editQuantity(OrderProduct orderProduct) {
        Cart cart = findByMember(orderProduct.getMember()).stream()
                .filter(c -> c.getProduct() == orderProduct.getProduct())
                .findAny().orElse(null);
        if (cart == null) {
            return;
        }
        Cart oriCart = findOne(cart.getId());
        int quantity = oriCart.getQuantity() - orderProduct.getQuantity();

        if (quantity == 0) {
            cartRepository.delete(cart);
        } else {
            oriCart.updateQuantity(quantity);
        }
    }

}
