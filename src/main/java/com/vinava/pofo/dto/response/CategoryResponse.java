package com.vinava.pofo.dto.response;

import com.vinava.pofo.enumeration.CategoryType;
import com.vinava.pofo.model.Category;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@Slf4j
public class CategoryResponse {

    private long id;
    private long clientId;
    private String name;
    private String description;
    private CategoryType categoryType;
    private String image;
    private Long parentCategoryId;
    private int level;
    private Date createdOn;
    private Date updatedOn;

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .clientId(category.getClientId())
                .name(category.getName())
                .description(category.getDescription())
                .categoryType(category.getCategoryType())
                .image(category.getImage())
                .parentCategoryId(category.getParentCategoryId())
                .level(category.getLevel())
                .createdOn(category.getCreatedOn())
                .updatedOn(category.getUpdatedOn())
                .build();
    }

    private static List<CategoryResponse> from(List<Category> categories) {
        List<CategoryResponse> clientResponses = new LinkedList<>();
        for (Category category: categories) {
            clientResponses.add(from(category));
        }
        return clientResponses;
    }

    public static ResponseEntity<List<CategoryResponse>> getResponseEntityFrom(List<Category> categories) {
        try {
            List<CategoryResponse> clientResponses = from(categories);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(clientResponses.size()));
            headers.add("Access-Control-Expose-Headers", "X-Total-Count");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(clientResponses);
        } catch (Exception e) {
            log.error("In exception block of getResponseEntityFrom for list of categories: {}", categories, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
