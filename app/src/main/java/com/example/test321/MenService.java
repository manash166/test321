package com.example.test321;

public class MenService {
    private String serviceName;
    private String price;

    public MenService(String serviceName, String price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getPrice() {
        return price;
    }
}

