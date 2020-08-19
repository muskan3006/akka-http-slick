name := "akka-slick"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq (
  "com.typesafe.akka" %% "akka-http" % "10.2.0",
  "com.typesafe.akka" %% "akka-stream" % "2.6.8",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.0",
  "com.typesafe.akka" %% "akka-actor" % "2.6.8",
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "mysql" % "mysql-connector-java" % "6.0.6",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "com.typesafe.slick" %% "slick-codegen" % "3.3.2",
 "org.slf4j" % "slf4j-nop" % "1.7.3"

)