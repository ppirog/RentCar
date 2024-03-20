package org.example;

import java.util.List;

interface UserRepository {

        void addUser(final User user);

        User getUser(final String surname);

        boolean exists(final String surname);

        List<User> getAllUsers();
}
