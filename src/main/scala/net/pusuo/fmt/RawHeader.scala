package net.pusuo.fmt

/**
 * raw header
 * Created by shijinkui on 10/21/15.
 */
case class RawHeader(
  fullTitle: String,
  author: String, //  译者
  extentSize: String,
  sourceFrom: String, // 来源描述
  publicationData: String, // 发布时间
  distributor: String, // 发布者
  providerDesc: String, // 提供者的英文描述
  revisionHistory: Array[String] // 修订历史
)

case class RawBody(
  docNumber: String, // 文档编号
  juanNumber: String, //  卷号码
  title: String, // 经题
  author: String,
  paragraph: Array[String]
)

case class Back()
