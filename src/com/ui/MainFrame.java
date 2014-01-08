package com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableRowSorter;

import com.controller.CoreController;
import com.module.table.DisplayModule;

public class MainFrame extends JFrame {

   private JTable table;
   private JLabel loaded;


   public MainFrame(final CoreController cont, final DisplayModule<?> mod) throws Exception {
      super("Crating");


      setSize(700, 850);

      final CustomTableModel tModel = (CustomTableModel) new CustomTableModel(mod);
      // this.setLayout(new BorderLayout());
      table = new JTable(tModel);
      // setContentPane(new JScrollPane(table));
      {
         final JPanel prim = new JPanel(new BorderLayout());
         JScrollPane scroll = new JScrollPane(table);
         scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
         prim.add(scroll, BorderLayout.CENTER);

         setContentPane(prim);
      }
      {
         JPanel bottom = new JPanel(new BorderLayout());

         loaded = new JLabel("Loaded: 0");
         bottom.add(mod.getOptions(), BorderLayout.CENTER);;
         bottom.add(loaded, BorderLayout.SOUTH);
         getContentPane().add(bottom, BorderLayout.SOUTH);
      }

      final TableRowSorter<CustomTableModel> sorter = new TableRowSorter<>();
      final AtomicInteger count = new AtomicInteger(1);
      table.setRowSorter(sorter);
      sorter.setModel(tModel);
      if(mod.getFilter() != null) {
         sorter.setRowFilter(mod.getFilter());
       /*  mod.addObserver(new Observer() {
            
            @Override
            public void update(Observable o, Object arg) {
               sorter.modelStructureChanged();
               
            }
         });*/
      }
      
      // final PrintWriter wr = new PrintWriter(new File("tmp.out"));
      cont.addObserver(new Observer() {

         @Override
         public void update(Observable o, final Object arg) {

            SwingUtilities.invokeLater(new Runnable() {

               @Override
               public void run() {
                  loaded.setText("Loaded: " + count.incrementAndGet());
                  ((DisplayModule<? super Object>)mod).add(arg);
                  //mod.a
                  tModel.fireTableRowsInserted(mod.size() - 1, mod.size() - 1);
               }
            });

         }
      });

      table.setRowHeight(30);

      for (int i = 0; i < mod.getColumns().length; i++) {
         if (mod.getRenderer(i) != null) {
            table.getColumn(mod.getColumns()[i])
                     .setCellRenderer(mod.getRenderer(i));
         }
      }
      
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocationByPlatform(true);
      setVisible(true);
      cont.load();

   }



}

