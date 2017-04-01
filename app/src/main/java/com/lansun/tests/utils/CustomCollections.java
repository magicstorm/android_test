package com.lansun.tests.utils;

import java.util.HashMap;

/**
 * Created by ly on 11/25/16.
 */

public class CustomCollections {
    public static class BidiHashMap<k, v> {

        private HashMap<k, Entry> kEntryHashMap = new HashMap<>();
        private HashMap<v, Entry> vEntryHashMap = new HashMap<>();

        class Entry {
            private k k;
            private v v;

            public Entry(k k, v v) {
                this.k = k;
                this.v = v;
            }

            public k getK() {
                return k;
            }

            public void setK(k k) {
                this.k = k;
            }

            public v getV() {
                return v;
            }

            public void setV(v v) {
                this.v = v;
            }
        }

        public boolean contains(k k) {
            return kEntryHashMap.containsKey(k);
        }

        public boolean containsValue(v v) {
            return vEntryHashMap.containsKey(v);
        }

        public v getByKey(k k) {
            Entry e = kEntryHashMap.get(k);
            if (e == null) {
                return null;
            }
            return e.getV();
        }

        public k getbyValue(v v) {
            Entry e = vEntryHashMap.get(v);
            if (e == null) {
                return null;
            }
            return e.getK();
        }

        public boolean put(k k, v v) {
            if (k == null || v == null) {
                return false;
            }
            Entry e = new Entry(k, v);
            if (contains(k)) {
                remove(k);
            }
            if (containsValue(v)) {
                removeByValue(v);
            }
            kEntryHashMap.put(k, e);
            vEntryHashMap.put(v, e);
            return true;
        }

        public v remove(k k) {
            Entry e = kEntryHashMap.remove(k);
            if (e == null) {
                return null;
            }
            vEntryHashMap.remove(e.getV());
            return e.getV();
        }

        public k removeByValue(v v) {
            Entry e = vEntryHashMap.remove(v);
            if (e == null) {
                return null;
            }
            kEntryHashMap.remove(e.getK());
            return e.getK();

        }
    }
}
