package tech.razymov.catharsis.repo;

import org.springframework.data.repository.CrudRepository;
import tech.razymov.catharsis.entity.PostEntity;

public interface PostRepository extends CrudRepository<PostEntity, Long> {

}
