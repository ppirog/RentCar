package org.example;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcUserRepository implements IUserRepository {
    private static JdbcUserRepository instance;
    private static String GET_ALL_USERS_SQL = "SELECT * FROM tuser";
    private static String GET_USER_SQL = "SELECT * FROM tuser WHERE login LIKE ?";
    private final DatabaseManager databaseManager;

    public JdbcUserRepository() {
        this.databaseManager = DatabaseManager.getInstance();
    }

    public static JdbcUserRepository getInstance() {
        if (JdbcUserRepository.instance == null) {
            JdbcUserRepository.instance = new JdbcUserRepository();
        }
        return instance;
    }

    @Override
    public User getUser(String login) {
        User user = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = databaseManager.getConnection();
            connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_SQL);
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }

    @Override
    public void addUser(User user) {
        Connection connection = null;
        try {
            connection = databaseManager.getConnection();
            connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tuser (login, password, role) VALUES (?, ?, ?)");
            preparedStatement.setString(1, user.login());
            preparedStatement.setString(2, user.password());
            preparedStatement.setString(3, user.role());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Override
    public boolean exists(final String surname) {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = databaseManager.getConnection();
            connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tuser WHERE login LIKE ?");
            preparedStatement.setString(1, surname);
            rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void removeUser(String login) {
        Connection connection = null;
        try {
            connection = databaseManager.getConnection();
            connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM tuser WHERE login LIKE ?");
            preparedStatement.setString(1, login);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Collection<User> getUsers() {
        Collection<User> users = new ArrayList<>();
        try (Connection connection = databaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_USERS_SQL)) {
            while (resultSet.next()) {
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                final User user = User.builder()
                        .login(login)
                        .password(password)
                        .role(role)
                        .build();
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}
