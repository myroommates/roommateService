name := "roommateService"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.jasypt" % "jasypt" % "1.9.2",
  "org.postgresql" % "postgresql" % "9.3-1101-jdbc41",
  "org.apache.velocity" % "velocity" % "1.7"
)     

play.Project.playJavaSettings
