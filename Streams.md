Yes, Java Streams API questions are commonly asked at JPMorgan Chase VP-level interviews, especially when evaluating your fluency with modern Java functional constructs. They assess your ability to write clean, efficient, and parallelizable data-processing code using Streams, lambdas, collectors, and functional interfaces.


---

✅ Typical Java Streams Questions Asked at JPMC (with Answers)


---

1. Convert List of Objects to Map (key = id, value = name)

Q: Given a list of Employee(id, name), convert it to a Map<Integer, String>.

Map<Integer, String> map = 
    employees.stream()
             .collect(Collectors.toMap(Employee::getId, Employee::getName));

✅ Follow-up: What if IDs are duplicated?

.collect(Collectors.toMap(
    Employee::getId,
    Employee::getName,
    (name1, name2) -> name1)) // Keep first


---

2. Group Employees by Department

Q: Group Employee objects by department and return a Map<String, List<Employee>>.

Map<String, List<Employee>> deptMap =
    employees.stream()
             .collect(Collectors.groupingBy(Employee::getDepartment));


---

3. Count Employees in Each Department

Map<String, Long> deptCounts =
    employees.stream()
             .collect(Collectors.groupingBy(
                 Employee::getDepartment,
                 Collectors.counting()));


---

4. Find Highest Paid Employee per Department

Map<String, Optional<Employee>> topEarners =
    employees.stream()
             .collect(Collectors.groupingBy(
                 Employee::getDepartment,
                 Collectors.maxBy(Comparator.comparing(Employee::getSalary))));


---

5. Sort a List by Salary Descending

List<Employee> sorted =
    employees.stream()
             .sorted(Comparator.comparing(Employee::getSalary).reversed())
             .toList();


---

6. Get Names of Employees with Salary > 100k

List<String> names =
    employees.stream()
             .filter(e -> e.getSalary() > 100_000)
             .map(Employee::getName)
             .toList();


---

7. Flatten a List of Lists

Q: Given List<List<String>>, flatten it to List<String>.

List<String> flatList =
    listOfLists.stream()
               .flatMap(List::stream)
               .toList();


---

8. Parallel Stream vs Stream

Q: What's the difference between .stream() and .parallelStream()?

✅ Answer:

.stream()	.parallelStream()

Sequential execution	Divides stream into substreams
Good for I/O, small sets	Good for large CPU-bound tasks
Deterministic order	Order may change unless handled



---

9. Custom Collector for Comma-Separated Names

String joinedNames =
    employees.stream()
             .map(Employee::getName)
             .collect(Collectors.joining(", "));


---

10. Reduce Example: Total Salary

int totalSalary =
    employees.stream()
             .map(Employee::getSalary)
             .reduce(0, Integer::sum);


---

🚨 VP-Level Follow-Up Questions

How does short-circuiting work in Streams?

What's the difference between map() and flatMap()?

Is Collectors.toList() thread-safe?

What happens if we mutate source collection during a stream?

Why is reduce() not always efficient in parallel streams?



---

🧠 Tips to Prepare for Stream Questions:

Know how to chain, filter, map, group, and reduce

Practice with groupingBy, partitioningBy, joining, toMap, reducing

Understand lazy evaluation, terminal vs intermediate operations

Read about Stream pipeline optimizations and avoid stateful lambdas



---

Would you like a mock question set with coding challenges or want to simulate a Stream-heavy JPMC coding round?

