package ru.razymov.catharsis.repo;

import org.springframework.data.repository.CrudRepository;
import ru.razymov.catharsis.entity.AvitoEntity;
import ru.razymov.catharsis.entity.CategoryEntity;

public interface AvitoRepository extends CrudRepository<AvitoEntity, Long> {
    Iterable<AvitoEntity> findAllByOrderByIdDesc();
}
