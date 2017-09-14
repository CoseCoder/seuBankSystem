package group_seven.testdemo


import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithLBFGS}
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.hive.HiveContext

object LogisticRegression {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("LogisticRegressionModel").master("local").enableHiveSupport().getOrCreate()
    val sc = spark.sparkContext

    spark.sql("use bank")
    val rs00 = spark.sql("select overdue.id,max(overdue.label),max(user_info.gender)," +
      "max(user_info.job),max(user_info.education),max(user_info.marriage)," +
      "max(user_info.residence),count(browse_history.*)" +
      "from (overdue join user_info on overdue.id=user_info.id) join browse_history " +
      "on overdue.id=browse_history.id group by overdue.id")
    val rs01 = spark.sql("select overdue.id,avg(bill_detail.lastbillmoney),avg(bill_detail.lastrepaymoney)," +
      "avg(bill_detail.creditlimit),avg(bill_detail.balance),avg(bill_detail.leastrepaymoney)," +
      "avg(bill_detail.billmoney),avg(bill_detail.repayatatus) from overdue join bill_detail " +
      "on overdue.id=bill_detail.id group by overdue.id")

    //.createTempView("bank")
    //    .show(50),m(1)
    //    rs.write.save("src/main/resources/data/rs01.txt")
    //    val rs = spark.read.load("src/main/resources/data/rs01.txt")
    val rs = rs00.join(rs01, "id")
      .rdd.map(m => (m(1).toString.toInt, Array(m(2).toString.toDouble,
      m(3).toString.toDouble, m(4).toString.toDouble, m(5).toString.toDouble,
      m(6).toString.toDouble, m(7).toString.toDouble, m(8).toString.toDouble,
      m(9).toString.toDouble, m(10).toString.toDouble, m(11).toString.toDouble,
      m(12).toString.toDouble, m(13).toString.toDouble, m(14).toString.toDouble)))

    //        var data = sc.textFile("src/main/resources/data/sample_libsvm_data.txt")
    //          .map(m => m.split("\t").map(_.toDouble))
    //          .map(m => (m(0).toInt, Array(m(1), m(2), m(3), m(4), m(5), m(6))))

    var data = rs.map(m => (LabeledPoint(m._1, Vectors.dense(m._2))))
    data.take(10).foreach(println)

    //    MLUtils.saveAsLibSVMFile(lines,"src/main/resources/data/libsvm_data.txt")

    // Load training data in LIBSVM format.
    //    val data = MLUtils.loadLibSVMFile(sc, "src/main/resources/data/libsvm_data.txt")

    // Split data into training (60%) and test (40%).
    val splits = data.randomSplit(Array(0.7, 0.3), seed = 11L)
    val training = splits(0).cache()
    val test = splits(1)

    // Run training algorithm to build the model
    val model = new LogisticRegressionWithLBFGS()
      .setNumClasses(2)
      .run(training)

    // Compute raw scores on the test set.
    val predictionAndLabels = test.map { case LabeledPoint(label, features) =>
      val prediction = model.predict(features)
      (prediction, label)
    }

    // Get evaluation metrics.
    val metrics = new MulticlassMetrics(predictionAndLabels)
    val accuracy = metrics.accuracy
    println(s"Accuracy = $accuracy")

    // Save and load model
    model.save(sc, "src/main/resources/data/scalaLogisticRegressionWithLBFGSModel")
    //    val sameModel = LogisticRegressionModel.load(sc,
    //      "src/main/resources/data/scalaLogisticRegressionWithLBFGSModel")
    spark.stop()
  }
}