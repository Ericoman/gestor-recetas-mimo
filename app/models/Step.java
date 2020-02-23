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
public class Step extends BaseModel {
    @Constraints.Required
    @NotNull
    @Constraints.Min(1)
    @Min(1)
    @Column(unique = true)
    private Long number;
    @Constraints.Required
    @NotNull
    private String title;
    private String description;
    @Constraints.Min(0)
    @Min(0)
    private Double time;
    @JsonIgnoreProperties(value="steps")
    @ManyToOne
    private Recipe recipe;


    public Step(String title, String description, Double time, Long number) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.number = number;
    }

    public  Step(){

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

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setAll(Step step){
        this.setTime(step.getTime());
        this.setDescription(step.getDescription());
        this.setTitle(step.getTitle());
        this.setNumber(step.getNumber());
        //this.setRecipe(step.getRecipe());
    }

    public static final Finder<Long,Step> find = new Finder<>(Step.class);

    public static Step findById(Long id){
        return find.byId(id);
    }
    public static Step findByIdFromRecipe(Long id, Long recipeId){
        return find.query().where().eq("id",id).and().eq("recipe_id",recipeId).findOne();
    }
    public static Step findByNumber(Long number){
        return find.query().where().eq("number",number).findOne();
    }
    public static List<Step> findAll(){
        return find.all();
    }
    public static List<Step> findAllFromRecipe(Long recipeId){
        return  find.query().where().eq("recipe_id",recipeId).findList();
    }
    public static Step findByTitle(String title){
        return find.query().where().eq("title",title).findOne();
    }
}
