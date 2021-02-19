package ru.mikejohn.springcource.dao;

import org.springframework.stereotype.Component;
import ru.mikejohn.springcource.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by mikejohn on 01.12.2020.
 */
@Component
public class PersonDAO {
    //класс для работы со списком людей - найти по id, добавить, удалить, обновить
    private static int PEOPLE_COUNT;
    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Progs2013";

    private static Connection connection;
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //метод возвращает список всех людей:
    public List<Person> index(){
        //return people;
        List<Person> people = new ArrayList<>();
        //напишем sql-запрос:
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Person";    //оставим обычный запрос, т.к. тут не изменяем данные
            ResultSet resultSet = statement.executeQuery(SQL); //метод для запроса для получения данных из БД - возвращает результат

            while(resultSet.next()){
                Person person = new Person();
                person.setId(resultSet.getInt("id")); //получаем значение из колонки id
                person.setName(resultSet.getString("name")); //получаем значение из колонки name
                person.setAge(resultSet.getInt("age")); //получаем значение из колонки age
                person.setEmail(resultSet.getString("email")); //получаем значение из колонки email

                people.add(person);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return people;
    }
    //метод возвращает человека по его id
    public Person show(int id) {
        Person person = null;   //создадим указатель на объект тут, т.к. переменная не будет видна из блока try
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Person WHERE id=?");
            preparedStatement.setInt(1, id); //это чтобы передать переменную в запрос

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();   //курсор в resultSet сдвигается на 1 и появляется объект из resultSet - т.е. Person

            person = new Person(); /*Алишев не сделал объявления переменной тут, т.к. она не будет видна из try,
            а нам нужно вернуть её в return, поэтому создали указатель Person person = null раньше.*/

            person.setId(resultSet.getInt("id")); //получаем значение из колонки id
            person.setName(resultSet.getString("name")); //получаем значение из колонки name
            person.setAge(resultSet.getInt("age")); //получаем значение из колонки age
            person.setEmail(resultSet.getString("email")); //получаем значение из колонки email
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }
    //метод добавляет человека
    public void save(Person person){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Person VALUES(1, ?, ?, ?)");

            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());

            preparedStatement.executeUpdate(); //метод для запроса для изменения данных в БД - ничего не возвращает
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //
    public void update(int id, Person updatedPerson) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Person SET name=?, age=?, email=? WHERE Id=?");

            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setInt(2, updatedPerson.getAge());
            preparedStatement.setString(3, updatedPerson.getEmail());
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate(); //метод для запроса для изменения данных в БД - ничего не возвращает
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void delete (int id){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Person WHERE id=?");

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate(); //метод для запроса для изменения данных в БД - ничего не возвращает
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
