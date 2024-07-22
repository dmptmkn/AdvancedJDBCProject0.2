package org.example.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Course {

    private Integer id;
    private String name;
    private Integer duration;
    private CourseType type;
    private String description;
    private Teacher teacherId;
    private Integer studentsCount;
    private Integer price;
    private Float pricePerHour;

}
