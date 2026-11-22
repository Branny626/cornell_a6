package a5;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A map with keys of type 'K' and values of type `V`, implemented using a hash table with linear
 * probing. 
 */
public class ProbingHashMap<K, V> implements Map<K, V> {
    /**
     * Represents an association of a `key` (of type `K`) with a `value` (of type `V`).
     */
    private record Entry<K, V>(K key, V value) { }

    /**
     * Represents a tombstone. If an entry at index `i` is removed, element `i` will be replaced
     * by a reference to this object. Tombstones count toward the load factor, and are cleared when
     * the hash table is resized.
     */
    @SuppressWarnings("rawtypes")
    private static final Entry TOMBSTONE = new Entry<>(null, null);

    /**
     * The initial capacity of the hash table for new instances of `ProbingHashMap`.
     */
    private static final int INITIAL_CAPACITY = 16;

    /**
     * The maximum load factor (inclusive) that is allowed in the `entries` hash table. If the load
     * factor ever exceeds this maximum, then the hash table length must be immediately doubled to
     * reduce the load factor. Must have `0 < MAX_LOAD_FACTOR < 1`.
     */
    public static final double MAX_LOAD_FACTOR = 0.5;

    /**
     * The probing hash table backing this map. Indices (i.e., buckets) that don't currently store an
     * entry (possibly a TOMBSTONE) are `null`. If this map contains an entry with a key whose hash
     * code maps to index `i`, then the (unique) entry containing that key is reachable via linear
     * search starting at index `i` (wrapping around the array if necessary) without encountering
     * `null`.
     */
    private Entry<K, V>[] entries;

    // TODO 2.1a-b: Add additional fields as required to meet runtime requirements. Be sure to
    //  document these fields with a description of how to interpret their values and list any of 
    //  their invariants.


    /**
     * Create a new empty `ProbingHashMap`.
     */
    @SuppressWarnings("unchecked")
    public ProbingHashMap() {
        entries = new Entry[INITIAL_CAPACITY];
        // TODO 2.1a-b: Initialize any additional fields you introduced above.
    }

    /**
     * Returns the number of keys currently associated with values in this map. Runs in O(1) time.
     */
    @Override
    public int size() {
        // TODO 2.1a: Complete the definition of this method to satisfy its specifications and
        //  meet its runtime requirement.
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the current load factor of the hash table backing this map. Runs in O(1) time.
     */
    private double loadFactor() {
        // TODO 2.1b: Complete the definition of this method to satisfy its specifications and
        //  meet its runtime requirement.
        throw new UnsupportedOperationException();
    }


    /**
     * If `key` is a key in this map, return the index in `entries` for this key. Otherwise, returns
     * the first index of a `null` or tombstone entry in the table at or after the index
     * corresponding to the key's hash code (wrapping around).
     */
    private int findEntry(K key) {
        // TODO 2.2a: Implement this method as specified.
        throw new UnsupportedOperationException();
    }

    /**
     * Returns whether a value is associated with the given `key`.
     * Runs in O(1) expected time.
     */
    @Override
    public boolean containsKey(K key) {
        // TODO 2.2b: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the value associated with the given `key`. Throws a `NoSuchElementException` if no
     * value is associated with that key. Requires `key` is not null. Runs in O(1) expected time.
     */
    @Override
    public V get(K key) {
        // TODO 2.2c: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

    /**
     * Associates the given `value` to the given `key`. Requires `key` and `value` are not null.
     * Runs in O(1) expected time.
     */
    @Override
    public void put(K key, V value) {
        // TODO 2.2d: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }


    /**
     * Removes the entry associated with the given `key` and returns its `value`. Throws a
     * `NoSuchElementException` if no value is associated with that key. Requires `key` is not null.
     * Runs in O(1) expected time.
     */
    @Override
    @SuppressWarnings("unchecked")
    public V remove(K key) {
        // TODO 2.2e: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new ProbingHashMapIterator();
    }

    /**
     * An iterator over the keys in this hash table. This map must not be structurally
     * modified while any such iterators are alive.
     */
    private class ProbingHashMapIterator implements Iterator<K> {

        /**
         * The index of the entry in `entries` containing the next value to yield, or
         * `entries.length` if all values have been yielded.
         */
        private int iNext;

        /**
         * Create a new iterator over this dictionary's keys.
         */
        ProbingHashMapIterator() {
            iNext = 0;
            findNext();
        }

        /**
         * Set `iNext` to the first index `i` not less than the current value of `iNext` such that
         * `entries[i] != null` and 'entries[i] != TOMBSTONE', or set it to `entries.length` if 
         * there are no remaining non-null and non-tombstone entries.  Note that if `iNext` is 
         * already the index of a non-null and non-tombstone entry, then it will not be changed.
         */
        private void findNext() {
            while (iNext < entries.length && (entries[iNext] == null || entries[iNext] == TOMBSTONE)) {
                iNext += 1;
            }
        }

        @Override
        public boolean hasNext() {
            return iNext < entries.length;
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            K ans = entries[iNext].key;
            iNext += 1;
            findNext();
            return ans;
        }
    }
}
