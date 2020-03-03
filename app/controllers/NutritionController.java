package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Nutrition;
import models.Recipe;
import models.Step;
import play.api.i18n.MessagesApi;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.xml.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NutritionController extends Controller {
    FormFactory formFactory;
    MessagesApi messagesApi;
    @Inject
    public NutritionController(FormFactory formFactory, MessagesApi messagesApi){
        this.formFactory=formFactory;
        this.messagesApi=messagesApi;
    }
    public Result getNutrition(Http.Request request,Long id){
        Nutrition selectedNutrition = Nutrition.findById(id);
        if(selectedNutrition == null){
            return Results.notFound();
        }
        if (request.accepts("application/xml")){
            return Results.ok(nutrition.render(selectedNutrition,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(selectedNutrition));
        }else{
            return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
        }
    }
    public Result getNutritionFromRecipe(Http.Request request,Long recipeId){
        Recipe selectedRecipe = Recipe.findById(recipeId);
        if(selectedRecipe == null){
            return Results.notFound();
        }
        Nutrition selectedNutrition = selectedRecipe.getNutrition();
        if(selectedNutrition == null){
            return Results.notFound();
        }
        if (request.accepts("application/xml")){
            return Results.ok(nutrition.render(selectedNutrition,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(selectedNutrition));
        }else{
            return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
        }
    }
    /*public Result createNutrition(Http.Request request){
        Nutrition nutricion;

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                nutricion = Json.fromJson(jsonBody, Nutrition.class);
                nutricion.save();
                break;
            default:
                Form<Nutrition> form =  formFactory.form(Nutrition.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                nutricion = form.get();
                nutricion.save();
                break;
        }
        if (request.accepts("application/xml")){
            return Results.created(nutrition.render(nutricion,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(nutricion));
        }else{
            return Results.created();
        }
    }*/
    @Transactional
    public Result createNutritionInRecipe(Http.Request request, Long recipeId){
        Nutrition nutricion;
        Recipe selectedRecipe = Recipe.findById(recipeId);
        Map<String,String> errors;
        if(selectedRecipe.getNutrition() != null){
            return Results.notFound();
        }

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                nutricion = Json.fromJson(jsonBody, Nutrition.class);
                errors = nutricion.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                selectedRecipe.setNutrition(nutricion);
                selectedRecipe.save();
                //nutricion.save(); con la cascada de SAVE no es necesario ya que se hace automáticamente
                break;
            case "application/x-www-form-urlencoded":
                Form<Nutrition> form =  formFactory.form(Nutrition.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                nutricion = form.get();
                selectedRecipe.setNutrition(nutricion);
                selectedRecipe.save();
                //nutricion.save(); con la cascada de SAVE no es necesario ya que se hace automáticamente
                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
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
        List<Nutrition> nutritions = new ArrayList<>();
        nutritions = Nutrition.findAll();
        if (request.accepts("application/xml")){
            return Results.ok(nutritionCollection.render(nutritions));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(nutritions));
        }else{
            return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
        }
    }
    /*public Result updateNutritionTotally(Http.Request request, Long id){
        Nutrition nutricion = Nutrition.findById(id);
        Nutrition nutricionRecibida;
        if (nutricion == null){
            return Results.notFound();
        }
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                nutricionRecibida = Json.fromJson(jsonBody, Nutrition.class);
                nutricion.setAll(nutricionRecibida);
                nutricion.update();
                break;
            default:
                Form<Nutrition> form =  formFactory.form(Nutrition.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                nutricionRecibida = form.get();
                nutricion.setAll(nutricionRecibida);
                nutricion.update();
                break;
        }
        if (request.accepts("application/xml")){
            return Results.created(nutrition.render(nutricion,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(nutricion));
        }else{
            return Results.created();
        }
    }*/
    public Result updateNutritionTotallyFromRecipe(Http.Request request, Long recipeId){
        Nutrition nutricion;
        Nutrition nutricionRecibida;
        Recipe selectedRecipe = Recipe.findById(recipeId);
        Map<String,String> errors;
        if (selectedRecipe == null){
            return Results.notFound();
        }
        nutricion = selectedRecipe.getNutrition();
        if(nutricion == null){
            return Results.notFound();
        }
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                nutricionRecibida = Json.fromJson(jsonBody, Nutrition.class);
                nutricion.setAll(nutricionRecibida);
                errors = nutricion.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                nutricion.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Nutrition> form =  formFactory.form(Nutrition.class).bindFromRequest(request);
                form = form.discardingErrors();
                nutricionRecibida = form.get();
                nutricion.setAll(nutricionRecibida);
                errors = nutricion.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                nutricion.update();
                break;
            default:
                return Results.status(415, messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
        }
        if (request.accepts("application/xml")){
            return Results.created(nutrition.render(nutricion,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(nutricion));
        }else{
            return Results.created();
        }
    }
    /*public Result updateNutrition(Http.Request request,Long id){
        Nutrition nutricion = Nutrition.findById(id);
        if(nutricion == null){
            return Results.notFound();
        }

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
                nutricion.update();
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
                nutricion.update();
                break;
        }
        if (request.accepts("application/xml")){
            return Results.ok(nutrition.render(nutricion,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(nutricion));
        }else{
            return Results.ok();
        }
    }*/
    public Result updateNutritionFromRecipe(Http.Request request,Long recipeId){
        Nutrition nutricion;
        Recipe selectedRecipe = Recipe.findById(recipeId);
        Map<String,String> errors;
        if(selectedRecipe == null){
            return Results.notFound();
        }
        nutricion = selectedRecipe.getNutrition();
        if(nutricion == null){
            return Results.notFound();
        }

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
                Form<Nutrition> validationForm = formFactory.form(Nutrition.class).bind(messagesApi.preferred(request).lang().asJava(),request.attrs(),Json.toJson(nutricion));
                if(validationForm.hasErrors()){
                    return Results.badRequest(validationForm.errorsAsJson());
                }
                errors = nutricion.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                nutricion.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Nutrition> form =  formFactory.form(Nutrition.class).bindFromRequest(request);
                Nutrition nutricionFormulario;
                form = form.discardingErrors();
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
                errors = nutricion.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                nutricion.update();
                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
        }
        if (request.accepts("application/xml")){
            return Results.ok(nutrition.render(nutricion,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(nutricion));
        }else{
            return Results.ok();
        }
    }
    @Transactional
    public Result deleteNutrition(Http.Request request,Long id){
        Nutrition nutricion = Nutrition.findById(id);
        if(nutricion == null){
            return Results.notFound();
        }
        Recipe recipe = nutricion.getRecipe();
        recipe.deleteNutrition();
        recipe.update();
        nutricion.delete();
        return Results.noContent();
    }
    @Transactional
    public Result deleteNutritionFromRecipe(Http.Request request,Long recipeId){
        Recipe recipe = Recipe.findById(recipeId);
        if(recipe == null){
            return Results.notFound();
        }
        Nutrition nutrition = recipe.getNutrition();
        recipe.deleteNutrition();
        recipe.update();
        nutrition.delete();
        return Results.noContent();
    }
}
