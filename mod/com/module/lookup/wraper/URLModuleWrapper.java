package com.module.lookup.wraper;

import java.util.Arrays;
import java.util.Observable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.util.EntityUtils;

import com.controller.ClientManager;
import com.controller.Job;
import com.controller.callback.Status;
import com.controller.callback.StatusCallback;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.module.lookup.LookupModule;

public class URLModuleWrapper extends ModuleWrapper<HttpRequestBase, JsonNode> {
   private static final Executor itemsExe = Executors.newFixedThreadPool(12);

   private static final HttpAsyncClient client = ClientManager.get();
   private static final ObjectMapper MAPPER = new ObjectMapper();

   @SuppressWarnings("unchecked")
   public URLModuleWrapper(LookupModule<? extends HttpRequestBase, JsonNode> mod) {
      super((LookupModule<HttpRequestBase, JsonNode>) mod);
   }

   @Override
   public void execute() {
      try {
         for (final Job<? extends HttpRequestBase> job : mod.getJobs()) {
            itemsExe.execute(new Runnable() {

               @Override
               public void run() {
                  load(job, null);
               }
            });
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void load(final Job<? extends HttpRequestBase> job, final StatusCallback<JsonNode> back) {
      // job.getValue().addHeader("User-Agent", "myscript");
      client.execute(job.getValue(), new FutureCallback<HttpResponse>() {

         @Override
         public void cancelled() {
         }

         @Override
         public void completed(HttpResponse arg0) {
            // System.out.println("!");
            System.out.println(arg0.getStatusLine());
            System.out.println(Arrays.toString(arg0.getAllHeaders()));
            byte[] r = new byte[1024];
            try {
               new GzipDecompressingEntity(arg0.getEntity())
                  .getContent()
                  .read(r);
            } catch (Exception e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }

            System.out.println(new String(r));
            if (1 == 1)
               return;
            if (back == null) {
               mod.handle(makeMap(arg0), job.getData());
            } else {
               back.done(makeMap(arg0), job.getData());
            }
         }

         @Override
         public void failed(Exception arg0) {
            arg0.printStackTrace();
            System.err.println(arg0.getMessage());
         }
      });
   }

   private JsonNode makeMap(final HttpResponse resp) {
      try {
         JsonNode node = MAPPER.readTree(resp.getEntity()
            .getContent());
         return node;

      } catch (Exception e) {
         e.printStackTrace();
         throw Throwables.propagate(e);
      } finally {
         EntityUtils.consumeQuietly(resp.getEntity());
      }
   }

   @Override
   public void update(Observable o, Object arg) {
      Status<?, ?> st = (Status<?, ?>) arg;
      if (st.isDone()) {
         System.out.println("Done");
         setChanged();
         notifyObservers(st.getData());
      } else {
         final Status<Job<? extends HttpRequestBase>, ?> job = (Status<Job<? extends HttpRequestBase>, ?>) st;
         itemsExe.execute(new Runnable() {

            @Override
            public void run() {
               load(job.getData(),
                  (job.getCallback() != null ? (StatusCallback<JsonNode>) job.getCallback() : null));
            }
         });

      }
   }

}
