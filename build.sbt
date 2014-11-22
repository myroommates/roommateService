name := "roommateService"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.postgresql" % "postgresql" % "9.3-1101-jdbc41"
)     

play.Project.playJavaSettings
