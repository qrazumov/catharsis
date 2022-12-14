package tech.razymov.catharsis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.razymov.catharsis.entity.CategoryEntity;
import tech.razymov.catharsis.repo.CategoryRepository;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Optional<CategoryEntity> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Iterable<CategoryEntity> findAll() {
        return categoryRepository.findAllByOrderByIdDesc();
    }
}
