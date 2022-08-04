package ru.razymov.catharsis.controller;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.razymov.catharsis.entity.CategoryEntity;
import ru.razymov.catharsis.service.CategoryService;

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