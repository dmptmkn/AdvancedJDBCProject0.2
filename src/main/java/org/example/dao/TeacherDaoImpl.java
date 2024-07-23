package org.example.dao;

import lombok.SneakyThrows;
import org.example.entity.Teacher;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TeacherDaoImpl implements TeacherDao {

    private static TeacherDaoImpl instance;

    private static final String SAVE_QUERY = """
            INSERT INTO teachers (name, salary, age)
            VALUES (?, ?, ?)
            """;
    private static final String FIND_ALL_QUERY = """
            SELECT * FROM teachers
            """;
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + """
            WHERE id = ?
            """;
    private static final String UPDATE_QUERY = """
                UPDATE teachers
                SET name   = ?,
                    salary = ?,
                    age    = ?
                WHERE id = ?
                """;
    private static final String DELETE_QUERY = """
                DELETE FROM teachers
                WHERE id = ?
                """;

    private TeacherDaoImpl() {
    }

    public static TeacherDaoImpl getInstance() {
        if (instance == null) {
            instance = new TeacherDaoImpl();
        }
        return instance;
    }

    @Override
    @SneakyThrows
    public void save(Teacher teacher) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY)) {
            preparedStatement.setString(1, teacher.getName());
            preparedStatement.setInt(2, teacher.getSalary());
            preparedStatement.setInt(3, teacher.getAge());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public Teacher findById(Integer id) {
        Teacher teacher = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            preparedStatement.setInt(1, id);
            if (resultSet.next()) {
                teacher = buildTeacher(resultSet);
            }
        }

        return teacher;
    }

    @Override
    @SneakyThrows
    public List<Teacher> findAll() {
        List<Teacher> teachers = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Teacher nextTeacher = buildTeacher(resultSet);
                teachers.add(nextTeacher);
            }
        }

        return teachers;
    }

    @Override
    @SneakyThrows
    public void update(Integer id, Teacher teacher) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, teacher.getName());
            preparedStatement.setInt(2, teacher.getSalary());
            preparedStatement.setInt(3, teacher.getAge());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public void delete(Integer id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    private Teacher buildTeacher(ResultSet resultSet) {
        return Teacher.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .salary(resultSet.getInt("salary"))
                .age(resultSet.getInt("age"))
                .build();
    }
}
