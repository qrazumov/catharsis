package ru.razymov.catharsis.repo;

import org.springframework.stereotype.Repository;
import ru.razymov.catharsis.entity.PostEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PostEntity> getAll(Optional<Integer> offset, Optional<Integer> limit) {

        StringBuilder q = new StringBuilder("SELECT * FROM posts");

        offset.ifPresent(integer -> q.append(" OFFSET ").append(integer));
        limit.ifPresent(integer -> q.append(" LIMIT ").append(integer));

        return entityManager.createNativeQuery(q.toString(),
                PostEntity.class).getResultList();

    }

    public List<PostEntity> findByCategoryId(Long categoryId, Optional<Integer> offset, Optional<Integer> limit) {

        StringBuilder q = new StringBuilder("SELECT * FROM posts WHERE category_id=");
        q.append(categoryId);

        offset.ifPresent(integer -> q.append(" OFFSET ").append(integer));
        limit.ifPresent(integer -> q.append(" LIMIT ").append(integer));

        return entityManager.createNativeQuery(q.toString(),
                PostEntity.class).getResultList();

    }

    @Transactional
    public void save(PostEntity post) {
        entityManager.persist(post);
    }

}
