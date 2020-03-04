
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
  "com.h2database" % "h2" % "1.4.197" % Test,
  "org.mockito" % "mockito-core" % "3.2.4" % Test,
// To provide an implementation of JAXB-API, which is required by Ebean.
  "javax.xml.bind" % "jaxb-api" % "2.3.1",
  "javax.activation" % "activation" % "1.1.1",
  "javax.el" % "javax.el-api" % "3.0.1-b06",
  "javax.el" % "el-api" % "2.2.1-b04",

)
//libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.18"