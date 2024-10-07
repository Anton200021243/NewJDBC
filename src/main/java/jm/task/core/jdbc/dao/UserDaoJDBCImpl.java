package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Util newConnect = new Util();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = newConnect.getConnection()) {
            Statement statement = connection.createStatement();

            String SQL = "create table if not exists User(\n" +
                    "                 id bigint primary key auto_increment,\n" +
                    "                 name varchar(255),\n" +
                    "                 lastName varchar(255),\n" +
                    "                 age tinyint\n" +
                    "             )";

            statement.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Connection connection = newConnect.getConnection()) {
            Statement statement = connection.createStatement();

            String SQL = "drop table if exists User";

            statement.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = newConnect.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into User (name, lastName, age) values (?, ?, ?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();

            System.out.println("User " + name + " created");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = newConnect.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("delete from User where id = ?");
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = newConnect.getConnection()) {
            Statement statement = connection.createStatement();
            String SQL = "select * from User";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));

                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    public void cleanUsersTable() {
        try (Connection connection = newConnect.getConnection()) {
            Statement statement = connection.createStatement();
            String SQL = "truncate table User";
            statement.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
