package com.module.table.display;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableCellRenderer;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.module.table.DisplayModule;
import com.module.table.annotations.Display;
import com.module.table.annotations.HasDisplay;
import com.module.table.annotations.MethodRender;
import com.module.table.annotations.NoTableCellRenderer;
import com.ui.CustomTableModel;

public class DisplayAnnotationProcessor {

   //pass instance instead of class?
   public DisplayModule<?> generate( Class<?> clz) {

      DisplayModule<?> rtn = null;
      final Map<String, Displayable> data = new HashMap<>();
      if(clz.isAnnotationPresent(HasDisplay.class)) {
         List<String> headersL = new ArrayList<>();
         for(Field f : clz.getDeclaredFields()) {
            if(f.isAnnotationPresent(Display.class)) {
               Display disp = f.getAnnotation(Display.class);
               String header = disp.value()
                  .length() > 0 ? disp.value() : f.getName()
                  .toUpperCase()
                  .charAt(0) + f.getName()
                  .substring(1);
               if (disp.index() == -1) {
                  headersL.add(header);
               } else {
                  headersL.add(disp.index(), header);
               }
               data.put(header, new Displayable(header, f, f.getAnnotation(Display.class),
                  f.getAnnotation(MethodRender.class)));
            }
            
         }
         final String[] headers = headersL.toArray(new String[0]);
         final Class<?>[] clzs = new Class<?>[headersL.size()];
         for(Entry<String, Displayable> ent : data.entrySet()) {

            clzs[headersL.indexOf(ent.getKey())] = ent.getValue().getType();
         }
         rtn = new DisplayModule<Object>(){

            @Override
            public String[] getColumns() {
               return headers;
            }

            @Override
            public Object getValueAt(int r, int c) {
               try {
                  return data.get(headers[c]).getValue(items.get(r));
               } catch (Exception e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                  return null;
               } 
            }

            @Override
            public Class<?> getClass(int c) {
               return clzs[c];
            }
            @Override
            public RowFilter<? super CustomTableModel, ? super Integer> getFilter() {
               return null;
            }
            @Override
            public TableCellRenderer getRenderer(int col) {
               return data.get(getColumns()[col]).getCellRenderer();
            }
            
         };
      }
      checkArgument(rtn != null);
      return rtn;
   }
   
   @Data
   @RequiredArgsConstructor
   private static class Displayable {
      private final String name;
      private final Field field;
      private final Display display;
      private final MethodRender render;
      private List<Method> mChain;
      
      public TableCellRenderer getCellRenderer() {
         try {
            return display.renderer() != NoTableCellRenderer.class ? display.renderer()
               .newInstance() : null;
         } catch (Exception e) {
            e.printStackTrace();
            return null;
         }
      }

      public Class<?> getType() {
         field.setAccessible(true);
         Class<?> rtn = field.getType();
         try {
            if(render != null) {
               rtn = getRendererClass(rtn);
            }
         }catch(Exception e) {
            e.printStackTrace();
            
         }
         return rtn;
      }

      public Object getValue(Object obj) {
         Object rtn = null;
         try {
            rtn = field.get(obj);
            if (mChain != null) {
    
               for (Method m : mChain) {
                  if (m.getParameterTypes().length > 0) {
                     rtn = m.invoke(rtn, rtn);
                  } else {
                     rtn = m.invoke(rtn);
                  }

               }

            }
         } catch (Exception e) {
            e.printStackTrace();
         }
         return rtn;
      }

      private Class<?> getRendererClass(Class<?> start) throws NoSuchMethodException,
         SecurityException {
         mChain = new ArrayList<>();
         Class<?> cur = start;
         if (render.start() != Object.class) {
            start = render.start();
         }
         Method mf = start.getDeclaredMethod(render.value());
         mChain.add(mf);
         start = mf.getReturnType();

         return start;

      }
   }
   
}
