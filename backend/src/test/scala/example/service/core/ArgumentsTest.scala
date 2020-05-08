package example.service.core

import example.service.utils.Arguments

class ArgumentsTest extends org.scalatest.FreeSpec {
  "parseArgs" - {
    "should work for empty array" in {
      assert(Arguments.parse(Array.empty).size === 0)
    }

    "should work with multiple argument" in {
      val args = "--env dev test --version 1.0".split(' ')
      val arguments = Arguments.parse(args)
      assert(arguments.valuesOf("env") === "dev" :: "test" :: Nil)
      assert(arguments.valuesOf("version") === "1.0" :: Nil)
    }
  }
}
