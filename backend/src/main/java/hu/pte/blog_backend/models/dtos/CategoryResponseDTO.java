package hu.pte.blog_backend.models.dtos;

import hu.pte.blog_backend.models.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryResponseDTO {
    private Integer id;
    private String name;

    public CategoryResponseDTO(Category category){
        this.id = category.getCategory_id();
        this.name = category.getName();
    }
}
