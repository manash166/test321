package com.example.test321;

public class MenService {
    private String serviceName;
    private String price;

    // Constructor
    public MenService(String serviceName, String price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    // Getter for serviceName
    public String getServiceName() {
        return serviceName;
    }

    // Setter for serviceName
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    // Getter for price
    public String getPrice() {
        return price;
    }

    // Setter for price
    public void setPrice(String price) {
        this.price = price;
    }
}
