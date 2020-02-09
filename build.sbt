
name := """gestor-recetas-mimo"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.1"

libraryDependencies += guice
enablePlugins(PlayEbean)
libraryDependencies ++= Seq(
  evolutions,
  jdbc,
  //"org.xerial" % "sqlite-jdbc" % "3.30.1",
  "org.postgresql" % "postgresql" % "42.2.9",
  // To provide an implementation of JAXB-API, which is required by Ebean.
  "javax.xml.bind" % "jaxb-api" % "2.3.1",
  "javax.activation" % "activation" % "1.1.1",
)
//libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.18"