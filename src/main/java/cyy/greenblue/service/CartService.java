package cyy.greenblue.service;

import cyy.greenblue.domain.Cart;
import cyy.greenblue.domain.Member;
import cyy.greenblue.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;

    public Cart add(Cart cart) {
        //이미 상품이 장바구니에 있으면 수량만 변경
        Cart oriCart = findByMemberAndProduct(cart);
        if (oriCart != null) {
            int sum = oriCart.getQuantity() + cart.getQuantity();
            oriCart.updateQuantity(sum);
            return edit(oriCart);
        }
        return cartRepository.save(cart);
    }

    public void deleteAll(List<Cart> carts) {
        cartRepository.deleteAll(carts);
    }

    public void deleteOne(Cart cart) {
        cartRepository.delete(cart);
    }

    public Cart edit(Cart cart) { //변경할 cart 값
        Cart oriCart = findOne(cart.getId());
        oriCart.updateCart(cart.getQuantity(), cart.getProduct());
        return oriCart;
    }

    public Cart findByMemberAndProduct(Cart cart) {
        return cartRepository.findByMemberAndProduct(cart.getMember(), cart.getProduct());
    }

    public List<Cart> findByMember(Member member) {
        return cartRepository.findByMember(member);
    }

    public void editQuantity(Cart cart, int quantity) {
        Cart oriCart = findOne(cart.getId());
        int newQuantity = oriCart.getQuantity() + quantity;

        if (newQuantity == 0) {
            deleteOne(oriCart);
        } else {
            oriCart.updateQuantity(newQuantity);
        }
    }

    public Cart findOne(long cartItemId) {
        return cartRepository.findById(cartItemId).orElse(null);
    }

}
