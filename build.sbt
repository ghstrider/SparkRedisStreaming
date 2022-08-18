ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.8"

lazy val root = (project in file("."))
  .settings(
    name := "SparkRedisStreaming"
).settings(
  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % "3.0.1",
    "org.apache.spark" %% "spark-sql" % "3.0.1",
    "com.redislabs" %% "spark-redis" % "3.0.0"
  )
)
