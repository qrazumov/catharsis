package tech.razymov.catharsis.controller;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.razymov.catharsis.entity.PostEntity;
import tech.razymov.catharsis.service.PostService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    @Autowired
    PostService postService;

    @SneakyThrows
    @GetMapping
    List<PostEntity> getAll(@RequestParam(required = false) Optional<Integer> offset,
                            @RequestParam(required = false) Optional<Integer> limit) {
        return postService.getAll(offset, limit);
    }

    @GetMapping("/{id}")
    Optional<PostEntity> findById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping("/categories/{categoryId}")
    List<PostEntity> findByCategoryId(@PathVariable Long categoryId,
                                      @RequestParam(required = false) Optional<Integer> offset,
                                      @RequestParam(required = false) Optional<Integer> limit) {
        return postService.findByCategoryId(categoryId, offset, limit);
    }

    @PostMapping
    void save(@RequestBody PostEntity post) {
        postService.save(post);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    void patch(@PathVariable Long id, @RequestBody PostEntity post) {
        post.setId(id);
        postService.patch(post);
    }


}