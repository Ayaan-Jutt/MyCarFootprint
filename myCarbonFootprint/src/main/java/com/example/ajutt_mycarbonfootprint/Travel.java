package com.example.ajutt_mycarbonfootprint;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Travel implements Serializable {
    private String name;
    private String fuelType;
    private LocalDate date;
    private Double PPL;
    private Double amount;
    private Double fuelCost;


    private Integer footprint;
    public Travel(String name, String fuelType, LocalDate date, Double PPL, Double amount, Double fuelCost, Integer footprint) {
        this.name = name;
        this.fuelType = fuelType;
        this.date = date;
        this.PPL = PPL;
        this.amount = amount;
        this.fuelCost = fuelCost;
        this.footprint = footprint;
    }


    //Getters and Setters

    public String getName() {
        return name;
    }

    public String getFuelType() {
        return fuelType;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getPPL() {
        return PPL;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getFuelCost() {
        return fuelCost;
    }


    public Integer getFootprint() {
        return footprint;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPPL(Double PPL) {
        this.PPL = PPL;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setFuelCost(Double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public void setFootprint(Integer footprint){
        this.footprint = footprint;
    }
}
