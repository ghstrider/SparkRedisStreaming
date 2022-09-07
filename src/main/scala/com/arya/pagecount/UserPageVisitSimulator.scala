package com.arya.pagecount

import com.arya.simple.Constants
import redis.clients.jedis.{HostAndPort, Jedis, StreamEntryID}

import java.util
import java.util.Random

object UserPageVisitSimulator extends App {

  val redisConnection = new Jedis(new HostAndPort(Constants.HOST, Constants.PORT))
  case class UserVisit(userId: String, pageUrl: String)
  val hash = new util.HashMap[String, String]()
//  val userIdList = (0 until(100)).map(_.toString).toList
  val domain = List("www.xyz.com", "www.abc.com", "www.pqr.com")
  val page = List("/dashboard","/charts", "/api","/settings")
  val subpage = List("/productA", "/productB", "/productC", "/productD", "/productE", "/productF", "/productG", "/productH", "/productI")
  while(true){
    Thread.sleep(1000)
    hash.clear()
    val id = new Random().nextInt(200).toString
    val userId = new Random().nextInt(200).toString
    val pageUrl = s"${domain(randomIndex(domain.size))}${page(randomIndex(page.size))}${subpage(randomIndex(subpage.size))}"

    val userVisit = UserVisit(userId, pageUrl)
    println("Inserting...")
    println(s"Id: $id Name: $userVisit")
    hash.put("uservisit", id + "|" + userVisit.userId+"|"+userVisit.pageUrl)
    val streamEntryID = redisConnection.xadd(Constants.pageCountStream, StreamEntryID.NEW_ENTRY, hash)
    println("Insert ID: "+ streamEntryID.toString)
  }

  def randomIndex(range: Int): Int = {
    new Random().nextInt(range)
  }
}
