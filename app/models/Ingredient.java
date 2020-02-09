package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
@Entity
public class Ingredient extends BaseModel {

    private String name; //no nulo
    private String category; //posible enum
    private String properties;

    public Ingredient(String name, String category, String properties) {
        this.name = name;
        this.category = category;
        this.properties = properties;
    }

    public Ingredient() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
