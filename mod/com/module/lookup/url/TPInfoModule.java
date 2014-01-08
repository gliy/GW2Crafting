package com.module.lookup.url;

import java.util.Map;

import org.apache.http.client.methods.HttpGet;

import com.controller.ClientManager;
import com.controller.Job;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import com.module.lookup.LookupModule;

public class TPInfoModule extends LookupModule<HttpGet, JsonNode> {

   public TPInfoModule() {
      try {
     
            ClientManager.getGW2().init("iankings111@hotmail.com", "N33dapin");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public Iterable<Job<HttpGet>> getJobs() {
      return ImmutableList.of(new Job<>( ClientManager.getGW2().getSellListings()));
   }

   @Override
   public void handle(JsonNode value, Map<String, ?> attr) {
      System.out.println(value);
   }


}
