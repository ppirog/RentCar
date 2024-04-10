package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure(); // Domyslna konfiguracja z pliku hibernate.cfg.xml

        // Tworzenie fabryki sesji na podstawie konfiguracji
        SessionFactory sessionFactory = configuration.buildSessionFactory();


        User currentUser = null;

        System.out.println("L - logowanie \nR - rejestracja");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        IUserRepository userRepository = new HibernateUserRepository(sessionFactory);//new HibernateUserRepository(session
        IVehicleRepository carManager = new HibernateVehicleRepository(sessionFactory);//new HibernateVehicleRepository(session);
//      przed użyciem trzeba utworzyć baze danych taką jaka jest w skrypcie create_table.sql

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
                        System.out.println("Zalogowano jako " + currentUser.login() + " rola: " + currentUser.role());
                        break;
                    }
                    System.out.println("Błędne dane, spróbuj ponownie");
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

                while (true) {

                    System.out.println("Podaj login");
                    String login2 = reader.readLine();

                    System.out.println("Podaj hasło");
                    String keyPassword = Hasher.hashPassword(reader.readLine());

                    if (Authentication.verify(login2, keyPassword, userRepository)) {
                        currentUser = userRepository.getUser(login2);
                        System.out.println("Zalogowano jako " + currentUser.login() + " rola: " + currentUser.role());
                        break;
                    }
                    System.out.println("Błędne dane, spróbuj ponownie");
                }
            }
        } catch (Exception e) {
            System.out.println("Błąd");
        }


        while (true) {

            System.out.println("1. Wypożycz samochód");
            System.out.println("2. Oddaj samochód");

            if (Objects.requireNonNull(currentUser).role().equals("admin")) {
                System.out.println("3. Dodaj samochód");
                System.out.println("4. Usuń samochód");
                System.out.println("5. Wyświetl wszystkich userów");
            }
            System.out.println("exit. Wyloguj");

            try {
                String choice = reader.readLine().trim();

                if (choice.equals("1")) {

                    System.out.println("Podaj numer rejestracyjny samochodu");
                    String registrationNumber = reader.readLine();
                    carManager.rentVehicle(registrationNumber, currentUser.login());

                } else if (choice.equals("2")) {

                    System.out.println("Podaj numer rejestracyjny samochodu");
                    String registrationNumber = reader.readLine();
                    carManager.returnVehicle(registrationNumber);

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

                    carManager.addVehicle(toAdd);

                } else if (choice.equals("4") && currentUser.role().equals("admin")) {

                    System.out.println("Podaj numer rejestracyjny samochodu");
                    String registrationNumber = reader.readLine();

                    if (carManager.getVehicle(registrationNumber).isRented) {
                        System.out.println("Samochód jest wypożyczony. Chwilowo nie można usunąć go z bazy.");
                    } else {
                        carManager.removeVehicle(registrationNumber);
                    }

                } else if (choice.equals("5") && currentUser.role().equals("admin")) {

                    System.out.println("Lista userów: ");
                    userRepository.getUsers()
                            .forEach(user -> System.out.println(user.login()));

                } else if (choice.equalsIgnoreCase("exit")) {
                    System.out.println("Wylogowano");
                    break;
                }

                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Błąd");
            }
        }


    }
}