package com.example.jockay.model;

/**
 * Created by Jockay on 2015.12.06..
 */
public class User {
    private String surname;
    private String firstName;
    private String birthDate;
    private String username;
    private String password;
    private String email;
    private String role;
    private String state;

    public User() {}
    public User(String Surname, String Firstname, String Birthdate, String Username,
                String Password, String Email, String Role, String State) {
        this.surname = Surname;
        this.firstName = Firstname;
        this.birthDate = Birthdate;
        this.email = Email;
        this.role = Role;
        this.state = State;
        this.password = Password;
        this.username = Username;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "surname='" + surname + "\'\n" +
                ", firstName='" + firstName + "\'\n" +
                ", birthDate='" + birthDate + "\'\n" +
                ", username='" + username + "\'\n" +
                ", password='" + password + "\'\n" +
                ", email='" + email + "\'\n" +
                ", role='" + role + "\'\n" +
                ", state='" + state + "\'\n" +
                '}';
    }
}
