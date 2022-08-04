package tech.razymov.catharsis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.razymov.catharsis.entity.PostEntity;
import tech.razymov.catharsis.repo.PostRepository;
import tech.razymov.catharsis.repo.PostRepositoryImpl;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    PostRepositoryImpl postRepositoryImpl;

    public Optional<PostEntity> findById(Long id) {
        return postRepository.findById(id);
    }

    public List<PostEntity> getAll(Optional<Integer> offset, Optional<Integer> limit) {
        return postRepositoryImpl.getAll(offset, limit);
    }

    public List<PostEntity> findByCategoryId(Long categoryId, Optional<Integer> offset, Optional<Integer> limit) {
        return postRepositoryImpl.findByCategoryId(categoryId, offset, limit);
    }

    public void save(PostEntity post) {
        postRepositoryImpl.save(post);
    }

}
