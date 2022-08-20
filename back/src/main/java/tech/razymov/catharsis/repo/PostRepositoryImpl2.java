package tech.razymov.catharsis.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tech.razymov.catharsis.entity.PostEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepositoryImpl2 {

    @Autowired
    PostRepository postRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<PostEntity> getAll(Optional<Integer> offset, Optional<Integer> limit) {

        StringBuilder q = new StringBuilder("SELECT * FROM posts ORDER BY id DESC");

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

    @Transactional
    public void patch(PostEntity post) {

        Optional<PostEntity> postEntity = postRepository.findById(post.getId());

        postEntity.ifPresent(i -> {
            if (post.getName() != null)
                i.setName(post.getName());
            if (post.getText() != null)
                i.setText(post.getText());
            if (post.getImg() != null)
                i.setImg(post.getImg());
            if (post.getVideo() != null)
                i.setVideo(post.getVideo());
            if (post.getCategory() != null)
                i.setCategory(post.getCategory());
            postRepository.save(i);
        });

    }

    public Optional<PostEntity> findById(Long id) {
        return postRepository.findById(id);

    }

}
