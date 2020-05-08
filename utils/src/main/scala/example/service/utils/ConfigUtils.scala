package example.service.utils

import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions, ConfigResolveOptions}

object ConfigUtils {
  def loadConfig(arguments: Arguments): Config = {
    val pathPrefix = arguments.valueOf("config-path-prefix")

    def resourcePath(resourceName: String): String = pathPrefix.map(_ + resourceName).getOrElse(resourceName)

    val defaultConfig = ConfigFactory.load(resourcePath("application"))
    arguments.envs.foldLeft(defaultConfig) { (config, env) =>
      val evnConfig = ConfigFactory.load(
        resourcePath(s"application-$env"),
        ConfigParseOptions.defaults().setAllowMissing(true),
        ConfigResolveOptions.defaults()
      )
      if (evnConfig.isEmpty) config else evnConfig.withFallback(config)
    }
  }
}
