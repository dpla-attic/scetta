package la.dp.scetta

import java.io.{File, PrintWriter, StringWriter}
import java.nio.file.Files
import java.nio.file.StandardOpenOption.CREATE
import java.nio.file.StandardOpenOption.TRUNCATE_EXISTING

object StandaloneMain extends App with CdlMapping {

  val inDirectory = new File(args(0))
  val outDirectory = new File(args(1))

  val start = System.currentTimeMillis()

  val results = inDirectory.list()
    .par
    .filter(fileName => fileName.endsWith(".json"))
    .map(fileName => mapFile(new File(inDirectory, fileName), new File(outDirectory, fileName + ".ttl")))

  val failures = for (
    result <- results
    if result.isInstanceOf[Failure]
  ) yield result

  val end = System.currentTimeMillis()

  println(end - start + "ms.")
  println(results.size + " records processed.")
  println(failures.size + " records failed.")

  for (failure <- failures) {
    println(failure.toString)
  }

  def mapFile(inFile: File, outFile: File): ExecutionResult = {
    try {
      val inData = new String(Files.readAllBytes(inFile.toPath), "utf8")
      val outData = map(inData)
      Files.write(outFile.toPath, outData.getBytes("utf8"), CREATE, TRUNCATE_EXISTING)
      Success(inFile.getAbsolutePath, outFile.getAbsolutePath)

    } catch {
      case e: Exception => Failure(inFile.getAbsolutePath, e)
    }
  }
}

sealed trait ExecutionResult {
  override def toString: String = super.toString
}

case class Success(inFile: String, outFile: String) extends ExecutionResult {
  override def toString: String = "Success: %s => %s".format(inFile, outFile)
}

case class Failure(inFile: String, exception: Exception) extends ExecutionResult {
  override def toString: String = {
    val sw = new StringWriter
    sw.write("Failure: %s \n".format(inFile))
    val pw = new PrintWriter(sw)
    exception.printStackTrace(pw)
    sw.toString
  }
}
