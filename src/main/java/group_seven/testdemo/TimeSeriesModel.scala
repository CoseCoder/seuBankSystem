package group_seven.testdemo

import java.text.SimpleDateFormat
import java.util.{Calendar, Properties}

import com.cloudera.sparkts.TimeSeriesRDD
import com.cloudera.sparkts.models.ARIMA
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext, SaveMode}

import scala.collection.mutable.ArrayBuffer

/**
  * 时间序列模型
  * Created by Administrator on 2017/4/19.
  */
class TimeSeriesModel {

  //预测后面N个值
  private var predictedN = 1
  //存放的表名字
  private var outputTableName = "timeseries_output"

  def this(predictedN: Int, outputTableName: String) {
    this()
    this.predictedN = predictedN
    this.outputTableName = outputTableName
  }

  /**
    * Arima模型：
    * 输出其p，d，q参数
    * 输出其预测的predictedN个值
    *
    * @param trainTsrdd
    */
  def arimaModelTrain(trainTsrdd: TimeSeriesRDD[String]): RDD[String] = {
    /** *参数设置 ******/
    val predictedN = this.predictedN

    /** *创建arima模型 ***/
    //创建和训练arima模型.其RDD格式为(ArimaModel,Vector)
    val arimaAndVectorRdd = trainTsrdd.map { line =>
      line match {
        case (key, denseVector) =>
          (ARIMA.autoFit(denseVector), denseVector)
      }
    }

    //参数输出:p,d,q的实际值和其系数值
    val coefficients = arimaAndVectorRdd.map { line =>
      line match {
        case (arimaModel, denseVector) => {
          (arimaModel.coefficients.mkString(","),
            (arimaModel.p,
              arimaModel.d,
              arimaModel.q))
        }
      }
    }
    coefficients.collect().map {
      _ match {
        case (coefficients, (p, d, q)) =>
          println("coefficients:" + coefficients + "=>" + "(p=" + p + ",d=" + d + ",q=" + q + ")")
      }
    }

    /** *预测出后N个的值 *****/
    val forecast = arimaAndVectorRdd.map { row =>
      row match {
        case (arimaModel, denseVector) => {
          arimaModel.forecast(denseVector, predictedN)
        }
      }
    }
    val forecastValue = forecast.map(_.toArray.mkString(","))

    //取出预测值
    val preditcedValueRdd = forecastValue.map { parts =>
      val partArray = parts.split(",")
      for (i <- partArray.length - predictedN until partArray.length) yield partArray(i)
    }.map(_.toArray.mkString(","))
    preditcedValueRdd.collect().map { row =>
      println("forecast of next " + predictedN + " observations: " + row)
    }
    return preditcedValueRdd
  }

