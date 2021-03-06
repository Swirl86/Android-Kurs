package com.example.lifecycle;

public class UserDetailsEntity {

    private String email;
    private String name;
    private String hobbies;
    private String country;
    private String gender;


    public UserDetailsEntity(String email) {
        this.email = email;
        this.name = "";
        this.hobbies = "";
        this.country = "";
        this.gender = "";
    }

    public UserDetailsEntity(String email, String name, String hobbies, String country, String gender) {
        this.email = email;
        this.name = name;
        this.hobbies = hobbies;
        this.country = country;
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
