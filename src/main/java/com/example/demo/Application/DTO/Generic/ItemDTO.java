package com.example.demo.Application.DTO.Generic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ItemDTO {
    private String title;
    private int quantity;
    private double unitPrice;
}
