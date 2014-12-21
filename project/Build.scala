import play.Project._
import sbt.Keys._
import sbt._

object ApplicationBuild extends Build {

  lazy val buildVersion = "2.2.4"
  lazy val playVersion = "2.2.4"

  val name = "roommateService"
  val version = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    javaCore,
    javaJdbc,
    javaJpa,
    cache,
    javaEbean,
    "org.postgresql" % "postgresql" % "9.3-1101-jdbc41",
    "org.jasypt" % "jasypt" % "1.9.2"
  )

  val main = play.Project(name, version, appDependencies)

  javaOptions ++= Seq("-Xmx512M", "-Xmx2048M", "-XX:MaxPermSize=2048M")

}