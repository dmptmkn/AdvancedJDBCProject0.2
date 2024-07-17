package org.example.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Course {

    private int id;
    private String name;
    private int duration;
    private CourseType type;
    private String description;
    private Teacher teacherId;
    private int studentsCount;
    private int price;
    private double pricePerHour;

}
