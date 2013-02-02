/*
 * Copyright 2012 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.errai.common.rebind;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mike Brock
 */
public abstract class CacheUtil {
  private CacheUtil() {
  }

  private static final Map<Class<? extends CacheStore>, CacheStore> CACHE_STORE_MAP
      = new HashMap<Class<? extends CacheStore>, CacheStore>();

  public static <T extends CacheStore> T getCache(final Class<T> type) {
    synchronized (type) {
      T cacheStore = (T) CACHE_STORE_MAP.get(type);
      if (cacheStore == null) {
        try {
          cacheStore = type.newInstance();
          CACHE_STORE_MAP.put(type, cacheStore);
        }
        catch (Throwable e) {
          throw new RuntimeException("failed to instantiate new type: " + type.getName(), e);
        }
      }

      return cacheStore;
    }
  }

  public static synchronized void clearAll() {
    System.out.println("*** CLEAR ALL CALLED ***");

    for (Map.Entry<Class<? extends CacheStore>, CacheStore> entry : CACHE_STORE_MAP.entrySet()) {
      synchronized (entry.getKey()) {
        entry.getValue().clear();
      }
    }
  }
}