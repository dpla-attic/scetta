package la.dp.scetta

import java.io.{PrintWriter, StringWriter}

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SparkMain extends CdlMapping {
  def main(args: Array[String]) {

    if (args.length != 2) {
      println("Args: <IN> <OUT>")
      sys.exit(-1)
    }

    val conf = new SparkConf().setAppName("CDL Mapping")
    val sc = new SparkContext(conf)

    val inputData: RDD[(String, Array[Byte])] = sc.sequenceFile[String, Array[Byte]](args(0))
    val outputData = inputData.map(record => (record._1, process(record._2)))
    outputData.saveAsSequenceFile(args(1), Some(classOf[org.apache.hadoop.io.compress.DefaultCodec]))

    sc.stop
  }

  def process(inputData: Array[Byte]): String = {
    try {
      map(new String(inputData, "utf8"))
    } catch {
      case e: Exception => {
        val sw = new StringWriter
        val pw = new PrintWriter(sw)
        e.printStackTrace(pw)
        sw.toString
      }
    }
  }
}
