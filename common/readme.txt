hystrix :
  线程 信号量隔离
  降级
  熔断 与 恢复
  请求缓存 请求合并等

  Hystrix is designed to do the following:

  Give protection from and control over latency and failure from dependencies accessed (typically over the network) via third-party client libraries.
  Stop cascading failures in a complex distributed system.
  Fail fast and rapidly recover.
  Fallback and gracefully degrade when possible.
  Enable near real-time monitoring, alerting, and operational control.


  Hystrix works by:

  Preventing any single dependency from using up all container (such as Tomcat) user threads.
  Shedding load and failing fast instead of queueing.
  Providing fallbacks wherever feasible to protect users from failure.
  Using isolation techniques (such as bulkhead, swimlane, and circuit breaker patterns) to limit the impact of any one dependency.
  Optimizing for time-to-discovery through near real-time metrics, monitoring, and alerting
  Optimizing for time-to-recovery by means of low latency propagation of configuration changes and support for dynamic property changes in most aspects of Hystrix, which allows you to make real-time operational modifications with low latency feedback loops.
  Protecting against failures in the entire dependency client execution, not just in the network traffic.

  Hystrix does this by:

  Wrapping all calls to external systems (or “dependencies”) in a HystrixCommand or HystrixObservableCommand object which typically executes within a separate thread (this is an example of the command pattern).
  Timing-out calls that take longer than thresholds you define. There is a default, but for most dependencies you custom-set these timeouts by means of “properties” so that they are slightly higher than the measured 99.5th percentile performance for each dependency.
  Maintaining a small thread-pool (or semaphore) for each dependency; if it becomes full, requests destined for that dependency will be immediately rejected instead of queued up.
  Measuring successes, failures (exceptions thrown by client), timeouts, and thread rejections.
  Tripping a circuit-breaker to stop all requests to a particular service for a period of time, either manually or automatically if the error percentage for the service passes a threshold.
  Performing fallback logic when a request fails, is rejected, times-out, or short-circuits.
  Monitoring metrics and configuration changes in near real-time.