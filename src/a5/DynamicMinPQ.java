package a5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A min priority queue of distinct elements of type `KeyType` associated with (extrinsic) double
 * priorities. Supports updating the priorities of elements currently in the queue, and guarantees
 * expected amortized O(log N) performance for all modifying operations, where N is the queue size.
 */
public class DynamicMinPQ<KeyType> {

    /**
     * Pairs an element `key` with its associated priority `priority`.
     */
    private record Entry<KeyType>(KeyType key, double priority) { }

    /**
     * ArrayList representing a binary min-heap of element-priority pairs.  Satisfies
     * `compare(heap.get(i), heap.get((i-1)/2)) >= 0` for all `i` in `[1..heap.size())`.
     */
    private final ArrayList<Entry<KeyType>> heap;

    /**
     * Associates each element in the queue with its index in `heap`.  Satisfies
     * `heap.get(index.get(e)).key().equals(e)` if `e` is an element in the queue.
     * Only maps elements that are in the queue (`index.size() == heap.size()`).
     */
    private final Map<KeyType, Integer> index;

    /**
     * Orders keys whose numeric priorities are equal.
     */
    private final Comparator<? super KeyType> tieBreaker;


    /**
     * Create an empty queue with arbitrary ordering among equal priorities.
     */
    public DynamicMinPQ() {
        this((a, b) -> 0);
    }

    /**
     * Create an empty queue that uses `tieBreaker` to order keys with equal numeric priorities.
     */
    public DynamicMinPQ(Comparator<? super KeyType> tieBreaker) {
        this.tieBreaker = tieBreaker;
        index = new ProbingHashMap<>();
        heap = new ArrayList<>();
    }

    /**
     * Return whether this queue contains no elements.
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Return the number of elements contained in this queue.
     */
    public int size() {
        return heap.size();
    }

    /**
     * Return an element associated with the smallest priority in this queue.  This is the same
     * element that would be removed by a call to `remove()` (assuming no mutations in between).
     * Throws a `NoSuchElementException` if this queue is empty.
     */
    public KeyType peek() {
        // Propagate exception from `List::getFirst()` if empty.
        return heap.getFirst().key();
    }

    /**
     * Return the minimum priority associated with an element in this queue.  Throws
     * a `NoSuchElementException` if this queue is empty.
     */
    public double minPriority() {
        return heap.getFirst().priority();
    }

    /**
     * Compare entries by numeric priority, then by the configured tiebreaker.
     */
    private int compare(Entry<KeyType> a, Entry<KeyType> b) {
        int byPriority = Double.compare(a.priority(), b.priority());
        return byPriority != 0 ? byPriority : tieBreaker.compare(a.key(), b.key());
    }

    /**
     * Swap the `Entry`s at indices `i` and `j` in `heap`, updating `index` accordingly.  Requires
     * `0 <= i,j < heap.size()`.
     */
    private void swap(int i, int j) {
        // TODO 3.1a: Implement this method according to its specification
        throw new UnsupportedOperationException();
    }

    // TODO 3.1b: Implement private helper methods for bubbling entries up and down in the heap.
    //  Their interfaces are up to you, but you must write precise specifications.

    /**
     * Add element `key` to this queue, associated with priority `priority`.  Requires `key` is not
     * contained in this queue.
     */
    private void add(KeyType key, double priority) {
        // TODO 3.1c: Implement this method according to its specification
        throw new UnsupportedOperationException();
    }

    /**
     * Change the priority associated with element `key` to `priority`.  Requires that `key` is
     * contained in this queue.
     */
    private void update(KeyType key, double priority) {
        assert index.containsKey(key);

        // TODO 3.1d: Implement this method according to its specification
        throw new UnsupportedOperationException();
    }

    /**
     * If `key` is already contained in this queue, change its associated priority to `priority`.
     * Otherwise, add it to this queue with that priority.
     */
    public void put(KeyType key, double priority) {
        if (!index.containsKey(key)) {
            add(key, priority);
        } else {
            update(key, priority);
        }
    }

    /**
     * Remove and return the element associated with the smallest priority in this queue.  If
     * multiple elements are tied for the smallest priority, the configured tie breaker is used.
     * If they also tie under that ordering, an arbitrary one will be removed.
     * Throws NoSuchElementException if this queue is empty.
     */
    public KeyType remove() {
        // TODO 3.1e: Implement this method according to its specification
        throw new UnsupportedOperationException();
    }

}
