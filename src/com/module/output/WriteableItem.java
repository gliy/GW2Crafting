package com.module.output;

import java.util.Map;

public interface WriteableItem<E> {
//marker interface?
   abstract Map<String, ?> getData(E object);
}
