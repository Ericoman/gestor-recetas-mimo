package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Ingredient;
import models.Recipe;
import play.api.i18n.MessagesApi;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.xml.ingredient;
import views.xml.ingredientCollection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class IngredientController extends Controller {
    FormFactory formFactory;
    MessagesApi messagesApi;
    @Inject
    public IngredientController(FormFactory formFactory,MessagesApi messagesApi){
        this.formFactory=formFactory;
        this.messagesApi=messagesApi;
    }
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
            return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
        }
    }
    @Transactional
    public Result createIngredient(Http.Request request){
        Ingredient ingrediente;
        List<Recipe> recipeList;
        Map<String,String> errors;
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                ingrediente = Json.fromJson(jsonBody, Ingredient.class);
                errors = ingrediente.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                recipeList = Recipe.findAllFromIngredientByTitle(ingrediente);
                ingrediente.clearRecipes();
                ingrediente.save();
                for(Recipe r: recipeList){
                    r.addIngredient(ingrediente);
                    r.save();
                }
                break;
            case "application/x-www-form-urlencoded":
                Form<Ingredient> form =  formFactory.form(Ingredient.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                ingrediente = form.get();
                recipeList = Recipe.findAllFromIngredientByTitle(ingrediente);
                ingrediente.clearRecipes();
                ingrediente.save();
                for(Recipe r: recipeList){
                    r.addIngredient(ingrediente);
                    r.save();
                }
                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
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
            return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
        }
    }
    public Result getIngredientCollectionFromRecipe(Http.Request request, Long recipeId){
        List<Ingredient> ingredients = new ArrayList<>();
        Recipe selectedRecipe = Recipe.findById(recipeId);
        if(selectedRecipe == null){
            return Results.notFound();
        }
        ingredients = Ingredient.findAllFromRecipe(selectedRecipe);
        if (request.accepts("application/xml")){
            return Results.ok(ingredientCollection.render(ingredients));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(ingredients));
        }else{
            return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
        }
    }
    @Transactional
    public Result updateIngredientTotally(Http.Request request, Long id){
        Ingredient ingrediente = Ingredient.findById(id);
        Ingredient ingredienteRecibido;
        List<Recipe> recipeList;
        Map<String,String> errors;
        if (ingrediente == null){
            return Results.notFound();
        }
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                ingredienteRecibido = Json.fromJson(jsonBody, Ingredient.class);
                ingrediente.setAll(ingredienteRecibido);
                errors = ingrediente.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                recipeList = Recipe.findAllFromIngredientByTitle(ingrediente);
                ingrediente.setRecipes(recipeList);
                ingrediente.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Ingredient> form =  formFactory.form(Ingredient.class).bindFromRequest(request);
                form = form.discardingErrors();
                ingredienteRecibido = form.get();
                ingrediente.setAll(ingredienteRecibido);
                errors = ingrediente.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                recipeList = Recipe.findAllFromIngredientByTitle(ingrediente);
                ingrediente.setRecipes(recipeList);
                ingrediente.update();
                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
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
        Ingredient ingrediente = Ingredient.findById(id);
        List<Recipe> recipeList;
        Map<String,String> errors;
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
                errors = ingrediente.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                if(jsonBody.has("recipes")){
                    List<Recipe> recipes = new ArrayList<>();
                    Iterator<JsonNode> iterator = jsonBody.get("recipes").elements();
                    while (iterator.hasNext()){
                        Recipe recipe = Json.fromJson(iterator.next(),Recipe.class);
                        recipe = Recipe.findByTitle(recipe.getTitle());
                        if(recipe != null){
                            recipes.add(recipe);
                        }
                    }
                    ingrediente.setRecipes(recipes);
                }

                ingrediente.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Ingredient> form =  formFactory.form(Ingredient.class).bindFromRequest(request);
                Ingredient ingredienteFormulario;
                form = form.discardingErrors();
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
                errors = ingrediente.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                if (ingredienteFormulario.getRecipes() != null){
                    recipeList = Recipe.findAllFromIngredientByTitle(ingrediente);
                    ingrediente.setRecipes(recipeList);
                }
                ingrediente.update();
                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
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
