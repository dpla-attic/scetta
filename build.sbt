name := "scetta"

version := "0.0.1"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.2" % "provided"
libraryDependencies += "org.json4s" % "json4s-core_2.11" % "3.4.2"
libraryDependencies += "org.json4s" % "json4s-jackson_2.11" % "3.4.2"
libraryDependencies += "org.apache.jena" % "jena-tdb" % "3.1.0"

mainClass in assembly := some("la.dp.scetta.SparkMain")
assemblyJarName := "cdl-mapping-spark.jar"


val meta = """META.INF(.)*""".r

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case n if n.endsWith(".conf") => MergeStrategy.concat
  case meta(_) => MergeStrategy.discard
  case x => MergeStrategy.first
}