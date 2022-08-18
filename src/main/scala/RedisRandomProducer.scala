import redis.clients.jedis.{HostAndPort, Jedis, JedisClusterCommand, StreamEntryID}

import java.util
import java.util.{HashMap, Random}

object RedisRandomProducer extends App {

  val redisConnection = new Jedis(new HostAndPort(Constants.HOST, Constants.PORT))

  val hash = new util.HashMap[String, String]()
  while(true){
    Thread.sleep(1000)
    hash.clear()
    val id = new Random().nextInt(200).toString
    val name = new Random().nextLong().toString

    println("Inserting...")
    println(s"Id: ${id} Name: ${name}")
    hash.put("id", id)
    hash.put("name", name)
    redisConnection.xadd(Constants.stream, StreamEntryID.NEW_ENTRY, hash)
  }

}
