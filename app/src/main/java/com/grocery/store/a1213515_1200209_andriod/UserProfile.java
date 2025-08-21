package com.grocery.store.a1213515_1200209_andriod;

public class UserProfile {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;
    private String city;
    private String profileImagePath;

    // Constructor
    public UserProfile() {}

    public UserProfile(String email, String firstName, String lastName, String phone,
                       String gender, String city, String profileImagePath) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.gender = gender;
        this.city = city;
        this.profileImagePath = profileImagePath;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}