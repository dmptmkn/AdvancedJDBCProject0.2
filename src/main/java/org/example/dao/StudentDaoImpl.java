package org.example.dao;

import lombok.SneakyThrows;
import org.example.entity.Student;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    private static StudentDaoImpl instance;

    private static final String SAVE_QUERY = """
            INSERT INTO students (name, age, registration_date) 
            VALUES (?, ?, ?)
            """;
    private static final String FIND_ALL_QUERY = """
            SELECT * FROM students
            """;
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + """
            WHERE id = ?
            """;
    private static final String UPDATE_QUERY = """
                UPDATE students
                SET name              = ?,
                    age               = ?,
                    registration_date = ?
                WHERE id = ?
                """;
    private static final String DELETE_QUERY = """
                DELETE FROM students
                WHERE id = ?
                """;

    private StudentDaoImpl() {
    }

    public static StudentDaoImpl getInstance() {
        if (instance == null) {
            instance = new StudentDaoImpl();
        }
        return instance;
    }

    @Override
    @SneakyThrows
    public void save(Student student) {
        if (student == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY)) {
            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setDate(3, Date.valueOf(student.getRegistrationDate()));
            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public Student findById(Integer id) {
        if (id == null || id < 1) throw new IllegalArgumentException();

        Student student = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            preparedStatement.setInt(1, id);
            if (resultSet.next()) {
                student = buildStudent(resultSet);
            }
        }

        return student;
    }

    @Override
    @SneakyThrows
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Student nextStudent = buildStudent(resultSet);
                students.add(nextStudent);
            }
        }

        return students;
    }

    @Override
    @SneakyThrows
    public void update(Integer id, Student student) {
        if (id == null || id < 1 || student == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setDate(3, Date.valueOf(student.getRegistrationDate()));
            preparedStatement.setInt(4, id);
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

    @SneakyThrows
    private Student buildStudent(ResultSet resultSet) {
        return Student.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .age(resultSet.getInt("age"))
                .registrationDate(resultSet.getObject("subscription_date", LocalDate.class))
                .build();
    }
}
