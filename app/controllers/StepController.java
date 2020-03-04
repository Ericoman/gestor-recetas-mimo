package controllers;

import com.fasterxml.jackson.databind.JsonNode;
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
import views.xml.step;
import views.xml.stepCollection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StepController extends Controller {
    FormFactory formFactory;
    MessagesApi messagesApi;
    @Inject
    public StepController(MessagesApi messagesApi, FormFactory formFactory){
        this.messagesApi = messagesApi;
        this.formFactory = formFactory;
    }
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
            return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
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
            return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
        }
    }

    @Transactional
    public Result createStepInRecipe(Http.Request request, Long recipeId){
        Step paso;
        Recipe selectedRecipe = Recipe.findById(recipeId);
        Map<String,String> errors;
        if(selectedRecipe==null){
            return Results.notFound();
        }
        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                paso = Json.fromJson(jsonBody, Step.class);
                errors = paso.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                selectedRecipe.addStep(paso);
                selectedRecipe.save();
                //paso.save();
                break;
            case "application/x-www-form-urlencoded":
                Form<Step> form =  formFactory.form(Step.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                paso = form.get();
                selectedRecipe.addStep(paso);
                selectedRecipe.save();
                //paso.save(); con la cascada de SAVE activada no es necesario
                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
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

            return Results.status(415, messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
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
            return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.Accept", request.acceptedTypes()));
        }
    }

    @Transactional
    public Result updateStepTotallyFromRecipe(Http.Request request, Long id, Long recipeId){
        Step paso = Step.findByIdFromRecipe(id,recipeId);
        Map<String,String> errors;
        if(paso == null){
            return Results.notFound();
        }
        Step pasoRecibido;

        switch (request.contentType().get()){
            case "application/json":
                JsonNode jsonBody = request.body().asJson();
                pasoRecibido = Json.fromJson(jsonBody, Step.class);
                paso.setAll(pasoRecibido);
                errors = paso.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                paso.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Step> form =  formFactory.form(Step.class).bindFromRequest(request);
                if (form.hasErrors()){
                    return Results.badRequest(form.errorsAsJson());
                }
                pasoRecibido = form.get();
                paso.setAll(pasoRecibido);
                paso.update();
                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
        }
        if (request.accepts("application/xml")){
            return Results.created(step.render(paso,Boolean.FALSE));
        } else if (request.accepts("application/json")){
            return Results.created(Json.toJson(paso));
        }else{
            return Results.created();
        }
    }
    @Transactional
    public Result updateStepFromRecipe(Http.Request request,Long id, Long recipeId){
        Step paso = Step.findByIdFromRecipe(id,recipeId);
        Map<String,String> errors;
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
                errors = paso.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                paso.update();
                break;
            case "application/x-www-form-urlencoded":
                Form<Step> form =  formFactory.form(Step.class).bindFromRequest(request);
                Step pasoFormulario;
                form = form.discardingErrors();
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
                errors = paso.forceValidate(messagesApi,request);
                if(!errors.isEmpty()){
                    return Results.badRequest(Json.toJson(errors));
                }
                paso.update();
                break;
            default:
                return Results.status(415,messagesApi.preferred(request).asJava().apply("unsuportedMediaType.ContentType", request.acceptedTypes()));
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
    @Transactional
    public Result deleteStepFromRecipe(Http.Request request,Long id, Long recipeId){
        Step paso = Step.findByIdFromRecipe(id,recipeId);
        if(paso == null){
            return Results.notFound();
        }
        paso.delete();
        return Results.noContent();
    }
}

