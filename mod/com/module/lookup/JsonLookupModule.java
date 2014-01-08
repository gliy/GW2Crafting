package com.module.lookup;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.controller.callback.StatusCallback;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.model.binder.json.Binder;

public abstract class JsonLookupModule<E, V> extends LookupModule<E, JsonNode> {

   private Class<V> resolve;

   public JsonLookupModule(Class<V> clz) {
      resolve = Binder.validate(clz);
   }

   @Override
   public final void handle(JsonNode value, Map<String, ?> attr) {

      List<JsonNode> use = value.findValue("results") != null ? newArrayList(value.findValue(
         "results")
         .iterator())
         : new ArrayList<JsonNode>();

      handle(Lists.transform(use, new Function<JsonNode, V>() {

         @Override
         public V apply(JsonNode input) {
            return Binder.bind(input, resolve);
         }

      }), value, attr);

   }

   protected abstract void handle(Iterable<V> data, JsonNode value, Map<String, ?> attr);

   public StatusCallback<JsonNode> getCallback(JsonStatusCallback<V> callback) {
      return new JsonStatusCallbackWrapper(callback);
   }

   @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
   private class JsonStatusCallbackWrapper extends StatusCallback<JsonNode> {
      private final JsonStatusCallback<V> callback;

      @Override
      public void done(JsonNode data, Map<String, ?> attr) {
         callback.done(Binder.bind(data.findValue("result"), callback.get()), data, attr);
      }

   }

   public interface JsonStatusCallback<V> {
      V get();

      void done(V item, JsonNode data, Map<String, ?> attr);
   }

}
