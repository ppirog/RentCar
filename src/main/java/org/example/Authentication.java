package org.example;

class Authentication {

    public static boolean verify(final String username, final String password, final UserRepository userRepository) {
        return userRepository.exists(username);
    }

    public static boolean exist(final String username, final UserRepository userRepository) {
        return userRepository.exists(username);
    }
}
