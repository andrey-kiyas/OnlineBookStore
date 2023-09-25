package com.onlinebookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.onlinebookstore.dto.cartitem.CartItemResponseDto;
import com.onlinebookstore.dto.cartitem.CartItemUpdateDto;
import com.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.onlinebookstore.model.Role;
import com.onlinebookstore.model.User;
import jakarta.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;

    @Resource
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/add-users-carts-items-before-testing.sql")
            );
        }
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/remove-users-carts-items-after-testing.sql")
            );
        }
    }

    @Order(1)
    @Test
    @DisplayName("Get shopping cart")
    void getShoppingCart_ValidRequest_Success() throws Exception {
        // Given
        User mockUser = getMockUser();
        ShoppingCartResponseDto expected = getShoppingCartResponseDto();

        // When
        MvcResult result = mockMvc.perform(get("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(mockUser)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartResponseDto.class);
        assertEquals(expected, actual);
    }

    @Order(2)
    @Test
    @DisplayName("Add book to shopping cart")
    void addBookToShoppingCart_ValidRequest_Success() throws Exception {
        // Given
        User mockUser = getMockUser();

        CartItemRequestDto expected = new CartItemRequestDto();
        expected.setBookId(2L);
        expected.setQuantity(11);

        String jsonRequest = objectMapper.writeValueAsString(expected);

        // When
        MvcResult result = mockMvc.perform(post("/cart")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(mockUser)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartResponseDto.class);
        assertNotNull(actual);
        assertThat(actual.getCartItems()).hasSize(3);
        assertTrue(actual.getCartItems().stream()
                .anyMatch(c -> expected.getBookId().equals((c.getBookId()))
                        && expected.getQuantity() == c.getQuantity()));
    }

    @Order(3)
    @Test
    @DisplayName("Update cart item")
    void updateCartItem_ValidRequest_Success() throws Exception {
        // Given
        User mockUser = getMockUser();

        Long cartItemId = 3L;
        CartItemUpdateDto expected = new CartItemUpdateDto();
        expected.setQuantity(25);

        String jsonRequest = objectMapper.writeValueAsString(expected);

        // When
        MvcResult result = mockMvc.perform(put("/cart/cart-items/" + cartItemId)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(mockUser)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartResponseDto.class);
        assertNotNull(actual);
        assertThat(actual.getCartItems()).hasSize(3);
        assertTrue(actual.getCartItems().stream()
                .anyMatch(c -> cartItemId.equals((c.getBookId()))
                        && expected.getQuantity() == c.getQuantity()));
    }

    @Order(4)
    @Test
    @DisplayName("Delete cart item")
    void deleteCartItem_ValidRequest_Success() throws Exception {
        // Given
        User mockUser = getMockUser();

        long cartItemId = 1L;

        // When
        MvcResult result = mockMvc.perform(delete("/cart/cart-items/" + cartItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(mockUser)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartResponseDto.class);
        assertNotNull(actual);
        assertThat(actual.getCartItems()).hasSize(2);
    }

    private ShoppingCartResponseDto getShoppingCartResponseDto() {
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setId(3L);
        shoppingCartResponseDto.setUserId(3L);

        CartItemResponseDto cartItemResponseDto1 = new CartItemResponseDto();
        cartItemResponseDto1.setId(3L);
        cartItemResponseDto1.setBookId(3L);
        cartItemResponseDto1.setBookTitle("Sample Book 3");
        cartItemResponseDto1.setQuantity(2);

        CartItemResponseDto cartItemResponseDto2 = new CartItemResponseDto();
        cartItemResponseDto2.setId(2L);
        cartItemResponseDto2.setBookId(1L);
        cartItemResponseDto2.setBookTitle("Sample Book 1");
        cartItemResponseDto2.setQuantity(1);

        Set<CartItemResponseDto> cartItems = new HashSet<>();
        cartItems.add(cartItemResponseDto2);
        cartItems.add(cartItemResponseDto1);
        shoppingCartResponseDto.setCartItems(cartItems);
        return shoppingCartResponseDto;
    }

    private User getMockUser() {
        User user = new User();
        user.setId(3L);
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(Role.RoleName.USER);
        roles.add(role);
        user.setRoles(roles);
        return user;
    }
}
