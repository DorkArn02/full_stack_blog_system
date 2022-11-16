package hu.pte.blog_backend.controllers;

import hu.pte.blog_backend.models.dtos.CategoryRequestDTO;
import hu.pte.blog_backend.models.dtos.CategoryResponseDTO;
import hu.pte.blog_backend.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping("/")
    public ResponseEntity<CategoryRequestDTO> addCategory(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO){
        return new ResponseEntity<>(categoryService.addCategory(categoryRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteCategoryById(@PathVariable Integer id){
        categoryService.deleteCategoryById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryRequestDTO> updateCategoryById(@PathVariable Integer id, @RequestBody @Valid CategoryRequestDTO categoryRequestDTO){
        return new ResponseEntity<>(categoryService.updateCategoryById(id, categoryRequestDTO), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Integer id){
        return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }
    @GetMapping("/")
    public List<CategoryResponseDTO> getCategories(){
        return categoryService.getCategories();
    }



}
