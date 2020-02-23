package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ebean.Finder;
import io.ebean.annotation.NotNull;
import org.checkerframework.checker.units.qual.A;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe extends BaseModel {
    @Constraints.Required
    @NotNull
    private String title; //No nulo
    @JsonIgnoreProperties(value="recipes")
    @ManyToMany(cascade = CascadeType.REMOVE)
    private List<Category> categories;// Many to Many
    private String source;
    private Integer portions;
    @Constraints.Required
    @NotNull
    @Constraints.Min(0)
    @Min(0)
    private Double time; // No nulo
    @Constraints.Min(0)
    @Min(0)
    @Constraints.Max(5)
    @Max(5)
    private Double calification;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnoreProperties(value="recipe")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private List<Step> steps; //One to Many
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnoreProperties(value="recipe")
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="id_nutrition")
    private Nutrition nutrition;
    @JsonIgnoreProperties(value="recipes")
    @ManyToMany(cascade = {CascadeType.REMOVE})
    private List<Ingredient> ingredients;




    public Recipe(String title, ArrayList <Category> categories,/*String categories,*/ String source, Integer portions, Double time, Double calification, ArrayList<Step> steps/*String steps*/, Nutrition nutrition) {
        this.title = title;
        this.categories = categories;
        //this.categories = categories;
        this.source = source;
        this.portions = portions;
        this.time = time;
        this.calification = calification;
        this.steps = steps;
        this.nutrition = nutrition;
    }

    public Recipe() {
        this.categories = new ArrayList<Category>();
        this.steps = new ArrayList<Step>();
        this.ingredients = new ArrayList<Ingredient>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {

        this.categories = categories;
    }

    public void  addCategory(Category category){
        if(!this.ingredients.contains(category)){
            category.addRecipe(this);
            this.categories.add(category);
        }
        /*boolean presente = false;
        for(Ingredient i : this.ingredients){
            if(i.getId() == ingredient.getId()){
                presente = true;
            }
        }
        if(!presente) {
            ingredient.addRecipe(this);
            this.ingredients.add(ingredient);
        }*/
    }
    public void removeCategory(Category category){
        this.categories.remove(category);
    }
    public void clearCategories(){
        this.categories.clear();
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

    public void setTime(Double time) {
        this.time = time;
    }

    public Double getCalification() {
        return calification;
    }

    public void setCalification(Double calification) {
        this.calification = calification;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        steps.forEach(step -> {
            step.setRecipe(this);
        });
        this.steps = steps;
    }
    public void addStep(Step step){
        step.setRecipe(this);
        if(!this.steps.contains(step)) {
            this.steps.add(step);
        }
    }
    /*
        public String getSteps() {
            return steps;
        }

        public void setSteps(String steps) {
            this.steps = steps;
        }
    */
    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition nutrition) {
        if(nutrition != null) {
            nutrition.setRecipe(this);
        }
            this.nutrition = nutrition;

    }

    public void deleteNutrition(){
        this.nutrition = null;
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public void  addIngredient(Ingredient ingredient){
        if(!this.ingredients.contains(ingredient)){
            ingredient.addRecipe(this);
            this.ingredients.add(ingredient);
        }
        /*boolean presente = false;
        for(Ingredient i : this.ingredients){
            if(i.getId() == ingredient.getId()){
                presente = true;
            }
        }
        if(!presente) {
            ingredient.addRecipe(this);
            this.ingredients.add(ingredient);
        }*/
    }
    public void removeIngredient(Ingredient ingredient){
        this.ingredients.remove(ingredient);
    }
    public void clearIngredients(){
        this.ingredients.clear();
    }
    public void setAll(Recipe recipe){
        this.setCalification(recipe.getCalification());
        this.setPortions(recipe.getPortions());
        this.setSource(recipe.getSource());
        this.setTitle(recipe.getTitle());
        this.setNutrition(recipe.getNutrition());
        this.setCategories(recipe.getCategories());
        this.setSteps(recipe.getSteps());
        this.setTime(recipe.getTime());
        this.setIngredients(recipe.getIngredients());
    }

    public static final Finder<Long,Recipe> find = new Finder<>(Recipe.class);

    public static Recipe findById(Long id){
        return find.byId(id);
    }
    public static List<Recipe> findAll(){
        return find.all();
    }
    public static Recipe findByTitle(String title){
        return find.query().where().eq("title",title).findOne();
    }
}
