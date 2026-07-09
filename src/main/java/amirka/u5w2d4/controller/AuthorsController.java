package amirka.u5w2d4.controller;

import amirka.u5w2d4.entities.Author;
import amirka.u5w2d4.exceptions.ValidationEx;
import amirka.u5w2d4.payloads.AuthorDTO;
import amirka.u5w2d4.payloads.AuthorResponseDTO;
import amirka.u5w2d4.services.AuthorsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
public class AuthorsController {

    private final AuthorsService authorsService;

    public AuthorsController(AuthorsService authorsService) {
        this.authorsService = authorsService;
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public AuthorResponseDTO saveAuthor(@RequestBody @Valid AuthorDTO authorDTO) {
//
//        Author saved = authorsService.save(authorDTO);
//
//        return new AuthorResponseDTO(saved.getId());
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponseDTO saveAuthor(@RequestBody @Validated AuthorDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationEx(errorsList);
        }
        Author saved = this.authorsService.save(body);
        return new AuthorResponseDTO(saved.getId());
    }

    @GetMapping
    public Page<Author> getAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String orderBy) {

        return authorsService.getAll(page, size, orderBy);
    }

    @GetMapping("/{authorId}")
    public Author getById(@PathVariable UUID authorId) {

        return authorsService.findById(authorId);
    }

    @PutMapping("/{authorId}")
    public Author updateAuthor(
            @PathVariable UUID authorId,
            @RequestBody @Valid AuthorDTO authorDTO) {

        return authorsService.findByIdAndUpdate(authorId, authorDTO);
    }

    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable UUID authorId) {

        authorsService.findByIdAndDelete(authorId);
    }

    @PatchMapping("/{authorId}/avatar")
    public Author updateAvatar(
            @PathVariable UUID authorId,
            @RequestParam("avatar") MultipartFile file
    ) {

        return authorsService.updateAvatar(authorId, file);
    }
}
