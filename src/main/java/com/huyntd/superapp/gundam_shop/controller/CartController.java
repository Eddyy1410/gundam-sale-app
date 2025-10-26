package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.dto.request.UpdateCartRequest;
import com.huyntd.superapp.gundam_shop.dto.response.CartResponse;
import com.huyntd.superapp.gundam_shop.service.cart.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/cart")
public class CartController {

    CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<CartResponse>> getCart(@PathVariable int userId) {
        var result = cartService.getCart(userId);
        return ResponseEntity.ok(
                ApiResponse.<CartResponse>builder()
                        .result(result)
                        .build()
        );
    }

    @PostMapping()
    public ResponseEntity<?> addToCart(@RequestParam int productId, @RequestParam int userId) {
        boolean success = cartService.addToCart(productId, userId);
        return ResponseEntity.ok(
                ApiResponse.<Boolean>builder()
                        .result(success)
                        .build()
        );
    }

    @DeleteMapping()
    public ResponseEntity<?> removeCart(@RequestParam int productId, @RequestParam int userId) {
        boolean success = cartService.removeCart(productId, userId);
        return ResponseEntity.ok(
                ApiResponse.<Boolean>builder()
                        .result(success)
                        .build()
        );
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<?> updateCartItem(@PathVariable int userId, @RequestBody UpdateCartRequest updateCart) {
        var cart = cartService.updateCartItem(userId, updateCart);
        return ResponseEntity.ok(
                ApiResponse.<CartResponse>builder()
                        .result(cart)
                        .build()
        );
    }
}
