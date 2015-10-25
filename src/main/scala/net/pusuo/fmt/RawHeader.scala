package net.pusuo.fmt

import java.util.Date

import com.alibaba.fastjson.{JSONArray, JSONObject}
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * raw header
 * Created by shijinkui on 10/21/15.
 */

abstract class Fmt

case class SutraEntry(header: RawHeader, body: RawBody, back: Back) {
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

    s"""
       |## ${body.title}
        |>	${body.author}
        |>	${body.juanNumber}
        |>	${body.docNumber}
        |
        |${body.paragraph.mkString("\n\n")}
        |
        |**《${body.title}》**
        |
        |* * *
        |
        |###	*后记*
        |*  文件名: ${header.fileName}
        |*  完整标题: ${header.fullTitle}
        |*  卷数: ${header.extentSize}
        |*  来源描述: ${header.sourceFrom}
        |*  发布时间: ${header.publicationData}
        |*  发布者: ${header.distributor}
        |*  经文提供者: ${header.providerDesc}
        |*  修订历史:
        |$rev_str
        |*  本文档生成时间: ${new Date()}
        |
    """.stripMargin
  }
}

case class RawHeader(
  fileName: String,
  fullTitle: String,
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
  author: String,
  paragraph: Array[String]
) {

  def toJson: JSONObject = {
    val json = new JSONObject()
    json.put("doc_number", docNumber)
    json.put("juan_number", juanNumber)
    json.put("title", title)
    json.put("author", author)

    val array = new JSONArray()
    paragraph.foreach(f => array.add(f))
    json.put("paragraph", array)

    json
  }
}

case class Back()
