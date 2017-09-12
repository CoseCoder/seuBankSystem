package group_seven.testdemo

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.hive.HiveContext

object LoadToHive {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("LoadToHive").master("local").enableHiveSupport().getOrCreate()
    val sc = spark.sparkContext

    //生成广播变量

    val user_info = sc.textFile("src/main/resources/data/user_info_train.txt").map(_.split(","))
      .map(m => (m(0).toInt, m(1).toInt, m(2).toInt, m(3).toInt, m(4).toInt, m(5).toInt))

    val overdue = sc.textFile("src/main/resources/data/overdue_train.txt").map(_.split(","))
      .map(m => (m(0).toInt, m(1).toInt))

    val bank_detail = sc.textFile("src/main/resources/data/bank_detail_train.txt").map(_.split(","))
      .map(m => (m(0).toInt, m(1), m(2).toInt, m(3).toDouble, m(4).toInt))

    val browse_history = sc.textFile("src/main/resources/data/browse_history_train.txt").map(_.split(","))
      .map(m => (m(0).toInt, m(1), m(2).toInt, m(3).toInt))

    val loan_time = sc.textFile("src/main/resources/data/loan_time_train.txt").map(_.split(","))
      .map(m => (m(0).toInt, m(1)))

    val bill_detail = sc.textFile("src/main/resources/data/bill_detail_train.txt").map(_.split(","))
      .map(m => (m(0).toInt, m(1), m(2).toInt, m(3).toDouble, m(4).toDouble,
        m(5).toDouble, m(6).toDouble, m(7).toDouble, m(8).toInt, m(9).toDouble
        , m(10).toDouble, m(11).toDouble, m(12).toDouble, m(13).toDouble, m(14).toInt))


    val hiveContext = new HiveContext(sc)
    import hiveContext.implicits._

    hiveContext.sql("create database IF NOT EXISTS bank")
    hiveContext.sql("use bank")
    //    hiveContext.sql("truncate table user_info")
    var df1 = user_info.toDF("id", "gender", "job", "education", "marriage", "residence").registerTempTable("user_info")

    hiveContext.sql("create table IF NOT EXISTS user_info as " +
      "select * from user_info")

    hiveContext.sql("select * from user_info").show()

    //    hiveContext.sql("truncate table overdue")
    var df2 = overdue.toDF("id", "label").registerTempTable("overdue")

    hiveContext.sql("create table IF NOT EXISTS overdue as " +
      "select * from overdue")

    hiveContext.sql("select * from overdue").show()

    //    hiveContext.sql("truncate table bank_detail")
    var df3 = bank_detail.toDF("id", "time", "billtype", "money", "wageflag").registerTempTable("bank_detail")

    hiveContext.sql("create table IF NOT EXISTS bank_detail as " +
      "select * from bank_detail")

    hiveContext.sql("select * from bank_detail").show()

    //    hiveContext.sql("truncate table browse_history")
    var df4 = browse_history.toDF("id", "time", "browsedata", "browsenumber").registerTempTable("browse_history")

    hiveContext.sql("create table IF NOT EXISTS browse_history as " +
      "select * from browse_history")

    hiveContext.sql("select * from browse_history").show()

    //    hiveContext.sql("truncate table loan_time")
    var df5 = loan_time.toDF("id", "time").registerTempTable("loan_time")

    hiveContext.sql("create table IF NOT EXISTS loan_time as " +
      "select * from loan_time")

    hiveContext.sql("select * from loan_time").show()

    //    hiveContext.sql("truncate table bill_detail")
    var df6 = bill_detail.toDF("id", "time", "bankid", "lastbillmoney",
      "lastrepaymoney", "creditlimit", "balance", "leastrepaymoney", "salesnumber",
      "billmoney", "adjustmoney", "cycleinterest", "available", "cashadvancelimit", "repayatatus").registerTempTable("bill_detail")

    hiveContext.sql("create table IF NOT EXISTS bill_detail as " +
      "select * from bill_detail")

    hiveContext.sql("select * from bill_detail").show()
  }
}
