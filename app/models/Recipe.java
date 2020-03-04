package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import io.ebean.annotation.NotNull;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import play.api.i18n.MessagesApi;
import play.data.validation.Constraints;
import play.mvc.Http;
import validators.RecipeUniqueTitle;
import views.CategoryRefSerializer;
import views.IngredientRefSerializer;
import views.SingleNutritionRefSerializer;
import views.StepRefSerializer;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.*;

@Entity
@RecipeUniqueTitle
public class Recipe extends BaseModel {
    @Constraints.Required(message = "validation.error.required")
    @NotNull
    @Column(unique = true)
    private String title;
    @JsonIgnoreProperties(value="recipes")
    @JsonSerialize(using = CategoryRefSerializer.class)
    @ManyToMany(cascade = CascadeType.REMOVE)
    private List<Category> categories;// Many to Many
    private String source;
    private Integer portions;
    @Constraints.Required(message = "validation.error.required")
    @NotNull
    @Constraints.Min(value = 0,message = "validation.error.min")
    @Min(value = 0,message = "validation.error.min" )
    private Double time;
    @Constraints.Min(value = 0,message = "validation.error.min")
    @Min(value = 0, message = "validation.error.min")
    @Constraints.Max(value = 5,message = "validation.error.max")
    @Max(value = 5, message = "validation.error.max")
    private Double calification;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnoreProperties(value="recipe")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    @JsonSerialize(using = StepRefSerializer.class)
    private List<Step> steps; //One to Many
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnoreProperties(value="recipe")
    @JsonSerialize(using = SingleNutritionRefSerializer.class)
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="id_nutrition")
    private Nutrition nutrition;
    @JsonIgnoreProperties(value="recipes")
    @JsonSerialize(using = IngredientRefSerializer.class)
    @ManyToMany(cascade = {CascadeType.REMOVE})
    private List<Ingredient> ingredients;




    public Recipe(String title, ArrayList <Category> categories, String source, Integer portions, Double time, Double calification, ArrayList<Step> steps, Nutrition nutrition) {
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
        if(!this.categories.contains(category)){
            category.addRecipe(this);
            this.categories.add(category);
        }

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
    public static List<Recipe> findAllFromIngredient(Ingredient ingredient){
        List<Long> recipesIds = new ArrayList<>();
        for(Recipe r : ingredient.getRecipes()){
            recipesIds.add(r.getId());
        }
        if(recipesIds.isEmpty()){
            return new ArrayList<>(ingredient.getRecipes());
        }
        return find.query().where().idIn(recipesIds).findList();
    }
    public static List<Recipe> findAllFromIngredientByTitle(Ingredient ingredient){
        List<String> recipesTitles = new ArrayList<>();
        for(Recipe r : ingredient.getRecipes()){
            recipesTitles.add(r.getTitle());
        }
        if(recipesTitles.isEmpty()){
            return new ArrayList<>(ingredient.getRecipes());
        }
        return find.query().where().in("title",recipesTitles).findList();
    }
    public static List<Recipe> findAllFromCategory(Category category){
        List<Long> recipesIds = new ArrayList<>();
        for(Recipe r : category.getRecipes()){
            recipesIds.add(r.getId());
        }
        if(recipesIds.isEmpty()){
            return new ArrayList<>(category.getRecipes());
        }
        return find.query().where().idIn(recipesIds).findList();
    }
    public static List<Recipe> findAllFromCategoryByTitle(Category category){
        List<String> recipesTitles = new ArrayList<>();
        for(Recipe r : category.getRecipes()){
            recipesTitles.add(r.getTitle());
        }
        if(recipesTitles.isEmpty()){
            return new ArrayList<>(category.getRecipes());
        }
        return find.query().where().in("title",recipesTitles).findList();
    }
    public static Recipe findByTitle(String title){
        return find.query().where().eq("title",title).findOne();
    }
    public Map<String,String> forceValidate(MessagesApi messagesApi, Http.RequestHeader request) {
        ValidatorFactory factory = Validation.byDefaultProvider().configure().messageInterpolator(new ParameterMessageInterpolator()).buildValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Recipe>> violations =
                validator.validate(this);
        Map<String,String> errors = new HashMap<String,String>();
        for (ConstraintViolation<Recipe> cv : violations) {
            errors.put(cv.getPropertyPath().toString(), messagesApi.preferred(request).asJava().apply(cv.getMessage()));
        }
        return errors;
    }
}
