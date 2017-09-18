package group_seven.testdemo

import java.sql.Timestamp
import java.time.{ZoneId, ZonedDateTime}

import com.cloudera.sparkts._
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext, SparkSession}

import scala.collection.mutable.ArrayBuffer

object TimeSeries {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("TimeSeries").master("local").enableHiveSupport().getOrCreate()
    val sc = spark.sparkContext

    //预测的月份数
    val predictedN = 6

    //输出到数据库的表名
    val outputTableName = "time_series"

    //数据表结构
    val columnsName=List("time","data")

    /**
      * Creates a Spark DataFrame of (timestamp, key, data) from a tab-separated file
      */
    val rowRdd = sc.textFile("src/main/resources/data/day_02_data.txt").map { line =>
      val tokens = line.split('\t')
      val dt = ZonedDateTime.of(tokens(0).substring(0, 4).toInt, tokens(0).substring(4).toInt, 1, 0, 0, 0, 0,
        ZoneId.systemDefault())
      val key = "key"
      val data = tokens(1).toDouble
      Row(Timestamp.from(dt.toInstant), key, data)
    }

    val fields = Seq(
      StructField("timestamp", TimestampType, true),
      StructField("key", StringType, true),
      StructField("data", DoubleType, true)
    )
    val schema = StructType(fields)
    val df = spark.createDataFrame(rowRdd, schema)

    //    df.show(24, false)

    // Create an daily DateTimeIndex over August and September 2015
    val zone = ZoneId.systemDefault()
    val dtIndex = DateTimeIndex.uniformFromInterval(
      ZonedDateTime.of(2010, 1, 1, 0, 0, 0, 0,
        zone),
      ZonedDateTime.of(2011, 12, 1, 0, 0, 0, 0,
        zone),
      new MonthFrequency(1))

    // Align the ticker data on the DateTimeIndex to create a TimeSeriesRDD
    val trainTsrdd = TimeSeriesRDD.timeSeriesRDDFromObservations(dtIndex, df,
      "timestamp", "key", "data")

    // Cache it in memory
    trainTsrdd.cache()

    //使用Arima模型预测
    //    val modelRdd = trainTsrdd.map { line =>
    //      line match {
    //        case (key, denseVector) =>
    //          (ARIMA.autoFit(denseVector), denseVector)
    //      }
    //    }
    //
    //    //参数输出:p,d,q的实际值和其系数值
    //    val coefficients = modelRdd.map { line =>
    //      line match {
    //        case (arimaModel, denseVector) => {
    //          (arimaModel.coefficients.mkString(","),
    //            (arimaModel.p,
    //              arimaModel.d,
    //              arimaModel.q))
    //        }
    //      }
    //    }
    //    coefficients.collect().map {
    //      _ match {
    //        case (coefficients, (p, d, q)) =>
    //          println("coefficients:" + coefficients + "=>" + "(p=" + p + ",d=" + d + ",q=" + q + ")")
    //      }
    //    }
    //
    //    /** *预测出后N个的值 *****/
    //    val forecast1 = modelRdd.map { row =>
    //      row match {
    //        case (arimaModel, denseVector) => {
    //          arimaModel.forecast(denseVector, predictedN)
    //        }
    //      }
    //    }
    //    val forecastValue1 = forecast1.map(_.toArray.mkString(","))
    //
    //    val preditcedValueRdd1 = forecastValue1.map { parts =>
    //      val partArray = parts.split(",")
    //      for (i <- partArray.length - predictedN until partArray.length) yield partArray(i)
    //    }.map(_.toArray.mkString(","))
    //    preditcedValueRdd1.collect().map { row =>
    //      println(predictedN + ":" + row)
    //    }

    //使用HoltWinters模型预测
    val holtWintersAndVectorRdd = trainTsrdd.map { line =>
      line match {
        case (key, denseVector) =>
          (HoltWinters.fitModel(denseVector, 4, "additive"), denseVector)
      }
    }

    /** *预测出后N个的值 *****/
    //构成N个预测值向量，之后导入到holtWinters的forcast方法中
    val predictedArrayBuffer = new ArrayBuffer[Double]()
    var i = 0
    while (i < predictedN) {
      predictedArrayBuffer += i
      i = i + 1
    }
    val predictedVectors = Vectors.dense(predictedArrayBuffer.toArray)

    //预测
    val forecast = holtWintersAndVectorRdd.map { row =>
      row match {
        case (holtWintersModel, denseVector) => {
          holtWintersModel.forecast(denseVector, predictedVectors)
        }
      }
    }
    val forecastValue = forecast.map(_.toArray.mkString(","))

    val timeSeriesModel=new TimeSeriesModel(predictedN,outputTableName)
    val sqlContext = new SQLContext(sc)
//    val value = forecast.flatMap(m => m.toArray)
//    value.foreach(println)
    //将得到的数据导入MySQL的数据表中
    timeSeriesModel.actualForcastDateSaveInMySQL(trainTsrdd,forecastValue,"holtwinters",
      predictedN,"201001","201112",sc,columnsName,sqlContext)

    spark.stop()
  }
}
