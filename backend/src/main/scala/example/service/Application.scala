//package example.service
//
//import akka.actor.ActorSystem
//import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions, ConfigResolveOptions}
//import example.service.db.DB
//import scaldi.{MutableInjectorAggregation, _}
//
//import scala.language.postfixOps
//
//class Application private (env: Option[String]) {
//  private var composeInjector: MutableInjectorAggregation = _
//
//  try {
//    val config = loadConfig()
//    composeInjector = makeupInjectors(config)
//    composeInjector.initNonLazy()
//  } catch {
//    case e: Exception =>
//      Option(composeInjector) foreach (_.destroy())
//      throw e
//  }
//
//  private def makeupInjectors(config: Config): MutableInjectorAggregation = {
//    val coreModule = new Module {
//      bind[Config] toNonLazy config
//      bind[DB] toNonLazy injected[DB] initWith (_.testConnection())
//      bind[ActorSystem] toNonLazy ActorSystemProvider.newSystem() destroyWith ActorSystemProvider.shutDownSync
//      bind[HttpProvider] toNonLazy HttpProvider.startup destroyWith (_.shutdownSync())
//    }
//    val serviceBindings = service.bindings()
//    coreModule :: serviceBindings :: TypesafeConfigInjector(config)
//  }
//
//  private def loadConfig() = env match {
//    case Some(envStr) =>
//      ConfigFactory.load(
//        s"application-$envStr",
//        ConfigParseOptions.defaults().setAllowMissing(false),
//        ConfigResolveOptions.defaults()
//      ) withFallback ConfigFactory.load()
//    case None =>
//      ConfigFactory.load()
//  }
//
//  implicit val injector: Injector = ImmutableWrapper(composeInjector)
//
//  def shutDown(): Unit = composeInjector.destroy()
//}
//
//object Application {
//  private var _instance: Application = _
//
//  def main(args: Array[String]): Unit = {
//    _instance = new Application(getArg(args, "env"))
//  }
//
//  private def getArg(args: Array[String], argName: String): Option[String] = {
//    val index = args.indexOf(s"--$argName")
//    if (index >= 0) Some(args(index + 1))
//    else None
//  }
//}
