package com.arya.pagecount

import redis.clients.jedis.{HostAndPort, Jedis, StreamEntryID}

import java.util
import java.util.Random
import com.arya.simple.Constants
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, Dataset, ForeachWriter, Row, SparkSession}
import org.apache.spark.sql.streaming.Trigger.ProcessingTime
import org.apache.spark.sql.types.{StringType, StructField, StructType}

object SparkStreamingPageCount {

  def eachBatch(dataFrame: DataFrame, batchId: Long): Unit = {
    dataFrame.show()
  }

  def main(args: Array[String]): Unit = {
    val logger = Logger.getLogger(classOf[Nothing].getName)
    logger.setLevel(Level.DEBUG)

    val spark = SparkSession.builder()
      .master("local[1]")
      .config("spark.redis.host", Constants.HOST)
      .config("spark.redis.port", Constants.PORT)
      .getOrCreate()

    import spark.implicits._

    val pageCountStream = spark.readStream.format("redis")
      .option("stream.keys", Constants.pageCountStream)
      .schema(StructType
        .apply(
          Array(
            StructField("uservisit", StringType)
          )
        )
      )
          .load()
      .map(r => {
      val ar = r.getString(0).split("\\|")
      (ar(0), ar(1), ar(2))
    }
    ).toDF("id", "user", "page")

    val query = pageCountStream.writeStream
      .format("csv")
      .trigger(ProcessingTime("10 seconds"))
      .option("checkpointLocation", "D:\\personal\\pagecount\\checkpoint")
      .option("path", "D:\\personal\\pagecount\\stream")
      .outputMode("append")
      .foreachBatch{(df: DataFrame, batchId: Long) => SparkPageCount.pageCount(df, spark)}
      .queryName("Timeline")
      .start()

//    val query = pageCountStream.writeStream.format("console").start()
    query.awaitTermination()
  }

}
