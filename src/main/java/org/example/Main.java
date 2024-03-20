package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {


    public static void main(String[] args) {

        // logowanie lub reestracja

        // wybór użytkownika

        System.out.println(" L - logowanie R - rejestracja");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String choice = reader.readLine().trim();

            if (choice.equals("L")) {
                System.out.println("Podaj login");
                String login = reader.readLine();
                System.out.println("Podaj hasło");
                String password = reader.readLine();
                User user = User.builder()
                        .surname(login)
                        .password(password)
                        .role("user").build();

                System.out.println(user);
            } else if (choice.equals("R")) {
                System.out.println("Podaj login");
                String login = reader.readLine();
                System.out.println("Podaj hasło");
                String password = reader.readLine();
                User user = User.builder()
                        .surname(login)
                        .password(password)
                        .role("user")
                        .build();
                System.out.println(user);
            }
        } catch (Exception e) {
            System.out.println("Błąd");
        }


//        CarManager carManager = new CarManager();
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        while (true) {
//
//            System.out.println("1. Wypożycz samochód");
//            System.out.println("2. Oddaj samochód");
//
//            try {
//                String choice = reader.readLine().trim();
//
//                if (choice.equals("1")) {
//                    System.out.println("Podaj numer rejestracyjny samochodu");
//                    String registrationNumber = reader.readLine();
//                    carManager.rentCar(registrationNumber);
//
//                } else if (choice.equals("2")) {
//                    System.out.println("Podaj numer rejestracyjny samochodu");
//                    String registrationNumber = reader.readLine();
//                    carManager.returnCar(registrationNumber);
//
//                }
//
//                Thread.sleep(1000);
//            } catch (Exception e) {
//                System.out.println("Błąd");
//            }
//        }
    }
}