package group_seven.testdemo

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.hive.HiveContext

object LoadToHive {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("LoadToHive").master("local").enableHiveSupport().getOrCreate()
    val sc = spark.sparkContext


    //将导入的文件数据处理成RDD
    //将user_info_train.txt的每一行处理成（用户 id,性别,职业,教育程度,婚姻状态,户口类型）
    val user_info = sc.textFile("src/main/resources/data/user_info_train.txt").map(_.split(","))
      .map(m => (m(0).toInt, m(1).toInt, m(2).toInt, m(3).toInt, m(4).toInt, m(5).toInt))

    //将overdue_train.txt的每一行处理成（用户 id,样本标签）
    val overdue = sc.textFile("src/main/resources/data/overdue_train.txt").map(_.split(","))
      .map(m => (m(0).toInt, m(1).toInt))

    //将bank_detail_train.txt的每一行处理成（用户 id,时间戳,交易类型,交易金额,工资收入标记）
    val bank_detail = sc.textFile("src/main/resources/data/bank_detail_train.txt").map(_.split(","))
      .map(m => (m(0).toInt, m(1), m(2).toInt, m(3).toDouble, m(4).toInt))

    //将browse_history_train.txt的每一行处理成（用户 id,时间戳,浏览行为数据,浏览子行为编号）
    val browse_history = sc.textFile("src/main/resources/data/browse_history_train.txt").map(_.split(","))
      .map(m => (m(0).toInt, m(1), m(2).toInt, m(3).toInt))

    //将loan_time_train.txt的每一行处理成（用户 id,放款时间）
    val loan_time = sc.textFile("src/main/resources/data/loan_time_train.txt").map(_.split(","))
      .map(m => (m(0).toInt, m(1)))

    //将bill_detail_train.txt的每一行处理成（用户 id,账单时间戳,银行 id,上期账单金额,上期还款金额,信用卡额度,本期账单余额,本期账单最低还款额,消费笔数,本期账单金额,调整金额,循环利息,可用金额,预借现金额
    //    度,还款状态）
    val bill_detail = sc.textFile("src/main/resources/data/bill_detail_train.txt").map(_.split(","))
      .map(m => (m(0).toInt, m(1), m(2).toInt, m(3).toDouble, m(4).toDouble,
        m(5).toDouble, m(6).toDouble, m(7).toDouble, m(8).toInt, m(9).toDouble
        , m(10).toDouble, m(11).toDouble, m(12).toDouble, m(13).toDouble, m(14).toInt))


    //将RDD导入Hive的数据表中
    val hiveContext = new HiveContext(sc)
    import hiveContext.implicits._

    //创建选定数据库
    hiveContext.sql("create database IF NOT EXISTS bank")
    hiveContext.sql("use bank")

    //创建数据表user_info并插入数据
    var df1 = user_info.toDF("id", "gender", "job",
      "education", "marriage", "residence").registerTempTable("user_info")
    hiveContext.sql("create table IF NOT EXISTS user_info as " +
      "select * from user_info")
    //    hiveContext.sql("select * from user_info").show()

    //创建数据表overdue并插入数据
    var df2 = overdue.toDF("id", "label").registerTempTable("overdue")
    hiveContext.sql("create table IF NOT EXISTS overdue as " +
      "select * from overdue")
    //    hiveContext.sql("select * from overdue").show()

    //创建数据表bank_detail并插入数据
    var df3 = bank_detail.toDF("id", "time", "billtype",
      "money", "wageflag").registerTempTable("bank_detail")
    hiveContext.sql("create table IF NOT EXISTS bank_detail as " +
      "select * from bank_detail")
    //    hiveContext.sql("select * from bank_detail").show()

    //创建数据表browse_history并插入数据
    var df4 = browse_history.toDF("id", "time", "browsedata",
      "browsenumber").registerTempTable("browse_history")
    hiveContext.sql("create table IF NOT EXISTS browse_history as " +
      "select * from browse_history")
    //    hiveContext.sql("select * from browse_history").show()

    //创建数据表loan_time并插入数据
    var df5 = loan_time.toDF("id", "time").registerTempTable("loan_time")
    hiveContext.sql("create table IF NOT EXISTS loan_time as " +
      "select * from loan_time")
    //    hiveContext.sql("select * from loan_time").show()

    //创建数据表bill_detail并插入数据
    var df6 = bill_detail.toDF("id", "time", "bankid", "lastbillmoney",
      "lastrepaymoney", "creditlimit", "balance", "leastrepaymoney",
      "salesnumber", "billmoney", "adjustmoney", "cycleinterest",
      "available", "cashadvancelimit", "repayatatus").registerTempTable("bill_detail")
    hiveContext.sql("create table IF NOT EXISTS bill_detail as " +
      "select * from bill_detail")
    //    hiveContext.sql("select * from bill_detail").show()
  }
}
