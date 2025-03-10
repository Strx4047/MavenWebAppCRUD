package ru.killreal.net.models;


import javax.validation.constraints.*;

public class Person {

    private int id;

    // Country, City, Post index (6 digits)
    @Pattern(regexp = "[A-Z]\\w+, [A-Z]\\w+, \\d{6}", message = "Your address should be in this form: Country, City, Post index(6 digits)")
    private String address;

    @NotEmpty(message = "Name shouldn't be empty")
    @Size(min = 2, max = 10, message = "Name should be in range at 2 from 10")
    private String name;

    @Min(value = 0, message = "Age should be greater than 0")
    @Max(value = 100, message = "Age should be less than 100")
    private int age;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email shouldn't be empty")
    private String email;

   public Person() {}

        public Person(String name, int id, int age, String email, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
       return address;
    }

    public void setAddress(String address) {
       this.address = address;
    }


}
