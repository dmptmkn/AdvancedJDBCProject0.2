package org.example.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Teacher {

    private int id;
    private String name;
    private int salary;
    private int age;

}
