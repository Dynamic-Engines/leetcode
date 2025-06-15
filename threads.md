Here’s your Java Concurrency & Threading Cheat Sheet (VP/Lead-Level, JPMC Edition) in plain text format. You can copy-paste it into a .txt, .docx, or .pdf file.


---

🧠 Java Concurrency & Threading Cheat Sheet

VP/Lead-Level Interview Prep (JPMC Edition)


---

🔹 1. Java Memory Model & volatile

volatile ensures visibility but not atomicity.

Happens-Before: Ensures memory write → read consistency.

Safe publication via:

final fields in constructors,

volatile,

static initializers.




---

🔹 2. synchronized vs ReentrantLock

Feature	synchronized	ReentrantLock

Blocking	Implicit	Explicit control
Timeout support	❌ No	✅ Yes (tryLock)
Interrupt support	❌ No	✅ Yes (lockInterruptibly)
Fairness	❌ No	✅ Configurable


> Use ReentrantLock for complex, non-blocking logic.




---

🔹 3. Thread Lifecycle

NEW -> RUNNABLE -> RUNNING -> BLOCKED/WAITING -> TERMINATED

Use Thread.getState() or jstack to inspect states.



---

🔹 4. Runnable vs Callable vs Future

Interface	Return Type	Exception	Used With

Runnable	void	❌	Thread, Executor
Callable	T	✅	ExecutorService
Future	T	✅	To retrieve results



---

🔹 5. CompletableFuture (Java 8+)

CompletableFuture.supplyAsync(() -> fetchData())
    .thenApply(data -> process(data))
    .thenAccept(System.out::println);

Chainable, async, functional.

Use thenCombine, thenCompose, exceptionally.



---

🔹 6. Fork/Join Framework & Parallel Streams

ForkJoinPool: Divide tasks recursively, use invoke().

parallelStream(): For CPU-bound collection operations.

Avoid shared mutable state!



---

🔹 7. Thread Coordination

Tool	Use Case

wait/notify	Low-level coordination
CountDownLatch	One-time countdown
CyclicBarrier	Reusable sync barrier
Phaser	Multi-phase task coordination



---

🔹 8. ThreadPoolExecutor

ThreadPoolExecutor executor = new ThreadPoolExecutor(
  corePoolSize, maxPoolSize, keepAliveTime, unit, queue);

Customize thread pool size, queue capacity, and policies.

Use metrics: getActiveCount(), getQueue().size().



---

🔹 9. Virtual Threads (Java 21)

Lightweight threads (Project Loom).

Use: Executors.newVirtualThreadPerTaskExecutor()

Ideal for IO-bound tasks, massive concurrency.


try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> System.out.println("Hello from virtual thread"));
}


---

🔹 10. Pitfalls & Debugging

Issue	Prevention / Tools

Deadlock	Lock ordering, tryLock()
Livelock	Use backoff strategies
Race Condition	Atomic operations, synchronization
Thread Leaks	Shutdown executors, use timeouts
Tools	jstack, jconsole, VisualVM



---

Would you like this as a Google Doc or Markdown file you can import into Notion or share with others?

Here are Java multithreading questions and answers often asked in JPMorgan Chase VP-level Zoom interviews, with deep focus on concurrency, thread-safety, design, and real-world scenarios.


---

✅ 1. Design a Thread-safe LRU Cache

Q: Design and implement a thread-safe Least Recently Used (LRU) cache in Java.

🔑 Key Concepts:

LinkedHashMap or custom Doubly Linked List

Synchronization or ReentrantLock

O(1) get/put operations

Use Collections.synchronizedMap() is not enough at VP level


✅ Sample Solution:

