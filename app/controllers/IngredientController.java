package controllers;

import akka.http.javadsl.model.HttpRequest;
import com.fasterxml.jackson.databind.JsonNode;
import models.Ingredient;
import models.Recipe;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.xml.ingredient;
import play.libs.Json;
import views.xml.ingredientCollection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class IngredientController extends Controller {
    @Inject
    FormFactory formFactory;

    public Result getIngredient(Http.Request request,Long id){
        Ingredient selectedIngredient = Ingredient.findById(id);
        if ( selectedIngredient == null){
            return Results.notFound();
        }
        if (request.accepts("application/xml")){
            return Results.ok(ingredient.render(selectedIngredient,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(selectedIngredient));
        }else{
            return Results.status(415);
        }
    }
    @Transactional
    public Result createIngredient(Http.Request request){
        Ingredient ingrediente;

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                ingrediente = Json.fromJson(jsonBody, Ingredient.class);
                ingrediente.save();
                break;
            case "application/x-www-form-urlencoded":
                Form<Ingredient> form =  formFactory.form(Ingredient.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                ingrediente = form.get();
                ingrediente.save();
                break;
            default:
                return Results.status(415);
        }
        //ingrediente.save();
        if (request.accepts("application/xml")){
            return Results.created(ingredient.render(Ingredient.findById(ingrediente.getId()),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(Ingredient.findById(ingrediente.getId())));
        }else{
            return Results.created();
        }
    }
    public Result getIngredientCollection(Http.Request request){
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients = Ingredient.findAll();
        if (request.accepts("application/xml")){
            return Results.ok(ingredientCollection.render(ingredients));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(ingredients));
        }else{
            return Results.status(415);
        }
    }
    public Result updateIngredientTotally(Http.Request request, Long id){
        Ingredient ingrediente = Ingredient.findById(id);
        Ingredient ingredienteRecibido;
        if (ingrediente == null){
            return Results.notFound();
        }
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                ingredienteRecibido = Json.fromJson(jsonBody, Ingredient.class);
                ingrediente.setAll(ingredienteRecibido);
                ingrediente.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Ingredient> form =  formFactory.form(Ingredient.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                ingredienteRecibido = form.get();
                ingrediente.setAll(ingredienteRecibido);
                ingrediente.update();
                break;
            default:
                return Results.status(415);
        }
        if (request.accepts("application/xml")){
            return Results.created(ingredient.render(Ingredient.findById(ingrediente.getId()),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(Ingredient.findById(ingrediente.getId())));
        }else{
            return Results.created();
        }
    }
    @Transactional
    public Result updateIngredient(Http.Request request,Long id){
        //Ingredient ingrediente = this.ingredients.get(id.intValue());
        Ingredient ingrediente = Ingredient.findById(id);
        if (ingrediente == null){
            return Results.notFound();
        }
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                if(jsonBody.has("name")){
                    ingrediente.setName(jsonBody.get("name").asText());
                }
                if(jsonBody.has("category")){
                    ingrediente.setCategory(jsonBody.get("category").asText());
                }
                if(jsonBody.has("properties")){
                    ingrediente.setProperties(jsonBody.get("properties").asText());
                }
                if(jsonBody.has("recipes")){
                    ingrediente.clearRecipes();
                    Iterator<JsonNode> iterator = jsonBody.get("recipes").elements();
                    while (iterator.hasNext()){
                        Recipe recipe = Json.fromJson(iterator.next(),Recipe.class);
                        recipe.addIngredient(ingrediente);
                    }
                }
                ingrediente.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Ingredient> form =  formFactory.form(Ingredient.class).bindFromRequest(request);
                Ingredient ingredienteFormulario;
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                ingredienteFormulario = form.get();
                if (ingredienteFormulario.getName() != null){
                    ingrediente.setName(ingredienteFormulario.getName());
                }
                if (ingredienteFormulario.getCategory() != null){
                    ingrediente.setCategory(ingredienteFormulario.getCategory());
                }
                if (ingredienteFormulario.getProperties() != null){
                    ingrediente.setProperties(ingredienteFormulario.getProperties());
                }
                if (ingredienteFormulario.getRecipes() != null){
                    ingrediente.setRecipes(ingredienteFormulario.getRecipes());
                }
                ingrediente.update();
                break;
            default:
                return Results.status(415);
        }
        if (request.accepts("application/xml")){
            return Results.ok(ingredient.render(Ingredient.findById(ingrediente.getId()),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(Ingredient.findById(ingrediente.getId())));
        }else{
            return Results.ok();
        }
    }
    public Result deleteIngredient(Http.Request request,Long id){
        Ingredient.find.deleteById(id);
        return Results.noContent();
    }
}
