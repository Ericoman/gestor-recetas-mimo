package models;

import javax.persistence.*;

@Entity
public class Recipe extends BaseModel {
    private String title; //No nulo
    private String categories; // Many to Many
    private String source;
    private Integer portions;
    private Double time; // No nulo
    private Double calification;
    private String steps; //One to Many
    @OneToOne()
    @JoinColumn(name="id_nutrition")
    private Nutrition nutrition;



    public Recipe(String title, String categories, String source, Integer portions, Double time, Double calification, String steps, Nutrition nutrition) {
        this.title = title;
        this.categories = categories;
        this.source = source;
        this.portions = portions;
        this.time = time;
        this.calification = calification;
        this.steps = steps;
        this.nutrition = nutrition;
    }

    public Recipe() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getPortions() {
        return portions;
    }

    public void setPortions(Integer portions) {
        this.portions = portions;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Double getCalification() {
        return calification;
    }

    public void setCalification(Double calification) {
        this.calification = calification;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }


}
