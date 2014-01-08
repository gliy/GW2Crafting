package com.model.crafting;

import javax.swing.ImageIcon;

import lombok.Getter;

public enum Disciple {
   ARTIFICER(2), HUNTSMAN(1), WEAPONSMITH(3);
   @Getter
   private ImageIcon image;
   @Getter
   private int val;

   private Disciple(int val) {
      this.val = val;
      try {
         image = new ImageIcon("resources/img/" + toString().toLowerCase()
               + ".png");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   @Override
   public String toString() {
      return name().toLowerCase();
   }
   
   
   
   public static Disciple fromString(String disc) {
      for(Disciple d : values()) {
         if(d.toString().equalsIgnoreCase(disc)) {
            return d;
         }
      }
      return null;
   }
   public static Disciple fromInt(int disc) {
      for(Disciple d : values()) {
         if(d.val == disc) {
            return d;
         }
      }
      return null;
   }
}