  /**
    * 实现HoltWinters模型
    *
    * @param trainTsrdd
    */
  def holtWintersModelTrain(trainTsrdd: TimeSeriesRDD[String], period: Int, holtWintersModelType: String): RDD[String] = {
    /** *参数设置 ******/
    //往后预测多少个值
    val predictedN = this.predictedN

    /** *创建HoltWinters模型 ***/
    //创建和训练HoltWinters模型.其RDD格式为(HoltWintersModel,Vector)
    val holtWintersAndVectorRdd = trainTsrdd.map { line =>
      line match {
        case (key, denseVector) =>
          (HoltWinters.fitModel(denseVector, period, holtWintersModelType), denseVector)
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
    forecastValue.collect().map { row =>
      println("HoltWinters forecast of next " + predictedN + " observations: " + row)
    }
    return forecastValue
  }


  /**
    * 批量生成日期（具体到月份的），用来保存
    *
    * @param predictedN
    * @param startTime
    * @param endTime
    */
  def productStartDatePredictDate(predictedN: Int, startTime: String, endTime: String): ArrayBuffer[String] = {
    //形成开始start到预测predicted的日期
    var dateArrayBuffer = new ArrayBuffer[String]()
    val dateFormat = new SimpleDateFormat("yyyyMM");
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()

    //设置训练数据中开始和结束日期
    cal1.set(startTime.substring(0, 4).toInt, startTime.substring(4).toInt, 0)
    cal2.set(endTime.substring(0, 4).toInt, endTime.substring(4).toInt, 0)

    //开始日期和预测日期的月份差
    val monthDiff = (cal2.getTime.getYear() - cal1.getTime.getYear()) * 12 + (cal2.getTime.getMonth() - cal1.getTime.getMonth()) + predictedN
    var iMonth = 0
    while (iMonth <= monthDiff) {
      //日期加1个月
      cal1.add(Calendar.MONTH, iMonth)
      //保存日期
      dateArrayBuffer += dateFormat.format(cal1.getTime)
      cal1.set(startTime.substring(0, 4).toInt, startTime.substring(4).toInt, 0)
      iMonth = iMonth + 1
    }
    return dateArrayBuffer
  }

  /**
    * 批量生成日期（具体到日的），用来保存
    *
    * @param predictedN
    * @param startTime
    * @param endTime
    */
  def productStartDayPredictDay(predictedN: Int, startTime: String, endTime: String): ArrayBuffer[String] = {
    //形成开始start到预测predicted的日期
    var dayArrayBuffer = new ArrayBuffer[String]()
    val dateFormat = new SimpleDateFormat("yyyyMMdd");
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()

    //设置训练数据中开始和结束日期
    cal1.set(startTime.substring(0, 4).toInt, startTime.substring(4, 6).toInt - 1, startTime.substring(6).toInt)
    cal2.set(endTime.substring(0, 4).toInt, endTime.substring(4, 6).toInt - 1, endTime.substring(6).toInt)

    //开始日期和预测日期的月份差
    val dayDiff = (cal2.getTimeInMillis - cal1.getTimeInMillis) / (1000 * 60 * 60 * 24) + predictedN
    var iDay = 0
    while (iDay <= dayDiff) {
      //日期加1天
      cal1.add(Calendar.DATE, iDay)
      //保存日期
      dayArrayBuffer += dateFormat.format(cal1.getTime)
      cal1.set(startTime.substring(0, 4).toInt, startTime.substring(4, 6).toInt - 1, startTime.substring(6).toInt)
      iDay = iDay + 1
    }

    return dayArrayBuffer
  }

  /**
    * 合并实际值和预测值，并加上日期,形成dataframe(Date,Data)
    * 并保存在MySQL中
    *
    * @param trainTsrdd     从hive中读取的数据
    * @param forecastValue  预测出来的数据（分为arima和holtwinters预测的）
    * @param modelName      选择哪个模型名字（arima和holtwinters）
    * @param predictedN     预测多少个值
    * @param startTime      开始日期
    * @param endTime        结束日期
    * @param sc
    * @param hiveColumnName 选择的列名字
    * @param sqlContext
    */
    def actualForcastDateSaveInMySQL(trainTsrdd:TimeSeriesRDD[String],forecastValue:RDD[String],modelName:String,predictedN:Int,startTime:String,endTime:String,sc:SparkContext,hiveColumnName:List[String],sqlContext:SQLContext): Unit ={

      //在真实值后面追加预测值
      val actualAndForcastArray=trainTsrdd.map{line=>
        line match {
          case (key,denseVector)=>
            denseVector.toArray.mkString(",")
        }
      }.union(forecastValue).collect()
      val actualAndForcastString=(actualAndForcastArray(0).toString+","+actualAndForcastArray(1).toString).split(",").map(data=>(data))
      val actualAndForcastRdd=sc.parallelize(actualAndForcastString)

      //获取日期，并转换成rdd
      var dateArray:ArrayBuffer[String]=new ArrayBuffer[String]()
      if(startTime.length==6){
        dateArray=productStartDatePredictDate(predictedN,startTime,endTime)
      }else if(startTime.length==8){
        dateArray=productStartDayPredictDay(predictedN,startTime,endTime)
      }
      val dateRdd=sc.parallelize(dateArray.toArray.mkString(",").split(",").map(date=>(date)))

      //合并日期和数据值,形成RDD[Row]
      val dateDataRdd=dateRdd.zip(actualAndForcastRdd).map{
        _ match {
          case (date,data)=>Row(date,data)
        }
      }

      //把dateData转换成dataframe
      val schemaString=hiveColumnName.mkString(" ")
      val schema=StructType(schemaString.split(" ")
        .map(fieldName=>StructField(fieldName,StringType,true)))
      val dateDataDf=sqlContext.createDataFrame(dateDataRdd,schema)

      //加载驱动
      Class.forName("com.mysql.jdbc.Driver")
      //设置用户名和密码
      val prop = new Properties()
      prop.setProperty("user","root")
      prop.setProperty("password","123456")
      prop.setProperty("transformedBitIsBoolean", "true")
      prop.setProperty("useSSL", "false")
      var sqlCommand=""
      //命名表格名字
      dateDataDf.registerTempTable("dateDataDf")
      //编写sql语句
      sqlCommand="select * from dateDataDf"
      // 调用DataFrameWriter将数据写入mysql（表可以不存在）
      sqlContext.sql(sqlCommand).write.mode(SaveMode.Overwrite).jdbc("jdbc:mysql://60.205.171.171:3306/bank",outputTableName,prop)

    }
}