/**
 * Problem: Find Median from Data Stream (LeetCode #295)
 * Difficulty: Hard
 * Topics: Design, Heap, Two Pointers
 * Frequently asked at: JP Morgan Chase (Round 1 — quant/algo focused)
 *
 * Description:
 *   Implement MedianFinder:
 *     void addNum(int num)  — add integer to the data structure
 *     double findMedian()   — return median of all elements so far
 *   If even count → median = average of two middle elements.
 *
 * Example:
 *   addNum(1), addNum(2) → findMedian()=1.5
 *   addNum(3)            → findMedian()=2.0
 *
 * JP Morgan Context:
 *   Real-time median of stock prices, order sizes, or spread values
 *   over a rolling window is a core quant analytics requirement.
 *
 * ============================================================
 * Approach — Two Heaps (Max-Heap + Min-Heap)
 * ============================================================
 *   Partition numbers into two halves:
 *     maxHeap (lower half): stores the smaller half — top is the largest small number
 *     minHeap (upper half): stores the larger half  — top is the smallest large number
 *
 *   Invariant:
 *     maxHeap.size() == minHeap.size()       (even total)
 *     OR maxHeap.size() == minHeap.size()+1  (odd total, extra in maxHeap)
 *
 *   addNum(num):
 *     Push to maxHeap. Move maxHeap top to minHeap (balance).
 *     If minHeap has more elements, move its top back to maxHeap.
 *
 *   findMedian():
 *     If sizes equal → (maxHeap.top + minHeap.top) / 2.0
 *     Else           → maxHeap.top
 *
 *   Step-by-step:
 *     addNum(1): maxHeap=[1], minHeap=[]
 *     addNum(2): push 2 to max→[2,1], move top(2) to min→[2]
 *                maxHeap=[1], minHeap=[2]
 *                findMedian() = (1+2)/2 = 1.5
 *     addNum(3): push 3 to max→[3,1], move top(3) to min→[2,3]
 *                min.size>max.size → move min top(2) to max→[2,1], min=[3]
 *                maxHeap=[2,1], minHeap=[3]
 *                findMedian() = 2.0
 *
 *   TC: O(log n) addNum, O(1) findMedian  SC: O(n)
 */

import java.util.PriorityQueue;
import java.util.Collections;

public class MedianFromDataStream {

    static class MedianFinder {
        private final PriorityQueue<Integer> maxHeap; // lower half
        private final PriorityQueue<Integer> minHeap; // upper half

        public MedianFinder() {
            maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            minHeap = new PriorityQueue<>();
        }

        public void addNum(int num) {
            maxHeap.offer(num);
            minHeap.offer(maxHeap.poll());
            if (minHeap.size() > maxHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        }

        public double findMedian() {
            if (maxHeap.size() == minHeap.size()) {
                return (maxHeap.peek() + minHeap.peek()) / 2.0;
            }
            return maxHeap.peek();
        }
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Find Median from Data Stream ===\n");

        MedianFinder mf = new MedianFinder();
        mf.addNum(1);
        mf.addNum(2);
        System.out.println("After addNum(1), addNum(2):");
        System.out.println("  findMedian(): " + mf.findMedian()); // 1.5
        mf.addNum(3);
        System.out.println("After addNum(3):");
        System.out.println("  findMedian(): " + mf.findMedian()); // 2.0

        System.out.println();
        MedianFinder mf2 = new MedianFinder();
        int[] stream = {5, 15, 1, 3};
        for (int num : stream) {
            mf2.addNum(num);
            System.out.printf("  addNum(%2d) → median=%.1f%n", num, mf2.findMedian());
        }
        // Expected: 5.0, 10.0, 5.0, 4.0
    }
}
