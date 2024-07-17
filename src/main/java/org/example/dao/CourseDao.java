package org.example.dao;

import org.example.entity.Course;
import org.example.entity.CourseType;

import java.util.List;

public interface CourseDao {

    void save(Course course);
    List<Course> findAll();
    void update(int id, int price, CourseType courseType);
    void delete(int duration);

}
