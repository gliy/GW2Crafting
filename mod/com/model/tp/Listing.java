package com.model.tp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Listing {

   private Integer unitPrice;
   private Integer quantity;
   private Date date;
   private String name;
   private ListingType type;

   public enum ListingType {
      BUY, SELL;
   }

   public static Listing from(JsonNode node, ListingType type) throws ParseException {
      Integer price = node.findValue("unit_price")
         .asInt();
      Integer quantity = node.findValue("quantity")
         .asInt();
      Date created = new SimpleDateFormat().parse(node.findValue("unit_price")
         .asText());
      String name = node.findValue("name")
         .asText();
      return new Listing(price, quantity, created, name, type);
   }
}
