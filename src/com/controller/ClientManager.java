package com.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Throwables;

public class ClientManager {

   private static CloseableHttpAsyncClient client;
   public static final String BASE_URL = "http://www.gw2spidy.com/api/v0.9/json/recipes/%d/%d";
   public static final String ITEM_URL = "http://www.gw2spidy.com/api/v0.9/json/item/%d";
   private static GW2Client gw2Client;
   private static SpidyClient spidyClient;

   public static HttpAsyncClient get() {
      try {
         if (client == null) {
            client = HttpAsyncClients.custom()

               .setMaxConnPerRoute(20)
               .setMaxConnTotal(20)

               .addInterceptorLast(new HttpResponseInterceptor() {
                  
                  @Override
                  public void process(HttpResponse arg0, HttpContext arg1) throws HttpException, IOException {
                     if(arg0.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                        System.err.println(arg0.getStatusLine());
                     }
                     if(arg0.getEntity().getContentEncoding() != null &&
                        arg0.getEntity().getContentEncoding().getValue().equals("gzip")) {
                       // EntityUtils.updateEntity(arg0, new GzipDecompressingEntity(arg0.getEntity()));
                         arg0.setEntity( new GzipDecompressingEntity(arg0.getEntity()));
                     //  arg1.
                     }
                     
                  }
               })
               .build();
            ((CloseableHttpAsyncClient) client).start();
            
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return client;
   }

   public static GW2Client getGW2() {

      return (gw2Client == null ? (gw2Client = new GW2Client()) : gw2Client);
   }

   public static SpidyClient getSpidy() {

      return (spidyClient == null ? (spidyClient = new SpidyClient()) : spidyClient);
   }

   public static class SpidyClient {
      private SpidyClient() {

      }

      public static final URL createItemURL(final int id) {
         try {
            return new URL(String.format(ClientManager.ITEM_URL, id));
         } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
         }
      }

      public static final URL createCraftingURL(final String disc, int page) {

         try {
            return new URL(String.format(ClientManager.BASE_URL, disc, page));
         } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
         }
      }

   }

   public static class GW2Client {
      private String id;
      private String s;
      private String expires;// 06 Jan 2015 07:12:59 GMT
      private static final String BASE = "https://tradingpost-live.ncplatform.net/%s";
      private GW2Client() {

      }
      
      public static final URL getBuyListings() {
         return null;
      }
      public final HttpGet getSellListings() {
         try {
            HttpGet get = new HttpGet(
               String.format(BASE, "ws/me.json?time=now&type=sell&charid=&offset=1&count=10"));
            System.out.println(get.getURI());
            //setHeaders(get);
            get.setHeader("Host","tradingpost-live.ncplatform.net");
            get.setHeader("Connection","keep-alive");
            get.setHeader("X-Requested-With","XMLHttpRequest");
            get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1003.1 Safari/535.19 Awesomium/1.7.1");
            get.setHeader("Accept","*/*");
            get.setHeader("Referer","https://tradingpost-live.ncplatform.net/me");
            get.setHeader("Accept-Encoding","gzip,deflate");
            get.setHeader("Accept-Language","en");
            get.setHeader("Accept-Charset","iso-8859-1,*,utf-8");

          //  get.setHeader("Cookie","s=7C40934F-F6CF-4C18-BD7B-583E859C4F4A");
            get.setHeader("Cookie","s=AE5331C5-A81C-40AE-B5E6-62F9857C28B3");
            
            
            return get;
         } catch (Exception e) {
            e.printStackTrace();
            throw Throwables.propagate(e);
         }
         
      }
      public void init(String user, String pass) {
         HttpPost post = new HttpPost("https://account.guildwars2.com/login");

         setHeaders(post);

         post.setEntity(new ByteArrayEntity(String.format("email=%s&password=%s", user, pass)
            .getBytes()));
         try {
            HttpResponse resp = ClientManager.get()
               .execute(post, null)
               .get();
            //HttpEntity use = new GzipDecompressingEntity(resp.getEntity());

            // String postResp = new
            // String(ByteStreams.toByteArray(use.getContent()));
            s = resp.getHeaders("Set-Cookie")[0].getElements()[0].getValue();
            System.out.println("s: " + s);
           
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            EntityUtils.consumeQuietly(post.getEntity());
         }
      }

      private static <E extends HttpRequest> void setHeaders(E r) {
         r.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
         r.setHeader("Accept-Encoding", "gzip, deflate");
         r.setHeader("Accept-Language", "en-US,en;q=0.5");
         r.setHeader("Cache-Control", "no-cache");
         r.setHeader("Connection", "keep-alive");
         r.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8 ");
         r.setHeader("Pragma", "no-cache");
         r.setHeader("Referer", "https://account.guildwars2.com/login");
         r.setHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
         r.setHeader("X-Requested-With", "XMLHttpRequest");
      }

   }
}