public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final DoublyLinkedList<K, V> dll;
    private final ReentrantLock lock = new ReentrantLock();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.dll = new DoublyLinkedList<>();
    }

    public V get(K key) {
        lock.lock();
        try {
            if (!map.containsKey(key)) return null;
            Node<K, V> node = map.get(key);
            dll.moveToFront(node);
            return node.value;
        } finally {
            lock.unlock();
        }
    }

    public void put(K key, V value) {
        lock.lock();
        try {
            if (map.containsKey(key)) {
                Node<K, V> node = map.get(key);
                node.value = value;
                dll.moveToFront(node);
            } else {
                if (map.size() == capacity) {
                    Node<K, V> lru = dll.removeLast();
                    map.remove(lru.key);
                }
                Node<K, V> newNode = new Node<>(key, value);
                dll.addFirst(newNode);
                map.put(key, newNode);
            }
        } finally {
            lock.unlock();
        }
    }

    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev, next;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    static class DoublyLinkedList<K, V> {
        Node<K, V> head = new Node<>(null, null);
        Node<K, V> tail = new Node<>(null, null);

        DoublyLinkedList() {
            head.next = tail;
            tail.prev = head;
        }

        void addFirst(Node<K, V> node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        void moveToFront(Node<K, V> node) {
            remove(node);
            addFirst(node);
        }

        void remove(Node<K, V> node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        Node<K, V> removeLast() {
            if (tail.prev == head) return null;
            Node<K, V> last = tail.prev;
            remove(last);
            return last;
        }
    }
}


---

✅ 2. Implement a Custom Thread Pool Executor

Q: Can you implement a simplified custom thread pool?

🔑 Concepts:

Fixed number of worker threads

Blocking queue

Graceful shutdown


✅ Sample Design (Outline):

public class CustomThreadPool {
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final List<Worker> workers = new ArrayList<>();
    private volatile boolean isStopped = false;

    public CustomThreadPool(int numThreads) {
        for (int i = 0; i < numThreads; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            new Thread(worker).start();
        }
    }

    public void submit(Runnable task) throws IllegalStateException {
        if (isStopped) throw new IllegalStateException("Thread pool is stopped");
        taskQueue.offer(task);
    }

    public void shutdown() {
        isStopped = true;
        for (Worker w : workers) {
            w.stop();
        }
    }

    private class Worker implements Runnable {
        private volatile boolean running = true;

        public void run() {
            while (running) {
                try {
                    Runnable task = taskQueue.poll(1, TimeUnit.SECONDS);
                    if (task != null) task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void stop() {
            running = false;
        }
    }
}


---

✅ 3. CompletableFuture Interview Concepts

Q: What operations are supported in CompletableFuture?

🔑 Key APIs to Mention:

Method	Description

thenApply()	transform result
thenAccept()	consume result
thenCompose()	flatMap chaining
thenCombine()	combine two futures
allOf()/anyOf()	wait for all or any to complete
exceptionally()	handle exceptions
completeExceptionally()	force fail a future


✅ Sample Use:

CompletableFuture.supplyAsync(() -> "JPMC")
    .thenApply(name -> name + " Interview")
    .thenAccept(System.out::println);


---

✅ 4. High Throughput Async Processing

Q: How do you design a high-throughput async task handler in Java?

✅ Answer Summary:

Use ExecutorService or ForkJoinPool

Apply CompletableFuture for async chaining

Use BlockingQueue for task buffering

Backpressure via Semaphore or RateLimiter

Ensure proper error handling and retries



---

✅ 5. Thread Safety: How do you make a class thread-safe?

✅ Answer:

Use synchronized blocks or methods

Use java.util.concurrent classes like ConcurrentHashMap

Use ReentrantLock for fine-grained locking

Avoid mutable shared state

Use thread-safe data structures

Make class immutable



---

✅ 6. Deadlock Scenario

Q: What is deadlock? How do you avoid it?

✅ Answer:

Occurs when multiple threads wait on each other to release resources

Avoid by:

Lock ordering

Using try-lock with timeout

Using one lock per thread

Using java.util.concurrent where possible




---

✅ Summary Table

Topic	Asked in JPMorgan?	Must Know

LRU with Threads	✅	✅
Custom ThreadPool	✅	✅
CompletableFuture Ops	✅	✅
Producer-Consumer with BlockingQ	✅	✅
Deadlock & Avoidance	✅	✅



---

Would you like me to simulate mock interview Q&A for these or convert into a .md file for practice?

