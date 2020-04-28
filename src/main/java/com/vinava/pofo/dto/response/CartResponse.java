package com.vinava.pofo.dto.response;

import com.vinava.pofo.enumeration.CartStatus;
import com.vinava.pofo.enumeration.CategoryType;
import com.vinava.pofo.model.Cart;
import com.vinava.pofo.model.embed.CartEntity;
import com.vinava.pofo.util.ComputationUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Slf4j
public class CartResponse {

    private long id;
    private long clientId;
    private long userId;
    private CartStatus cartStatus;
    private List<CartEntityResponse> cartEntityResponses;

    @Data
    @Builder
    private static class CartEntityResponse {
        private long productId;
        private BigDecimal unitPrice;
        private BigDecimal discountPercentage;
        private BigDecimal taxablePrice;
        private BigDecimal gstPercentage;
        private BigDecimal tax;
        private BigDecimal finalPrice;
        private CategoryType cartEntityType;
    }

    public static CartResponse from(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .clientId(cart.getClientId())
                .userId(cart.getUserId())
                .cartStatus(cart.getCartStatus())
                .cartEntityResponses(getCartEntityResponseSetFrom(cart.getCartEntities()))
                .build();
    }

    private static List<CartEntityResponse> getCartEntityResponseSetFrom(List<CartEntity> cartEntities) {
        List<CartEntityResponse> responseList = new ArrayList<>();
        for (CartEntity cartEntity : cartEntities) {
            BigDecimal unitPrice = cartEntity.getUnitPrice();
            BigDecimal discountPercentage = cartEntity.getDiscountPercentage();
            BigDecimal taxablePrice = ComputationUtil.getDiscountedPrice(unitPrice, discountPercentage);
            BigDecimal finalPrice = ComputationUtil.getFinalAmount(taxablePrice, cartEntity.getGstPercentage());
            BigDecimal tax = finalPrice.subtract(taxablePrice);
            CartEntityResponse cartEntityResponse = CartEntityResponse.builder()
                    .productId(cartEntity.getProductId())
                    .unitPrice(unitPrice)
                    .discountPercentage(discountPercentage)
                    .taxablePrice(taxablePrice)
                    .gstPercentage(cartEntity.getGstPercentage())
                    .tax(tax)
                    .finalPrice(finalPrice)
                    .cartEntityType(cartEntity.getCartEntityType())
                    .build();
            responseList.add(cartEntityResponse);
        }
        return responseList;
    }

    private static List<CartResponse> from(List<Cart> carts) {
        List<CartResponse> cartResponses = new ArrayList<>();
        for (Cart cart: carts) {
            cartResponses.add(from(cart));
        }
        return cartResponses;
    }

    public static ResponseEntity<List<CartResponse>> getResponseEntityFrom(List<Cart> carts) {
        try {
            List<CartResponse> clientResponses = from(carts);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(clientResponses.size()));
            headers.add("Access-Control-Expose-Headers", "X-Total-Count");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(clientResponses);
        } catch (Exception e) {
            log.error("In exception block of getResponseEntityFrom for list of carts: {}", carts, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
