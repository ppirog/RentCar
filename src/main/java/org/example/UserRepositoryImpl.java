package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class UserRepositoryImpl implements UserRepository {
    private final String PATH = "src/main/resources/users.csv";

    private final File plikCSV = new File(PATH);
    private final Map<String, User> users = new HashMap<>();

    UserRepositoryImpl() {
        readFromCsv();
    }

    private void readFromCsv() {
        try (java.util.Scanner scanner = new java.util.Scanner(plikCSV)) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                users.put(line[0], new User(line[0], line[1], line[2]));
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas odczytywania danych z pliku CSV: " + e.getMessage());
        }
    }

    @Override
    public void addUser(final User user) {
        users.put(user.login(), user);

        // save to csv
        try (FileWriter writer = new FileWriter(plikCSV)) {
            for (User u : users.values()) {
                writer.append(u.login())
                        .append(",")
                        .append(u.password())
                        .append(",")
                        .append(u.role())
                        .append("\n");
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisywania danych do pliku CSV: " + e.getMessage());
        }
    }

    @Override
    public User getUser(final String surname) {
        return users.get(surname);
    }

    @Override
    public boolean exists(final String surname) {
        return users.containsKey(surname);
    }
}
