package amirka.u5w2d4.payloads;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateDTO(

        @NotBlank(message = "Category is required")
        String category,

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Content is required")
        String content

) {
}
