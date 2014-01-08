package com.controller;

import java.util.Observable;
import java.util.Observer;

import lombok.AllArgsConstructor;

import com.module.lookup.wraper.ModuleWrapper;

@AllArgsConstructor
public class CoreController extends Observable implements Observer {

   private ModuleWrapper<?, ?> mod;
   
   
   public void load() {
      mod.addObserver(this);
      mod.execute();
   }

   @Override
   public void update(Observable o, Object arg) {
      setChanged();
      notifyObservers(arg);
      
   }
}
