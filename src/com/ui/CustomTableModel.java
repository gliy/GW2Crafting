package com.ui;

import javax.swing.table.AbstractTableModel;

import lombok.AllArgsConstructor;

import com.module.table.DisplayModule;
@AllArgsConstructor
public class CustomTableModel extends AbstractTableModel {

   //Change
   private DisplayModule<?> mod;
   
   //public final List<String> COLS = ImmutableList
    //        .of("Disciple", "Item", "Cost", "Level", "Type");

   @Override
   public String getColumnName(int i) {
      return mod.getColumns()[i];
   }

   @Override
   public Object getValueAt(int arg0, int arg1) {

      /*if (getColumnName(arg1).equals("Disciple")) {
         return items.get(arg0).disc().getImage();
      } else if (getColumnName(arg1).equals("Cost")) {
         return items.get(arg0).cost();
      } else if (getColumnName(arg1).equals("Level")) {
         return items.get(arg0).level();
      } else if (getColumnName(arg1).equals("Type")) {
         return items.get(arg0).type().toString();
      } else if (getColumnName(arg1).equals("Item")) {
         return items.get(arg0).name();
      } else if (getColumnName(arg1).equals("...")) {
         // return items.get(arg0).getDisplayCost();
      }
      return items.get(arg0);*/
      return mod.getValueAt(arg0, arg1);
   }

   @Override
   public Class<?> getColumnClass(int column) {
     /* if (getColumnName(column).equals(COLS.get(0))) {
         return ImageIcon.class;
      } else if (getColumnName(column).equals(COLS.get(2))) {
         return Integer.class;
      }
      return String.class;*/
      return mod.getClass(column);

   }

   @Override
   public int getRowCount() {
      return mod.size();
   }

   @Override
   public int getColumnCount() {
      return mod.getColumns().length;
   }
}