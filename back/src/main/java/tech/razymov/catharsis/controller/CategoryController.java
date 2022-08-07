package tech.razymov.catharsis.controller;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.razymov.catharsis.entity.CategoryEntity;
import tech.razymov.catharsis.service.CategoryService;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @SneakyThrows
    @GetMapping
    Iterable<CategoryEntity> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    Optional<CategoryEntity> findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

}