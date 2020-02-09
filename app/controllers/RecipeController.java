package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import models.Recipe;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;

import  play.mvc.Results;

import play.twirl.api.Template2;
import views.xml.recipe;
import views.xml.recipeCollection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class RecipeController extends Controller {
    @Inject
    FormFactory formFactory;
    //TODO Borrar esto y hacerlo con la db
    List<Recipe> recipes = new ArrayList<>();
    //
    public Result getRecipe(Http.Request request,Integer id){
        Recipe selectedRecipe = this.recipes.get(id.intValue());
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
                this.recipes.add(receta);
                break;
            default:
                Form<Recipe> form =  formFactory.form(Recipe.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                receta = form.get();
                this.recipes.add(receta);
                break;
        }
        if (request.accepts("application/xml")){
            return Results.created(recipe.render(receta,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(receta));
        }else{
            return Results.created();
        }
    }
    public Result getRecipeCollection(Http.Request request){
        if (request.accepts("application/xml")){
            return Results.ok(recipeCollection.render(this.recipes));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(this.recipes));
        }else{
            return Results.status(415);
        }
    }
    public Result updateRecipe(Http.Request request,Integer id){
        //TODO hacer la comprobación de que existe ese id
        Recipe receta = this.recipes.get(id.intValue());

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                if(jsonBody.has("title")){
                    receta.setTitle( jsonBody.get("title").asText());
                }
                if(jsonBody.has("categories")){
                    receta.setCategories(jsonBody.get("categories").asText());
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
                if(jsonBody.has("steps")){
                    receta.setSteps( jsonBody.get("steps").asText());
                }
                //Ver como hacer nutricion
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
                if (recetaFormulario.getSteps() != null){
                    receta.setSteps(recetaFormulario.getSteps());
                }

                break;
        }
        if (request.accepts("application/xml")){
            return Results.ok(recipe.render(this.recipes.get(id),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(this.recipes.get(id)));
        }else{
            return Results.ok();
        }
    }
    public Result deleteRecipe(Http.Request request,Integer id){
        this.recipes.remove(id.intValue());
        return Results.noContent();
    }
}
