/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//adapted from package org.apache.activemq.util; by David Johnson (18 Jan 2010)
package external.util;

import net.jxta.impl.cm.LRUCache;

/**
 * A Simple LRU Set
 *
 * @version $Revision$
 * @param <K>
 * @param <V>
 */

public class LRUSet {

    private static final Object IGNORE = new Object();
    private final LRUCache cache;

    /**
     * Default constructor for an LRU Cache The default capacity is 10000
     */
    public LRUSet() {
        this(10000);
    }

    /**
     * Constructs a LRUCache with a maximum capacity
     *
     * @param maximumCacheSize
     */
    public LRUSet(int maximumCacheSize) {
        this.cache = new LRUCache(maximumCacheSize);
    }

//    public Iterator iterator() {
//        return cache.keySet().iterator();
//    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
//        return cache.isEmpty();
        return cache.size()==0;
    }

    public boolean contains(Object o) {
        return cache.contains(o);
    }

    public boolean add(Object o) {
        cache.put(o, IGNORE);
        return cache.contains(o);
    }

    public boolean remove(Object o) {
        return cache.remove(o)==IGNORE;
    }

    public void clear() {
        cache.clear();
    }

}
