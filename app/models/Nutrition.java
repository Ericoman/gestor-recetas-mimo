package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ebean.Finder;
import io.ebean.annotation.NotNull;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
public class Nutrition extends BaseModel {
    @Constraints.Required
    @NotNull
    private String portionSize; //no nulo
    @Constraints.Required
    @NotNull
    @Constraints.Min(0)
    @Min(0)
    private Double calories; // no nulo
    @Constraints.Min(0)
    @Min(0)
    private Double totalFat;
    @Constraints.Min(0)
    @Min(0)
    private Double saturatedFat;
    @Constraints.Min(0)
    @Min(0)
    private Double cholesterol;
    @Constraints.Min(0)
    @Min(0)
    private Double totalCarbohydrates;
    @Constraints.Min(0)
    @Min(0)
    private Double fiber;
    @Constraints.Min(0)
    @Min(0)
    private Double sugar;
    @Constraints.Min(0)
    @Min(0)
    private Double protein;
    @NotNull
    @JsonIgnoreProperties(value="nutrition")
    @OneToOne(mappedBy="nutrition")
    private Recipe recipe;

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

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setAll(Nutrition nutrition){
        this.setProtein(nutrition.getProtein());
        this.setSugar(nutrition.getSugar());
        this.setFiber(nutrition.getFiber());
        this.setTotalCarbohydrates(nutrition.getTotalCarbohydrates());
        this.setCholesterol(nutrition.getCholesterol());
        this.setSaturatedFat(nutrition.getSaturatedFat());
        this.setTotalFat(nutrition.getTotalFat());
        this.setCalories(nutrition.getCalories());
        this.setPortionSize(nutrition.getPortionSize());
    }

    public static final Finder<Long,Nutrition> find = new Finder<>(Nutrition.class);

    public static Nutrition findById(Long id){
        return find.byId(id);
    }
    public static List<Nutrition> findAll(){
        return find.all();
    }
}
