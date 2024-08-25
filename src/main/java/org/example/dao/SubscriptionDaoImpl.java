package org.example.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.entity.*;
import org.example.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionDaoImpl implements SubscriptionDao {

    private static SubscriptionDaoImpl instance;

    private static final String SAVE_QUERY = """
            INSERT INTO subscriptions (student_id, course_id, subscription_date)
            VALUES (?, ?, ?)
            """;
    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM subscriptions AS sub
                     JOIN courses AS c on c.id = sub.course_id
                     JOIN teachers AS t on t.id = c.teacher_id
                     JOIN students AS s on s.id = sub.student_id
            """;
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + """
            WHERE s.id = ? AND c.id = ?
            """;
    private static final String UPDATE_QUERY = """
            UPDATE subscriptions
            SET student_id        = ?,
                course_id         = ?,
                subscription_date = ?
            WHERE student_id = ?
              AND course_id = ?
            """;
    private static final String DELETE_QUERY = """
            DELETE
            FROM subscriptions
            WHERE student_id = ?
              AND course_id = ?
            """;

    public static SubscriptionDaoImpl getInstance() {
        if (instance == null) {
            instance = new SubscriptionDaoImpl();
        }
        return instance;
    }

    @Override
    @SneakyThrows
    public void save(Subscription subscription) {
        if (subscription == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY)) {
            preparedStatement.setInt(1, subscription.getStudentId().getId());
            preparedStatement.setInt(2, subscription.getCourseId().getId());
            preparedStatement.setDate(3, Date.valueOf(subscription.getSubscriptionDate()));
        }
    }

    @Override
    @SneakyThrows
    public Subscription findById(SubscriptionPrimaryKey id) {
        if (id == null) throw new IllegalArgumentException();

        Subscription subscription = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            preparedStatement.setInt(1, id.studentId());
            preparedStatement.setInt(2, id.courseId());
            if (resultSet.next()) {
                subscription = buildSubscription(resultSet);
            }
        }
        return subscription;
    }

    @Override
    @SneakyThrows
    public List<Subscription> findAll() {
        List<Subscription> subscriptions = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Subscription nextSubscription = buildSubscription(resultSet);
                subscriptions.add(nextSubscription);
            }
        }

        return subscriptions;
    }

    @Override
    @SneakyThrows
    public void update(SubscriptionPrimaryKey id, Subscription subscription) {
        if (id == null || subscription == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setInt(1, subscription.getStudentId().getId());
            preparedStatement.setInt(2, subscription.getCourseId().getId());
            preparedStatement.setDate(3, Date.valueOf(subscription.getSubscriptionDate()));
            preparedStatement.setInt(1, id.studentId());
            preparedStatement.setInt(2, id.courseId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public void delete(SubscriptionPrimaryKey id) {
        if (id == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, id.studentId());
            preparedStatement.setInt(2, id.courseId());
            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    private Subscription buildSubscription(ResultSet resultSet) {
        return Subscription.builder()
                .id(new SubscriptionPrimaryKey(resultSet.getInt("s.id"), resultSet.getInt("c.id")))
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
                        .pricePerHour(resultSet.getFloat("c.price_per_hour"))
                        .build())
                .subscriptionDate(resultSet.getObject("sub.subscription_date", LocalDate.class))
                .build();
    }
}
