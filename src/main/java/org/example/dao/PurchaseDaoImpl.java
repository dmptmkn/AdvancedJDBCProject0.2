package org.example.dao;

import lombok.SneakyThrows;
import org.example.entity.Purchase;
import org.example.entity.PurchasePrimaryKey;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDaoImpl implements PurchaseDao {

    private static PurchaseDaoImpl instance;

    private static final String SAVE_QUERY = """
            INSERT INTO purchaseList (student_name, course_name, price, subscription_date)
            VALUES (?, ?, ?, ?)
            """;
    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM purchaseList
            """;
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + """
            WHERE student_name = ?
              AND course_name = ?
            """;
    private static final String UPDATE_QUERY = """
            UPDATE purchaseList
            SET student_name      = ?,
                course_name       = ?,
                price             = ?,
                subscription_date = ?
            WHERE student_name = ?
              AND course_name = ?
            """;
    private static final String DELETE_QUERY = """
            DELETE
            FROM purchaseList
            WHERE student_name = ?
              AND course_name = ?
            """;

    private PurchaseDaoImpl() {
    }

    public static PurchaseDaoImpl getInstance() {
        if (instance == null) {
            instance = new PurchaseDaoImpl();
        }
        return instance;
    }

    @Override
    @SneakyThrows
    public void save(Purchase purchase) {
        if (purchase == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY)) {
            preparedStatement.setString(1, purchase.getStudentName());
            preparedStatement.setString(2, purchase.getCourseName());
            preparedStatement.setInt(3, purchase.getPrice());
            preparedStatement.setDate(4, Date.valueOf(purchase.getSubscriptionDate()));
            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public Purchase findById(PurchasePrimaryKey id) {
        if (id == null) throw new IllegalArgumentException();

        Purchase purchase = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            preparedStatement.setString(1, id.studentName());
            preparedStatement.setString(2, id.courseName());
            if (resultSet.next()) {
                purchase = buildPurchase(resultSet);
            }
        }

        return purchase;
    }

    @Override
    @SneakyThrows
    public List<Purchase> findAll() {
        List<Purchase> purchases = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            Purchase nextPurchase = buildPurchase(resultSet);
            purchases.add(nextPurchase);
        }

        return purchases;
    }

    @Override
    @SneakyThrows
    public void update(PurchasePrimaryKey id, Purchase purchase) {
        if (id == null || purchase == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, purchase.getStudentName());
            preparedStatement.setString(2, purchase.getCourseName());
            preparedStatement.setInt(3, purchase.getPrice());
            preparedStatement.setDate(4, Date.valueOf(purchase.getSubscriptionDate()));
            preparedStatement.setString(5, id.studentName());
            preparedStatement.setString(6, id.courseName());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public void delete(PurchasePrimaryKey id) {
        if (id == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setString(1, id.studentName());
            preparedStatement.setString(2, id.courseName());
            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    private Purchase buildPurchase(ResultSet resultSet) {
        return Purchase.builder()
                .id(new PurchasePrimaryKey(resultSet.getString("student_name"), resultSet.getString("course_name")))
                .studentName(resultSet.getString("student_name"))
                .courseName(resultSet.getString("course_name"))
                .price(resultSet.getInt("price"))
                .subscriptionDate(resultSet.getObject("subscription_date", LocalDate.class))
                .build();
    }
}
