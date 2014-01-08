package com.module.lookup.url;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;

import com.controller.ClientManager.SpidyClient;
import com.controller.Job;
import com.controller.callback.Status;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.model.crafting.CraftingItem;
import com.model.crafting.Disciple;
import com.model.crafting.ItemType;
import com.module.lookup.JsonLookupModule;
import com.module.lookup.ModuleInfo;
@ModuleInfo("URL")
public class CraftingModule extends JsonLookupModule<HttpGet, CraftingItem> {
  
   
   public CraftingModule() {
      super(CraftingItem.class);
   }

   @Override
   public Iterable<Job<HttpGet>> getJobs() {

      List<Job<HttpGet>> jobs = newArrayList();
      for (Disciple d : Disciple.values()) {
         jobs.add(job(d, 1));
        // System.out.println("S " + jobs.get(jobs.size() -1).getValue());
      }
      return jobs;
   }

   @Override
   public void handle(Iterable<CraftingItem> items, JsonNode value, final Map<String, ?> attr) {
      System.out.println(":: " + items);
      extract(items, (Disciple) attr.get("disc"), value, Integer.parseInt("" + attr.get("page")));

   }

   private void extract(final Iterable<CraftingItem> items,
      final Disciple disc, final JsonNode node, final int page) {
      try {
         int last = node.findValue("last_page")
            .asInt();

         for (final CraftingItem rtn : items) {
            rtn.disc(disc);
            setChanged();
            notifyObservers(Status.more(new Job<>(new HttpGet(SpidyClient.createItemURL(rtn.result_item_data_id())
               .toString())), getCallback(new JsonStatusCallback<CraftingItem>() {

               @Override
               public void done(CraftingItem item, JsonNode node, Map<String, ?> attr) {

                  node = node.findValue("result");
                  ItemType type;
                  if (node.findValue("rarity")
                     .intValue() == 4 && (type = ItemType.fromId(node.findValue("type_id")
                     .asInt(), node.findValue("sub_type_id")
                     .asInt())) != null) {
                     rtn.level(node.findValue("restriction_level")
                        .asInt())
                        .type(type);

                     if (rtn.level() >= 65) {
                        setChanged();
                        notifyObservers(Status.done(rtn));
                     }
                  } else {

                  }
               }

               @Override
               public CraftingItem get() {
                  return rtn;
               }
            })));
         }
         
         if (page < last) {
            setChanged();
            notifyObservers(Status.more(job(disc, page + 1)));
            
         }

      } catch (Exception e) {
         e.printStackTrace();
      }

   }
   
   private static Job<HttpGet> job(final Disciple disc, final int page) {
      return new Job<>(
         new HttpGet(SpidyClient.createCraftingURL("" + disc.getVal(), page).toString()),
         ImmutableMap.of("disc", disc, "page", page));
   }

   

}
