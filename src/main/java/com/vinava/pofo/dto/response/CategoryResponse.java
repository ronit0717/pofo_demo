package com.vinava.pofo.dto.response;

import com.vinava.pofo.enumeration.CategoryType;
import com.vinava.pofo.model.Category;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CategoryResponse {

    private long id;
    private long clientId;
    private String name;
    private String description;
    private CategoryType categoryType;
    private Long imageId;
    private Long parentCategoryId;
    private Date createdOn;
    private Date updatedOn;

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .clientId(category.getClientId())
                .name(category.getName())
                .description(category.getDescription())
                .categoryType(category.getCategoryType())
                .imageId(category.getImageId())
                .parentCategoryId(category.getParentCategoryId())
                .createdOn(category.getCreatedOn())
                .updatedOn(category.getUpdatedOn())
                .build();
    }

}
