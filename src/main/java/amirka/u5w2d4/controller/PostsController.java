package amirka.u5w2d4.controller;

import amirka.u5w2d4.entities.Post;
import amirka.u5w2d4.exceptions.ValidationEx;
import amirka.u5w2d4.payloads.PostDTO;
import amirka.u5w2d4.payloads.PostResponseDTO;
import amirka.u5w2d4.payloads.PostUpdateDTO;
import amirka.u5w2d4.services.PostsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostsController {

    private final PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public PostResponseDTO savePost(@RequestBody @Valid PostDTO postDTO) {
//
//        Post saved = postsService.save(postDTO);
//
//        return new PostResponseDTO(saved.getId());
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDTO savePost(@RequestBody @Validated PostDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationEx(errorsList);
        }

        Post saved = postsService.save(body);

        return new PostResponseDTO(saved.getId());
    }

    @GetMapping
    public Page<Post> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "category") String orderBy) {

        return postsService.getAll(page, size, orderBy);
    }

    @GetMapping("/{postId}")
    public Post getById(@PathVariable UUID postId) {

        return postsService.findById(postId);
    }

    @PutMapping("/{postId}")
    public Post updatePost(
            @PathVariable UUID postId,
            @RequestBody @Valid PostUpdateDTO postUpdateDTO) {

        return postsService.findByIdAndUpdate(postId, postUpdateDTO);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable UUID postId) {

        postsService.findByIdAndDelete(postId);
    }
}
