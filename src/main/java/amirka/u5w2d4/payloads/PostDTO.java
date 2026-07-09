package amirka.u5w2d4.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PostDTO(

        @NotBlank(message = "Category is required")
        String category,

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Content is required")
        String content,

        @NotNull(message = "Author id is required")
        UUID authorId

) {
}
