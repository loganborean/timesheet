package annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Annotation for sheets collection no-DB implementation.
 *
 */
@Qualifier
@Target({ANNOTATION_TYPE, TYPE, METHOD, FIELD, PARAMETER})
@Documented
@Retention(RUNTIME)
public @interface DBsheets {

}
