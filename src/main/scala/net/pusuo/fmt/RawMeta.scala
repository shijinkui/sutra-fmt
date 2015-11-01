package net.pusuo.fmt

import java.util.Date

import com.alibaba.fastjson.{JSONArray, JSONObject}
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * raw header
 * Created by shijinkui on 10/21/15.
 */
case class RawMeta(
  fileName: String,
  fullTitle: String,
  title: String,
  author: String, //  译者
  extentSize: String,
  sourceFrom: String, // 来源描述
  publicationData: String, // 发布时间
  distributor: String, // 发布者
  providerDesc: String, // 提供者的英文描述
  revisionHistory: Array[String] // 修订历史
) {
  def toJson: JSONObject = {
    val json = new JSONObject()
    json.put("file_name", fileName)
    json.put("full_title", fullTitle)
    json.put("title", title)
    json.put("author", author)
    json.put("extent_size", extentSize)
    json.put("source_from", sourceFrom)
    json.put("publication_data", publicationData)
    json.put("distributor", distributor)
    json.put("provider_desc", providerDesc)
    val array = new JSONArray()
    revisionHistory.foreach(f => array.add(f))
    json.put("revision_history", array)

    json
  }
}

case class RawBody(
  docNumber: String, // 文档编号
  juanNumber: String, //  卷号码
  title: String, // 经题
  content: Array[String] = Array.empty[String], //  未分品的经文正文
  xuEntry: Array[XuEntry] = Array.empty[XuEntry],
  fenEntry: Array[FenEntry] = Array.empty[FenEntry]
) {

  def toJson: JSONObject = {
    val json = new JSONObject()
    json.put("doc_number", docNumber)
    json.put("juan_number", juanNumber)
    json.put("title", title)

    val c1 = new JSONArray()
    content.foreach(c1.add)
    json.put("content", c1)

    val c2 = new JSONArray()
    xuEntry.foreach(f => c2.add(f.toJson))
    json.put("xu_list", c2)

    val c3 = new JSONArray()
    fenEntry.foreach(f => c3.add(f.toJson))
    json.put("fen_list", c3)

    json
  }
}

case class Back()

case class SutraEntry(header: RawMeta, body: RawBody, back: Back) {
  def toJson: String = {
    val json = new JSONObject()
    json.put("header", header.toJson)
    json.put("body", body.toJson)
    json.put("back", "")

    val mapper = new ObjectMapper()
    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json)
  }

  def toMarkDown: String = {
    val rev_str = header.revisionHistory.map(f => "   * " + f).mkString("\n")
    val xu_str = body.xuEntry.map(f => {
      val content = f.body.map {
        case div: DivObj =>
          val title = if (div.title.nonEmpty) div.title else div.mulu
          s"""
             |#### $title
              |${div.content}
          """.stripMargin
        case x => x
      }.mkString("\n")

      s"""
         |### ${f.mulu}
          |$content
          |* * *
          """.stripMargin
    }).mkString("\n")

    val fen_str = body.fenEntry.map(f => {
      s"""
         |### ${f.head}
          |* * *
          |${f.body.mkString("\n\n")}
          """.stripMargin
    }).mkString

    s"""
       |## ${header.title}
        |>	${header.author}
        |$xu_str
        |${body.content.mkString("\n\n")}
        |$fen_str
        |**《${body.title}》**
                           |
                           |* * *
                           |
                           |###	*后记*
                           |*  文件名: ${header.fileName}
        |*  完整标题: ${header.fullTitle}
        |*  卷数: ${header.extentSize}
        |*  文档编号: ${body.docNumber}
        |*  来源描述: ${header.sourceFrom}
        |*  发布时间: ${header.publicationData}
        |*  发布者: ${header.distributor}
        |*  经文提供者: ${header.providerDesc}
        |*  修订历史:
        |$rev_str
        |*  本文档生成时间: ${new Date()}
    """.stripMargin
  }
}

