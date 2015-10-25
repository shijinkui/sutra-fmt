package net.pusuo.fmt

import java.io.{File, FilenameFilter}

import com.google.common.base.Charsets
import com.google.common.io.Files

/**
 * 根据cbeta的xml生成大藏经目录
 * Created by shijinkui on 9/24/15.
 */
object FileUtil {
  def write(path: String, str: String): Unit = {
    val f = new File(path)
    f.getParentFile.mkdirs()
    Files.write(str, f, Charsets.UTF_8)
  }

  def listFiles(file: File): Array[String] = {
    file.isFile match {
      case true => Array(file.getAbsolutePath)
      case false => file.listFiles(new FilenameFilter {
        override def accept(dir: File, name: String): Boolean = !name.startsWith(".")
      }).map(f => listFiles(f)).flatten
    }
  }
}
