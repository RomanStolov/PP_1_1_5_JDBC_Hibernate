package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT, " +
                "name VARCHAR(25) NOT NULL, " +
                "lastName VARCHAR(25) NOT NULL, " +
                "age TINYINT NOT NULL, " +
                "CHECK((age > 0 AND age < 100) AND (name != '') AND (lastName != '')), " +
                "PRIMARY KEY (id))";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//    // Вариант №1
//    @Override
//    public void saveUser(String name, String lastName, byte age) {
//        String sql = "INSERT INTO users (name, lastName, age) VALUES ('" +
////                    name + "', '" +
////                    lastName + "', " +
////                    age + ")";
//        try (Connection connection = Util.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.executeUpdate(sql);
//            System.out.println("User с именем – " + name + " добавлен в базу данных");
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }

    // Вариант 2
    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//    // Вариант №1
//    @Override
//    public void removeUserById(long id) {
//        String sql = "DELETE FROM users WHERE id = " + (long) id;
//        try (Connection connection = Util.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.executeUpdate(sql);
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }

    // Вариант №2
    @Override
    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User userTemp = new User();
                userTemp.setId(resultSet.getLong("id"));
                userTemp.setName(resultSet.getString("name"));
                userTemp.setLastName(resultSet.getString("lastName"));
                userTemp.setAge(resultSet.getByte("age"));
                userList.add(userTemp);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
