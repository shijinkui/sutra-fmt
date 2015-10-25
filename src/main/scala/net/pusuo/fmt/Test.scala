package net.pusuo.fmt

/**
 * test
 * Created by sjk on 10/25/15.
 */
object Test {
  def main(args: Array[String]) {
    val a = XmlParse.parse("/Users/sjk/workspace/github/sutras/sutra-fmt/src/main/resources/T03n0163.xml")
    println(a.toJson)
    println()
    println()
    println()
    println(a.toMarkDown)
  }
}
