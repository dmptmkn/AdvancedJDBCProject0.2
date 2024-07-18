package org.example.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Student {

    private int id;
    private String name;
    private int age;
    private LocalDate registrationDate;

}
