package org.example.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Student {

    private Integer id;
    private String name;
    private Integer age;
    private LocalDate registrationDate;

}
