# Apache Spark: Data Cleaning, Transformation, Actions, and Optimization

## ✅ 1. Data Cleaning Functions in Spark

| Task             | Function             | Example                                                      |
|------------------|----------------------|--------------------------------------------------------------|
| Drop nulls       | `na.drop()`          | `df.na.drop()`                                               |
| Fill nulls       | `na.fill()`          | `df.na.fill(0)`                                              |
| Replace values   | `na.replace()`       | `df.na.replace("status", Map("N/A" -> "unknown"))`           |
| Remove duplicates| `dropDuplicates()`   | `df.dropDuplicates()` or `df.dropDuplicates(Seq("id", "ts"))`|
| Trim spaces      | `trim()`             | `df.withColumn("name", trim($"name"))`                      |
| Filter rows      | `filter()` / `where()`| `df.filter($"age" > 30)`                                     |
| Remove bad data  | `rlike`, `isNull`    | `df.filter($"email".rlike(".*@.*"))`                         |

---

## ✅ 2. Data Transformation Functions

| Task              | Function                 | Example                                                    |
|-------------------|--------------------------|------------------------------------------------------------|
| Select columns     | `select()`               | `df.select("name", "age")`                                 |
| Rename column      | `withColumnRenamed()`    | `df.withColumnRenamed("dob", "date_of_birth")`             |
| Add/update column  | `withColumn()`           | `df.withColumn("bonus", $"salary" * 0.1)`                  |
| Conditional logic  | `when`, `otherwise`      | `df.withColumn("grade", when($"score" > 90, "A"))`         |
| Explode arrays     | `explode()`              | `df.select($"id", explode($"tags"))`                       |
| Aggregation        | `groupBy().agg()`        | `df.groupBy("dept").agg(avg("salary"))`                    |
| Pivoting           | `pivot()`                | `df.groupBy("name").pivot("month").sum("sales")`           |
| Window functions   | `over()` + `rank()`, etc | Use for analytics functions like row_number, rank, etc.    |

---

## ✅ 3. Spark Actions (Triggers Execution)

| Action         | Purpose                               |
|----------------|----------------------------------------|
| `show()`       | Displays the first N rows              |
| `collect()`    | Returns all rows to the driver         |
| `count()`      | Returns number of rows                 |
| `take(n)`      | Returns first n rows                   |
| `first()`      | Returns the first row                  |
| `foreach()`    | Applies a function to each row         |
| `write()`      | Writes data to file/table/external DB  |

---

## ✅ 4. Spark Performance Optimization Functions / Tips

| Area               | Function / Tip               | Description                                |
|--------------------|------------------------------|--------------------------------------------|
| Caching            | `cache()`, `persist()`       | Avoid recomputation of reused DataFrames   |
| Partitioning       | `repartition()`, `coalesce()`| Tune number of partitions                  |
| Join Optimization  | `broadcast()`                | Avoid shuffle with small lookup tables     |
| Column pruning     | Use `select()` smartly       | Read only required columns                 |
| Predicate pushdown | Applies with Parquet/ORC     | Push filters down to file scan level       |
| File formats       | Use Parquet/Delta/ORC        | Columnar, compressed, optimized            |
| Shuffle tuning     | `spark.sql.shuffle.partitions` | Tune number of shuffle partitions        |

---

## ✅ 5. End-to-End Example Pipeline (Scala)

```scala
val rawDF = spark.read.option("header", "true").csv("input.csv")

val cleanedDF = rawDF
  .na.fill("unknown", Seq("name", "country"))
  .na.drop(Seq("id"))
  .withColumn("email", lower(trim($"email")))
  .withColumn("signup_year", year(to_date($"signup_date", "yyyy-MM-dd")))
  .dropDuplicates(Seq("id"))

val aggDF = cleanedDF
  .groupBy("signup_year", "country")
  .agg(count("*").as("user_count"))

val resultDF = aggDF.cache()

resultDF
  .orderBy($"user_count".desc)
  .show(10)

resultDF.write.mode("overwrite").parquet("cleaned_output/")