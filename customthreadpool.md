Absolutely. Let's redesign your CustomThreadPool based on SOLID principles — making it modular, extensible, and clean, just like a production-grade system you'd design at a VP-level engineering role.


---

🔑 SOLID Principles Recap (as applied to thread pool):

Principle	Application

Single Responsibility	Each class handles one job (worker runs tasks, task queue stores, pool manages threads)
Open/Closed	Easily extend behavior (e.g., retry, metrics) without modifying existing classes
Liskov Substitution	Allow interface-based use of components (e.g., TaskHandler)
Interface Segregation	Use minimal, focused interfaces (e.g., for task execution)
Dependency Inversion	High-level modules depend on abstractions, not concrete classes



---

✅ Final Design Overview

📦 Components:

1. TaskQueue – wraps a BlockingQueue<Runnable>


2. TaskHandler – interface for executing tasks


3. Worker – pulls from queue and delegates to TaskHandler


4. CustomThreadPool – manages lifecycle and coordination




---

📁 Folder-like Structure

com.example.threadpool
├── CustomThreadPool.java
├── TaskQueue.java
├── TaskHandler.java
├── Worker.java
└── Main.java (for demo)


---

✅ Code: SOLID-Based Custom Thread Pool

TaskHandler.java (Interface)

public interface TaskHandler {
    void handle(Runnable task);
}


---

DefaultTaskHandler.java

public class DefaultTaskHandler implements TaskHandler {
    @Override
    public void handle(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            System.err.println("Task failed: " + e.getMessage());
        }
    }
}


---

TaskQueue.java

import java.util.concurrent.*;

public class TaskQueue {
    private final BlockingQueue<Runnable> queue;

    public TaskQueue(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    public boolean submit(Runnable task) {
        return queue.offer(task);
    }

    public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}


---

Worker.java

public class Worker extends Thread {
    private final TaskQueue taskQueue;
    private final TaskHandler taskHandler;
    private volatile boolean running = true;

    public Worker(TaskQueue taskQueue, TaskHandler taskHandler) {
        this.taskQueue = taskQueue;
        this.taskHandler = taskHandler;
    }

    public void run() {
        while (running || !taskQueue.isEmpty()) {
            try {
                Runnable task = taskQueue.poll(1, java.util.concurrent.TimeUnit.SECONDS);
                if (task != null) {
                    taskHandler.handle(task);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopWorker() {
        running = false;
        this.interrupt();
    }
}


---

CustomThreadPool.java

import java.util.*;

public class CustomThreadPool {
    private final TaskQueue taskQueue;
    private final List<Worker> workers = new ArrayList<>();
    private volatile boolean isShutdown = false;

    public CustomThreadPool(int numThreads, int queueCapacity) {
        this.taskQueue = new TaskQueue(queueCapacity);
        TaskHandler handler = new DefaultTaskHandler();

        for (int i = 0; i < numThreads; i++) {
            Worker worker = new Worker(taskQueue, handler);
            workers.add(worker);
            worker.start();
        }
    }

    public synchronized void submit(Runnable task) {
        if (isShutdown) throw new IllegalStateException("Pool is shutdown");
        boolean accepted = taskQueue.submit(task);
        if (!accepted) throw new RejectedExecutionException("Task queue full");
    }

    public synchronized void shutdown() {
        isShutdown = true;
        for (Worker w : workers) {
            w.stopWorker();
        }
    }

    public void awaitTermination() {
        for (Worker w : workers) {
            try {
                w.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}


---

Main.java (Usage Example)

public class Main {
    public static void main(String[] args) {
        CustomThreadPool pool = new CustomThreadPool(3, 10);

        for (int i = 0; i < 20; i++) {
            int id = i;
            pool.submit(() -> {
                System.out.println("Task " + id + " running in " + Thread.currentThread().getName());
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            });
        }

        pool.shutdown();
        pool.awaitTermination();
        System.out.println("All tasks completed.");
    }
}


---

🧠 How This Design Follows SOLID

Principle	How It's Followed

SRP	Each class has one role (queueing, handling, threading)
OCP	Swap in a new TaskHandler for retries, metrics, etc.
LSP	You can replace DefaultTaskHandler with any handler that conforms
ISP	Minimal interfaces (TaskHandler)
DIP	Worker depends on TaskHandler abstraction



---

Would you like an advanced version with:

Retry logic (RetryingTaskHandler)?

Metrics recording (MetricsTaskHandler)?

Priority task queue?


Let me know — I can plug them in modularly now.

