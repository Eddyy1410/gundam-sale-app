package com.huyntd.superapp.gundam_shop.service.cart.impl;

import com.huyntd.superapp.gundam_shop.dto.enums.CountType;
import com.huyntd.superapp.gundam_shop.dto.request.UpdateCartItemRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UpdateCartRequest;
import com.huyntd.superapp.gundam_shop.dto.response.CartItemResponse;
import com.huyntd.superapp.gundam_shop.dto.response.CartResponse;
import com.huyntd.superapp.gundam_shop.dto.wsResponse.CountResponse;
import com.huyntd.superapp.gundam_shop.mapper.CartMapper;
import com.huyntd.superapp.gundam_shop.model.Cart;
import com.huyntd.superapp.gundam_shop.model.CartItem;
import com.huyntd.superapp.gundam_shop.repository.CartItemRepository;
import com.huyntd.superapp.gundam_shop.repository.CartRepository;
import com.huyntd.superapp.gundam_shop.repository.ProductRepository;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import com.huyntd.superapp.gundam_shop.service.cart.CartService;
import com.huyntd.superapp.gundam_shop.service.wsNotification.WsNotificationService;
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
    final WsNotificationService wsNotificationService;

    CartMapper cartMapper;

    @Override
    public boolean addToCart(int productId, int userId) {
        var cart = cartRepository.findAllByUserId(userId);
        int newItemCount = 0;

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
        for(var item : cart.getCartItems()) {
            newItemCount += item.getQuantity();
        }
        cartRepository.save(cart);
        wsNotificationService.sendUnreadCountUpdate(userId, newItemCount, CountType.QUANTITY_CART);
        return true;
    }

    @Override
    public boolean removeCart(int productId, int userId) {
        int newItemCount = 0;
        var cart = cartRepository.findAllByUserId(userId);
        var cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        cartItemRepository.delete(cartItem);

        var product = productRepository.findById(productId);
        BigDecimal itemTotal = product.get().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        cart.setTotalPrice(cart.getTotalPrice().subtract(itemTotal));
        for(var item : cart.getCartItems()) {
            newItemCount += item.getQuantity();
        }
        cartRepository.save(cart);
        wsNotificationService.sendUnreadCountUpdate(userId, newItemCount, CountType.QUANTITY_CART);
        return true;
    }

    @Override
    public CartResponse updateCartItem(int userId, UpdateCartRequest updateCart) {
        var updateList = new ArrayList<CartItem>();
        var totalPrice = BigDecimal.ZERO;
        int newItemCount = 0;

        for(var item : updateCart.getItems()) {
            var cartItem = cartItemRepository.findByCartIdAndProductId(updateCart.getCartId(), item.getProductId())
                    .orElseThrow(() -> new RuntimeException("CartItem not found"));
            var product = productRepository.findById(item.getProductId());
            cartItem.setQuantity(item.getQuantity());
            newItemCount += item.getQuantity();

            BigDecimal itemTotal = product.get().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
            updateList.add(cartItem);
        }

        cartItemRepository.saveAll(updateList);

        var cart = cartRepository.findAllByUserId(userId);
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
        var cartResponse = getCart(userId);

        wsNotificationService.sendUnreadCountUpdate(userId, newItemCount, CountType.QUANTITY_CART);
        return cartResponse;
    }

    @Override
    public CartResponse getCart(int userId) {
        var cart = cartRepository.findAllByUserId(userId);
        if (cart == null) {
            new RuntimeException("Cart not found");
        }

        var cartItemList = new ArrayList<CartItemResponse>();
        var searchList = cartItemRepository.findAllByCartId(cart.getId()).orElseThrow(
                () -> new RuntimeException("CartItem not found")
        );
        for (var item : searchList) {
            var cartItem = cartMapper.toCartResponse(item);
            var product = productRepository.findById(cartItem.getProductId());
            cartItem.setProductName(product.get().getName());
            cartItem.setProductImage(product.get().getProductImages().get(0).getImageUrl());
            cartItem.setProductPrice(product.get().getPrice().doubleValue());
            cartItemList.add(cartItem);
        }

        var cartResponse = new CartResponse();
        cartResponse.setItems(cartItemList);
        cartResponse.setCartId(cart.getId());
        cartResponse.setTotalPrice(cart.getTotalPrice());
        cartResponse.setUserId(cart.getUser().getId());
        return cartResponse;
    }

    @Override
    public CountResponse countItems(int userId) {
        int count = 0;
        var cart = cartRepository.findAllByUserId(userId);
        if (cart == null) {
            new RuntimeException("Cart not found");
        }

        var searchList = cartItemRepository.findAllByCartId(cart.getId()).orElse(null);
        if (searchList == null) {
            return CountResponse.builder()
                    .count(0)
                    .type(CountType.QUANTITY_CART)
                    .build();
        }
        if(searchList.isEmpty()) {}
        for (var item : searchList) {
            count += item.getQuantity();
        }

        return CountResponse.builder()
                .count(count)
                .type(CountType.QUANTITY_CART)
                .build();
    }
}
