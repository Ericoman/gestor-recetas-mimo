package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import play.api.i18n.MessagesApi;
import play.data.validation.Constraints;
import play.mvc.Http;
import validators.CategoryUniqueName;
import views.IngredientRefSerializer;
import views.RecipeRefSerializer;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

@Entity
@CategoryUniqueName
public class Category extends BaseModel{
    @Constraints.Required(message = "validation.error.required")
    @Column(unique = true)
    private String name;
    private Double popularity;
    @JsonIgnoreProperties(value="categories")
    @JsonSerialize(using = RecipeRefSerializer.class)
    @ManyToMany(mappedBy = "categories")
    private List<Recipe> recipes;


    public Category(String name, Double popularity, List<Recipe> recipes) {
        this.name = name;
        this.popularity = popularity;
        this.recipes = recipes;
    }

    public Category(String name, Double popularity) {
        this.name = name;
        this.popularity = popularity;
    }
    public Category(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void setAll(Category category){
        this.setName(category.getName());
        this.setPopularity(category.getPopularity());
        this.setRecipes(category.getRecipes());
    }

    public void clearRecipes(){
        for(Recipe recipe : this.recipes){
            recipe.removeCategory(this);
        }
        this.recipes.clear();
    }
    public  void addRecipe(Recipe recipe){
        this.recipes.add(recipe);
    }

    public static final Finder<Long,Category> find = new Finder<>(Category.class);

    public static Category findById(Long id){
        return find.byId(id);
    }
    public static List<Category> findAll(){
        return find.all();
    }
    public static List<Category> findAllFromRecipe(Recipe recipe){
        List<Long> categoriesIds = new ArrayList<>();
        for(Category c : recipe.getCategories()){
            categoriesIds.add(c.getId());
        }
        if(categoriesIds.isEmpty()){
            return recipe.getCategories();
        }
        return find.query().where().idIn(categoriesIds).findList();
    }
    public static List<Category> findAllFromRecipeByName(Recipe recipe){
        List<String> categoriesNames = new ArrayList<>();
        for(Category c : recipe.getCategories()){
            categoriesNames.add(c.getName());
        }
        if(categoriesNames.isEmpty()){
            return recipe.getCategories();
        }
        return find.query().where().in("name",categoriesNames).findList();
    }
    public static Category findByName(String name){
        return find.query().where().eq("name",name).findOne();
    }
    public Map<String,String> forceValidate(MessagesApi messagesApi, Http.RequestHeader request) {
        ValidatorFactory factory = Validation.byDefaultProvider().configure().messageInterpolator(new ParameterMessageInterpolator()).buildValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Category>> violations =
                validator.validate(this);
        Map<String,String> errors = new HashMap<String,String>();
        for (ConstraintViolation<Category> cv : violations) {
            errors.put(cv.getPropertyPath().toString(), messagesApi.preferred(request).asJava().apply(cv.getMessage()));
        }
        return errors;
    }
}
