package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Category;
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
import views.xml.category;
import views.xml.categoryCollection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CategoryController extends Controller {
    FormFactory formFactory;
    MessagesApi messagesApi;
    @Inject
    public CategoryController(FormFactory formFactory,MessagesApi messagesApi){
        this.formFactory=formFactory;
        this.messagesApi=messagesApi;
    }
    public Result getCategory(Http.Request request, Long id){
        Category selectedCategory = Category.findById(id);
        if(selectedCategory == null){
            return  Results.notFound();
        }
        if (request.accepts("application/xml")){
            return Results.ok(category.render(selectedCategory,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(selectedCategory));
        }else{
            return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
        }
    }
    @Transactional
    public Result createCategory(Http.Request request){
        Category categoria;
        List<Recipe> recipeList;
        Map<String, String> errors;
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                categoria = Json.fromJson(jsonBody, Category.class);
                errors = categoria.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                recipeList = Recipe.findAllFromCategoryByTitle(categoria);
                categoria.clearRecipes();
                categoria.save();
                for(Recipe r: recipeList){
                    r.addCategory(categoria);
                    r.save();
                }
                //categoria.save();
                break;
            case "application/x-www-form-urlencoded":
                Form<Category> form =  formFactory.form(Category.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                categoria = form.get();
                recipeList = Recipe.findAllFromCategoryByTitle(categoria);
                categoria.clearRecipes();
                categoria.save();
                for(Recipe r: recipeList){
                    r.addCategory(categoria);
                    r.save();
                }

                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
        }
        if (request.accepts("application/xml")){
            return Results.created(category.render(Category.findById(categoria.getId()),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(Category.findById(categoria.getId())));
        }else{
            return Results.created();
        }
    }
    public Result getCategoryCollection(Http.Request request){
        List<Category> categories = new ArrayList<>();
        categories = Category.findAll();
        if (request.accepts("application/xml")){
            return Results.ok(categoryCollection.render(categories));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(categories));
        }else{
            return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
        }
    }
    public Result getCategoryCollectionFromRecipe(Http.Request request,Long recipeId){
        List<Category> categories = new ArrayList<>();
        Recipe selectedRecipe = Recipe.findById(recipeId);
        if(selectedRecipe == null){
            return Results.notFound();
        }
        categories = Category.findAllFromRecipe(selectedRecipe);
        if (request.accepts("application/xml")){
            return Results.ok(categoryCollection.render(categories));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(categories));
        }else{
            return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
        }
    }
    @Transactional
    public Result updateCategoryTotally(Http.Request request,Long id){
        Category categoria = Category.findById(id);
        Category categoriaRecibida;
        List<Recipe> recipeList;
        Map<String,String> errors;
        if(categoria == null){
            return Results.notFound();
        }
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                categoriaRecibida = Json.fromJson(jsonBody, Category.class);
                categoria.setAll(categoriaRecibida);
                errors = categoria.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                recipeList = Recipe.findAllFromCategoryByTitle(categoria);
                categoria.setRecipes(recipeList);
                /*categoria.clearRecipes();
                for(Recipe r: recipeList){
                    r.addCategory(categoria);
                    r.update();
                }*/
                categoria.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Category> form =  formFactory.form(Category.class).bindFromRequest(request);
                form = form.discardingErrors();
                categoriaRecibida = form.get();
                categoria.setAll(categoriaRecibida);
                errors = categoria.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                recipeList = Recipe.findAllFromCategoryByTitle(categoria);
                categoria.setRecipes(recipeList);
                /*categoria.clearRecipes();
                for(Recipe r: recipeList){
                    r.addCategory(categoria);
                    r.update();
                }*/
                categoria.update();

                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
        }
        if (request.accepts("application/xml")){
            return Results.created(category.render(Category.findById(categoria.getId()),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(Category.findById(categoria.getId())));
        }else{
            return Results.created();
        }
    }
    @Transactional
    public Result updateCategory(Http.Request request,Long id){
        Category categoria = Category.findById(id);
        List<Recipe> recipeList;
        Map<String,String> errors;
        if(categoria == null){
            return Results.notFound();
        }
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                if(jsonBody.has("name")){
                    categoria.setName(jsonBody.get("name").asText());
                }
                if(jsonBody.has("popularity")){
                    categoria.setPopularity(jsonBody.get("popularity").asDouble());
                }
                errors = categoria.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                if(jsonBody.has("recipes")){
                    categoria.clearRecipes();
                    Iterator<JsonNode> iterator = jsonBody.get("recipes").elements();
                    List<Recipe> recipes = new ArrayList<>();
                    while (iterator.hasNext()){
                        Recipe recipe = Json.fromJson(iterator.next(),Recipe.class);
                        recipe = Recipe.findByTitle(recipe.getTitle());
                        if(recipe!= null){
                            recipes.add(recipe);
                        }
                        /*if(recipe != null) {
                            recipe.addCategory(categoria);
                            recipe.update();
                        }*/
                    }
                    categoria.setRecipes(recipes);
                }
                categoria.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Category> form =  formFactory.form(Category.class).bindFromRequest(request);
                Category categoriaFormulario;
                form = form.discardingErrors();
                categoriaFormulario = form.get();
                if (categoriaFormulario.getName() != null){
                    categoria.setName(categoriaFormulario.getName());
                }
                if (categoriaFormulario.getPopularity() != null){
                    categoria.setPopularity(categoriaFormulario.getPopularity());
                }
                if(categoriaFormulario.getRecipes() != null){
                    categoria.setRecipes(categoriaFormulario.getRecipes());
                }
                errors = categoria.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                recipeList = Recipe.findAllFromCategoryByTitle(categoria);
                categoria.setRecipes(recipeList);
                /*categoria.clearRecipes();
                for(Recipe r: recipeList){
                    r.addCategory(categoria);
                    r.update();
                }*/
                categoria.update();
                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
        }
        if (request.accepts("application/xml")){
            return Results.ok(category.render(Category.findById(categoria.getId()),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(Category.findById(categoria.getId())));
        }else{
            return Results.ok();
        }
    }
    public Result deleteCategory(Http.Request request,Long id){
        Category.find.deleteById(id);
        return Results.noContent();
    }
}
