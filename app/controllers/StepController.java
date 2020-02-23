package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.internal.cglib.core.$ReflectUtils;
import models.Recipe;
import models.Step;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.xml.step;
import views.xml.stepCollection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class StepController extends Controller {
    @Inject
    FormFactory formFactory;

    public Result getStep(Http.Request request, Long id){
        Step selectedStep = Step.findById(id);
        if(selectedStep == null){
            return Results.notFound();
        }
        if (request.accepts("application/xml")){
            return Results.ok(step.render(selectedStep,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(selectedStep));
        }else{
            return Results.status(415);
        }
    }
    public Result getStepFromRecipe(Http.Request request, Long id, Long recipeId){
        Step selectedStep = Step.findById(id);
        if(selectedStep == null){
            return Results.notFound();
        }
        if (request.accepts("application/xml")){
            return Results.ok(step.render(selectedStep,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(selectedStep));
        }else{
            return Results.status(415);
        }
    }
    /*public Result createStep(Http.Request request){
        Step paso;
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                paso = Json.fromJson(jsonBody, Step.class);
                paso.save();
                break;
            case "application/x-www-form-urlencoded":
                Form<Step> form =  formFactory.form(Step.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                paso = form.get();
                paso.save();
                break;
            default:
                return Results.status(415);
        }
        if (request.accepts("application/xml")){
            return Results.created(step.render(paso,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(paso));
        }else{
            return Results.created();
        }
    }*/
    public Result createStepInRecipe(Http.Request request, Long recipeId){
        Step paso;
        Recipe selectedRecipe = Recipe.findById(recipeId);
        if(selectedRecipe==null){
            return Results.notFound();
        }
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                paso = Json.fromJson(jsonBody, Step.class);
                selectedRecipe.addStep(paso);
                paso.save();
                break;
            case "application/x-www-form-urlencoded":
                Form<Step> form =  formFactory.form(Step.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                paso = form.get();
                selectedRecipe.addStep(paso);
                paso.save();
                break;
            default:
                return Results.status(415);
        }
        if (request.accepts("application/xml")){
            return Results.created(step.render(paso,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(paso));
        }else{
            return Results.created();
        }
    }
    public Result getStepCollection(Http.Request request){
        List<Step> steps = new ArrayList<>();
        steps = Step.findAll();

        if (request.accepts("application/xml")){
            return Results.ok(stepCollection.render(steps));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(steps));
        }else{
            return Results.status(415);
        }
    }
    public Result getStepCollectionFromRecipe(Http.Request request, Long recipeId){
        List<Step> steps = new ArrayList<>();
        steps = Step.findAllFromRecipe(recipeId);

        if (request.accepts("application/xml")){
            return Results.ok(stepCollection.render(steps));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(steps));
        }else{
            return Results.status(415);
        }
    }
    public Result updateStepTotally(Http.Request request, Long id){
        Step paso = Step.findById(id);
        if(paso == null){
            return Results.notFound();
        }
        Step pasoRecibido;

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                pasoRecibido = Json.fromJson(jsonBody, Step.class);
                paso.setAll(pasoRecibido);
                paso.update();
                break;
            default:
                Form<Step> form =  formFactory.form(Step.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                pasoRecibido = form.get();
                paso.setAll(pasoRecibido);
                paso.update();
                break;
        }
        if (request.accepts("application/xml")){
            return Results.created(step.render(paso,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(paso));
        }else{
            return Results.created();
        }
    }

    public Result updateStepTotallyFromRecipe(Http.Request request, Long id, Long recipeId){
        Step paso = Step.findByIdFromRecipe(id,recipeId);
        if(paso == null){
            return Results.notFound();
        }
        Step pasoRecibido;

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                pasoRecibido = Json.fromJson(jsonBody, Step.class);
                paso.setAll(pasoRecibido);
                paso.update();
                break;
            default:
                Form<Step> form =  formFactory.form(Step.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                pasoRecibido = form.get();
                paso.setAll(pasoRecibido);
                paso.update();
                break;
        }
        if (request.accepts("application/xml")){
            return Results.created(step.render(paso,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(paso));
        }else{
            return Results.created();
        }
    }
    public Result updateStep(Http.Request request,Long id){
        Step paso = Step.findById(id);
        if(paso == null){
            return Results.notFound();
        }

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                if(jsonBody.has("title")){
                    paso.setTitle(jsonBody.get("title").asText());
                }
                if(jsonBody.has("description")){
                    paso.setDescription(jsonBody.get("description").asText());
                }
                if(jsonBody.has("time")){
                    paso.setTime(jsonBody.get("time").asDouble());
                }
                if(jsonBody.has("number")){
                    paso.setNumber(jsonBody.get("number").asLong());
                }
                paso.update();
                break;
            default:
                Form<Step> form =  formFactory.form(Step.class).bindFromRequest(request);
                Step pasoFormulario;
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                pasoFormulario = form.get();
                if (pasoFormulario.getTitle() != null){
                    paso.setTitle(pasoFormulario.getTitle());
                }
                if (pasoFormulario.getDescription() != null){
                    paso.setDescription(pasoFormulario.getDescription());
                }
                if (pasoFormulario.getTime() != null){
                    paso.setTime(pasoFormulario.getTime());
                }
                if (pasoFormulario.getNumber() != null){
                    paso.setNumber(pasoFormulario.getNumber());
                }
                paso.update();
                break;
        }
        //paso.update();
        if (request.accepts("application/xml")){
            return Results.ok(step.render(paso,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(paso));
        }else{
            return Results.ok();
        }
    }
    public Result updateStepFromRecipe(Http.Request request,Long id, Long recipeId){
        Step paso = Step.findByIdFromRecipe(id,recipeId);
        if(paso == null){
            return Results.notFound();
        }

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                if(jsonBody.has("title")){
                    paso.setTitle(jsonBody.get("title").asText());
                }
                if(jsonBody.has("description")){
                    paso.setDescription(jsonBody.get("description").asText());
                }
                if(jsonBody.has("time")){
                    paso.setTime(jsonBody.get("time").asDouble());
                }
                if(jsonBody.has("number")){
                    paso.setNumber(jsonBody.get("number").asLong());
                }
                paso.update();
                break;
            default:
                Form<Step> form =  formFactory.form(Step.class).bindFromRequest(request);
                Step pasoFormulario;
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                pasoFormulario = form.get();
                if (pasoFormulario.getTitle() != null){
                    paso.setTitle(pasoFormulario.getTitle());
                }
                if (pasoFormulario.getDescription() != null){
                    paso.setDescription(pasoFormulario.getDescription());
                }
                if (pasoFormulario.getTime() != null){
                    paso.setTime(pasoFormulario.getTime());
                }
                if (pasoFormulario.getNumber() != null){
                    paso.setNumber(pasoFormulario.getNumber());
                }
                paso.update();
                break;
        }
        //paso.update();
        if (request.accepts("application/xml")){
            return Results.ok(step.render(paso,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.ok(Json.toJson(paso));
        }else{
            return Results.ok();
        }
    }
    public Result deleteStep(Http.Request request,Long id){
        Step.find.deleteById(id);
        return Results.noContent();
    }
    public Result deleteStepFromRecipe(Http.Request request,Long id, Long recipeId){
        Step paso = Step.findByIdFromRecipe(id,recipeId);
        if(paso == null){
            return Results.notFound();
        }
        paso.delete();
        return Results.noContent();
    }
}

