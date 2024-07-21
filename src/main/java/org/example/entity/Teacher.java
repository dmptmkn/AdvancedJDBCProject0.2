package org.example.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Teacher {

    private Integer id;
    private String name;
    private Integer salary;
    private Integer age;

}
