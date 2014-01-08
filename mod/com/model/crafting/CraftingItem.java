package com.model.crafting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import com.model.binder.json.Bind;
import com.module.table.annotations.Display;
import com.module.table.annotations.HasDisplay;
import com.module.table.annotations.MethodRender;

@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@HasDisplay
@Bind
public class CraftingItem {

   @Display("Name")
   private String name;
   
   @Display("Disc")
   @MethodRender("getImage")
   private Disciple disc;
   
   private int level;
   
   private ItemType type;
   
   private int result_item_data_id;
   
   private int rating;
   
   @Display("Cost")  
   @MethodRender("getDisplayCost")
   private int crafting_cost;
 
   
   public String getDisplayCost() {
      StringBuilder bu = new StringBuilder();
      if(crafting_cost == 0) {
         return "0c";
      }
      
      int nC = crafting_cost;
      bu.append(crafting_cost % 100 + "c");
      nC = crafting_cost / 100;

      if (nC > 0) {
         bu.insert(0, nC % 100 + "s ");
         nC /= 100;
      }
      if (nC > 0) {
         bu.insert(0, nC + "g ");
      }

      return bu.toString();
   }
   
   @Override
   public String toString() {
      return String.format("%s,%s,%d,%s,%d", name, disc, level, type, crafting_cost);
   }
   
 /*  @Override
   public Map<String, Object> map() {
      Map<String, Object> rtn = super.map();
      rtn.put("display", g)
   }*/
}
