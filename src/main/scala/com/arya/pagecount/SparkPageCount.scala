package com.arya.pagecount

import com.arya.simple.Constants
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

object SparkPageCount {
  def pageCount(df: DataFrame, spark: SparkSession): Unit = {
    // Read older written data
    val finalDf = spark.read.schema(StructType.apply(Array(
      StructField("user", StringType),
      StructField("page", StringType),
      StructField("count", StringType)
    ))).csv(Constants.sparkFinal)

    df.persist()
    // Read the new delta
    val delta = df.groupBy("user", "page").count()

    // merge the data and write to intermediate location
    val inter = finalDf.union(delta).groupBy("user", "page").count()
    inter.coalesce(1).write.mode("overwrite").format("csv").csv(Constants.sparkInter)
    df.unpersist()


    // read from intermediate location and write to final location
    spark.read
      .schema(
        StructType.apply(
          Array(
            StructField("user", StringType),
            StructField("page", StringType),
            StructField("count", StringType)
          )
        )
      )
      .csv(Constants.sparkInter).coalesce(1).write.mode("overwrite").format("csv")
      .csv(Constants.sparkFinal)
  }

}
