package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Ingredient;
import models.Nutrition;
import models.Recipe;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.xml.nutrition;
import views.xml.nutritionCollection;
import views.xml.recipe;
import views.xml.recipeCollection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NutritionController extends Controller {
    @Inject
    FormFactory formFactory;
    //TODO Borrar esto y hacerlo con la db
    List<Nutrition> nutritions = new ArrayList<>();
    //
    public Result getNutrition(Http.Request request,Integer id){
        Nutrition selectedNutrition = this.nutritions.get(id.intValue());
        if (request.accepts("application/xml")){
            return Results.ok(nutrition.render(selectedNutrition,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(selectedNutrition));
        }else{
            return Results.status(415);
        }
    }
    public Result createNutrition(Http.Request request){
        Nutrition nutricion;

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                nutricion = Json.fromJson(jsonBody, Nutrition.class);
                this.nutritions.add(nutricion);
                break;
            default:
                Form<Nutrition> form =  formFactory.form(Nutrition.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                nutricion = form.get();
                this.nutritions.add(nutricion);
                break;
        }
        if (request.accepts("application/xml")){
            return Results.created(nutrition.render(nutricion,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(nutricion));
        }else{
            return Results.created();
        }
    }
    public Result getNutritionCollection(Http.Request request){
        if (request.accepts("application/xml")){
            return Results.ok(nutritionCollection.render(this.nutritions));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(this.nutritions));
        }else{
            return Results.status(415);
        }
    }
    public Result updateNutrition(Http.Request request,Integer id){
        //TODO hacer la comprobación de que existe ese id
        Nutrition nutricion = this.nutritions.get(id.intValue());

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                if(jsonBody.has("portionSize")){
                    nutricion.setPortionSize( jsonBody.get("portionSize").asText());
                }
                if(jsonBody.has("calories")){
                    nutricion.setCalories(jsonBody.get("calories").asDouble());
                }
                if(jsonBody.has("totalFat")){
                    nutricion.setTotalFat(jsonBody.get("totalFat").asDouble());
                }
                if(jsonBody.has("saturatedFat")){
                    nutricion.setSaturatedFat( jsonBody.get("saturatedFat").asDouble());
                }
                if(jsonBody.has("cholesterol")){
                    nutricion.setCholesterol(jsonBody.get("cholesterol").asDouble());
                }
                if(jsonBody.has("totalCarbohydrates")){
                    nutricion.setTotalCarbohydrates(jsonBody.get("totalCarbohydrates").asDouble());
                }
                if(jsonBody.has("fiber")){
                    nutricion.setFiber( jsonBody.get("fiber").asDouble());
                }
                if(jsonBody.has("sugar")){
                    nutricion.setSugar(jsonBody.get("sugar").asDouble());
                }
                if(jsonBody.has("protein")){
                    nutricion.setProtein( jsonBody.get("protein").asDouble());
                }
                break;
            default:
                Form<Nutrition> form =  formFactory.form(Nutrition.class).bindFromRequest(request);
                Nutrition nutricionFormulario;
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                nutricionFormulario = form.get();
                if (nutricionFormulario.getPortionSize() != null){
                    nutricion.setPortionSize(nutricionFormulario.getPortionSize());
                }
                if (nutricionFormulario.getCalories() != null){
                    nutricion.setCalories(nutricionFormulario.getCalories());
                }
                if (nutricionFormulario.getTotalFat() != null){
                    nutricion.setTotalFat(nutricionFormulario.getTotalFat());
                }
                if (nutricionFormulario.getSaturatedFat() != null){
                    nutricion.setSaturatedFat(nutricionFormulario.getSaturatedFat());
                }
                if (nutricionFormulario.getCholesterol() != null) {
                    nutricion.setCholesterol(nutricionFormulario.getCholesterol());
                }
                if (nutricionFormulario.getTotalCarbohydrates() != null){
                    nutricion.setTotalCarbohydrates(nutricionFormulario.getTotalCarbohydrates());
                }
                if (nutricionFormulario.getFiber() != null){
                    nutricion.setFiber(nutricionFormulario.getFiber());
                }
                if (nutricionFormulario.getSugar() != null){
                    nutricion.setSugar(nutricionFormulario.getSugar());
                }
                if (nutricionFormulario.getProtein() != null){
                    nutricion.setProtein(nutricionFormulario.getProtein());
                }

                break;
        }
        if (request.accepts("application/xml")){
            return Results.ok(nutrition.render(this.nutritions.get(id),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(this.nutritions.get(id)));
        }else{
            return Results.ok();
        }
    }
    public Result deleteNutrition(Http.Request request,Integer id){
        this.nutritions.remove(id.intValue());
        return Results.noContent();
    }
}
