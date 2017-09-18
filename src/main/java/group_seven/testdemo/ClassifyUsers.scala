package group_seven.testdemo

import java.util.Properties

import org.apache.spark.sql.{SQLContext, SparkSession}

object ClassifyUsers {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("classify_user").master("local").enableHiveSupport().getOrCreate()
    val sc = spark.sparkContext


    //设置与MySQL连接的属性
    val sqlContext = new SQLContext(sc)
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "123456")
    prop.put("transformedBitIsBoolean", "true")
    prop.put("useSSL", "false")

    //从MySQL读取数据
    val df = spark.read.jdbc("jdbc:mysql://60.205.171.171:3306/group7", "new_bankbill", prop)
    //        df.show()

    //处理数据，过滤数据
    val data = df.rdd.map(m => (m.getString(1), m.getInt(4), m.getString(7))).filter(_._2 == 0)
      .map(m => (m._1, m._3))
    val shopping = data.filter(_._2 == "购物").map(m => (m._1, 1)).reduceByKey(_ + _).sortBy(_._2, false).collect()
    val qq = data.filter(_._2 == "QQ充值").map(m => (m._1, 1)).reduceByKey(_ + _).sortBy(_._2, false).collect()
    val w_e = data.filter(_._2 == "水电费").map(m => (m._1, 1)).reduceByKey(_ + _).sortBy(_._2, false).collect()
    val food = data.filter(_._2 == "外卖").map(m => (m._1, 1)).reduceByKey(_ + _).sortBy(_._2, false).collect()
    val game = data.filter(_._2 == "游戏").map(m => (m._1, 1)).reduceByKey(_ + _).sortBy(_._2, false).collect()

    val spr = shopping.size.toDouble
    val qr = qq.size.toDouble
    val w_er = w_e.size.toDouble
    val fr = food.size.toDouble
    val gr = game.size.toDouble

    import sqlContext.implicits._
    //购物
    val ndf = shopping.slice(0, Math.ceil(spr / 3).toInt).map(m => (m._1, "购物", 2))
      .union(shopping.slice(Math.ceil(spr / 3).toInt, Math.floor(spr / 3 * 2).toInt).map(m => (m._1, "购物", 1)))
      .union(shopping.slice(Math.floor(spr / 3 * 2).toInt, spr.toInt).map(m => (m._1, "购物", 0)))
      //QQ充值
      .union(qq.slice(0, Math.ceil(qr / 3).toInt).map(m => (m._1, "QQ充值", 2)))
      .union(qq.slice(Math.ceil(qr / 3).toInt, Math.floor(qr / 3 * 2).toInt).map(m => (m._1, "QQ充值", 1)))
      .union(qq.slice(Math.floor(qr / 3 * 2).toInt, qr.toInt).map(m => (m._1, "QQ充值", 0)))
      //水电费
      .union(w_e.slice(0, Math.ceil(w_er / 3).toInt).map(m => (m._1, "水电费", 2)))
      .union(w_e.slice(Math.ceil(w_er / 3).toInt, Math.floor(w_er / 3 * 2).toInt).map(m => (m._1, "水电费", 1)))
      .union(w_e.slice(Math.floor(w_er / 3 * 2).toInt, w_er.toInt).map(m => (m._1, "水电费", 0)))
      //外卖
      .union(food.slice(0, Math.ceil(fr / 3).toInt).map(m => (m._1, "外卖", 2)))
      .union(food.slice(Math.ceil(fr / 3).toInt, Math.floor(fr / 3 * 2).toInt).map(m => (m._1, "外卖", 1)))
      .union(food.slice(Math.floor(fr / 3 * 2).toInt, fr.toInt).map(m => (m._1, "外卖", 0)))
      //游戏
      .union(game.slice(0, Math.ceil(gr / 3).toInt).map(m => (m._1, "游戏", 2)))
      .union(game.slice(Math.ceil(gr / 3).toInt, Math.floor(gr / 3 * 2).toInt).map(m => (m._1, "游戏", 1)))
      .union(game.slice(Math.floor(gr / 3 * 2).toInt, gr.toInt).map(m => (m._1, "游戏", 0)))
      .toList
      .toDF("Account", "Purpose", "Frequence")

    //将数据写入MySQL
    val dfWriter = ndf.write.mode("append")
    dfWriter.jdbc("jdbc:mysql://60.205.171.171:3306/group7", "personas", prop)
  }
}
