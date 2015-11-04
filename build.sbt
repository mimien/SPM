name := "Software-Project-Management"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "8.0.60-R9",
  "com.typesafe.slick" %% "slick" % "3.1.0",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.1.0",
  "org.postgresql" % "postgresql" % "9.4-1204-jdbc4",
  "com.zaxxer" % "HikariCP" % "2.4.1",
  "org.slf4j" % "slf4j-nop" % "1.7.12"
)
