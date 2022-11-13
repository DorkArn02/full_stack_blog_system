package hu.pte.blog_backend.services;

import hu.pte.blog_backend.models.Category;
import hu.pte.blog_backend.models.dtos.CategoryRequestDTO;
import hu.pte.blog_backend.models.dtos.CategoryResponseDTO;
import hu.pte.blog_backend.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    /**
     * Add a category to database
     * @param categoryRequestDTO CategoryRequestDTO
     * @return CategoryRequestDTO
     */
    public CategoryRequestDTO addCategory(CategoryRequestDTO categoryRequestDTO){
        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        return new CategoryRequestDTO(categoryRepository.save(category));
    }

    /**
     * Delete a category from the db
     * @param id Category's id
     */
    public void deleteCategoryById(Integer id){
        if(categoryRepository.findById(id).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        categoryRepository.deleteById(id);
    }

    /**
     * Update category by id
     * @param id Category's id
     * @param categoryRequestDTO CategoryRequestDTO
     * @return CategoryRequestDTO
     */
    public CategoryRequestDTO updateCategoryById(Integer id, CategoryRequestDTO categoryRequestDTO){
        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        return new CategoryRequestDTO(categoryRepository.save(category));
    }

    /**
     * Get category by id
     * @param id CategoryResponseDTO
     * @return CategoryResponseDTO
     */
    public CategoryResponseDTO getCategoryById(Integer id){
        if(categoryRepository.findById(id).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new CategoryResponseDTO(categoryRepository.findById(id).get());
    }

    /**
     * Get all categories
     * @return CategoryResponseDTO
     */
    public List<CategoryResponseDTO> getCategories(){
        List<CategoryResponseDTO> categoryResponseDTOS = new ArrayList<>();
        categoryRepository.findAll().forEach(category -> categoryResponseDTOS.add(new CategoryResponseDTO(category)));

        return categoryResponseDTOS;
    }
}
