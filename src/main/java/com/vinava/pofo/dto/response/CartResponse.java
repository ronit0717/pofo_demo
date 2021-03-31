package com.vinava.pofo.dto.response;

import com.vinava.pofo.enumeration.CartStatus;
import com.vinava.pofo.enumeration.CategoryType;
import com.vinava.pofo.model.Cart;
import com.vinava.pofo.model.embed.CartEntity;
import com.vinava.pofo.service.StockService;
import com.vinava.pofo.util.ComputationUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Builder
@Slf4j
@Data
public class CartResponse {

    private long id;
    private long clientId;
    private Long userId;
    private CartStatus cartStatus;
    private List<CartEntityResponse> cartEntityResponses;

    @Data
    @Builder
    public static class CartEntityResponse {
        private StockResponse stock;
        private BigDecimal unitPrice;
        private BigDecimal quantity;
        private BigDecimal discountPercentage;
        private BigDecimal taxablePrice;
        private BigDecimal gstPercentage;
        private BigDecimal tax;
        private BigDecimal finalPrice;
        private CategoryType cartEntityType;
    }

    public static CartResponse from(Cart cart, StockService stockService) {
        return CartResponse.builder()
                .id(cart.getId())
                .clientId(cart.getClientId())
                .userId(cart.getUserId())
                .cartStatus(cart.getCartStatus())
                .cartEntityResponses(getCartEntityResponseSetFrom(cart.getCartEntities(), stockService, cart.getClientId()))
                .build();
    }

    private static List<CartEntityResponse> getCartEntityResponseSetFrom
            (List<CartEntity> cartEntities, StockService stockService, long clientId) {
        List<CartEntityResponse> responseList = new LinkedList<>();
        for (CartEntity cartEntity : cartEntities) {
            BigDecimal unitPrice = cartEntity.getUnitPrice();
            BigDecimal discountPercentage = cartEntity.getDiscountPercentage();
            BigDecimal taxablePrice = ComputationUtil.getDiscountedPrice(unitPrice, discountPercentage);
            BigDecimal finalPrice = ComputationUtil.getFinalAmount(taxablePrice, cartEntity.getGstPercentage());
            BigDecimal tax = finalPrice.subtract(taxablePrice);
            StockResponse stockResponse = stockService.getStockById(cartEntity.getStockId(), clientId);
            CartEntityResponse cartEntityResponse = CartEntityResponse.builder()
                    .stock(stockResponse)
                    .quantity(cartEntity.getQuantity())
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

    private static List<CartResponse> from(List<Cart> carts, StockService stockService) {
        List<CartResponse> cartResponses = new ArrayList<>();
        for (Cart cart: carts) {
            cartResponses.add(from(cart, stockService));
        }
        return cartResponses;
    }

    public static ResponseEntity<List<CartResponse>> getResponseEntityFrom(List<Cart> carts, StockService stockService) {
        try {
            List<CartResponse> clientResponses = from(carts, stockService);
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
