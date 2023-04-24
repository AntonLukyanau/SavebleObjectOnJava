package org.example;

import java.util.Objects;

public class Address {

    private String street;
    private String city;
    private String stateCode;
    private String postIndex;

    public Address() {
    }

    public Address(String street, String city, String stateCode, String postIndex) {
        this.street = street;
        this.city = city;
        this.stateCode = stateCode;
        this.postIndex = postIndex;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getPostIndex() {
        return postIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        return Objects.equals(
                getStreet(), address.getStreet())
                && Objects.equals(getCity(), address.getCity())
                && Objects.equals(getStateCode(), address.getStateCode())
                && Objects.equals(getPostIndex(), address.getPostIndex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreet(), getCity(), getStateCode(), getPostIndex());
    }

    @Override
    public String toString() {
        return "org.example.Address{" +
                "\n\tstreet='" + street + '\'' +
                ",\n\tcity='" + city + '\'' +
                ",\n\tstateCode='" + stateCode + '\'' +
                ",\n\tpostIndex='" + postIndex + '\'' +
                '}';
    }

}
