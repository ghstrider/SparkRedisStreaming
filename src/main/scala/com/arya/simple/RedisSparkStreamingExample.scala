package com.arya.simple

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StringType, StructField, StructType}

object RedisSparkStreamingExample {
  def main(args: Array[String]): Unit = {
    val logger = Logger.getLogger(classOf[Nothing].getName)
    logger.setLevel(Level.DEBUG)

    val spark = SparkSession.builder()
      .master("local[*]")
      .config("spark.redis.host", Constants.HOST)
      .config("spark.redis.port", Constants.PORT)
      .getOrCreate()

    val mystream = spark.readStream.format("redis")
      .option("stream.keys", Constants.stream)
      .schema(StructType
        .apply(
          Array(
            StructField("id", StringType),
            StructField("name", StringType)
          )
        )
      ).load()

    val query = mystream.writeStream.format("console").start()

    query.awaitTermination()
  }
}
