package tech.razymov.catharsis.repo;

import org.springframework.data.repository.CrudRepository;
import tech.razymov.catharsis.entity.CategoryEntity;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {

    Iterable<CategoryEntity> findAllByOrderByIdDesc();

}
