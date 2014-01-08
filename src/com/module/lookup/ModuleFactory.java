package com.module.lookup;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import lombok.Getter;

import com.module.table.DisplayModule;
import com.module.table.DisplayModuleInfo;

public class ModuleFactory {

   private static TreeMap<String, LookupModule<?,?>> loadedModules = new TreeMap<>();
   private static Map<String, Entry<Class<?>, DisplayModule<?>>> loadedDisplayModules = 
            new HashMap<>();

   @Getter
   private static final List<Class<?>> defaultLookup;
   //@Getter
   //private static final Multimap<Class<?>, Class<?>> defaultDisplay;
   
   static {
      
      try {
         List<Class<?>> defaults = new ArrayList<>();
         File defaultFile = new File("default_module.config");
         
         for(String line : Files.readAllLines(defaultFile.toPath(), Charset.defaultCharset())) {
            Class<?> load = ModuleFactory.class.getClassLoader().loadClass(line);
            if(LookupModule.class.isAssignableFrom(load)) {
               defaults.add(load);
            }
         }
         defaultLookup = defaults;
      }catch(Exception e) {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }


   private ModuleFactory() {

   }

   public static Entry<Class<?>, DisplayModule<?>> load(
            Class<?> clz) {
      return loadedDisplayModules.get(clz);
   }

   @SuppressWarnings("unchecked")
   public static <E,V> LookupModule<E,V> load(String key) {
      return (LookupModule<E,V>) loadedModules.get(key);
   }

   public static List<String> registerLookup(Class<?> base) {
      try {
         List<String> loaded = new ArrayList<>();
         search(base, loaded);
         return loaded;
      } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }
   
   public static List<String> registerDisplay(Class<?> base) {
      try {
         List<String> loaded = new ArrayList<>();
         searchDisplay(base, loaded);
         return loaded;
      } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   private static void search(Class<?> cur, List<String> loaded) throws InstantiationException,
            IllegalAccessException {
      if (LookupModule.class.isAssignableFrom(cur)) {
         if (cur.isAnnotationPresent(ModuleInfo.class)) {
            String name = cur.getAnnotation(ModuleInfo.class)
                     .value();
            loadedModules.put(name, (LookupModule<?, ?>) cur.newInstance());
            loaded.add(name);
         } else {
            throw new RuntimeException("Module " + cur.getSimpleName() + " has no annotation");
         }
      }
      for (Class<?> sub : cur.getDeclaredClasses()) {
         search(sub, loaded);
      }
   }

   private static void searchDisplay(Class<?> cur, List<String> loaded)
            throws InstantiationException, IllegalAccessException {
      if (DisplayModule.class.isAssignableFrom(cur)) {
         if (cur.isAnnotationPresent(DisplayModuleInfo.class)) {
            Class<?> name = cur.getAnnotation(DisplayModuleInfo.class)
                     .value();
            loadedDisplayModules.put(
                     name.getSimpleName(),
                     new AbstractMap.SimpleEntry<Class<?>, DisplayModule<?>>(
                              name, (DisplayModule<?>) cur.newInstance()));
            loaded.add(name.getSimpleName());
         } else {
            throw new RuntimeException("Module " + cur.getSimpleName() + " has no annotation");
         }
      }
      for (Class<?> sub : cur.getDeclaredClasses()) {
         search(sub, loaded);
      }
   }

}
