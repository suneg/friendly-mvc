package com.company.web.models;

public class User {
    private final String name;
    private final String email;
    private final String company;

    public User(String name, String email, String company) {
        this.name = name;
        this.email = email;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }
}
