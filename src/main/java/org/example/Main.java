package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {


    public static void main(String[] args) {

        // logowanie lub reestracja
        // wybór użytkownika
        User currentUser = null;

        System.out.println(" L - logowanie \nR - rejestracja");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String choice = reader.readLine().trim();

            UserRepository userRepository = new UserRepositoryImpl();
            if (choice.equals("L")) {

                while (true) {

                    System.out.println("Podaj login");
                    String login = reader.readLine();
                    System.out.println("Podaj hasło");
                    String keyPassword = Hasher.hashPassword(reader.readLine());

                    if (userRepository.exists(login) && userRepository.getUser(login).password().equals(keyPassword)) {
                        currentUser = userRepository.getUser(login);
                        break;
                    }
                    System.out.println("Błędne dane logowania");
                }


            } else if (choice.equals("R")) {

                //najpierw sprawdzamy czy użytkownik istnieje
                System.out.println("Podaj login");
                String login = reader.readLine();
                while (userRepository.exists(login)) {
                    System.out.println("Użytkownik o podanym loginie już istnieje, podaj inny login");
                    login = reader.readLine();
                }

                System.out.println("Podaj hasło");
                String password = reader.readLine();
                password = Hasher.hashPassword(password);
                String role = "user";

                User user = User.builder()
                        .login(login)
                        .password(password)
                        .role(role)
                        .build();

                userRepository.addUser(user);
                currentUser = user;
            }
        } catch (Exception e) {
            System.out.println("Błąd");
        }

        System.out.println("Zalogowano jako " + currentUser.login() + " rola: " + currentUser.role());

        CarManager carManager = new CarManager();

        while (true) {

            System.out.println("1. Wypożycz samochód");
            System.out.println("2. Oddaj samochód");

            try {
                String choice = reader.readLine().trim();

                if (choice.equals("1")) {

                    System.out.println("Podaj numer rejestracyjny samochodu");
                    String registrationNumber = reader.readLine();
                    carManager.rentCar(registrationNumber, currentUser.login());

                } else if (choice.equals("2")) {

                    System.out.println("Podaj numer rejestracyjny samochodu");
                    String registrationNumber = reader.readLine();
                    carManager.returnCar(registrationNumber);

                }

                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Błąd");
            }
        }
    }
}