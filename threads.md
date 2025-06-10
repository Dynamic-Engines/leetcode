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

