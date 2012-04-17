package org.apache.tapestry5.portlet.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marker for portlet related services and handlers.
 * 
 * @author ccordenier
 * 
 */
@Target({ PARAMETER, FIELD })
@Retention(RUNTIME)
@Documented
public @interface Portlet {

}
