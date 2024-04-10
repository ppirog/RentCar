package org.example;

class Authentication {

    public static boolean verify(final String username, final String password,final IUserRepository userRepository) {
        final boolean exist = exist(username, userRepository);
        if(!exist) return false;
        final User user = userRepository.getUser(username);
        return user.password().equals(password);
    }

    public static boolean exist(final String username, final IUserRepository userRepository) {
        return userRepository.exists(username);
    }
}
