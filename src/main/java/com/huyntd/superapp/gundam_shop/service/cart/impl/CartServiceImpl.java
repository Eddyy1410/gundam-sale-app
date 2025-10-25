package com.huyntd.superapp.gundam_shop.service.cart.impl;

import com.huyntd.superapp.gundam_shop.dto.request.UpdateCartItemRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UpdateCartRequest;
import com.huyntd.superapp.gundam_shop.dto.response.CartResponse;
import com.huyntd.superapp.gundam_shop.mapper.CartMapper;
import com.huyntd.superapp.gundam_shop.model.Cart;
import com.huyntd.superapp.gundam_shop.model.CartItem;
import com.huyntd.superapp.gundam_shop.repository.CartItemRepository;
import com.huyntd.superapp.gundam_shop.repository.CartRepository;
import com.huyntd.superapp.gundam_shop.repository.ProductRepository;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import com.huyntd.superapp.gundam_shop.service.cart.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CartServiceImpl implements CartService {

    UserRepository userRepository;
    ProductRepository productRepository;
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;

    CartMapper cartMapper;

    @Override
    public boolean addToCart(int productId, int userId) {
        var cart = cartRepository.findAllByUserId(userId);

        if (cart == null) {
            Cart newCart = new Cart();
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            newCart.setUser(user);
            newCart.setTotalPrice(BigDecimal.ZERO);
            cart = cartRepository.save(newCart);
        }


        var cartItem = new CartItem();
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        cartItem.setCart(cart);
        cartItem.setProduct(product);

        //Không cho ra lỗi runtime khi không tìm thấy
        var search = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        if(search.isPresent()){
            search.get().setQuantity(search.get().getQuantity() + 1);
            cartItemRepository.save(search.get());
        } else {
            cartItem.setQuantity(1);
            cartItemRepository.save(cartItem);
        }

        cart.setTotalPrice(cart.getTotalPrice().add(cartItem.getProduct().getPrice()));
        cartRepository.save(cart);
        return true;
    }

    @Override
    public boolean removeCart(int productId, int userId) {
        var cart = cartRepository.findAllByUserId(userId);
        var cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        cartItemRepository.delete(cartItem);

        var product = productRepository.findById(productId);
        BigDecimal itemTotal = product.get().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        cart.setTotalPrice(cart.getTotalPrice().subtract(itemTotal));
        cartRepository.save(cart);

        return true;
    }

    @Override
    public CartResponse updateCartItem(int userId, UpdateCartRequest updateCart) {
        var updateList = new ArrayList<CartItem>();
        var totalPrice = BigDecimal.ZERO;
        for(var item : updateCart.getItems()) {
            var cartItem = cartItemRepository.findByCartIdAndProductId(updateCart.getCartId(), item.getProductId())
                    .orElseThrow(() -> new RuntimeException("CartItem not found"));
            var product = productRepository.findById(item.getProductId());
            cartItem.setQuantity(item.getQuantity());

            BigDecimal itemTotal = product.get().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
            updateList.add(cartItem);
        }

        cartItemRepository.saveAll(updateList);

        var cart = cartRepository.findAllByUserId(userId);
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);

        var cartResponse = getCart(userId);
        return cartResponse;
    }

    @Override
    public CartResponse getCart(int userId) {
        var cart = cartRepository.findAllByUserId(userId);
        if (cart == null) {
            new RuntimeException("Cart not found");
        }

        var cartItemList = new ArrayList<UpdateCartItemRequest>();
        var searchList = cartItemRepository.findAllByCartId(cart.getId());
        for (var item : searchList) {
            var cartItem = cartMapper.toCartResponse(item);
            cartItemList.add(cartItem);
        }

        var cartResponse = new CartResponse();
        cartResponse.setItems(cartItemList);
        cartResponse.setCartId(cart.getId());
        cartResponse.setTotalPrice(cart.getTotalPrice());
        cartResponse.setUserId(cart.getUser().getId());
        return cartResponse;
    }
}
