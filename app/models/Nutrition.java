package models;

import javax.persistence.*;

@Entity
public class Nutrition extends BaseModel {
    private String portionSize;
    private Double calories; // no nulo
    private Double totalFat;
    private Double saturatedFat;
    private Double cholesterol;
    private Double totalCarbohydrates;
    private Double fiber;
    private Double sugar;
    private Double protein;

    public Nutrition(String portionSize, Double calories, Double totalFat, Double saturatedFat, Double cholesterol, Double totalCarbohydrates, Double fiber, Double sugar, Double protein) {
        this.portionSize = portionSize;
        this.calories = calories;
        this.totalFat = totalFat;
        this.saturatedFat = saturatedFat;
        this.cholesterol = cholesterol;
        this.totalCarbohydrates = totalCarbohydrates;
        this.fiber = fiber;
        this.sugar = sugar;
        this.protein = protein;
    }

    public Nutrition() {

    }

    public String getPortionSize() {
        return portionSize;
    }

    public void setPortionSize(String portionSize) {
        this.portionSize = portionSize;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(Double totalFat) {
        this.totalFat = totalFat;
    }

    public Double getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(Double saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Double getTotalCarbohydrates() {
        return totalCarbohydrates;
    }

    public void setTotalCarbohydrates(Double totalCarbohydrates) {
        this.totalCarbohydrates = totalCarbohydrates;
    }

    public Double getFiber() {
        return fiber;
    }

    public void setFiber(Double fiber) {
        this.fiber = fiber;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }
}
