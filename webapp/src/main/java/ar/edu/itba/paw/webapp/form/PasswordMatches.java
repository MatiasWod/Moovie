/*package ar.edu.itba.paw.webapp.form;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {PasswordMatchesValidatorConstraint.class, ChangePasswordMatchesValidatorConstraint.class})
@Documented
public @interface PasswordMatches {
    String message() default "Passwords don't match.";

    Class<?> [] groups() default {};

    Class<? extends Payload> [] payload() default {};
}*/