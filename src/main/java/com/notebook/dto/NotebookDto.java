package com.notebook.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotebookDto {
    private Long id;

    @NotEmpty(message = "Title should not be empty")
    private String title;

    @NotEmpty(message = "Content should not be empty")
    private String content;

    @NotEmpty(message = "Category should not be empty")
    private String category;
}
