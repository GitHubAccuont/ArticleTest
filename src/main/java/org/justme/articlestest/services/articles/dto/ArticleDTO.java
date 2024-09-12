package org.justme.articlestest.services.articles.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ArticleDTO {


    @Size(max = 100, message = "В названии может быть не более 100 символов.")
    private String title;

    @NotBlank(message = "Автор статьи не может быть пустым")
    private String author;

    @NotBlank(message = "Содержимое не может быть пустым")
    private String content;

    @PastOrPresent(message = "Дата публикации не может быть указана в будущем времени.")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate publishDate;
}

