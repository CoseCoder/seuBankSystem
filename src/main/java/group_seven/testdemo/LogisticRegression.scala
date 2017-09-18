package group_seven.testdemo

import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.sql.SparkSession

object LogisticRegression {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("LogisticRegressionModel").master("local").enableHiveSupport().getOrCreate()
    val sc = spark.sparkContext

    spark.sql("use bank")

    //获取hive数据表中数据
    val rs00 = spark.sql("select overdue.id,max(overdue.label),max(user_info.gender)," +
      "max(user_info.job),max(user_info.education),max(user_info.marriage)," +
      "max(user_info.residence),count(browse_history.*)" +
      "from (overdue join user_info on overdue.id=user_info.id) join browse_history " +
      "on overdue.id=browse_history.id group by overdue.id")
    val rs01 = spark.sql("select overdue.id,avg(bill_detail.lastbillmoney),avg(bill_detail.lastrepaymoney)," +
      "avg(bill_detail.creditlimit),avg(bill_detail.balance),avg(bill_detail.leastrepaymoney)," +
      "avg(bill_detail.billmoney),avg(bill_detail.repayatatus) from overdue join bill_detail " +
      "on overdue.id=bill_detail.id group by overdue.id")

    //处理数据格式，转换成RDD[(Int,Array[Double])]
    val temp = rs00.join(rs01, "id")
    val rs = temp.rdd.map(m => (m(1).toString.toInt, Array(m(2).toString.toDouble,
      m(3).toString.toDouble, m(4).toString.toDouble, m(5).toString.toDouble,
      m(6).toString.toDouble, m(7).toString.toDouble, m(8).toString.toDouble,
      m(9).toString.toDouble, m(10).toString.toDouble, m(11).toString.toDouble,
      m(12).toString.toDouble, m(13).toString.toDouble, m(14).toString.toDouble)))

    val rs0 = rs.filter(_._1 == 1).union(sc.parallelize(rs.filter(_._1 == 0).take(10000)))

    //转换成labeledPoint格式
    var data = rs0.map(m => (LabeledPoint(m._1, Vectors.dense(m._2))))

    // Run training algorithm to build the model
    val model = new LogisticRegressionWithLBFGS()
      .setNumClasses(2)
      .run(data)

    // Compute raw scores on the test set.
    val predictionAndLabels = data.filter(_.label == 1).map { case LabeledPoint(label, features) =>
      val prediction = model.predict(features)
      (prediction, label)
    }

    // Get evaluation metrics.
    val metrics = new MulticlassMetrics(predictionAndLabels)
    val accuracy = metrics.accuracy
    println(s"Accuracy = $accuracy")

    // Save model
    model.save(sc, "src/main/resources/data/scalaLogisticRegressionWithLBFGSModel")
    spark.stop()
  }
}