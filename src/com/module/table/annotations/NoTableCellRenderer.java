package com.module.table.annotations;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class NoTableCellRenderer implements TableCellRenderer {

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
      boolean hasFocus, int row, int column) {
      // TODO Auto-generated method stub
      return null;
   }

}
