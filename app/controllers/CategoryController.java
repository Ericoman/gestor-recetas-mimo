package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Category;
import models.Recipe;
import play.data.Form;
import play.data.FormFactory;
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
import java.util.concurrent.CancellationException;

public class CategoryController extends Controller {
    @Inject
    FormFactory formFactory;

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
            return Results.status(415);
        }
    }
    public Result createCategory(Http.Request request){
        Category categoria;

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                categoria = Json.fromJson(jsonBody, Category.class);
                categoria.save();
                break;
            default:
                Form<Category> form =  formFactory.form(Category.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                categoria = form.get();
                categoria.save();

                break;
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
            return Results.status(415);
        }
    }
    public Result updateCategoryTotally(Http.Request request,Long id){
        Category categoria = Category.findById(id);
        Category categoriaRecibida;
        if(categoria == null){
            return Results.notFound();
        }
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                categoriaRecibida = Json.fromJson(jsonBody, Category.class);
                categoria.setAll(categoriaRecibida);
                categoria.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Category> form =  formFactory.form(Category.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                categoriaRecibida = form.get();
                categoria.setAll(categoriaRecibida);
                categoria.update();

                break;
            default:
                return Results.status(415);
        }
        if (request.accepts("application/xml")){
            return Results.created(category.render(Category.findById(categoria.getId()),Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(Category.findById(categoria.getId())));
        }else{
            return Results.created();
        }
    }
    public Result updateCategory(Http.Request request,Long id){
        Category categoria = Category.findById(id);
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
                if(jsonBody.has("recipes")){
                    categoria.clearRecipes();
                    Iterator<JsonNode> iterator = jsonBody.get("recipes").elements();
                    while (iterator.hasNext()){
                        Recipe recipe = Json.fromJson(iterator.next(),Recipe.class);
                        recipe.addCategory(categoria);
                    }
                }
                categoria.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Category> form =  formFactory.form(Category.class).bindFromRequest(request);
                Category categoriaFormulario;
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
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
                categoria.update();
                break;
            default:
                return Results.status(415);
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
