package group_seven.testdemo

import java.util.Properties

import org.apache.spark.sql.{SQLContext, SparkSession}

object data_b_bill {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("data_b_bill").master("local").enableHiveSupport().getOrCreate()
    val sc = spark.sparkContext

    //获取文件中的数据，并转化成RDD[Array[String]]
    val bill_data = sc.textFile("src/main/resources/data/data_b_bill.csv").map(_.split(","))

    //删除bill_Data中每个Array[]中每一个String对象头尾的双引号,且把RDD[Array[String]]
    // 转换成RDD[(String.String,Double.String,String,String)]
    // 并将每行的第三个元素(m(3)即时间字符串)中的"/"替换成"-"
    val data00 = bill_data.map(row => (row.map(colume => (colume.replace("\"", "")))))
      .map(m => (m(0), m(1), m(2).toDouble, m(3).replace("/", "-"), m(4), m(5)))

    //修正每行的第三个元素(m(3)即时间字符串)的格式,"yyyy-mm-dd HH:MM:SS"
    //将"yyyy-m-d HH:MM:SS"转换成"yyyy-mm-d HH:MM:SS",即在月份前补0
    val data01 = data00.filter(m => (m._4.length == 17)).
      map(m => (m._1, m._2, m._3, m._4.substring(0, 5) + "0" + m._4.substring(5, 17),
        m._5, m._6))

    //将"yyyy-m-dd HH:MM:SS"转换成"yyyy-mm-dd HH:MM:SS",,即在月份前补0
    val data02 = data00.filter(m => (m._4.length == 18 && m._4.lastIndexOf("-") == 6))
      .map(m => (m._1, m._2, m._3, m._4.substring(0, 5) + "0" + m._4.substring(5, 18),
        m._5, m._6))

    //将"yyyy-mm-d HH:MM:SS"转换成"yyyy-mm-dd HH:MM:SS",即在日期前补0
    val data03 = data00.filter(m => (m._4.length == 18 && m._4.lastIndexOf("-") == 7))
      .union(data01)
      .map(m => (m._1, m._2, m._3, m._4.substring(0, 8) + "0" + m._4.substring(8, 18), m._5, m._6))

    //将前面修正过后的RDD与格式正确的RDD合并
    val data = data00.filter(m => (m._4.length == 19)).union(data02).union(data03).sortBy(m => m._4)

    //找出交易金额为大额整数的交易信息(>=2000)
    val bigInteger = data.filter(row => (row._3 >= 2000 && row._3 % 100 == 0))
      .map(m => (m._1, m._2, m._3, m._4, m._5, m._6, "大额整数"))

    //找出交易金额差距过大的交易信息(>=3000)
    data.keyBy(_._2).groupByKey()
      .filter(m => ((m._2.map(m => m._3).max - m._2.map(m => m._3).min) >= 3000))
      .flatMap(m => (m._2)).map(m => (m._1, m._2, m._3, m._4, m._5, m._6))
      .foreach(println)

    //找出交易时间异常的交易信息(<=8:00,>=22:00)
    val exTime = data.filter(m => (m._1.substring(8, 10).toInt < 8 || m._1.substring(8, 10).toInt > 22))
      .map(m => (m._1, m._2, m._3, m._4, m._5, m._6, "异常时间"))

    //将得到的数据导入MySQL的数据表exceptionaccount中
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    var df = bigInteger.union(exTime)
      .toDF("BillNumber", "Account", "Money", "BillDate", "BillExInfo", "BankSiteID", "ExReason")

    //    df.show()

    val dfWriter = df.write.mode("append")

    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "123456")
    prop.put("transformedBitIsBoolean", "true")
    prop.put("useSSL", "false")

    dfWriter.jdbc("jdbc:mysql://60.205.171.171:3306/group7", "exceptionaccount", prop)
  }
}
