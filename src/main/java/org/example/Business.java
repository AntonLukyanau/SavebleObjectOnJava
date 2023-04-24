package org.example;

public class Business extends SavableObject {

    private String name;
    private Address address;

    public Business() {
    }

    public Business(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }
}
