package org.example.dao;

import org.example.entity.Course;
import org.example.entity.CourseType;
import org.example.entity.Teacher;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements CourseDao {

    private static CourseDaoImpl instance;

    private CourseDaoImpl() {
    }

    public static CourseDaoImpl getInstance() {
        if (instance == null) {
            instance = new CourseDaoImpl();
        }
        return instance;
    }

    @Override
    public void save(Course course) {
        String sqlQuery = """
                INSERT INTO courses (name, duration, type, description, teacher_id, students_count, price, price_per_hour)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, course.getName());
            preparedStatement.setInt(2, course.getDuration());
            preparedStatement.setString(3, course.getType().name());
            preparedStatement.setString(4, course.getDescription());
            preparedStatement.setInt(5, course.getTeacherId().getId());
            preparedStatement.setInt(6, course.getStudentsCount());
            preparedStatement.setInt(7, course.getPrice());
            preparedStatement.setFloat(8, (float) course.getPricePerHour());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> findAll() {
        String sqlQuery = """
                SELECT *
                FROM courses AS c
                         JOIN teachers AS t on t.id = c.teacher_id
                WHERE c.duration >= 30
                """;

        List<Course> courses = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Course nextCourse = Course.builder()
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
                        .build();
                courses.add(nextCourse);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return courses;
    }

    @Override
    public void update(int id, int price, CourseType courseType) {
        String sqlQuery = """
                UPDATE courses
                SET type  = ?,
                    price = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, courseType.name());
            preparedStatement.setInt(2, price);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int duration) {
        String sqlQuery = """
                DELETE FROM courses
                WHERE duration = ?
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, duration);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
