package com.example.demo.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ErrorDTO {
    private String message;
    private int status;
    private LocalDate time;
}
