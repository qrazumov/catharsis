package ru.razymov.catharsis.repo;

import org.springframework.data.repository.CrudRepository;
import ru.razymov.catharsis.entity.PostEntity;

public interface PostRepository extends CrudRepository<PostEntity, Long> {

}
