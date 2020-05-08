package example.service.core

trait LifeCycleAware {
  /**
    * The priority in descending
    * Means, bigger priority instance will be called first
    */
  def priority: Int = 0
  def onStart(): Unit
  def onStop(): Unit
}
