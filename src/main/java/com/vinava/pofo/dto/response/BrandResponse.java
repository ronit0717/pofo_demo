package com.vinava.pofo.dto.response;

import com.vinava.pofo.model.Brand;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Data
@Builder
@Slf4j
public class BrandResponse {

    private long id;
    private long clientId;
    private String name;
    private String brandLogo;
    private String brandLink;
    private String description;
    private Date createdOn;
    private Date updatedOn;

    public static BrandResponse from(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .clientId(brand.getClientId())
                .name(brand.getName())
                .description(brand.getDescription())
                .brandLogo(brand.getBrandLogo())
                .brandLink(brand.getBrandLink())
                .createdOn(brand.getCreatedOn())
                .updatedOn(brand.getUpdatedOn())
                .build();
    }

    private static List<BrandResponse> from(List<Brand> brands) {
        List<BrandResponse> brandResponses = new LinkedList<>();
        for (Brand brand : brands) {
            brandResponses.add(from(brand));
        }
        return brandResponses;
    }

    public static ResponseEntity<List<BrandResponse>> getResponseEntityFrom(List<Brand> brands) {
        try {
            List<BrandResponse> brandResponses = from(brands);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(brandResponses.size()));
            headers.add("Access-Control-Expose-Headers", "X-Total-Count");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(brandResponses);
        } catch (Exception e) {
            log.error("In exception block of getResponseEntityFrom for list of brands: {}", brands, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
