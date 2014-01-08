package com.controller.callback;

import java.util.Map;

public abstract class StatusCallback<E> {
   public abstract void done(E data, Map<String,?> attr);
}
