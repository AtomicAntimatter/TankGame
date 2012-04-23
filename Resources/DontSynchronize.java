package Resources;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.annotation.*;

/**
 * Marks that the annotated method can safely be *not* marked synchronized.
 * 
 * Valid unit consistency tests should mark any annotated methods which are 
 * synchronized as incorrect.
 * 
 * @author harrison
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface DontSynchronize {
    
}
