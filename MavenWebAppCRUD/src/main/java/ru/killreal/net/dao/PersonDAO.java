package ru.killreal.net.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.killreal.net.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
//    private static int PERSON_ID;
//    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
//    private static final String PASSWORD = "postgres";
//    private static final String USERNAME = "postgres";
//
//    private static Connection connection;
//    static {
//        try {
//            Class.forName("org.postgresql.Driver");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public List<Person> index() {
//        List<Person> people = new ArrayList<>();
//        try {
//            Statement statement = connection.createStatement();
//            String SQL = "SELECT * FROM Person ";
//            ResultSet resultSet = statement.executeQuery(SQL);
//
//            while (resultSet.next()) {
//                Person person = new Person();
//
//                person.setId(resultSet.getInt("id"));
//                person.setName(resultSet.getString("name"));
//                person.setAge(resultSet.getInt("age"));
//                person.setEmail(resultSet.getString("email"));
//
//                people.add(person);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        // Через поток
//        return people.stream().filter(person -> person.getId() == id)
//                .findAny().orElse(null);

        //Через JDBC api
//        Person person = null;
//        try {
//            PreparedStatement preparedStatement =
//                    connection.prepareStatement("SELECT * FROM Person WHERE id=?");
//            preparedStatement.setInt(1,id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            resultSet.next();
//
//            person = new Person();
//            person.setId(resultSet.getInt("id"));
//            person.setAge(resultSet.getInt("age"));
//            person.setName(resultSet.getString("name"));
//            person.setEmail(resultSet.getString("email"));
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return jdbcTemplate.query("SELECT * FROM Person WHERE id =?",new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }


    public Optional<Person> show(String email) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE email = ?",new Object[]{email}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    public void save(Person person) {
        //Без jdbc api
//        person.setId(++PERSON_ID);
//        people.add(person);

        //С использованием jdbc api
//        try {
//            String SQL = "INSERT INTO Person VALUES(1,?,?,?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
//
//            preparedStatement.setString(1,person.getName());
//            preparedStatement.setInt(2,person.getAge());
//            preparedStatement.setString(3,person.getEmail());
//
//            preparedStatement.executeUpdate();
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        jdbcTemplate.update("INSERT INTO Person(name, age, email,address) VALUES(?, ?, ?, ?)", person.getName(), person.getAge(), person.getEmail(), person.getAddress());
    }
    public void update(int id, Person updatedPerson) {
        //Без jdbc api
//        Person personToBeUpdated = show(id);
//        personToBeUpdated.setName(updatedPerson.getName());
//        personToBeUpdated.setAge(updatedPerson.getAge());
//        personToBeUpdated.setEmail(updatedPerson.getEmail());

        //С использованием jdbc api

//        try {
//            PreparedStatement preparedStatement =
//                    connection.prepareStatement("UPDATE Person SET name=?, age=?, email=?, id=?");
//            preparedStatement.setString(1,updatedPerson.getName());
//            preparedStatement.setInt(2,updatedPerson.getAge());
//            preparedStatement.setString(3,updatedPerson.getEmail());
//            preparedStatement.setInt(4,id);
//
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=?, address=? WHERE id=?",updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(),updatedPerson.getAddress(), id);
    }
    public void delete(int id) {
        //Без jdbc api
//        people.removeIf(person -> person.getId()==id);
        //С использованием jdbc api
//        try {
//            PreparedStatement preparedStatement =
//                    connection.prepareStatement("DELETE FROM Person WHERE id=?");
//            preparedStatement.setInt(1,id);
//
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }
    // //////////////
    // Тест производительности пакетной вставки
    // //////////////

    public void testMultipleUpdate() {
        List<Person> people = create1000people();
        long before = System.currentTimeMillis();
        for (Person person: people) {
            jdbcTemplate.update("INSERT INTO Person VALUES(?, ?, ?, ?)",person.getId(), person.getName(), person.getAge(), person.getEmail());
        }
        long after = System.currentTimeMillis();
        System.out.println("Time is " + (after-before));
    }

    public List<Person> create1000people() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            people.add(new Person("Name" + i, i,i /2,"Name"+ i + "@mail.ru","address"));
        }
        return people;
    }

    public void testBatchUpdate() {
        List<Person> people = create1000people();
        long before = System.currentTimeMillis();
        jdbcTemplate.batchUpdate("INSERT INTO Person VALUES (?,?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1,people.get(i).getId());
                preparedStatement.setString(2,people.get(i).getName());
                preparedStatement.setInt(3,people.get(i).getAge());
                preparedStatement.setString(4,people.get(i).getEmail());

            }

            @Override
            public int getBatchSize() {
                return people.size();
            }
        });
        long after = System.currentTimeMillis();
        System.out.println("Time is " + (after - before));
    }
}
