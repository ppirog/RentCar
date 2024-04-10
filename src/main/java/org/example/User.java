package org.example;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Builder
@Entity(name = "tuser")
public class User {


    @Id
    @Column(name = "login", nullable = false)
    private final String login;
    @Column(name = "password", nullable = false)
    private final String password;
    @Column(name = "role", nullable = false)
    private final String role;


    User(

            String login,
            String password,
            String role
    ) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User() {
        this.login = null;
        this.password = null;
        this.role = null;
    }

    public String login() {
        return login;
    }

    public String password() {
        return password;
    }

    public String role() {
        return role;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (User) obj;
        return Objects.equals(this.login, that.login) &&
                Objects.equals(this.password, that.password) &&
                Objects.equals(this.role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, role);
    }

    @Override
    public String toString() {
        return "User[" +
                "login=" + login + ", " +
                "password=" + password + ", " +
                "role=" + role + ']';
    }

}
