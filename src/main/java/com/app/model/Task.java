package com.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private int id;
    private String title;
    private String description;
    private String category;
    private LocalDate deadline;
    private int userId;
}
