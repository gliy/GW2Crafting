package com.module.output;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class DefaultWritableItem<E> implements WriteableItem<E> {

   @Override
   public Map<String, Object> getData(E object) {
      try {
         Map<String, Object> rtn = new HashMap<>();

         List<String> fields = Lists.transform(Arrays.asList(object.getClass().getFields()),
                  new Function<Field, String>() {

                     @Override
                     public String apply(Field input) {
                        return input.getName();
                     }
                  });
         for (Method m : object.getClass().getDeclaredMethods()) {
            if (m.getParameterTypes().length == 0 && m.getReturnType() != Void.class) {
               String name = m.getName();
               if (m.getName().startsWith("get")) {
                  name = m.getName().substring(3);

               }
               if (fields.contains(name)) {
                  rtn.put(name, m.invoke(object));
               }
            }
         }
         return rtn;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}
