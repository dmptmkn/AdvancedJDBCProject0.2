package org.example.dao;


import org.example.entity.Course;
import org.example.entity.Student;
import org.example.entity.Subscription;

import java.util.List;

public interface SubscriptionDao extends Dao<Student, Subscription> {

    Subscription find(Student student, Course course);
    void update(Student student, Course course, Subscription subscription);
    void delete(Student student, Course course);
}
