package net.pusuo.fmt

import java.io.{File, FilenameFilter}

/**
 * 根据cbeta的xml生成大藏经目录
 * Created by shijinkui on 9/24/15.
 */
object FileUtil {
  def build(path: String, outputPath: String): Unit = {
    val src = new File(path)
    val base = src.getPath
    val list = listFiles(src).map(
      f => f.replaceFirst(base, "")
    ).filter(_.toLowerCase.endsWith(".xml")).sorted

    list.foreach(println(_))
  }

  private def listFiles(file: File): Array[String] = {
    file.isFile match {
      case true => Array(file.getAbsolutePath)
      case false => file.listFiles(new FilenameFilter {
        override def accept(dir: File, name: String): Boolean = !name.startsWith(".")
      }).map(f => listFiles(f)).flatten
    }
  }
}
