package com.onlinebookstore.service.impl;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.onlinebookstore.dto.cartitem.CartItemUpdateDto;
import com.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.onlinebookstore.exception.EntityNotFoundException;
import com.onlinebookstore.mapper.CartItemMapper;
import com.onlinebookstore.mapper.ShoppingCartMapper;
import com.onlinebookstore.model.Book;
import com.onlinebookstore.model.CartItem;
import com.onlinebookstore.model.ShoppingCart;
import com.onlinebookstore.model.User;
import com.onlinebookstore.repository.CartItemRepository;
import com.onlinebookstore.repository.ShoppingCartRepository;
import com.onlinebookstore.repository.UserRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = {ShoppingCartServiceImpl.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ShoppingCartServiceImplTest {
    @MockBean
    private CartItemMapper cartItemMapper;

    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean
    private ShoppingCartMapper shoppingCartMapper;

    @MockBean
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartServiceImpl shoppingCartServiceImpl;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetShoppingCartDtoByUserId() {
        User user = new User();
        user.setDeleted(true);
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        user.setShippingAddress("42 Main St");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setCartItems(new HashSet<>());
        shoppingCartResponseDto.setId(1L);
        shoppingCartResponseDto.setUserId(1L);
        when(shoppingCartMapper.toDto(Mockito.any()))
                .thenReturn(shoppingCartResponseDto);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        Optional<ShoppingCart> ofResult2 = Optional.of(shoppingCart);
        when(shoppingCartRepository.findByUserId(Mockito.<Long>any())).thenReturn(ofResult2);
        assertSame(shoppingCartResponseDto, shoppingCartServiceImpl.getShoppingCartDtoByUserId(1L));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(shoppingCartMapper).toDto(Mockito.any());
        verify(shoppingCartRepository).findByUserId(Mockito.<Long>any());
    }

    @Test
    public void testAddCartItemByUserId() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        Book book = new Book();
        book.setAuthor("JaneDoe");
        book.setCategories(new HashSet<>());
        book.setCoverImage("Cover Image");
        book.setDeleted(true);
        book.setDescription("The characteristics of someone or something");
        book.setId(1L);
        book.setIsbn("Isbn");
        book.setPrice(BigDecimal.valueOf(1L));
        book.setTitle("Dr");

        User user = new User();
        user.setDeleted(true);
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        user.setShippingAddress("42 Main St");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setDeleted(true);
        cartItem.setId(1L);
        cartItem.setQuantity(1);
        cartItem.setShoppingCart(shoppingCart);
        when(cartItemMapper.toEntity(Mockito.<CartItemRequestDto>any())).thenReturn(cartItem);

        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(1L);
        cartItemRequestDto.setQuantity(1);
        assertThrows(RuntimeException.class,
                () -> shoppingCartServiceImpl.addCartItemByUserId(1L, cartItemRequestDto)
        );
        verify(userRepository).findById(Mockito.<Long>any());
        verify(cartItemMapper).toEntity(Mockito.<CartItemRequestDto>any());
    }

    @Test
    public void testUpdateCartItem() {
        User user = new User();
        user.setDeleted(true);
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        user.setShippingAddress("42 Main St");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<ShoppingCart> emptyResult = Optional.empty();
        when(shoppingCartRepository.findByUserId(Mockito.<Long>any())).thenReturn(emptyResult);

        Book book = new Book();
        book.setAuthor("JaneDoe");
        book.setCategories(new HashSet<>());
        book.setCoverImage("Cover Image");
        book.setDeleted(true);
        book.setDescription("The characteristics of someone or something");
        book.setId(1L);
        book.setIsbn("Isbn");
        book.setPrice(BigDecimal.valueOf(1L));
        book.setTitle("Dr");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setDeleted(true);
        cartItem.setId(1L);
        cartItem.setQuantity(1);
        cartItem.setShoppingCart(shoppingCart);
        when(cartItemMapper.toEntity(Mockito.<CartItemUpdateDto>any())).thenReturn(cartItem);

        Book book2 = new Book();
        book2.setAuthor("JaneDoe");
        book2.setCategories(new HashSet<>());
        book2.setCoverImage("Cover Image");
        book2.setDeleted(true);
        book2.setDescription("The characteristics of someone or something");
        book2.setId(1L);
        book2.setIsbn("Isbn");
        book2.setPrice(BigDecimal.valueOf(1L));
        book2.setTitle("Dr");

        ShoppingCart shoppingCart2 = new ShoppingCart();
        shoppingCart2.setCartItems(new HashSet<>());
        shoppingCart2.setId(1L);
        shoppingCart2.setUser(user);

        CartItem cartItem2 = new CartItem();
        cartItem2.setBook(book2);
        cartItem2.setDeleted(true);
        cartItem2.setId(1L);
        cartItem2.setQuantity(1);
        cartItem2.setShoppingCart(shoppingCart2);

        Book book3 = new Book();
        book3.setAuthor("JaneDoe");
        book3.setCategories(new HashSet<>());
        book3.setCoverImage("Cover Image");
        book3.setDeleted(true);
        book3.setDescription("The characteristics of someone or something");
        book3.setId(1L);
        book3.setIsbn("Isbn");
        book3.setPrice(BigDecimal.valueOf(1L));
        book3.setTitle("Dr");

        ShoppingCart shoppingCart3 = new ShoppingCart();
        shoppingCart3.setCartItems(new HashSet<>());
        shoppingCart3.setId(1L);
        shoppingCart3.setUser(user);

        CartItem cartItem3 = new CartItem();
        cartItem3.setBook(book3);
        cartItem3.setDeleted(true);
        cartItem3.setId(1L);
        cartItem3.setQuantity(1);
        cartItem3.setShoppingCart(shoppingCart3);
        when(cartItemRepository.save(Mockito.any())).thenReturn(cartItem3);
        Optional<CartItem> ofResult2 = Optional.of(cartItem2);
        when(cartItemRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        CartItemUpdateDto cartItemUpdateDto = new CartItemUpdateDto();
        cartItemUpdateDto.setQuantity(1);
        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartServiceImpl.updateCartItem(1L, 1L, cartItemUpdateDto));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(shoppingCartRepository).findByUserId(Mockito.<Long>any());
        verify(cartItemMapper).toEntity(Mockito.<CartItemUpdateDto>any());
        verify(cartItemRepository).save(Mockito.any());
        verify(cartItemRepository).findById(Mockito.<Long>any());
    }

    @Test
    public void testDeleteCartItem() {
        User user = new User();
        user.setDeleted(true);
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        user.setShippingAddress("42 Main St");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<ShoppingCart> emptyResult = Optional.empty();
        when(shoppingCartRepository.findByUserId(Mockito.<Long>any())).thenReturn(emptyResult);
        doNothing().when(cartItemRepository).deleteById(Mockito.<Long>any());
        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartServiceImpl.deleteCartItem(1L, 1L)
        );
        verify(userRepository).findById(Mockito.<Long>any());
        verify(shoppingCartRepository).findByUserId(Mockito.<Long>any());
        verify(cartItemRepository).deleteById(Mockito.<Long>any());
    }

    @Test
    public void testGetShoppingCartById() {
        User user = new User();
        user.setDeleted(true);
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        user.setShippingAddress("42 Main St");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        Optional<ShoppingCart> ofResult2 = Optional.of(shoppingCart);
        when(shoppingCartRepository.findByUserId(Mockito.<Long>any())).thenReturn(ofResult2);
        assertSame(shoppingCart, shoppingCartServiceImpl.getShoppingCartById(1L));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(shoppingCartRepository).findByUserId(Mockito.<Long>any());
    }
}
