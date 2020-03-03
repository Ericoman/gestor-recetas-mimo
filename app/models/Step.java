package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import io.ebean.annotation.NotNull;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import play.api.i18n.MessagesApi;
import play.data.validation.Constraints;
import play.mvc.Http;
import views.IngredientRefSerializer;
import views.SingleRecipeRefSerializer;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
public class Step extends BaseModel {
    @Constraints.Required(message = "validation.error.required")
    @NotNull
    @Constraints.Min(value = 1,message = "validation.error.min")
    @Min(value = 1,message = "validation.error.min")
    @Digits(integer = 10,fraction = 0, message = "validation.error.digits")
    private Long number;
    @Constraints.Required(message = "validation.error.required")
    @NotNull
    private String title;
    private String description;
    @Constraints.Min(value = 0,message = "validation.error.min")
    @Min(value = 0,message = "validation.error.min")
    @Digits(integer = 10,fraction = 3,message = "validation.error.digits")
    private Double time;
    @JsonIgnoreProperties(value="steps")
    @JsonSerialize(using = SingleRecipeRefSerializer.class)
    @ManyToOne
    private Recipe recipe;


    public Step(String title, String description, Double time, Long number) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.number = number;
    }

    public  Step(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setAll(Step step){
        this.setTime(step.getTime());
        this.setDescription(step.getDescription());
        this.setTitle(step.getTitle());
        this.setNumber(step.getNumber());
        //this.setRecipe(step.getRecipe());
    }

    public static final Finder<Long,Step> find = new Finder<>(Step.class);

    public static Step findById(Long id){
        return find.byId(id);
    }
    public static Step findByIdFromRecipe(Long id, Long recipeId){
        return find.query().where().eq("id",id).and().eq("recipe_id",recipeId).findOne();
    }
    public static Step findByNumber(Long number){
        return find.query().where().eq("number",number).findOne();
    }
    public static List<Step> findAll(){
        return find.all();
    }
    public static List<Step> findAllFromRecipe(Long recipeId){
        return  find.query().where().eq("recipe_id",recipeId).findList();
    }
    public static Step findByTitle(String title){
        return find.query().where().eq("title",title).findOne();
    }

    public Map<String,String> forceValidate(MessagesApi messagesApi, Http.RequestHeader request) {
        ValidatorFactory factory = Validation.byDefaultProvider().configure().messageInterpolator(new ParameterMessageInterpolator()).buildValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Step>> violations =
                validator.validate(this);
        Map<String,String> errors = new HashMap<String,String>();
        for (ConstraintViolation<Step> cv : violations) {
            errors.put(cv.getPropertyPath().toString(), messagesApi.preferred(request).asJava().apply(cv.getMessage()));
        }
        return errors;
    }
}
