package example.service

import java.util.concurrent.atomic.AtomicInteger

class ServiceStatus {
  val pendingRequest = new AtomicInteger(0)

  var isRunning = true
}
