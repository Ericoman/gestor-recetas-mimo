package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Category;
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
import views.xml.recipe;
import views.xml.recipeCollection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RecipeController extends Controller {
    FormFactory formFactory;
    MessagesApi messagesApi;
    @Inject
    public RecipeController(FormFactory formFactory, MessagesApi messagesApi){
        this.formFactory=formFactory;
        this.messagesApi=messagesApi;
    }
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
            return Results.status(415, messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
        }
    }
    @Transactional
    public Result createRecipe(Http.Request request){
        Recipe receta;
        List<Category> categoryList;
        List<Ingredient> ingredientList;
        Map<String,String> errors;
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                receta = Json.fromJson(jsonBody, Recipe.class);
                errors = receta.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                categoryList = Category.findAllFromRecipeByName(receta);
                ingredientList = Ingredient.findAllFromRecipeByName(receta);
                receta.setCategories(categoryList);
                receta.setIngredients(ingredientList);
                receta.save();

                break;
            case "application/x-www-form-urlencoded":
                Form<Recipe> form =  formFactory.form(Recipe.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                receta = form.get();
                categoryList = Category.findAllFromRecipeByName(receta);
                ingredientList = Ingredient.findAllFromRecipeByName(receta);
                receta.setCategories(categoryList);
                receta.setIngredients(ingredientList);
                receta.save();
                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
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
            return Results.status(415, messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
        }
    }
    @Transactional
    public Result updateRecipeTotally(Http.Request request, Long id){
        Recipe receta = Recipe.findById(id);
        Recipe recetaRecibida;
        List<Category> categoryList;
        List<Ingredient> ingredientList;
        Map<String,String> errors;
        if(receta == null){
            return Results.notFound();
        }

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                recetaRecibida = Json.fromJson(jsonBody, Recipe.class);
                receta.setAll(recetaRecibida);
                errors = receta.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                categoryList = Category.findAllFromRecipeByName(receta);
                ingredientList = Ingredient.findAllFromRecipeByName(receta);
                receta.setCategories(categoryList);
                receta.setIngredients(ingredientList);
                receta.update();

                break;
            case "application/x-www-form-urlencoded":
                Form<Recipe> form =  formFactory.form(Recipe.class).bindFromRequest(request);
                form.discardingErrors();
                recetaRecibida = form.get();
                receta.setAll(recetaRecibida);
                errors = receta.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                categoryList = Category.findAllFromRecipeByName(receta);
                ingredientList = Ingredient.findAllFromRecipeByName(receta);
                receta.setCategories(categoryList);
                receta.setIngredients(ingredientList);
                receta.update();
                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
        }
        if (request.accepts("application/xml")){
            return Results.created(recipe.render(Recipe.findById(receta.getId()),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(Recipe.findById(receta.getId())));
        }else{
            return Results.created();
        }
    }
    @Transactional
    public Result updateRecipe(Http.Request request,Long id){
        Recipe receta = Recipe.findById(id);
        List<Category> categoryList;
        List<Ingredient> ingredientList;
        Map<String,String> errors;
        if(receta == null){
            return Results.notFound();
        }

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                if(jsonBody.has("title")){
                    receta.setTitle( jsonBody.get("title").asText());
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
                errors = receta.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                if(jsonBody.has("categories")){
                    receta.clearCategories();
                    Iterator<JsonNode> iterator = jsonBody.get("categories").elements();
                    List<Category> categories = new ArrayList<>();
                    while (iterator.hasNext()){
                        Category category = Json.fromJson(iterator.next(),Category.class);
                        category = Category.findByName(category.getName());
                        if(category != null) {
                            categories.add(category);
                        }

                    }
                    receta.setCategories(categories);
                }
                if(jsonBody.has("ingredients")){
                    receta.clearIngredients();
                    Iterator<JsonNode> iterator = jsonBody.get("ingredients").elements();
                    List<Ingredient> ingredients = new ArrayList<>();
                    while (iterator.hasNext()){
                        Ingredient ingredient = Json.fromJson(iterator.next(),Ingredient.class);
                        ingredient = Ingredient.findByName(ingredient.getName());
                        if(ingredient != null) {
                            ingredients.add(ingredient);
                        }

                    }
                    receta.setIngredients(ingredients);
                }
                receta.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Recipe> form =  formFactory.form(Recipe.class).bindFromRequest(request);
                Recipe recetaFormulario;
                form.discardingErrors();
                recetaFormulario = form.get();
                if (recetaFormulario.getTitle() != null){
                    receta.setTitle(recetaFormulario.getTitle());
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
                errors = receta.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                if (recetaFormulario.getCategories() != null){
                    categoryList = Category.findAllFromRecipeByName(receta);
                    receta.setCategories(categoryList);

                }
                if (recetaFormulario.getIngredients() != null){
                    ingredientList = Ingredient.findAllFromRecipeByName(receta);
                    receta.setIngredients(ingredientList);

                }
                receta.update();
                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
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
