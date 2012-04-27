/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author harrison
 */
@Retention(RetentionPolicy.RUNTIME)
public @ interface ToDo {
    public String value();
}
