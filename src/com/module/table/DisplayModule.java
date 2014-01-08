package com.module.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.table.TableCellRenderer;

import com.ui.CustomTableModel;

public abstract class DisplayModule<E> extends Observable {
   protected List<E> items = new ArrayList<>();
   public abstract String[] getColumns();

   public abstract Object getValueAt(int r, int c);
   public abstract Class<?> getClass(int c);
   
   public JPanel getOptions() {
      return new JPanel();
   }
   public int size() {
      return items.size();
   }
   public void add(E item) {
      items.add(item);
   }

   public TableCellRenderer getRenderer(int col) {
      return null;
   }

   public RowFilter<? super CustomTableModel, ? super Integer> getFilter() {
      return null;
   }
   
}
