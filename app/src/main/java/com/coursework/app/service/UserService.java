package com.coursework.app.service;

import com.coursework.app.entity.User;
import com.coursework.app.exception.NoUserByLoginException;
import com.coursework.app.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public List<User> getUsers() throws SQLException {
        return userRepository.getUsers();
    }

    public User getUser(String userLogin) throws SQLException, NoUserByLoginException {
        List<User> users = userRepository.getUsers();

        Optional<User> optionalUser = users.stream().filter(user -> user.getUserLogin().equals(userLogin)).findFirst();

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        throw new NoUserByLoginException("Not find user by login " + userLogin);
    }

    public void setActiveStatus(String userLogin, int status) throws SQLException {
        userRepository.setActiveStatus(userLogin, status);
    }

    public void addUser(User user) throws SQLException {
        userRepository.addUser(user);
    }
}
