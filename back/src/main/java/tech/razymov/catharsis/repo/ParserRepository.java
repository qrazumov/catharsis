package tech.razymov.catharsis.repo;

import org.springframework.data.repository.CrudRepository;
import tech.razymov.catharsis.entity.ParserEntity;

public interface ParserRepository extends CrudRepository<ParserEntity, Long> {
    Iterable<ParserEntity> findAllByOrderByIdDesc();
}
