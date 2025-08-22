package com.vitasync.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String phone;
    private String address;
    private String city;
    private Boolean isAvailable;
}