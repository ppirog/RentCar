package org.example;

import java.util.Collection;

interface IUserRepository {

        User getUser(String login);

        void addUser(User user);

        boolean exists(final String surname);

        void removeUser(String login);

        Collection<User> getUsers();
}
