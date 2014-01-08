package com.runner;

import com.controller.CoreController;
import com.module.lookup.ModuleFactory;
import com.module.lookup.url.TPInfoModule;
import com.module.lookup.wraper.URLModuleWrapper;

public class Runner {

   
   public static void main(String ... args) throws Exception {
    /*DisplayModule<?> display = new DisplayAnnotationProcessor().generate(CraftingItem.class);
     LookupModule<? extends HttpRequestBase, JsonNode> look = ModuleFactory.<HttpRequestBase, JsonNode>load(ModuleFactory.getDefaultLookup().get(0));
     new MainFrame(new CoreController(
           new URLModuleWrapper(look))
              ,display);*/
     new CoreController(new URLModuleWrapper(new TPInfoModule())).load();
    //new MainFrame(null, new CraftingDisplayModule());
      ModuleFactory.getDefaultLookup();
     
   }
}
