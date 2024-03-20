package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        User currentUser = null;

        System.out.println("L - logowanie \nR - rejestracja");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        UserRepository userRepository = new UserRepositoryImpl();
        try {
            String choice = reader.readLine().trim();

            if (choice.equals("L")) {

                while (true) {

                    System.out.println("Podaj login");
                    String login = reader.readLine();
                    System.out.println("Podaj hasło");
                    String keyPassword = Hasher.hashPassword(reader.readLine());

                    if (Authentication.verify(login, keyPassword, userRepository)) {
                        currentUser = userRepository.getUser(login);
                        break;
                    }
                    System.out.println("Błędne dane logowania");
                }


            } else if (choice.equals("R")) {

                //najpierw sprawdzamy czy użytkownik istnieje
                System.out.println("Podaj login");
                String login = reader.readLine();

                while (Authentication.exist(login, userRepository)) {
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

        assert currentUser != null;
        System.out.println("Zalogowano jako " + currentUser.login() + " rola: " + currentUser.role());

        CarManager carManager = new CarManager();

        while (true) {

            System.out.println("1. Wypożycz samochód");
            System.out.println("2. Oddaj samochód");

            if (currentUser.role().equals("admin")) {
                System.out.println("3. Dodaj samochód");
                System.out.println("4. Usuń samochód");
                System.out.println("5. Wyświetl wszystkich userów");
            }

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

                } else if (choice.equals("3") && currentUser.role().equals("admin")) {

                    System.out.println("1. Dodawanie samochodu");
                    System.out.println("2. Dodawanie motocykla");

                    String vehicleChoice = reader.readLine().trim();

                    System.out.println("Podaj markę");
                    String brand = reader.readLine();

                    System.out.println("Podaj model");
                    String model = reader.readLine();

                    System.out.println("Podaj rok produkcji");
                    int year = Integer.parseInt(reader.readLine());

                    System.out.println("Podaj cenę");
                    int price = Integer.parseInt(reader.readLine());

                    System.out.println("Podaj numer rejestracyjny");
                    String registrationNumber = reader.readLine();

                    String category = "";
                    String rentedBy = "-1";

                    if (vehicleChoice.equals("2")) {
                        System.out.println("Podaj kategorię");
                        category = reader.readLine();
                    }

                    Vehicle toAdd = null;

                    if (vehicleChoice.equals("1")) {
                        toAdd = new Car(registrationNumber, brand, model, year, price, false, rentedBy);
                    } else if (vehicleChoice.equals("2")) {
                        toAdd = new Motorcycle(registrationNumber, brand, model, year, price, false, rentedBy, category);
                    }

                    carManager.addToDatabase(toAdd);

                } else if (choice.equals("4") && currentUser.role().equals("admin")) {

                    System.out.println("Podaj numer rejestracyjny samochodu");
                    String registrationNumber = reader.readLine();

                    if (carManager.getVehicle(registrationNumber).isRented) {
                        System.out.println("Samochód jest wypożyczony. Chwilowo nie można usunąć go z bazy.");
                    } else {
                        carManager.removeFromDatabase(registrationNumber);
                    }

                } else if (choice.equals("5") && currentUser.role().equals("admin")) {

                    System.out.println("Lista userów: ");
                    userRepository.getAllUsers()
                            .forEach(user -> System.out.println(user.login()));

                }

                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Błąd");
            }
        }


    }
}