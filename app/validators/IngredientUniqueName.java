package validators;

import models.Ingredient;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IngredientUniqueNameValidator.class)
public @interface IngredientUniqueName {
    String message() default "validation.error.unique.name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
//Referencias http://dolszewski.com/java/custom-parametrized-validation-annotation/
