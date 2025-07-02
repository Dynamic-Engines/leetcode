
# 🔄 Spark Structured Streaming: Joins & End-to-End Pipeline

---

## 🧠 What is Structured Streaming?

Structured Streaming is Apache Spark’s scalable, high-level, fault-tolerant stream processing engine. It allows you to express streaming logic declaratively, just like batch queries.

---

## 🔁 Supported Join Types in Structured Streaming

| Join Type          | Stream-Static Join | Stream-Stream Join | Notes                                |
|--------------------|--------------------|---------------------|--------------------------------------|
| Inner Join         | ✅ Supported        | ✅ Supported         | Most commonly used                   |
| Left Outer Join    | ❌ Not Supported    | ✅ Supported         | Requires watermarking                |
| Right Outer Join   | ❌ Not Supported    | ❌ Not Supported     | Not allowed in streaming             |
| Full Outer Join    | ❌ Not Supported    | ❌ Not Supported     | Not allowed in streaming             |

---

## 📌 1. Stream-to-Static Join

**✅ Allowed: Only Inner Join**

Used when enriching streaming data with small static reference tables.

```scala
val enriched = ordersStream.join(
  broadcast(productsDF),
  "product_id"
)
```

- `productsDF` should be small enough to broadcast
- Used for product, customer, or region lookups

---

## 📌 2. Stream-to-Stream Inner Join

**✅ Requires Watermarking + Event-Time Condition**

```scala
val ordersW = ordersStream.withWatermark("timestamp", "10 minutes")
val paymentsW = paymentsStream.withWatermark("timestamp", "10 minutes")

val joined = ordersW.join(
  paymentsW,
  expr("""
    ordersW.order_id = paymentsW.order_id AND
    paymentsW.timestamp BETWEEN ordersW.timestamp AND ordersW.timestamp + interval 15 minutes
  """)
)
```

- Spark holds state until watermark expires
- Only matched pairs are emitted

---

## 📌 3. Stream-to-Stream Left Outer Join

**✅ Also requires watermarking on both sides**

```scala
val joinedLeft = ordersW.join(
  paymentsW,
  expr("""
    ordersW.order_id = paymentsW.order_id AND
    paymentsW.timestamp BETWEEN ordersW.timestamp AND ordersW.timestamp + interval 15 minutes
  """),
  "leftOuter"
)
```

- Emits unmatched left-side rows with nulls on right
- Useful when right events may be delayed or missing

---

## ⚙️ Full End-to-End Streaming Pipeline Example

**Goal:**  
- Ingest Kafka `orders` and `payments` streams  
- Enrich with static product data (`products.csv`)  
- Perform stream-to-stream join  
- Write to sink (console or Parquet)  

---

### 🧾 Step 1: Define Schemas

```scala
val orderSchema = new StructType()
  .add("order_id", StringType)
  .add("product_id", StringType)
  .add("user_id", StringType)
  .add("timestamp", TimestampType)

val paymentSchema = new StructType()
  .add("order_id", StringType)
  .add("amount", DoubleType)
  .add("timestamp", TimestampType)
```

---

### 📥 Step 2: Read Kafka Streams

```scala
val ordersStream = spark.readStream
  .format("kafka")
  .option("subscribe", "orders")
  .option("kafka.bootstrap.servers", "localhost:9092")
  .load()
  .selectExpr("CAST(value AS STRING)")
  .select(from_json($"value", orderSchema).as("data"))
  .select("data.*")

val paymentsStream = spark.readStream
  .format("kafka")
  .option("subscribe", "payments")
  .option("kafka.bootstrap.servers", "localhost:9092")
  .load()
  .selectExpr("CAST(value AS STRING)")
  .select(from_json($"value", paymentSchema).as("data"))
  .select("data.*")
```

---

### 📚 Step 3: Load Static Product Table

```scala
val productsDF = spark.read
  .option("header", "true")
  .csv("products.csv") // Columns: product_id, product_name, category
```

---

### 🔗 Step 4: Stream-to-Static Join (Enrich Orders)

```scala
val enrichedOrders = ordersStream.join(
  broadcast(productsDF),
  "product_id"
)
```

---

### 🔁 Step 5: Stream-to-Stream Join (Orders + Payments)

```scala
val ordersWithWatermark = enrichedOrders.withWatermark("timestamp", "10 minutes")
val paymentsWithWatermark = paymentsStream.withWatermark("timestamp", "10 minutes")

val fullyEnriched = ordersWithWatermark.join(
  paymentsWithWatermark,
  expr("""
    ordersWithWatermark.order_id = paymentsWithWatermark.order_id AND
    paymentsWithWatermark.timestamp BETWEEN ordersWithWatermark.timestamp AND ordersWithWatermark.timestamp + interval 15 minutes
  """)
)
```

---

### 💾 Step 6: Write to Sink

#### Option A: Console (for debugging)

```scala
fullyEnriched.writeStream
  .format("console")
  .outputMode("append")
  .option("truncate", false)
  .start()
  .awaitTermination()
```

#### Option B: Parquet (for production)

```scala
fullyEnriched.writeStream
  .format("parquet")
  .option("path", "output/enriched_orders/")
  .option("checkpointLocation", "output/checkpoints/")
  .outputMode("append")
  .start()
```

---

## 🧠 Key Concepts

- **Watermarking**:
  - Required for stream-stream joins to control state retention
  - Late data older than watermark is dropped

- **Join Conditions**:
  - Must use event-time columns
  - Must bound time range (`BETWEEN start AND end`)

- **Output Modes**:
  - `append` – for insert-only data (joins, logs)
  - `update` – for aggregations with watermark
  - `complete` – for full re-materialized tables

- **Checkpointing**:
  - Required for recovery and exactly-once guarantees

---

## ✅ Best Practices

- Always define watermarks for stream-stream joins
- Use broadcast joins for stream-static lookups
- Monitor state size in Spark UI or logs
- Avoid full outer joins — not supported
- Use Delta/Parquet sinks for analytics

---

## 🧪 Summary

- ✅ **Stream-to-static**: inner join only, use broadcast
- ✅ **Stream-to-stream**: inner and left outer joins allowed
- ❌ **Right/Full outer joins**: not supported in streaming
- ✅ **Watermarks**: mandatory to evict state in joins
- ✅ Use **append mode** for stream joins with checkpoints

---

✅ You're good to copy this entire block into a .md file or Markdown editor.

Let me know if you'd like:

✅ PySpark version

✅ Kafka + Delta + BigQuery pipeline

✅ Real interview questions based on this scenario (JPMC-style)

✅ Streaming aggregations (like windowed counts or top-N)


