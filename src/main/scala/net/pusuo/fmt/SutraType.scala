package net.pusuo.fmt

import com.alibaba.fastjson.{JSON, JSONObject}

/**
 * type
 * Created by sjk on 10/20/15.
 */

case class SutraType(name: String, code: String, nick: String, tpe: String)

object SutraType {
  val map = parseMeta

  def parseMeta: Map[String, SutraType] = {
    val json = """
                 |[
                 |  {
                 |    "code": "A",
                 |    "en_name": "Jin Edition of the Canon",
                 |    "name": "赵城金藏",
                 |    "nick": "【金藏】",
                 |    "type": "北京：北京图书馆出版社, 2008."
                 |  },
                 |  {
                 |    "code": "B",
                 |    "en_name": "Supplement to the Dazangjing",
                 |    "name": "大藏经补编",
                 |    "nick": "【补编】",
                 |    "type": "蓝吉富主编<br>台北：华宇出版社, 1985."
                 |  },
                 |  {
                 |    "code": "C",
                 |    "en_name": "Zhonghua Canon - Zhonghua shuju Edition",
                 |    "name": "中华大藏经-中华书局版",
                 |    "nick": "【中华】",
                 |    "type": "中华大藏经编辑局编<br>北京：中华书局, 1984-1997."
                 |  },
                 |  {
                 |    "code": "D",
                 |    "en_name": "Selections from the Taipei National Central Library Buddhist Rare Book Collectio",
                 |    "name": "国家图书馆善本佛典",
                 |    "nick": "【国图】",
                 |    "type": "国家图书馆特藏组 收藏"
                 |  },
                 |  {
                 |    "code": "F",
                 |    "en_name": "Fangshan shijing",
                 |    "name": "房山石经",
                 |    "nick": "【房山】",
                 |    "type": "中国佛教图书文物馆编<br>北京：华夏出版社, 2000."
                 |  },
                 |  {
                 |    "code": "G",
                 |    "en_name": "Fojiao Canon",
                 |    "name": "佛教大藏经",
                 |    "nick": "【佛教】",
                 |    "type": "佛教书局编辑<br>台北：佛教书局, 1978."
                 |  },
                 |  {
                 |    "code": "H(ZS)",
                 |    "en_name": "Passages concerning Buddhism from the Official Histories",
                 |    "name": "正史佛教资料类编",
                 |    "nick": "【正史】",
                 |    "type": "杜斗城辑编<br>兰州：甘肃文化出版社, 2006."
                 |  },
                 |  {
                 |    "code": "I",
                 |    "en_name": "Selections of Buddhist Stone Rubbings from the Northern Dynasties",
                 |    "name": "北朝佛教石刻拓片百品",
                 |    "nick": "-",
                 |    "type": "颜娟英主编<br>台北：中央研究院历史语言研究所, 2008."
                 |  },
                 |  {
                 |    "code": "J",
                 |    "en_name": "Jiaxing Canon - Xinwenfeng Edition",
                 |    "name": "嘉兴大藏经-新文丰版",
                 |    "nick": "【嘉兴】",
                 |    "type": "径山藏版版藏<br>台北：新文丰, 1987."
                 |  },
                 |  {
                 |    "code": "K",
                 |    "en_name": "Tripiṭaka Koreana - Xinwenfeng Edition",
                 |    "name": "高丽大藏经-新文丰版",
                 |    "nick": "【丽】",
                 |    "type": "高丽大藏经完刊推进委员会原刊<br>台北：新文丰, 1982."
                 |  },
                 |  {
                 |    "code": "L",
                 |    "en_name": "Qianlong Edition of the Canon - Xinwenfeng Edition",
                 |    "name": "乾隆大藏经-新文丰版",
                 |    "nick": "【龙】",
                 |    "type": "台北：新文丰, 1991."
                 |  },
                 |  {
                 |    "code": "M",
                 |    "en_name": "Manji Daizōkyō - Xinwenfeng Edition",
                 |    "name": "卍正藏经-新文丰版",
                 |    "nick": "【卍正】",
                 |    "type": "京都．藏经书院原刊<br>台北：新文丰, 1980."
                 |  },
                 |  {
                 |    "code": "N",
                 |    "en_name": "Chinese Translation of the Pali Tipiṭaka",
                 |    "name": "汉译南传大藏经",
                 |    "nick": "【南传】",
                 |    "type": "元亨寺汉译南传大藏经编译委员会<br>高雄：元亨寺妙林出版社, 1995."
                 |  },
                 |  {
                 |    "code": "P",
                 |    "en_name": "Northern Yongle Edition of the Canon",
                 |    "name": "永乐北藏",
                 |    "nick": "【北藏】",
                 |    "type": "永乐北藏整理委员会编<br>北京：线装书局, 2000."
                 |  },
                 |  {
                 |    "code": "Q",
                 |    "en_name": "Qisha Edition of the Canon - Xinwenfeng Edition",
                 |    "name": "碛砂大藏经-新文丰版",
                 |    "nick": "【碛砂】",
                 |    "type": "延圣院大藏经局编辑<br>台北：新文丰, 1987."
                 |  },
                 |  {
                 |    "code": "R",
                 |    "en_name": "Manji Zokuzōkyō - Xinwenfeng Edition",
                 |    "name": "卍续藏经-新文丰版",
                 |    "nick": "-",
                 |    "type": "京都．藏经书院原刊<br>台北：新文丰, 1994."
                 |  },
                 |  {
                 |    "code": "S",
                 |    "en_name": "Songzang yizhen - Xinwenfeng Edition",
                 |    "name": "宋藏遗珍-新文丰版",
                 |    "nick": "【宋遗】",
                 |    "type": "范成辑补<br>台北：新文丰, 1978."
                 |  },
                 |  {
                 |    "code": "T",
                 |    "en_name": "Taishō Tripiṭaka",
                 |    "name": "大正新修大藏经",
                 |    "nick": "【大】",
                 |    "type": "大正新修大藏经刊行会编<br>东京：大藏出版株式会社, Popular Edition in 1988."
                 |  },
                 |  {
                 |    "code": "U",
                 |    "en_name": "Southern Hongwu Edition of the Canon",
                 |    "name": "洪武南藏",
                 |    "nick": "【洪武】",
                 |    "type": "四川省佛教协会<br>成都：四川省佛教协会, 1999."
                 |  },
                 |  {
                 |    "code": "W(ZW)",
                 |    "en_name": "Buddhist Texts not contained in the Tripiṭaka",
                 |    "name": "藏外佛教文献",
                 |    "nick": "【藏外】",
                 |    "type": "方广锠主编<br>北京：宗教文化出版社, 1995-2003."
                 |  },
                 |  {
                 |    "code": "X",
                 |    "en_name": "Manji Shinsan Dainihon Zokuzōkyō",
                 |    "name": "卍新纂大日本续藏经",
                 |    "nick": "【卍续】",
                 |    "type": "河村照孝编集<br>东京：株式会社国书刊行会, 1975-1989."
                 |  },
                 |  {
                 |    "code": "Z",
                 |    "en_name": "Manji Dainihon Zokuzōkyō",
                 |    "name": "卍大日本续藏经",
                 |    "nick": "-",
                 |    "type": "京都：藏经书院, 1905-1912"
                 |  }
                 |]
               """.stripMargin
    import scala.collection.JavaConversions._
    JSON.parseArray(json).map(f => {
      val obj = f.asInstanceOf[JSONObject]
      val tpe = SutraType(
        obj.getString("name"),
        obj.getString("code"),
        obj.getString("name"),
        obj.getString("type")
      )

      tpe.code -> tpe
    }).toMap
  }
}
