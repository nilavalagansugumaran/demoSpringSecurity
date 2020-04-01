package com.example.demoSpringSecurity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Size(min = 2, max = 50, message = "customer name length must be between 2 and 50")
    private String customerName;

    @NotNull(message = "items can not be null")
    private String items;

    @Positive(message = "items value mut be in positive")
    private Double itemsValue;

}
