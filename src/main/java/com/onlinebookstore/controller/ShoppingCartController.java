package com.onlinebookstore.controller;

import com.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.onlinebookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for shopping cart management")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Retrieve user's shopping cart",
            description = "Retrieve user's shopping cart")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        String email = authentication.getName();
        return shoppingCartService.getShoppingCart(email);
    }

    @Operation(summary = "Add book to the shopping cart",
            description = "Add book to the shopping cart")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto addBookToShoppingCart(
            Authentication authentication,
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        String email = authentication.getName();
        shoppingCartService.addCartItem(email, cartItemRequestDto);
        return shoppingCartService.getShoppingCart(email);
    }

    @Operation(summary = "Update quantity of a book in the shopping cart",
            description = "Update quantity of a book in the shopping cart")
    @PutMapping("cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto updateCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemRequestDto requestDto) {
        String email = authentication.getName();
        shoppingCartService.updateCartItem(email, cartItemId, requestDto);
        return shoppingCartService.getShoppingCart(email);
    }

    @Operation(summary = "Remove a book from the shopping cart",
            description = "Remove a book from the shopping cart")
    @DeleteMapping("cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto deleteCartItem(Authentication authentication,
                                                  @PathVariable Long cartItemId) {
        String email = authentication.getName();
        shoppingCartService.deleteCartItem(email, cartItemId);
        return shoppingCartService.getShoppingCart(email);
    }
}
