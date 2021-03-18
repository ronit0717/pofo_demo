package com.vinava.pofo.dto.request;

import com.vinava.pofo.enumeration.CategoryType;
import com.vinava.pofo.model.Category;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class CategoryRequest {

    private Long parentCategoryId;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private CategoryType categoryType;

    private String image;

    @Size(max = 1000)
    private String description;

    public Category from(long clientId) {
        return Category.builder()
                .clientId(clientId)
                .parentCategoryId(this.parentCategoryId)
                .name((this.name))
                .categoryType(this.categoryType)
                .image(this.image)
                .description(this.description)
                .build();
    }

}
