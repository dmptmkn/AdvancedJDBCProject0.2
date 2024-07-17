package org.example.printer;

import org.example.dao.CourseDaoImpl;
import org.example.entity.Course;

import java.io.PrintStream;
import java.util.List;
import java.util.Locale;

public class CoursePrinter {

    private PrintStream printStream;
    private List<Course> courses;

    public CoursePrinter(PrintStream printStream) {
        this.printStream = printStream;
        collectData();
    }

    private void collectData() {
        courses = CourseDaoImpl.getInstance().findAll();
    }

    public void printData() {
        printStream.println("Вывожу данные по курсам:");
        for (Course c : courses) {
            String formattedCourseInfo = String.format(Locale.US, "Курс №%d «%s»\nCпециальность: %s\nОписание курса: %s\nПреподаватель: %s\nДлительность курса: %d ч.\nКоличество студентов на курсе: %d\nСтоимость курса: ₽%d (или ₽%.0f за час)",
                    c.getId(),
                    c.getName(),
                    c.getType().getDescription(),
                    c.getDescription(),
                    c.getTeacherId().getName(),
                    c.getDuration(),
                    c.getStudentsCount(),
                    c.getPrice(),
                    c.getPricePerHour());
            printStream.println(formattedCourseInfo);
            printStream.println("========================================================================================");
        }
        printStream.println();
    }
}
