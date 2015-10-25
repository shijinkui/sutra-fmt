package net.pusuo.fmt

import java.io.{File, FilenameFilter}

import org.slf4j.LoggerFactory

/**
 * transformer
 * Created by sjk on 9/15/15.
 */
object Main {
  private val logger = LoggerFactory.getLogger(Main.getClass)

  def main(args: Array[String]) {

    val jsonFmt = "/Users/sjk/g/sutras/sutra-json"
    val markdownFmt = "/Users/sjk/g/sutras/sutra-markdown"
    val src = "/Users/sjk/g/sutras/xml-p5"

    new File(src).listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = {
        !name.startsWith(".") && name.length == 1
      }
    }).foreach(f => {
      //  藏经分类
      val originDir = f.getAbsolutePath
      val tpe = SutraType.map(f.getName)

      FileUtil.listFiles(f).filter(_.endsWith(".xml")).par.foreach(p => {
        val jsonDir = jsonFmt + "/" + tpe.dir
        val mdDir = markdownFmt + "/" + tpe.dir

        //  生成json格式
        generate(Fmt.json, originDir, jsonDir, p)
        //  生成markdown格式
        generate(Fmt.markdown, originDir, mdDir, p)
      })
    })
  }

  private def generate(fmt: Fmt.Fmt, originDir: String, baseDir: String, xmlFile: String): Unit = {
    val target = xmlFile.replace(originDir, baseDir).replace(".xml", "." + fmt.toString)
    logger.info(target)
    val entry = XmlParse.parse(xmlFile)
    if (entry != null) {
      fmt match {
        case Fmt.json =>
          FileUtil.write(target, entry.toJson)
        case Fmt.markdown =>
          FileUtil.write(target, entry.toMarkDown)
        case _ =>
      }
    }
  }
}

object Fmt extends Enumeration {
  type Fmt = Value
  val markdown, json = Value
}
