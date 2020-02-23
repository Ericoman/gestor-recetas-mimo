package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ebean.Finder;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Category extends BaseModel{
    private String name;
    private Double popularity;
    @JsonIgnoreProperties(value="categories")
    @ManyToMany(mappedBy = "categories")
    private Set<Recipe> recipes;


    public Category(String name, Double popularity, Set<Recipe> recipes) {
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

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
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
    public static Category findByName(String name){
        return find.query().where().eq("name",name).findOne();
    }
}
