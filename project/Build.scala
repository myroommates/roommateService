import play.Project._
import sbt.Keys._
import sbt._

object ApplicationBuild extends Build {

  lazy val buildVersion = "2.2.4"
  lazy val playVersion = "2.2.4"

  val name = "roommateservice"
  val version = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    javaCore,
    javaJdbc,
    javaJpa
      .exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api")
      .exclude("javax.persistence.persistence-api", "persistence-api-1.0.2"),
    cache,
    "org.hibernate" % "hibernate-entitymanager" % "4.3.5.Final",
    "org.hibernate" % "hibernate-ehcache" % "4.3.5.Final",
    "org.hibernate.javax.persistence" % "hibernate-jpa-2.1-api" % "1.0.0.Final",
    "org.postgresql" % "postgresql" % "9.4-1204-jdbc42",
    "org.jasypt" % "jasypt" % "1.9.2",
    "commons-beanutils" % "commons-beanutils" % "1.9.2",
    "org.apache.velocity" % "velocity" % "1.7",
    "org.apache.commons" % "commons-email" % "1.3.1",
    "commons-io" % "commons-io" % "2.3"
  )

  val main = play.Project(name, version, appDependencies)

  javaOptions ++= Seq("-Xmx512M", "-Xmx2048M", "-XX:MaxPermSize=2048M")

}