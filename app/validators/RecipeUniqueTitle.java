package validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RecipeUniqueTitleValidator.class)
public @interface RecipeUniqueTitle {
    String message() default "validation.error.unique.title";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
//Referencias http://dolszewski.com/java/custom-parametrized-validation-annotation/
