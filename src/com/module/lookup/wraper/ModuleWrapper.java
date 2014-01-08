package com.module.lookup.wraper;

import java.util.Observable;
import java.util.Observer;

import com.module.lookup.LookupModule;

public abstract class ModuleWrapper<E, V> extends Observable implements Observer {

   protected final LookupModule<E, V> mod;

   public ModuleWrapper(LookupModule<E, V> mod) {
      this.mod = mod;
      mod.addObserver(this);
   }
   
   public abstract void execute();
   
   public LookupModule<E, V> getMod() {
      return mod;
   }
   
   @Override
   public void update(Observable obj, Object arg) {
      
   }
}
