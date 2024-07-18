package org.example.dao;

import org.example.entity.*;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDaoImpl implements SubscriptionDao {

    private static SubscriptionDaoImpl instance;

    private SubscriptionDaoImpl() {
    }

    public static SubscriptionDaoImpl getInstance() {
        if (instance == null) {
            instance = new SubscriptionDaoImpl();
        }
        return instance;
    }

    @Override
    public List<Subscription> findAll() {
        String sqlQuery = """
                SELECT *
                FROM subscriptions AS sub
                         JOIN courses AS c on c.id = sub.course_id
                         JOIN teachers AS t on t.id = c.teacher_id
                         JOIN students AS s on s.id = sub.student_id
                """;

        List<Subscription> subscriptions = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Subscription nextSubscription = Subscription.builder()
                        .studentId(Student.builder()
                                .id(resultSet.getInt("s.id"))
                                .name(resultSet.getString("s.name"))
                                .age(resultSet.getInt("s.age"))
                                .registrationDate(resultSet.getObject("s.registration_date", LocalDate.class))
                                .build())
                        .courseId(Course.builder()
                                .id(resultSet.getInt("c.id"))
                                .name(resultSet.getString("c.name"))
                                .duration(resultSet.getInt("c.duration"))
                                .type(CourseType.valueOf(resultSet.getString("c.type")))
                                .description(resultSet.getString("c.description"))
                                .teacherId(Teacher.builder()
                                        .id(resultSet.getInt("t.id"))
                                        .name(resultSet.getString("t.name"))
                                        .salary(resultSet.getInt("t.salary"))
                                        .age(resultSet.getInt("t.age"))
                                        .build())
                                .studentsCount(resultSet.getInt("c.students_count"))
                                .price(resultSet.getInt("c.price"))
                                .pricePerHour(resultSet.getInt("c.price_per_hour"))
                                .build())
                        .subscriptionDate(resultSet.getObject("sub.subscription_date", LocalDate.class))
                        .build();
                subscriptions.add(nextSubscription);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return subscriptions;
    }
}
