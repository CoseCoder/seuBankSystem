package group_seven.testdemo

import org.apache.spark.sql.SparkSession


object data_b_bill {

  def isAbnormal(args: Map[String, Tuple6[String,String,Double,String,String,String]]): Boolean ={
    val list = args.map(m=>m._2).map(m=>m._3)
    return list.max-list.min>=3000
  }
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("data_b_bill").master("local").enableHiveSupport().getOrCreate()
    val sc= spark.sparkContext

    //生成广播变量
    val fields = List("BillNumber","Account","Money","BillDate","BillType","BillExInfo","BankSiteID")
    val bcfields = sc.broadcast(fields)

    val bill_data = sc.textFile("src/main/resources/data/data_b_bill.csv").map(_.split(","))
    val data = bill_data.map(row=>(row.map(colume=>(colume.substring(1,colume.length()-1)))))

    println("全部交易金额都是大额的整数（>=2000）")
    data.filter(row=>(row(2).toDouble >= 2000 && row(2).toDouble%100==0))
      .map(row=>(row(0),row(1),row(2),row(3),row(4),row(5))).foreach(println)

    println("交易金额差距过大（>=3000）")

    data.map(m=>(m(0),m(1),m(2).toDouble,m(3),m(4),m(5))).keyBy(_._2).groupByKey()
      .filter(m=>((m._2.map(m=>m._3).max-m._2.map(m=>m._3).min)>=3000))
      .flatMap(m=>(m._2)).map(m=>(m._1,m._2,m._3,m._4,m._5,m._6))
      .foreach(println)

    println("交易的时间不对")
    data.filter(m=>(m(0).substring(8,10).toInt<8 || m(0).substring(8,10).toInt>22 ))
      .map(row=>(row(0),row(1),row(2),row(3),row(4),row(5))).foreach(println)

//    //移动互联网日活跃用户（DAU）的统计
//    bill_data.map{m =>
//      val imei = m(bcfields.value.indexOf("IMEI"))//取出IMEI
//    val time = m(bcfields.value.indexOf("Time"))//取出Time
//      imei + ":" + time
//    }.distinct().map(_.split(":")).map(m => (m(1),1)).reduceByKey(_+_)
//      .foreach(m => println(m._1 + "日活动用户量是：" + m._2))
//
//    //移动互联网月活跃用户（MAU）的统计
//    bill_data.map{m =>
//      val imei = m(bcfields.value.indexOf("IMEI"))//取出IMEI
//    val time = m(bcfields.value.indexOf("Time")).substring(0,7)//取出Time
//      imei + ":" + time
//    }.distinct().map(_.split(":")).map(m => (m(1),1)).reduceByKey(_+_)
//      .foreach(m => println(m._1 + "月活动用户量是：" + m._2))
//
//    //统计在不同应用中的上行流量
//    bill_data.map(m => (m(bcfields.value.indexOf("APP")), m(bcfields.value.indexOf("UplinkBytes")).toInt))
//      .reduceByKey(_+_).foreach(m => println(m._1 + "应用上行的流量是：" + m._2))
//
//    //统计在不同应用中的下行流量
//    bill_data.map(m => (m(bcfields.value.indexOf("APP")), m(bcfields.value.indexOf("DownlinkBytes")).toInt))
//      .reduceByKey(_+_).foreach(m => println(m._1 + "应用下行的流量是：" + m._2))
//
//    //统计在不同应用中的上下行流量
//    bill_data.map(m => (m(bcfields.value.indexOf("APP")),
//      List(m(bcfields.value.indexOf("UplinkBytes")).toInt, m(bcfields.value.indexOf("DownlinkBytes")).toInt)))
//      .reduceByKey((a, b) => List(a(0)+b(0), a(1)+b(1)))
//      .foreach(m => println(m._1 + "应用上行的流量是：" + m._2(0) + "\t" +
//        "下行的流量是：" + m._2(1)))
  }
}
