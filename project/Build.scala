import sbt.Build
import sbt.Keys._

object ApplicationBuild extends Build {

  lazy val buildVersion = "2.2.4"
  lazy val playVersion = "2.2.4"

  val name = "roommateService"
  val version = "1.0-SNAPSHOT"

  val projectDependencies = Seq(
    javaJdbc,
    javaEbean,
    cache,
    "org.postgresql" % "postgresql" % "9.3-1101-jdbc41",
    "org.jasypt" % "jasypt" % "1.9.2",
    "org.apache.velocity" % "velocity" % "1.7"
  )

  val main = play.Project(name, version, projectDependencies)

  javaOptions ++= Seq("-Xmx512M", "-Xmx2048M", "-XX:MaxPermSize=2048M")

}