package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.ebean.Finder;
import io.ebean.annotation.NotNull;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import play.api.i18n.MessagesApi;
import play.data.validation.Constraints;
import play.mvc.Http;
import views.SingleRecipeRefSerializer;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
public class Nutrition extends BaseModel {
    @Constraints.Required(message = "validation.error.required")
    @NotNull
    private String portionSize;
    @Constraints.Required(message = "validation.error.required")
    @NotNull
    @Constraints.Min(value = 0,message = "validation.error.min")
    @Min(value = 0,message = "validation.error.min")
    private Double calories;
    @Constraints.Min(value = 0, message = "validation.error.min")
    @Min(value = 0,message = "validation.error.min")
    private Double totalFat;
    @Constraints.Min(value = 0, message = "validation.error.min")
    @Min(value = 0,message = "validation.error.min")
    private Double saturatedFat;
    @Constraints.Min(value = 0, message = "validation.error.min")
    @Min(value = 0,message = "validation.error.min")
    private Double cholesterol;
    @Constraints.Min(value = 0,message = "validation.error.min")
    @Min(value = 0,message = "validation.error.min")
    private Double totalCarbohydrates;
    @Constraints.Min(value = 0,message = "validation.error.min")
    @Min(value = 0,message = "validation.error.min")
    private Double fiber;
    @Constraints.Min(value = 0,message = "validation.error.min")
    @Min(value = 0,message = "validation.error.min")
    private Double sugar;
    @Constraints.Min(value = 0,message = "validation.error.min")
    @Min(value = 0,message = "validation.error.min")
    private Double protein;
    @NotNull
    @JsonIgnoreProperties(value="nutrition")
    @JsonSerialize(using = SingleRecipeRefSerializer.class)
    @OneToOne(mappedBy="nutrition")
    private Recipe recipe;

    public Nutrition(String portionSize, Double calories, Double totalFat, Double saturatedFat, Double cholesterol, Double totalCarbohydrates, Double fiber, Double sugar, Double protein) {
        this.portionSize = portionSize;
        this.calories = calories;
        this.totalFat = totalFat;
        this.saturatedFat = saturatedFat;
        this.cholesterol = cholesterol;
        this.totalCarbohydrates = totalCarbohydrates;
        this.fiber = fiber;
        this.sugar = sugar;
        this.protein = protein;
    }

    public Nutrition() {

    }

    public String getPortionSize() {
        return portionSize;
    }

    public void setPortionSize(String portionSize) {
        this.portionSize = portionSize;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(Double totalFat) {
        this.totalFat = totalFat;
    }

    public Double getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(Double saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Double getTotalCarbohydrates() {
        return totalCarbohydrates;
    }

    public void setTotalCarbohydrates(Double totalCarbohydrates) {
        this.totalCarbohydrates = totalCarbohydrates;
    }

    public Double getFiber() {
        return fiber;
    }

    public void setFiber(Double fiber) {
        this.fiber = fiber;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setAll(Nutrition nutrition){
        this.setProtein(nutrition.getProtein());
        this.setSugar(nutrition.getSugar());
        this.setFiber(nutrition.getFiber());
        this.setTotalCarbohydrates(nutrition.getTotalCarbohydrates());
        this.setCholesterol(nutrition.getCholesterol());
        this.setSaturatedFat(nutrition.getSaturatedFat());
        this.setTotalFat(nutrition.getTotalFat());
        this.setCalories(nutrition.getCalories());
        this.setPortionSize(nutrition.getPortionSize());
    }

    public static final Finder<Long,Nutrition> find = new Finder<>(Nutrition.class);

    public static Nutrition findById(Long id){
        return find.byId(id);
    }
    public static List<Nutrition> findAll(){
        return find.all();
    }
    public Map<String,String> forceValidate(MessagesApi messagesApi, Http.RequestHeader request) {
        ValidatorFactory factory = Validation.byDefaultProvider().configure().messageInterpolator(new ParameterMessageInterpolator()).buildValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Nutrition>> violations =
                validator.validate(this);
        Map<String,String> errors = new HashMap<String,String>();
        for (ConstraintViolation<Nutrition> cv : violations) {
            errors.put(cv.getPropertyPath().toString(), messagesApi.preferred(request).asJava().apply(cv.getMessage()));
        }
        return errors;
    }
}
