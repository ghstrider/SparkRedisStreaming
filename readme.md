## Redis Streaming
This project shows spark streaming capability with Redis

### Producer
`RedisRandomProducer` is a random data producer.
It produces data to stream name `mystream`

### Consumer
`RedisSparkStreamingExample` is a consumer of random data.
It consumes data from stream name `mystream`

### Configuration
`Constants` can be used to change the `host`, `port` and `stream` name.

### Requirements
- Redis

### WebPage Visit Example

In the package `pagecount` lies the example of streaming redis event of user page visit and counting the number of user visit.
`Constant` object in package `simple` can be used to configure few variables.

### Docker Redis Settings 
###### if redis is not locally available

` docker run --name spark-redis-test  -p 6379:6379 -d redis`
