/**
 * Problem: Find Median from Data Stream (LeetCode #295)
 * Difficulty: Hard
 * Topics: Two Pointers, Design, Sorting, Heap
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Design a data structure that supports addNum and findMedian in
 *   efficient time.
 *
 * Approach 1 – Brute Force (Sort on every findMedian)
 *   Store numbers in list; sort on query. TC addNum: O(1), findMedian: O(n log n)
 *
 * Approach 2 – Insertion Sort (Optimal)
 *   Keep list sorted; binary search insert. TC addNum: O(n), findMedian: O(1)
 *
 * Approach 3 – Two Heaps (Best)
 *   Max-heap (lower half) + Min-heap (upper half).
 *   TC addNum: O(log n), findMedian: O(1)
 */

import java.util.*;

public class FindMedianDataStream {

    // =========================================================================
    // Approach 1: Brute (Sort on query) — addNum O(1), findMedian O(n log n)
    // =========================================================================
    static class MedianBrute {
        List<Integer> list = new ArrayList<>();
        void addNum(int num) { list.add(num); }
        double findMedian() {
            Collections.sort(list);
            int n = list.size();
            return n % 2 == 1 ? list.get(n/2) : (list.get(n/2-1) + list.get(n/2)) / 2.0;
        }
    }

    // =========================================================================
    // Approach 2: Insertion Sort — addNum O(n), findMedian O(1)
    // =========================================================================
    static class MedianInsert {
        List<Integer> list = new ArrayList<>();
        void addNum(int num) {
            int pos = Collections.binarySearch(list, num);
            if (pos < 0) pos = -(pos + 1);
            list.add(pos, num);
        }
        double findMedian() {
            int n = list.size();
            return n % 2 == 1 ? list.get(n/2) : (list.get(n/2-1) + list.get(n/2)) / 2.0;
        }
    }

    // =========================================================================
    // Approach 3: Two Heaps (Best) — addNum O(log n), findMedian O(1)
    // =========================================================================
    static class MedianBest {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder()); // lower half
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // upper half

        void addNum(int num) {
            maxHeap.offer(num);
            minHeap.offer(maxHeap.poll());
            if (minHeap.size() > maxHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        }

        double findMedian() {
            if (maxHeap.size() > minHeap.size()) return maxHeap.peek();
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Find Median from Data Stream ===");

        MedianBest mf = new MedianBest();
        mf.addNum(1); mf.addNum(2);
        System.out.println("After [1,2]: " + mf.findMedian());  // 1.5
        mf.addNum(3);
        System.out.println("After [1,2,3]: " + mf.findMedian()); // 2.0
        mf.addNum(4);
        System.out.println("After [1,2,3,4]: " + mf.findMedian()); // 2.5
    }
}
