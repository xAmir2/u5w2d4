package amirka.u5w2d4.services;

import amirka.u5w2d4.entities.Author;
import amirka.u5w2d4.entities.Post;
import amirka.u5w2d4.exceptions.NotFoundEx;
import amirka.u5w2d4.payloads.PostDTO;
import amirka.u5w2d4.payloads.PostUpdateDTO;
import amirka.u5w2d4.repositories.AuthorsRepository;
import amirka.u5w2d4.repositories.PostsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final AuthorsRepository authorsRepository;

    public PostsService(PostsRepository postsRepository, AuthorsRepository authorsRepository) {
        this.postsRepository = postsRepository;
        this.authorsRepository = authorsRepository;
    }

    public Post save(PostDTO postDTO) {

        Author author = authorsRepository.findById(postDTO.authorId())
                .orElseThrow(() -> new NotFoundEx(postDTO.authorId()));

        Post post = new Post(
                postDTO.category(),
                postDTO.title(),
                postDTO.content(),
                author
        );

        return postsRepository.save(post);
    }

    public Page<Post> getAll(int page, int size, String orderBy) {

        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));

        return postsRepository.findAll(pageable);
    }

    public Post findById(UUID id) {

        return postsRepository.findById(id)
                .orElseThrow(() -> new NotFoundEx(id));
    }

    public Post findByIdAndUpdate(UUID id, PostUpdateDTO postUpdateDTO) {

        Post found = findById(id);

        found.setCategory(postUpdateDTO.category());
        found.setTitle(postUpdateDTO.title());
        found.setContent(postUpdateDTO.content());

        return postsRepository.save(found);
    }

    public void findByIdAndDelete(UUID id) {

        Post found = findById(id);

        postsRepository.delete(found);
    }
}