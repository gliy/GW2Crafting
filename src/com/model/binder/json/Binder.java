package com.model.binder.json;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;

public class Binder {

   public static <E> Class<E> validate(Class<E> clz) {
      return clz;
   }

   public static <E> E bind(JsonNode node, Class<E> clz) {
      try {
         return bind(node, clz.newInstance());
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public static <E> E bind(JsonNode node, E base) {
      try {
         Class<?> use = base.getClass();
         boolean all = true;
         if ((all = use.isAnnotationPresent(Bind.class))) {
            checkArgument(use.getAnnotation(Bind.class)
               .value()
               .equals(""));
         }
         for (Field f : use.getDeclaredFields()) {
            String name = null;
            f.setAccessible(true);
            if (f.isAnnotationPresent(Bind.class)) {
               name = f.getAnnotation(Bind.class)
                  .value();
            }
            if (name != null || all) {
               name = Objects.firstNonNull(name, f.getName());

               Object set = val(node.findValue(name), f.getType());
               if (set != null) {
                  f.set(base, set);
               }
            }

         }
         return base;
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }

   }

   private static Object val(JsonNode n, Class<?> type) {
      if (n == null) {
         return null;
      }
      try {
         Object val = handleToken(n);
         if (val == null) {
            return null;
         }
         if (type == Date.class) {
            val = new SimpleDateFormat().parse(val.toString());
         }
        /*if (!type.isAssignableFrom(val.getClass())) {
            throw new RuntimeException(String.format("%s is not compatible with %s",
               val.getClass(), type));
         }*/
         return val;
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   private static Object handleToken(JsonNode node) {
      switch (node.asToken()) {
      case VALUE_FALSE:
      case VALUE_TRUE:
         return node.asBoolean();
      case VALUE_NUMBER_FLOAT:
         return node.asDouble();
      case VALUE_NUMBER_INT:
         return node.asInt();
      case VALUE_STRING:
         return node.asText();
      case VALUE_NULL:
         return null;
      default:
         throw new RuntimeException("Type " + node.asToken() + " Not found");

      }
   }
}
