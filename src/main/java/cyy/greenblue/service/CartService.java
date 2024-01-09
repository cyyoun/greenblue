package cyy.greenblue.service;

import cyy.greenblue.domain.Cart;
import cyy.greenblue.domain.Member;
import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.Product;
import cyy.greenblue.dto.CartInputDto;
import cyy.greenblue.dto.ProductOutputDto;
import cyy.greenblue.dto.CartOutputDto;
import cyy.greenblue.dto.ProductMainImgDto;
import cyy.greenblue.exception.SoldOutException;
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

    public CartOutputDto add(CartInputDto cartInputDto, Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);
        Product product = productService.findOne(cartInputDto.getProductId());
        if (product.getQuantity() <= 0) {
            throw new SoldOutException("품절된 상품");
        }
        Cart oriCart = cartRepository.findByMemberAndProduct(member, product);
        if (oriCart != null) {
            oriCart.updateQuantity(oriCart.getQuantity() + cartInputDto.getQuantity());
            oriCart.updateMember(member);
            return convertDto(oriCart);
        }
        Cart cart = toEntity(cartInputDto, member, product);
        return convertDto(cartRepository.save(cart));
    }

    public void deleteAll(List<Long> cartIdList, Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);
        for (Long id : cartIdList) {
            if (findOne(id).getMember().equals(member)) {
                cartRepository.deleteById(id);
            }
        }
    }

    public CartOutputDto edit(CartInputDto cartInputDto, Authentication authentication) { //변경할 cart 값
        Member member = findMemberByAuthentication(authentication);
        Product product = productService.findOne(cartInputDto.getProductId());
        Cart oriCart = findByMemberAndProduct(member, product);
        oriCart.updateQuantity(cartInputDto.getQuantity());
        return convertDto(oriCart);
    }

    public Cart findByMemberAndProduct(Member member, Product product) {
        Cart cart = cartRepository.findByMemberAndProduct(member, product);
        if (cart == null) {
            throw new RuntimeException("변경 상품 존재하지 않음");
        }
        return cart;
    }

    public Cart findOne(long cartItemId) {
        return cartRepository.findById(cartItemId).orElse(null);
    }

    public void editQuantity(OrderProduct orderProduct) {
        Cart oriCart = findByMemberAndProduct(orderProduct.getMember(), orderProduct.getProduct());
        int quantity = oriCart.getQuantity() - orderProduct.getQuantity();

        if (quantity == 0) {
            cartRepository.delete(oriCart);
        } else {
            oriCart.updateQuantity(quantity);
        }
    }

    public CartOutputDto convertDto(Cart cart) {
        Product product = productService.findOne(cart.getProduct().getId());
        ProductMainImgDto mainImgDto = productMainImgService.findDtoByProduct(product);
        ProductOutputDto productOutputDto = productService.convertProductOutputDto(product, mainImgDto);
        return toDto(cart, productOutputDto);
    }

    public CartOutputDto toDto(Cart cart, ProductOutputDto productOutputDto) {
        return CartOutputDto.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .productOutputDto(productOutputDto)
                .build();
    }

    public Cart toEntity(CartInputDto cartInputDto, Member member, Product product) {
        return Cart.builder()
                .quantity(cartInputDto.getQuantity())
                .member(member)
                .product(product)
                .build();
    }
}
