package com.arya.simple

object Constants {
  val HOST = "localhost"
  val PORT = 6379
  val stream = "mystream"

  val pageCountStream = "pagecount1"

  val sparkDir = "file:/D:/personal/pagecount/"
  val sparkFinal: String = sparkDir + "final"
  val sparkInter: String = sparkDir + "inter"
  val sparkStream: String = sparkDir + "stream"

}
