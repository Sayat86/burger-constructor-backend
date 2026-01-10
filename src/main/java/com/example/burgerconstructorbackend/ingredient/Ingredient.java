package com.example.burgerconstructorbackend.ingredient;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @UuidGenerator
    private UUID id;

    private String name;
    private String type;

    private int proteins;
    private int fat;
    private int carbohydrates;
    private int calories;

    private double price;

    private String image;
    private String imageLarge;
    private String imageMobile;
}
