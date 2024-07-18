Задачи:

1) Вывести все курсы из таблицы courses, у которых duration >= 30
2) Реализовать логику добавления нового курса в таблицу courses
3) реализовать логику изменения колонок price и type любого курса в таблице courses
4) реализовать логику удаления курса из таблицу courses по значению колонки duration

Дополнительно:
5) Изучите запрос, который выводит средний возраст студентов для каждого курса с
   типом "DESIGN":
   SELECT c.name AS course_name, ROUND(AVG(s.age)) AS average_age
   FROM courses c
   JOIN subscriptions sub ON c.id = sub.course_id
   JOIN student s ON sub.student_id = s.id
   WHERE c.type = 'DESIGN'
   GROUP BY c.name;
Задание: используя возможности языка программирования Java, выполните его без
участия SQL функций