package models;

import javax.persistence.*;

@Entity
public class Step extends BaseModel {
    private String title;
    private String description;
    private Double time;
    //private String tools;
        //Nombre, Material

    public Step(String title, String description, Double time) {
        this.title = title;
        this.description = description;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
