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

    //    spark.sql("use bank")
    //
    //    val rs = spark.sql("select user_info.*,overdue.label,browse_history.time as br_time," +
    //      "bill_detail.lastbillmoney,bill_detail.lastrepaymoney," +
    //      "bill_detail.creditlimit,bill_detail.balance,bill_detail.leastrepaymoney," +
    //      "bill_detail.billmoney,bill_detail.repayatatus " +
    //      "from ((user_info join overdue on user_info.id=overdue.id) join browse_history " +
    //      "on user_info.id=browse_history.id) join bill_detail " +
    //      "on user_info.id=bill_detail.id order by user_info.id")
    //.createTempView("bank")
    //    .show(50),m(1)
    //    rs.write.save("src/main/resources/data/rs01.txt")
    val rs = spark.read.load("src/main/resources/data/rs01.txt")
      .rdd.map(m => (m(0).toString, m(1).toString, m(2).toString, m(3).toString,
      m(4).toString, m(5).toString, m(6).toString, m(7).toString.toLong,
      m(8).toString.toDouble, m(9).toString.toDouble, m(10).toString.toDouble,
      m(11).toString.toDouble, m(12).toString.toDouble, m(13).toString.toDouble,
      m(14).toString.toDouble))
      .filter(m => (m._8 != 0)).keyBy(_._1).groupByKey()
      .map(m => m._2).map(m => (m.map(_._7).max, Array(m.map(_._2).max.toDouble,
      m.map(_._3).max.toDouble, m.map(_._4).max.toDouble, m.map(_._5).max.toDouble,
      m.map(_._6).max.toDouble, m.size.toDouble / (m.map(_._8).max - m.map(_._8).min),
      m.map(_._9).sum / m.size, m.map(_._10).sum / m.size,
      m.map(_._11).sum / m.size, m.map(_._12).sum / m.size,
      m.map(_._13).sum / m.size, m.map(_._14).sum / m.size,
      m.map(_._15).sum / m.size)))
    rs.take(2).foreach(println)


    //        var data = sc.textFile("src/main/resources/data/sample_libsvm_data.txt")
    //          .map(m => m.split("\t").map(_.toDouble))
    //          .map(m => (m(0).toInt, Array(m(1), m(2), m(3), m(4), m(5), m(6))))

    var data = rs.map(m => (LabeledPoint(m._1.toInt, Vectors.dense(m._2))))
    data.take(2).foreach(println)

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
  }
}