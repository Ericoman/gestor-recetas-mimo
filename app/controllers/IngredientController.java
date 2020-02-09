package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Ingredient;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import views.xml.ingredient;
import play.libs.Json;
import views.xml.ingredientCollection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class IngredientController extends Controller {
    @Inject
    FormFactory formFactory;
    //TODO Borrar esto y hacerlo con la db
    List<Ingredient> ingredients = new ArrayList<>();
    //
    public Result getIngredient(Http.Request request,Integer id){
        Ingredient selectedIngredient = this.ingredients.get(id.intValue());
        if (request.accepts("application/xml")){
            return Results.ok(ingredient.render(selectedIngredient,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(selectedIngredient));
        }else{
            return Results.status(415);
        }
    }
    public Result createIngredient(Http.Request request){
        Ingredient ingrediente;

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                ingrediente = Json.fromJson(jsonBody, Ingredient.class);
                this.ingredients.add(ingrediente);
                break;
            default:
                Form<Ingredient> form =  formFactory.form(Ingredient.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                ingrediente = form.get();
                this.ingredients.add(ingrediente);

                break;
        }
        if (request.accepts("application/xml")){
            return Results.created(ingredient.render(ingrediente,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(ingrediente));
        }else{
            return Results.created();
        }
    }
    public Result getIngredientCollection(Http.Request request){

        if (request.accepts("application/xml")){
            return Results.ok(ingredientCollection.render(this.ingredients));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(this.ingredients));
        }else{
            return Results.status(415);
        }
    }
    public Result updateIngredient(Http.Request request,Integer id){
        //TODO hacer la comprobación de que existe ese id
        Ingredient ingrediente = this.ingredients.get(id.intValue());

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
                break;
            default:
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

                break;
        }
        if (request.accepts("application/xml")){
            return Results.ok(ingredient.render(this.ingredients.get(id),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(this.ingredients.get(id)));
        }else{
            return Results.ok();
        }
    }
    public Result deleteIngredient(Http.Request request,Integer id){
        this.ingredients.remove(id.intValue());
        return Results.noContent();
    }
}
