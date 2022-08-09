package tech.razymov.catharsis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.razymov.catharsis.entity.PostEntity;
import tech.razymov.catharsis.repo.PostRepositoryImpl2;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    PostRepositoryImpl2 postRepositoryImpl;

    public Optional<PostEntity> findById(Long id) {
        return postRepositoryImpl.findById(id);
    }

    public List<PostEntity> getAll(Optional<Integer> offset, Optional<Integer> limit) {
        return postRepositoryImpl.getAll(offset, limit);
    }

    public List<PostEntity> findByCategoryId(Long categoryId, Optional<Integer> offset, Optional<Integer> limit) {
        return postRepositoryImpl.findByCategoryId(categoryId, offset, limit);
    }

    public void save(PostEntity post)
    {
        postRepositoryImpl.save(post);
    }

    public void patch(PostEntity post) {
        postRepositoryImpl.patch(post);
    }

}
