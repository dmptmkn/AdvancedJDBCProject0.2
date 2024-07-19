package org.example;

import org.example.dao.SubscriptionDaoImpl;
import org.example.entity.CourseType;
import org.example.entity.Student;
import org.example.entity.Subscription;

import java.util.List;

public class SubscriptionInfo {

    private List<Subscription> subscriptions = SubscriptionDaoImpl.getInstance().findAll();

    public SubscriptionInfo() {
    }

    public int getDesignStudentAverageAge() {
        return (int) SubscriptionDaoImpl.getInstance().findAll().stream()
                .filter(s -> s.getCourseId().getType() == CourseType.DESIGN)
                .map(Subscription::getStudentId)
                .mapToInt(Student::getAge)
                .average()
                .orElseThrow();
    }

}
