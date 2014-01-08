package com.controller.callback;

import java.util.Map;

import lombok.Getter;

import com.controller.Job;

public class Status<E, V> {

   private boolean done;
   @Getter
   private E data;
   @Getter
   private StatusCallback<V> callback;
   private Status(E data, StatusCallback<V> callback, boolean done) {
      this.done = done;
      this.data = data;
      this.callback = callback;
   }

   public static <E> Status<E, Void> done(E data) {
      return new Status<>(data, null, true);
   }
   public static <E> Status<Job<E>, Void> more(Job<E> data) {
      return new Status<>(data, null, false);
   }
   public static <E, V> Status<Job<E>, V> more(Job<E> data, StatusCallback<V> callback) {
      return new Status<>(data, callback, false);
   }
   public boolean isDone() {
      return done;
   }
   
   
}
