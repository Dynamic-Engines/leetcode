# 🔄 Spark Structured Streaming: Joins & End-to-End Pipeline

## 🧠 What is Structured Streaming?

Structured Streaming is Apache Spark’s high-level, declarative, and fault-tolerant stream processing engine. It allows you to build streaming applications using the same API as batch DataFrames.

---

## 🔁 Join Types in Structured Streaming

| Join Type          | Stream-Static Join | Stream-Stream Join | Notes                                |
|--------------------|--------------------|---------------------|--------------------------------------|
| Inner Join         | ✅ Supported        | ✅ Supported         | Most commonly used                   |
| Left Outer Join    | ❌ Not Supported    | ✅ Supported         | Requires watermarking                |
| Right Outer Join   | ❌ Not Supported    | ❌ Not Supported     | Not allowed in streaming             |
| Full Outer Join    | ❌ Not Supported    | ❌ Not Supported     | Not allowed in streaming             |

---

## 📌 Stream-to-Static Join

> ✅ Only **inner join** is supported in stream-static joins.

```scala
val enriched = ordersStream.join(
  broadcast(productsDF),
  "product_id"
)
```

- Used to enrich streaming data with small static tables.
- Static DataFrame must be small enough for broadcasting.

---

## 📌 Stream-to-Stream Inner Join

> ✅ Requires watermarking and event-time-based join condition.

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

---

## 📌 Stream-to-Stream Left Outer Join

> ✅ Requires watermarking on both sides.

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

---

## ⚙️ End-to-End Spark Streaming Pipeline

**Goal:**  
- Stream `orders` and `payments` from Kafka  
- Enrich orders with static `products` table  
- Join orders and payments  
- Write enriched data to sink  

---

### Step 1: Define Schemas

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

### Step 2: Read Orders and Payments Streams from Kafka

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

### Step 3: Read Static Product Data

```scala
val productsDF = spark.read
  .option("header", "true")
  .csv("products.csv") // Columns: product