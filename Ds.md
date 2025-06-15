Yes — JPMorgan Chase VP-level interviews regularly include data structures (DS) questions, especially involving real-world scenarios, Java implementation, and problem-solving with concurrency and system design in mind.


---

✅ Frequently Asked Data Structures Questions at JPMC (with Solutions)


---

🔹 1. LRU Cache Design (with Thread Safety)

Asked: "Design a thread-safe LRU Cache using Java."

✅ Concepts Tested: LinkedHashMap, concurrency, design patterns.

public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, V> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = Collections.synchronizedMap(new LinkedHashMap<>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        });
    }

    public V get(K key) {
        return cache.getOrDefault(key, null);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }
}


---

🔹 2. Thread-safe Rate Limiter

Asked: "Implement a rate limiter that allows 100 requests per minute."

✅ Concepts Tested: Sliding window/log bucket/leaky bucket, concurrency.

public class RateLimiter {
    private final int maxRequests;
    private final long timeWindow;
    private final Queue<Long> requestTimestamps = new LinkedList<>();

    public RateLimiter(int maxRequests, long timeWindowMillis) {
        this.maxRequests = maxRequests;
        this.timeWindow = timeWindowMillis;
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        while (!requestTimestamps.isEmpty() && (now - requestTimestamps.peek()) > timeWindow) {
            requestTimestamps.poll();
        }
        if (requestTimestamps.size() < maxRequests) {
            requestTimestamps.offer(now);
            return true;
        }
        return false;
    }
}


---

🔹 3. Top K Frequent Words

public List<String> topKFrequent(String[] words, int k) {
    Map<String, Integer> count = new HashMap<>();
    for (String word : words) count.put(word, count.getOrDefault(word, 0) + 1);

    PriorityQueue<String> heap = new PriorityQueue<>(
        (a, b) -> count.get(a).equals(count.get(b)) ?
                  b.compareTo(a) : count.get(a) - count.get(b));

    for (String word : count.keySet()) {
        heap.offer(word);
        if (heap.size() > k) heap.poll();
    }

    List<String> result = new ArrayList<>();
    while (!heap.isEmpty()) result.add(0, heap.poll());
    return result;
}


---

🔹 4. Validate Binary Search Tree

public boolean isValidBST(TreeNode root) {
    return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
}

private boolean validate(TreeNode node, long min, long max) {
    if (node == null) return true;
    if (node.val <= min || node.val >= max) return false;
    return validate(node.left, min, node.val) &&
           validate(node.right, node.val, max);
}


---

🔹 5. Sliding Window Maximum

Asked: "Find the maximum of each sliding window of size k."

public int[] maxSlidingWindow(int[] nums, int k) {
    Deque<Integer> dq = new LinkedList<>();
    int[] result = new int[nums.length - k + 1];
    for (int i = 0; i < nums.length; i++) {
        if (!dq.isEmpty() && dq.peekFirst() == i - k)
            dq.pollFirst();
        while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i])
            dq.pollLast();
        dq.offerLast(i);
        if (i >= k - 1)
            result[i - k + 1] = nums[dq.peekFirst()];
    }
    return result;
}


---

🔹 6. Design a Thread-safe Blocking Queue

public class BlockingQueue<T> {
    private Queue<T> queue = new LinkedList<>();
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void enqueue(T item) throws InterruptedException {
        while (queue.size() == capacity) wait();
        queue.add(item);
        notifyAll();
    }

    public synchronized T dequeue() throws InterruptedException {
        while (queue.isEmpty()) wait();
        T item = queue.poll();
        notifyAll();
        return item;
    }
}


---

🔹 7. Find First Non-Repeating Character

public char firstNonRepeatingChar(String s) {
    Map<Character, Integer> map = new LinkedHashMap<>();
    for (char c : s.toCharArray()) map.put(c, map.getOrDefault(c, 0) + 1);
    for (Map.Entry<Character, Integer> entry : map.entrySet()) {
        if (entry.getValue() == 1) return entry.getKey();
    }
    return '_';
}


---

🔹 8. Detect Cycle in Directed Graph

public boolean hasCycle(int V, List<List<Integer>> adj) {
    boolean[] visited = new boolean[V];
    boolean[] stack = new boolean[V];
    for (int i = 0; i < V; i++) {
        if (dfs(i, adj, visited, stack)) return true;
    }
    return false;
}

private boolean dfs(int v, List<List<Integer>> adj, boolean[] visited, boolean[] stack) {
    if (stack[v]) return true;
    if (visited[v]) return false;
    visited[v] = true;
    stack[v] = true;
    for (int neighbor : adj.get(v)) {
        if (dfs(neighbor, adj, visited, stack)) return true;
    }
    stack[v] = false;
    return false;
}


---

🏁 Common DS Topics Asked at JPMorgan

Topic	Asked?	Real-World Focus

LinkedList (LRU, cycle)	✅	Thread-safe cache
Queue (Producer/Consumer)	✅	BlockingQueue
HashMap/Set	✅	De-dupe, indexing
Trees & BST	✅	Navigation trees
Sliding Window	✅	Rate limiters
Graphs (DFS/BFS)	✅	Fraud detection
Heaps	✅	Top-K analysis
Concurrency + DS	✅✅	High-performance systems



---

Would you like a VP-level DS mock test, or a Markdown workbook covering all these with problem statements?

