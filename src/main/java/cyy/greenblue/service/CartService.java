package cyy.greenblue.service;

import cyy.greenblue.domain.Cart;
import cyy.greenblue.domain.CartItem;
import cyy.greenblue.repository.CartItemRepository;
import cyy.greenblue.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    public CartItem save(CartItem cartItem) {
        // Cart의 ArraList<CartItem> 가져오기
        Cart cart = cartRepository.findById(cartItem.getCart().getId()).orElse(null);
        List<CartItem> cartItems = cart.getCartItems();

        // Product 중복 값이 있는지 확인하기
        CartItem item = cartItems.stream()
                .filter(i -> i.getProduct().getId() == cartItem.getProduct().getId())
                .findAny()
                .orElse(null);


        if (item == null) { //신규 상품 추가
            cartItems.add(cartItem);
            return cartItemRepository.save(cartItem);
        } else { //상품 누적 (수량 증가)
            item.setQuantity(cartItem.getQuantity() + item.getQuantity());
            return cartItem;
        }
    }

    public String delete(long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return "삭제되었습니다...";
    }

    public CartItem changeQuantity(CartItem cartItem, long cartItemId) {
        CartItem item = findOne(cartItemId);
        int quantity = cartItem.getQuantity();
        if (quantity == 0) {
            delete(cartItemId);
            return null;
        }
        item.setQuantity(quantity);
        return cartItem;
    }

    public CartItem findOne(long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElse(null);
    }

}
