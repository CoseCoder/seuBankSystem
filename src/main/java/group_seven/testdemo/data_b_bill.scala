package group_seven.testdemo

import java.util.Properties

import org.apache.spark.sql.{SQLContext, SparkSession}

object data_b_bill {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("data_b_bill").master("local").enableHiveSupport().getOrCreate()
    val sc = spark.sparkContext

    //生成广播变量
    //    val fields = List("BillNumber","Account","Money","BillDate","BillType","BillExInfo","BankSiteID")
    //    val bcfields = sc.broadcast(fields)

    val bill_data = sc.textFile("src/main/resources/data/data_b_bill.csv").map(_.split(",")).filter(m => (m != ""))
    val data00 = bill_data.map(row => (row.map(colume => (colume.replace("\"", "")))))
      .map(m => (m(0), m(1), m(2).toDouble, m(3).replace("/", "-"), m(4), m(5)))

    val data01 = data00.filter(m => (m._4.length == 17)).map(m => (m._1, m._2, m._3, m._4.substring(0, 5) + "0" + m._4.substring(5, 17), m._5, m._6))
    val data02 = data00.filter(m => (m._4.length == 18 && m._4.lastIndexOf("-") == 6)).map(m => (m._1, m._2, m._3, m._4.substring(0, 5) + "0" + m._4.substring(5, 18), m._5, m._6))
    val data03 = data00.filter(m => (m._4.length == 18 && m._4.lastIndexOf("-") == 7)).union(data01).map(m => (m._1, m._2, m._3, m._4.substring(0, 8) + "0" + m._4.substring(8, 18), m._5, m._6))
    val data = data00.filter(m => (m._4.length == 19)).union(data02).union(data03).sortBy(m => m._4)
    //    data.foreach(println)

//    println("全部交易金额都是大额的整数（>=2000）")
    val bigInteger = data.filter(row => (row._3 >= 2000 && row._3 % 100 == 0))
      .map(m=>(m._1,m._2,m._3,m._4,m._5,m._6,"大额整数"))
    //      .foreach(println)

    //    println("交易金额差距过大（>=3000）")
    //    data.keyBy(_._2).groupByKey()
    //      .filter(m=>((m._2.map(m=>m._3).max-m._2.map(m=>m._3).min)>=3000))
    //      .flatMap(m=>(m._2)).map(m=>(m._1,m._2,m._3,m._4,m._5,m._6))
    //      .foreach(println)

//    println("交易的时间不对")
    val exTime = data.filter(m => (m._1.substring(8, 10).toInt < 8 || m._1.substring(8, 10).toInt > 22))
      .map(m=>(m._1,m._2,m._3,m._4,m._5,m._6,"异常时间"))
    //      .foreach(println)

    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    var df=bigInteger.union(exTime).toDF("BillNumber","Account","Money","BillDate","BillExInfo","BankSiteID","ExReason")
    df.show()
    val dfWriter = df.write.mode("append")

    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "root")
//    prop.put("useUnicode","true")
//    prop.put("characterEncoding","utf-8")
    prop.put("transformedBitIsBoolean", "true")
    prop.put("useSSL", "false")
    dfWriter.jdbc("jdbc:mysql://localhost:3306/bank"," exceptionaccount", prop)
  }
}
