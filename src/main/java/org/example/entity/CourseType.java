package org.example.entity;

public enum CourseType {

    DESIGN("дизайн"),
    PROGRAMMING("программирование"),
    MARKETING("маркетинг"),
    MANAGEMENT("менеджемент"),
    BUSINESS("предпринимательтсво");

    private final String description;

    CourseType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
