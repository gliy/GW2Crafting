package com.controller;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Job<E> {

   @Getter
   private final E value;
   @Getter
   private Map<String, ?> data;
   
   public Job(E value, Map<String, ?> data) {
      this(value);
      this.data = data;
   }
}
