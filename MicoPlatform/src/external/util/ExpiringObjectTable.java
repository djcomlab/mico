/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package external.util;

import com.sun.java.util.collections.Collections;
import com.sun.java.util.collections.Hashtable;
import com.sun.java.util.collections.Iterator;
import com.sun.java.util.collections.Map;
import com.sun.java.util.collections.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author David Johnson
 */
public class ExpiringObjectTable {
    
    private Map hashtable = Collections.synchronizedMap(new Hashtable());
    private long timeout = Long.MAX_VALUE;
    private Timer timer = new Timer();

    public ExpiringObjectTable(long timeout) {
        this.timeout = timeout;
        timer.schedule(new TimerTask() {
            public void run() {
                Set keySet = hashtable.keySet();
                for(Iterator keys = keySet.iterator();keys.hasNext();) {
                    Object keyObj = keys.next();
                    ExpiringObject expiringObj = (ExpiringObject)hashtable.get(keyObj);
                    if (expiringObj!=null && expiringObj.hasExpired()) {
                        hashtable.remove(keyObj);
                    }
                }
            }
        }, 0, timeout); // run timer task every timeout length to make it granular
    }

    public synchronized Object get(Object obj) {
        ExpiringObject expiringObj = (ExpiringObject)hashtable.get(obj);
        return expiringObj.getObj();
    }

    public synchronized Object put(Object obj, Object obj1) {
        return hashtable.put(obj, new ExpiringObject(obj1));
    }

    public synchronized Object remove(Object obj) {
        return hashtable.remove(obj);
    }

    public synchronized void touch(Object obj) {
        ExpiringObject expiringObj = (ExpiringObject)hashtable.get(obj);
        expiringObj.touch();
    }

    public synchronized int size() {
        return hashtable.size();
    }

    private class ExpiringObject {

        private long lastTouched = System.currentTimeMillis();
        private Object obj;

        public ExpiringObject(Object obj) {
            this.obj = obj;
        }

        public void touch() {
           lastTouched = System.currentTimeMillis();
        }

        public boolean hasExpired() {
            return (System.currentTimeMillis() - lastTouched) > timeout;
        }

        public Object getObj() {
            return obj;
        }

    }

}
