import play.Project._

name := """hello-play-java"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  javaCore,
	"org.webjars" %% "webjars-play" % "2.2.0", 
	"org.webjars" % "bootstrap" % "2.3.2",
  "postgresql" % "postgresql" % "8.4-702.jdbc4",
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
)

playJavaSettings
