package net.pusuo.fmt

import scala.xml.{NodeSeq, XML}

/**
 * xml parse
 * Created by sjk on 10/20/15.
 */
object XmlParse {
  def parse(xml: String): Unit = {

  }

  def main(args: Array[String]) {
    val s = "/Users/sjk/workspace/github/sutras/sutra-transformer/src/main/resources/T03n0163.xml"
    val xml = XML.loadFile(s)
    //  TEI  xml:id="T85n2920"
    val fileName = xml.attributes.asAttrMap.apply("xml:id")

    val head = parseHeader(xml.\\("teiHeader"))
    val body = parseBody(xml.\\("body"))
    val back = parseBack(xml.\\("back"))
  }

  private def parseHeader(head: NodeSeq): RawHeader = {
    val fullTitle = head.\\("title").text
    val author = head.\\("author").text
    val extent = head.\\("extent").text
    val sourceFrom = head.\\("sourceDesc").\\("bibl").text
    val publicationData = head.\\("publicationStmt").\\("date").text.replace("$Date: ", "").replace("$", "").trim
    val distributor = head.\\("distributor").\("name").text
    val providerDesc = head.\\("encodingDesc").\("projectDesc").text.split("\n").map(_.trim).mkString("\n")
    val revisionDesc = head.\\("revisionDesc").\("change").map(f => {
      trim(f.attribute("when").getOrElse("") + " " + f.nonEmptyChildren.map(_.text).mkString(" "))
    }).toArray

    RawHeader(
      fullTitle,
      author,
      extent,
      sourceFrom,
      publicationData,
      distributor,
      trim(providerDesc),
      revisionDesc
    )
  }


  private def parseBody(body: NodeSeq): RawBody = {
    val docNumber = body.\\("docNumber").text.trim
    val mulu = body.\\("mulu").head
    val juanNumber = mulu.attribute("n").get.text + mulu.attribute("type").get.text
    val title = body.\\("jhead").head.text.trim
    val author = body.\\("byline").text.trim
    val p = body.\\("div").\\("p").map(p => {
      trim(p.text.trim).replace("\n", "")
    }).toArray

    RawBody(docNumber, juanNumber, title, author, p)
  }

  /**
   * 解析后记
   * @param back 文本
   */
  private def parseBack(back: NodeSeq): Unit = {
    val tpe = back.\\("div").filter(_.attribute("type").get.text == "apparatus").map(f => {
      val apps = f.nonEmptyChildren.map(p => {
        App(p.\\("lem").text, p.\\("space").\@("quantity"))
      }).toArray

      AppRevision(
        f.\\("head").text,
        f.attribute("type").head.text,
        apps
      )
    })
  }


  private def trim(str: String): String = {
    str.split("\n").filter(_.nonEmpty).map(_.trim).mkString("\n")
  }
}

case class AppRevision(head: String, tpe: String, apps: Array[App])

case class App(lem: String, rdg: String)

case class NoteRevision(head: String, tpe: String, note: String, target: String)

