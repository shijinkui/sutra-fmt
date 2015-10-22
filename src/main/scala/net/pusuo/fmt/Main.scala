package net.pusuo.fmt

import java.io.{File, FilenameFilter}

/**
 * transformer
 * Created by sjk on 9/15/15.
 */
object Main {
  def main(args: Array[String]) {
    val root = new File("/Users/sjk/g/sutras/xml-p5")
    val list = getXml(root).filter(_.getName.endsWith(".xml"))



    val catalogBuilder = FileUtil.build("", "")
  }


  def getXml(path: File): Array[File] = {
    if (path.isFile)
      Array(path)
    else
      path.listFiles(new FilenameFilter() {
        override def accept(dir: File, name: String): Boolean = !name.startsWith(".")
      }).map(getXml).flatten
  }

}
