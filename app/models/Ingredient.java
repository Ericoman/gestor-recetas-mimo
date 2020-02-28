package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import io.ebean.annotation.NotNull;
import play.data.validation.Constraints;
import views.IngredientRefSerializer;
import views.RecipeRefSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Ingredient extends BaseModel {
    @Constraints.Required
    @NotNull
    @Column(unique = true)
    private String name; //no nulo
    private String category; //posible enum
    private String properties;
    @JsonIgnoreProperties(value="ingredients")
    @JsonSerialize(using = RecipeRefSerializer.class)
    @ManyToMany(mappedBy = "ingredients")
    private Set<Recipe> recipes;

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

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void clearRecipes(){
        for(Recipe recipe : this.recipes){
            recipe.removeIngredient(this);
        }
        this.recipes.clear();
    }
    public  void addRecipe(Recipe recipe){
        this.recipes.add(recipe);
    }
    public void setAll(Ingredient ingredient){
        this.setName(ingredient.getName());
        this.setCategory(ingredient.getCategory());
        this.setProperties(ingredient.getProperties());
        this.setRecipes(ingredient.getRecipes());
    }
    public static final Finder<Long,Ingredient> find = new Finder<>(Ingredient.class);

    public static Ingredient findById(Long id){
        return find.byId(id);
    }
    public static List<Ingredient> findAll(){
        return find.all();
    }
    public static List<Ingredient> findAllFromRecipe(Recipe recipe){
        List<Long> ingredientsIds = new ArrayList<>();
        for(Ingredient i : recipe.getIngredients()){
            ingredientsIds.add(i.getId());
        }
        if(ingredientsIds.isEmpty()){
            return recipe.getIngredients();
        }
        return find.query().where().idIn(ingredientsIds).findList();
    }
    public static List<Ingredient> findAllFromRecipeByName(Recipe recipe){
        List<String> ingredientsNames = new ArrayList<>();
        for(Ingredient i : recipe.getIngredients()){
            ingredientsNames.add(i.getName());
        }
        if(ingredientsNames.isEmpty()){
            return recipe.getIngredients();
        }
        return find.query().where().in("name",ingredientsNames).findList();
    }
    public static Ingredient findByName(String name){
        return find.query().where().eq("name",name).findOne();
    }
}
