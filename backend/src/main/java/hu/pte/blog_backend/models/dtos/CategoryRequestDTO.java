package hu.pte.blog_backend.models.dtos;

import hu.pte.blog_backend.models.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class CategoryRequestDTO {
    private String name;

    public CategoryRequestDTO(Category category){
        this.name = category.getName();
    }
}
