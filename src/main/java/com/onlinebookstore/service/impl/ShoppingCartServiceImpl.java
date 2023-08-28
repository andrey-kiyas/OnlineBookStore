package com.onlinebookstore.service.impl;

import com.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.onlinebookstore.dto.cartitem.CartItemUpdateDto;
import com.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.onlinebookstore.exception.EntityNotFoundException;
import com.onlinebookstore.mapper.CartItemMapper;
import com.onlinebookstore.mapper.ShoppingCartMapper;
import com.onlinebookstore.model.CartItem;
import com.onlinebookstore.model.ShoppingCart;
import com.onlinebookstore.model.User;
import com.onlinebookstore.repository.CartItemRepository;
import com.onlinebookstore.repository.ShoppingCartRepository;
import com.onlinebookstore.repository.UserRepository;
import com.onlinebookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserRepository userRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto getShoppingCartDtoByUserId(Long id) {
        ShoppingCart shoppingCartById = getShoppingCartById(id);
        return shoppingCartMapper.toDto(shoppingCartById);
    }

    @Override
    public ShoppingCartResponseDto addCartItemByUserId(Long id,
                                                       CartItemRequestDto cartItemRequestDto) {
        CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDto);
        ShoppingCart shoppingCart = getShoppingCartById(id);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(getShoppingCartById(id));
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(Long id,
                                                  Long cartItemId,
                                                  CartItemUpdateDto cartItemUpdateDto) {
        CartItem cartItemDb = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by item id: " + cartItemId)
        );
        CartItem cartItemDto = cartItemMapper.toEntity(cartItemUpdateDto);
        cartItemDb.setQuantity(cartItemDto.getQuantity());
        cartItemRepository.save(cartItemDb);
        return shoppingCartMapper.toDto(getShoppingCartById(id));
    }

    @Override
    public ShoppingCartResponseDto deleteCartItem(Long id, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return shoppingCartMapper.toDto(getShoppingCartById(id));
    }

    public ShoppingCart getShoppingCartById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find user by user id: " + id)
        );
        return shoppingCartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by id " + user.getId())
        );
    }
}
