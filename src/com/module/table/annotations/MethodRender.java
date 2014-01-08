package com.module.table.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.swing.table.TableCellRenderer;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodRender {

   String value();
   
   Class<?> start() default Object.class;
  
}
