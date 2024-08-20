package org.example.dao;

import lombok.SneakyThrows;
import org.example.entity.Course;
import org.example.entity.CourseType;
import org.example.entity.Teacher;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements CourseDao {

    private static CourseDaoImpl instance;

    private static final String SAVE_QUERY = """
            INSERT INTO courses (name, duration, type, description, teacher_id, students_count, price, price_per_hour)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM courses AS c
                     JOIN teachers AS t on t.id = c.teacher_id
            """;
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + """
            WHERE c.id = ?
            """;
    private static final String UPDATE_QUERY = """
            UPDATE courses
            SET name           = ?,
                duration       = ?,
                type           = ?,
                description    = ?,
                teacher_id     = ?,
                students_count = ?,
                price          = ?,
                price_per_hour = ?
            WHERE id = ?
            """;
    private static final String UPDATE_PRICE_AND_TYPE_QUERY = """
            UPDATE courses
            SET type  = ?,
                price = ?
            WHERE id = ?
            """;
    private static final String DELETE_QUERY = """
            DELETE
            FROM courses
            WHERE id = ?
            """;
    private static final String DELETE_BY_DURATION_QUERY = """
            DELETE
            FROM courses
            WHERE duration = ?
            """;

    private CourseDaoImpl() {
    }

    public static CourseDaoImpl getInstance() {
        if (instance == null) {
            instance = new CourseDaoImpl();
        }
        return instance;
    }

    @Override
    @SneakyThrows
    public void save(Course course) {
        if (course == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY)) {
            preparedStatement.setString(1, course.getName());
            preparedStatement.setInt(2, course.getDuration());
            preparedStatement.setString(3, course.getType().name());
            preparedStatement.setString(4, course.getDescription());
            preparedStatement.setInt(5, course.getTeacherId().getId());
            preparedStatement.setInt(6, course.getStudentsCount());
            preparedStatement.setInt(7, course.getPrice());
            preparedStatement.setFloat(8, course.getPricePerHour());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public Course findById(Integer id) {
        if (id == null || id < 1) throw new IllegalArgumentException();

        Course course = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                course = buildCourse(resultSet);
            }
        }

        return course;
    }

    @Override
    @SneakyThrows
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Course nextCourse = buildCourse(resultSet);
                courses.add(nextCourse);
            }
        }

        return courses;
    }

    @Override
    @SneakyThrows
    public void update(Integer id, Course course) {
        if (id == null || id < 1 || course == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, course.getName());
            preparedStatement.setInt(2, course.getDuration());
            preparedStatement.setString(3, course.getType().name());
            preparedStatement.setString(4, course.getDescription());
            preparedStatement.setInt(5, course.getTeacherId().getId());
            preparedStatement.setInt(6, course.getStudentsCount());
            preparedStatement.setInt(7, course.getPrice());
            preparedStatement.setFloat(8, course.getPricePerHour());
            preparedStatement.setInt(9, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public void updatePriceAndType(Integer id, Integer price, CourseType courseType) {
        if (id == null || id < 1 || price == null || price < 0 || courseType == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRICE_AND_TYPE_QUERY)) {
            preparedStatement.setString(1, courseType.name());
            preparedStatement.setInt(2, price);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public void delete(Integer id) {
        if (id == null || id < 1) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public void deleteByDuration(Integer duration) {
        if (duration == null || duration < 1) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_DURATION_QUERY)) {
            preparedStatement.setInt(1, duration);
            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    private Course buildCourse(ResultSet resultSet) {
        return Course.builder()
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
                .build();
    }
}
