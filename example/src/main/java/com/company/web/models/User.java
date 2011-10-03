package com.company.web.models;

public class User {
    private int id;
    private String name;
    private String email;
    private String company;
    private int age;
    
    public static User construct(int id, String name, String company, int age) {
        User user = new User(name, company);
        user.setAge(age);
        user.setId(id);

        return user;
    }

    public User(String name, String company) {
        this.name = name;
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

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
