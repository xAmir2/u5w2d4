package amirka.u5w2d4.services;

import amirka.u5w2d4.entities.Author;
import amirka.u5w2d4.exceptions.BadRequestEx;
import amirka.u5w2d4.exceptions.FileUploadEx;
import amirka.u5w2d4.exceptions.NotFoundEx;
import amirka.u5w2d4.payloads.AuthorDTO;
import amirka.u5w2d4.repositories.AuthorsRepository;
import amirka.u5w2d4.repositories.PostsRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthorsService {

    private final AuthorsRepository authorsRepository;
    private final PostsRepository postsRepository;
    private final Cloudinary fileUploader;

    public AuthorsService(AuthorsRepository authorsRepository, PostsRepository postsRepository, Cloudinary fileUploader) {
        this.authorsRepository = authorsRepository;
        this.postsRepository = postsRepository;
        this.fileUploader = fileUploader;
    }

    public amirka.u5w2d4.entities.Author save(AuthorDTO authorDTO) {

        if (authorsRepository.existsByEmail(authorDTO.email())) {
            throw new BadRequestEx(
                    "The following e-mail address " + authorDTO.email() + " is already in use."
            );
        }

        Author author = new Author(
                authorDTO.name(),
                authorDTO.surname(),
                authorDTO.email(),
                authorDTO.birthDate()
        );

        return authorsRepository.save(author);
    }

    public Page<Author> getAll(int page, int size, String orderBy) {

        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));

        return authorsRepository.findAll(pageable);
    }

    public Author findById(UUID id) {

        return authorsRepository.findById(id)
                .orElseThrow(() -> new NotFoundEx(id));
    }

    public Author findByIdAndUpdate(UUID id, AuthorDTO authorDTO) {

        Author found = findById(id);

        if (!found.getEmail()
                .equals(authorDTO.email())
                && authorsRepository.existsByEmail(authorDTO.email())) {

            throw new BadRequestEx(
                    "Email address " + authorDTO.email() + " is already in use."
            );
        }

        found.setName(authorDTO.name());
        found.setSurname(authorDTO.surname());
        found.setEmail(authorDTO.email());
        found.setBirthDate(authorDTO.birthDate());

        return authorsRepository.save(found);
    }

    @Transactional
    public void findByIdAndDelete(UUID id) {

        Author found = findById(id);

        postsRepository.deleteAllByAuthor_Id(id);

        authorsRepository.delete(found);
    }

    public Author updateAvatar(UUID authorId, MultipartFile file) {

        if (file.isEmpty()) {
            throw new FileUploadEx("The uploaded file cannot be empty");
        }

        if (!file.getContentType()
                .equals("image/gif")
                && !file.getContentType()
                .equals("image/jpeg")
                && !file.getContentType()
                .equals("image/png")) {

            throw new FileUploadEx(
                    "Only GIF, JPEG and PNG images are allowed"
            );
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new FileUploadEx(
                    "The image cannot be bigger than 5MB"
            );
        }

        Author author = authorsRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundEx(authorId));

        try {

            Map result = fileUploader.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());

            String url = (String) result.get("secure_url");

            author.setAvatar(url);

            return authorsRepository.save(author);

        } catch (IOException e) {

            throw new FileUploadEx(
                    "Error while uploading image"
            );
        }
    }
}
