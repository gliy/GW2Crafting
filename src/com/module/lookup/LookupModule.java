package com.module.lookup;

import java.util.Map;
import java.util.Observable;

import com.controller.Job;


public abstract class LookupModule<E, V> extends Observable {

    public abstract Iterable<Job<E>> getJobs();
    public abstract void handle(V value, Map<String, ?> attr);
}
