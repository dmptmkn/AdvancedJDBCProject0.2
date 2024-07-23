package org.example.dao;

import org.example.entity.Course;
import org.example.entity.CourseType;

public interface CourseDao extends Dao<Integer, Course> {

    void updatePriceAndType(Integer id, Integer price, CourseType courseType);
    void deleteByDuration(Integer duration);

}
