package net.pusuo.fmt

import com.alibaba.fastjson.{JSONArray, JSONObject}
import org.slf4j.LoggerFactory

import scala.xml.{Node, NodeSeq, XML}

/**
 * xml parse
 * Created by sjk on 10/20/15.
 */
object XmlParse {

  private val logger = LoggerFactory.getLogger(XmlParse.getClass)

  def main(args: Array[String]) {
    //    val file = "/Users/sjk/workspace/github/sutras/sutra-fmt/src/main/resources/T03n0163.xml"
    val file = "/Users/sjk/workspace/github/sample-code/scala-sample/src/test/java/T85n2732.xml"
    val ret = parse(file)
    println(ret.toJson)
    println(ret.toMarkDown)
  }

  def parse(xmlFile: String): SutraEntry = {
    try {
      val xml = XML.loadFile(xmlFile)
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

  private def parseHeader(head: NodeSeq, fileName: String): RawMeta = {
    val fullTitle = head.\\("title").text
    val title = fullTitle.split(" ").last
    val author = head.\\("author").text
    val extent = head.\\("extent").text
    val sourceFrom = head.\\("sourceDesc").\("bibl").text
    val publicationData = head.\\("publicationStmt").\("date").text.replace("$Date: ", "").replace("$", "").trim
    val distributor = head.\\("distributor").\("name").text
    val providerDesc = head.\\("encodingDesc").\("projectDesc").text.split("\n").map(_.trim).mkString("\n")
    val revisionDesc = head.\\("revisionDesc").\("change").map(f => {
      trim(f.attribute("when").getOrElse("") + " " + trim(f.nonEmptyChildren.map(_.text).mkString(" ")))
    }).toArray

    new RawMeta(
      fileName,
      fullTitle,
      title,
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

    //  序言
    val xu = body.\("div").apply(f => f.\@("type") == "xu")
    val xuEntry = parseXu(xu)

    //  分/品
    val fen = body.\("div").apply(f => f.\@("type") == "fen")
    val fenEntry = parseFen(fen)

    //  未分品次的正文
    val jing = body.\("div").apply(f => f.\@("type") == "jing")
    val content = if (jing != null && jing.nonEmpty) {
      jing.map(p => {
        p.nonEmptyChildren.filter(f => f.label == "p" || f.label == "lg").map(parseSegment).toArray
      }).flatten.toArray
    } else Array.empty[String]

    val title = body.\\("jhead").headOption match {
      case Some(s) => s.text.trim
      case _ => ""
    }

    new RawBody(docNumber, juanNumber, title, content, xuEntry, fenEntry)
  }

  private def parseFen(fen: NodeSeq): Array[FenEntry] = {
    val title = fen.\\("jhead").text
    fen.map(f => {
      val head = f.\("head").text
      val mulu = f.\("mulu").text
      val content = f.nonEmptyChildren.filter(p => {
        p.label == "p" || p.label == "lg"
      }).map(parseSegment).toArray
      FenEntry(title, head, mulu, content)
    }).toArray
  }

  //  解析正文段落
  private def parseSegment(node: Node): String = {
    if (node.label == "p") {
      trim(node.text)
    } else if (node.label == "lg") {
      node.nonEmptyChildren.map(f => {
        f.label match {
          case "lb" => "\n"
          case "l" => f.text.trim + "    \n"
          case x => "  "
        }
      }.mkString).mkString
    } else {
      "========todo未识别的内容1=========" + node
    }
  }

  private def parseDiv(node: Node): DivObj = {
    val mulu = trim(node.\("mulu").text)
    val head = trim(node.\("head").text)
    val body = node.nonEmptyChildren.toArray.filter(f => {
      f.label == "p" || f.label == "lg"
    }).map(parseSegment).mkString("\n")

    DivObj(mulu, body, head)
  }

  private def parseXu(xuSeq: NodeSeq): Array[XuEntry] = {
    xuSeq.iterator.map(n => {
      //  单个序言, 可以有多个子序言
      val mulu = trim(n.\("mulu").text)

      //  string or DivObj
      val content: Array[_] = n.nonEmptyChildren.toArray.filter(f => {
        val lab = f.label
        lab == "p" || lab == "lg" || lab == "div"
      }).map(f => {
        if (f.label == "div") {
          parseDiv(f)
        } else if (f.label == "p" || f.label == "lg") {
          parseSegment(f)
        } else {
          f.text
        }
      })
      XuEntry(mulu, content)
    }).toArray
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
    str.split("\n").filter(_.nonEmpty).map(_.trim).mkString("")
  }
}

case class FenEntry(jtitle: String, head: String, mulu: String, body: Array[String]) {
  def toJson: JSONObject = {
    val json = new JSONObject()

    val b = new JSONArray()
    body.foreach(b.add)

    json.put("jtitle", jtitle)
    json.put("head", head)
    json.put("mulu", mulu)
    json.put("body", b)

    json
  }
}

case class DivObj(mulu: String, content: String, title: String) {
  def toJson: JSONObject = {
    val json = new JSONObject()
    json.put("mulu", mulu)
    json.put("title", title)
    json.put("content", content)
    json
  }
}

case class XuEntry(mulu: String, body: Array[_]) {
  def toJson: JSONObject = {
    val json = new JSONObject()
    json.put("mulu", mulu)

    val c = new JSONArray()
    body.foreach {
      case str: String => c.add(str)
      case obj: DivObj => c.add(obj.toJson)
      case _ =>
    }

    json.put("body_list", c)
    json
  }
}

case class AppRevision(head: String, tpe: String, apps: Array[App])

case class App(lem: String, rdg: String)

case class NoteRevision(head: String, tpe: String, note: String, target: String)

