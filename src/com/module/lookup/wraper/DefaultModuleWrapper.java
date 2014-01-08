package com.module.lookup.wraper;

import com.controller.Job;
import com.module.lookup.LookupModule;

public class DefaultModuleWrapper<E> extends ModuleWrapper<E, E>{

   public DefaultModuleWrapper(LookupModule<E, E> mod) {
      super(mod);
   }

   @Override
   public void execute() {
      for (Job<E> job : mod.getJobs()) {
         mod.handle(job.getValue(), job.getData());
      }
   }

}
