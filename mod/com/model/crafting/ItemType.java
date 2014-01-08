package com.model.crafting;


public enum ItemType {
   SWORD(0), HAMMER(1), LONGBOW(2), SHORT_BOW(3), AXE(4), DAGGER(5), GREATSWORD(6), MACE(7), PISTOL(
            8), RIFLE(10), SCEPTER(11), STAFF(12), FOCUS(13), TORCH(14), WARHORN(15), SHIELD(16), SPEAR(
            19), HARPOON_GUN(20), TRIDENT(21) ;
   private int type;

   private ItemType(int type) {
      this.type = type;
   }

   public static ItemType fromId(final int typeId, final int subType) {
      if (typeId != 18) {
         return null;
      }
      for (ItemType type : values()) {
         if (type.type == subType) {
            return type;
         }
      }
      return null;
   }

   @Override
   public String toString() {
      return name().charAt(0) + name().substring(1).toLowerCase().replaceAll("_", " ");
   }

   public static ItemType fromString(String name) {
      for (ItemType type : values()) {
         if (type.toString().equals(name)) {
            return type;
         }
      }
      return null;
   }
}
