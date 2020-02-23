package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import models.Category;
import models.Ingredient;
import models.Recipe;
import models.Step;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;

import  play.mvc.Results;

import views.xml.recipe;
import views.xml.recipeCollection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecipeController extends Controller {
    @Inject
    FormFactory formFactory;

    public Result getRecipe(Http.Request request,Long id){
        Recipe selectedRecipe = Recipe.findById(id);
        if(selectedRecipe == null){
            return Results.notFound();
        }
        if (request.accepts("application/xml")){
            return Results.ok(recipe.render(selectedRecipe,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(selectedRecipe));
        }else{
            return Results.status(415);
        }
    }
    public Result createRecipe(Http.Request request){
        Recipe receta;

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                receta = Json.fromJson(jsonBody, Recipe.class);
                receta.save();
                break;
            default:
                Form<Recipe> form =  formFactory.form(Recipe.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                receta = form.get();
                receta.save();
                break;
        }
        if (request.accepts("application/xml")){
            return Results.created(recipe.render(Recipe.findById(receta.getId()),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(Recipe.findById(receta.getId())));
        }else{
            return Results.created();
        }
    }
    public Result getRecipeCollection(Http.Request request){
        List<Recipe> recipes = new ArrayList<>();
        recipes = Recipe.findAll();
        if (request.accepts("application/xml")){
            return Results.ok(recipeCollection.render(recipes));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(recipes));
        }else{
            return Results.status(415);
        }
    }
    public Result updateRecipeTotally(Http.Request request, Long id){
        Recipe receta = Recipe.findById(id);
        Recipe recetaRecibida;
        if(receta == null){
            return Results.notFound();
        }

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                recetaRecibida = Json.fromJson(jsonBody, Recipe.class);
                receta.setAll(recetaRecibida);
                receta.update();
                break;
            default:
                Form<Recipe> form =  formFactory.form(Recipe.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                recetaRecibida = form.get();
                receta.setAll(recetaRecibida);
                receta.update();
                break;
        }
        if (request.accepts("application/xml")){
            return Results.created(recipe.render(Recipe.findById(receta.getId()),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(Recipe.findById(receta.getId())));
        }else{
            return Results.created();
        }
    }
    public Result updateRecipe(Http.Request request,Long id){
        Recipe receta = Recipe.findById(id);
        if(receta == null){
            return Results.notFound();
        }

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                if(jsonBody.has("title")){
                    receta.setTitle( jsonBody.get("title").asText());
                }
                if(jsonBody.has("categories")){
                    receta.clearCategories();
                    Iterator<JsonNode> iterator = jsonBody.get("categories").elements();
                    while (iterator.hasNext()){
                        Category category = Json.fromJson(iterator.next(),Category.class);
                        receta.addCategory(category);
                    }
                }
                if(jsonBody.has("source")){
                    receta.setSource(jsonBody.get("source").asText());
                }
                if(jsonBody.has("portions")){
                    receta.setPortions( jsonBody.get("portions").asInt());
                }
                if(jsonBody.has("time")){
                    receta.setTime(jsonBody.get("time").asDouble());
                }
                if(jsonBody.has("calification")){
                    receta.setCalification(jsonBody.get("calification").asDouble());
                }
                if(jsonBody.has("ingredients")){
                    receta.clearIngredients();
                    Iterator<JsonNode> iterator = jsonBody.get("ingredients").elements();
                    while (iterator.hasNext()){
                        Ingredient ingredient = Json.fromJson(iterator.next(),Ingredient.class);
                        receta.addIngredient(ingredient);
                    }
                }
                receta.update();
                break;
            default:
                Form<Recipe> form =  formFactory.form(Recipe.class).bindFromRequest(request);
                Recipe recetaFormulario;
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                recetaFormulario = form.get();
                if (recetaFormulario.getTitle() != null){
                    receta.setTitle(recetaFormulario.getTitle());
                }
                if (recetaFormulario.getCategories() != null){
                    receta.setCategories(recetaFormulario.getCategories());
                }
                if (recetaFormulario.getSource() != null){
                    receta.setSource(recetaFormulario.getSource());
                }
                if (recetaFormulario.getPortions() != null){
                receta.setPortions(recetaFormulario.getPortions());
                }
                if (recetaFormulario.getTime() != null) {
                    receta.setTime(recetaFormulario.getTime());
                }
                if (recetaFormulario.getCalification() != null){
                    receta.setCalification(recetaFormulario.getCalification());
                }
                if (recetaFormulario.getIngredients() != null){
                    receta.setIngredients(recetaFormulario.getIngredients());
                }
                receta.update();
                break;
        }
        if (request.accepts("application/xml")){
            return Results.ok(recipe.render(Recipe.findById(receta.getId()),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(Recipe.findById(receta.getId())));
        }else{
            return Results.ok();
        }
    }
    public Result deleteRecipe(Http.Request request,Long id){
        Recipe.find.deleteById(id);
        return Results.noContent();
    }
}
