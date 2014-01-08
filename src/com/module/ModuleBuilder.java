package com.module;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.module.lookup.LookupModule;
import com.module.lookup.wraper.ModuleWrapper;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ModuleBuilder<E, V> {

   private final LookupModule<E, V> mod;

   public static <E, V> ModuleBuilder<E, V> from(LookupModule<E, V> mod) {
      return new ModuleBuilder<>(mod);
   }
   
   public FinishedModule build() {
      return null;
   }
   
   @AllArgsConstructor(access = AccessLevel.PRIVATE)
   public static class FinishedModule {
      @Getter
      private final ModuleWrapper<?, ?> wrapper;
   }
}
