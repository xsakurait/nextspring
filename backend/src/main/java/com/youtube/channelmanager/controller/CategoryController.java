package com.youtube.channelmanager.controller;

import com.youtube.channelmanager.dto.CategoryDTO;
import com.youtube.channelmanager.entity.Category;
import com.youtube.channelmanager.entity.User;
import com.youtube.channelmanager.mapper.dto.CategoryDTOMapper;
import com.youtube.channelmanager.repository.CategoryRepository;
import com.youtube.channelmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryDTOMapper categoryDTOMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        List<Category> categories = categoryRepository.findByUserIdOrderByNameAsc(user.getId());
        return ResponseEntity.ok(categoryDTOMapper.toDTOList(categories));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            Authentication authentication,
            @RequestBody CategoryDTO categoryDTO) {

        User user = getUserFromAuthentication(authentication);

        Category category = Category.builder()
            .userId(user.getId())
            .name(categoryDTO.getName())
            .description(categoryDTO.getDescription())
            .color(categoryDTO.getColor())
            .build();

        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(categoryDTOMapper.toDTO(savedCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO) {

        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setColor(categoryDTO.getColor());

        Category updatedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(categoryDTOMapper.toDTO(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok("Category deleted");
    }

    private User getUserFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
