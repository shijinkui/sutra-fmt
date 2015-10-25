package net.pusuo.fmt

import org.slf4j.LoggerFactory

import scala.xml.{NodeSeq, XML}

/**
 * xml parse
 * Created by sjk on 10/20/15.
 */
object XmlParse {

  private val logger = LoggerFactory.getLogger(XmlParse.getClass)

  def parse(xmlFile: String): SutraEntry = {
    try {
      val xml = XML.loadFile(xmlFile)
      //  TEI  xml:id="T85n2920"
      val fileName = xml.attributes.asAttrMap.apply("xml:id")
      val header = parseHeader(xml.\\("teiHeader"), fileName)
      val body = parseBody(xml.\\("body"))
      val back = parseBack(xml.\\("back"))
      SutraEntry(header, body, null)
    } catch {
      case e: Throwable =>
        logger.error("parse xmlFile err, " + xmlFile, e)
        null
    }
  }

  private def parseHeader(head: NodeSeq, fileName: String): RawHeader = {
    val fullTitle = head.\\("title").text
    val author = head.\\("author").text
    val extent = head.\\("extent").text
    val sourceFrom = head.\\("sourceDesc").\\("bibl").text
    val publicationData = head.\\("publicationStmt").\\("date").text.replace("$Date: ", "").replace("$", "").trim
    val distributor = head.\\("distributor").\("name").text
    val providerDesc = head.\\("encodingDesc").\("projectDesc").text.split("\n").map(_.trim).mkString("\n")
    val revisionDesc = head.\\("revisionDesc").\("change").map(f => {
      trim(f.attribute("when").getOrElse("") + " " + trim(f.nonEmptyChildren.map(_.text).mkString(" ")))
    }).toArray

    new RawHeader(
      fileName,
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
    val juanNumber = body.\\("mulu").headOption match {
      case Some(mulu) =>
        val n = mulu.attribute("n") match {
          case Some(t) => t.text
          case _ => ""
        }
        n + mulu.attribute("type").get.text
      case _ => ""
    }

    val title = body.\\("jhead").headOption match {
      case Some(s) => s.text.trim
      case _ => ""
    }
    val author = body.\\("byline").text.trim

    //  todo 偈诵 特殊处理, 缩紧
    val p = body.\\("div").\\("p").map(p => {
      trim(p.text.trim).replace("\n", "")
    }).toArray

    new RawBody(docNumber, juanNumber, title, author, p)
  }

  /**
   * 解析后记
   * @param back 文本
   */
  private def parseBack(back: NodeSeq): Unit = {
    back.\\("div").filter(_.attribute("type").get.text == "apparatus").map(f => {
      val apps = f.nonEmptyChildren.map(p => {
        App(p.\\("lem").text, p.\\("space").\@("quantity"))
      }).toArray

      AppRevision(
        f.\\("head").text,
        f.attribute("type").head.text,
        apps
      )
    }).toArray
  }


  private def trim(str: String): String = {
    str.split("\n").filter(_.nonEmpty).map(_.trim).mkString(" ")
  }
}

case class AppRevision(head: String, tpe: String, apps: Array[App])

case class App(lem: String, rdg: String)

case class NoteRevision(head: String, tpe: String, note: String, target: String)

