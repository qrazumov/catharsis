package ru.razymov.catharsis.repo;

import org.springframework.data.repository.CrudRepository;
import ru.razymov.catharsis.entity.CategoryEntity;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {

    Iterable<CategoryEntity> findAllByOrderByIdDesc();

}
