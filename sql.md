Here are the SQL questions commonly asked in JPMorgan Chase VP-level Zoom interviews, categorized by difficulty, concept, and whether coding or explanation is expected.


---

✅ 🧠 Frequently Asked SQL Questions at JPMorgan (With Answers)


---

🔹 1. Find the Second Highest Salary

Q: Get the second highest salary from a Employee table.

SELECT MAX(salary) AS second_highest_salary
FROM Employee
WHERE salary < (SELECT MAX(salary) FROM Employee);


---

🔹 2. Find Duplicate Records

Q: Find duplicate employee names in a table.

SELECT name, COUNT(*)
FROM Employee
GROUP BY name
HAVING COUNT(*) > 1;


---

🔹 3. Nth Highest Salary (Flexible)

Q: Find the Nth highest salary using DENSE_RANK.

SELECT salary
FROM (
    SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) AS rnk
    FROM Employee
) ranked
WHERE rnk = 3;  -- Change 3 to N


---

🔹 4. Employees with Greater Salary than Manager

Q: Find employees who earn more than their manager.

SELECT e.name
FROM Employee e
JOIN Employee m ON e.manager_id = m.id
WHERE e.salary > m.salary;


---

🔹 5. Department-wise Highest Salary

SELECT *
FROM (
    SELECT *,
           RANK() OVER (PARTITION BY department ORDER BY salary DESC) AS rnk
    FROM Employee
) ranked
WHERE rnk = 1;


---

🔹 6. Join 3 Tables: Employee, Department, Location

SELECT e.name, d.dept_name, l.city
FROM Employee e
JOIN Department d ON e.dept_id = d.id
JOIN Location l ON d.location_id = l.id;


---

🔹 7. Running Total

SELECT id, name, salary,
       SUM(salary) OVER (ORDER BY id) AS running_total
FROM Employee;


---

🔹 8. Pivot Monthly Sales by Product

SELECT product,
       SUM(CASE WHEN MONTH(sale_date) = 1 THEN amount ELSE 0 END) AS Jan,
       SUM(CASE WHEN MONTH(sale_date) = 2 THEN amount ELSE 0 END) AS Feb
       -- ...
FROM Sales
GROUP BY product;


---

🔹 9. Detect Gaps in ID Sequence

SELECT id + 1 AS missing_id
FROM Employee e
WHERE NOT EXISTS (
    SELECT 1 FROM Employee e2 WHERE e2.id = e.id + 1
);


---

🔹 10. Find Consecutive Login Days

SELECT user_id, login_date,
       DATEDIFF(day, login_date, 
         ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY login_date)) AS grp
FROM LoginHistory;


---

🏆 Bonus VP-Level Challenges


---

🔸 A. Find Users Who Logged in 3 Days in a Row

SELECT DISTINCT l1.user_id
FROM Login l1
JOIN Login l2 ON l1.user_id = l2.user_id AND l1.date = DATEADD(day, 1, l2.date)
JOIN Login l3 ON l2.user_id = l3.user_id AND l2.date = DATEADD(day, 1, l3.date);


---

🔸 B. Top N Customers by Transaction Volume Per Month

SELECT *
FROM (
    SELECT customer_id, MONTH(txn_date) AS month, amount,
           RANK() OVER (PARTITION BY MONTH(txn_date) ORDER BY amount DESC) AS rnk
    FROM Transactions
) ranked
WHERE rnk <= 3;


---

🔸 C. Find Employees Who Never Took Leave

SELECT e.name
FROM Employee e
LEFT JOIN Leave l ON e.id = l.emp_id
WHERE l.id IS NULL;


---

✅ Interview Format at JPMorgan:

Type	Example Question	Expectation

Writing SQL	Top 3 salaries per dept	Write optimized SQL
Debugging	Fix syntax or logic errors	Identify mistake
Optimization	How to index/filter/join faster	Explain query tuning
Explanation	Why use window vs subquery	Conceptual understanding
System scenario	Audit trail DB, fraud detection SQL patterns	Design + query



---

Would you like:

A mock SQL HackerRank-style test?

A markdown workbook for SQL prep?

Real DB schema + challenge questions (e.g., for HR, e-commerce, finance)?


