package org.example;

interface UserRepository {

        void addUser(final User user);

        User getUser(final String surname);

        boolean exists(final String surname);
}
