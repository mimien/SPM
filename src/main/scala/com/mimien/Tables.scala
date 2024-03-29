package com.mimien

import slick.lifted.{Rep, Tag}

import DB.dbConfig.driver.api._

/**
  * @author emiliocornejo
  * @version 02/11/15
  *          @(#)Tables.scala
  */

case class User(name: String, password: String, isAdmin: Boolean = false, id: Option[Int] = None) {
  def isNotAdmin = !isAdmin

  override def toString = name
}

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  // the * projection (e.g. select * ...) auto-transforms the tupled
  // column values to / from a User
  def * = (name, password, admin, id) <>(User.tupled, User.unapply)

  // Auto Increment the id primary key column
  def id: Rep[Option[Int]] = column[Option[Int]]("ID", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("NAME") // can't be null

  def password: Rep[String] = column[String]("PASSWORD")

  def admin: Rep[Boolean] = column[Boolean]("ADMIN") // true if it has admin privileges

}

case class Project(name: String, id: Option[Int] = None) {
  override def toString = name
}

class Projects(tag: Tag) extends Table[Project](tag, "PROJECTS") {

  def * = (name, id) <>(Project.tupled, Project.unapply) // * projection

  def id: Rep[Option[Int]] = column[Option[Int]]("ID", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("NAME")
}

case class ProjectUser(project: Option[Int], user: Option[Int])

class ProjectsUsers(tag: Tag) extends Table[ProjectUser](tag, "PROJECTS_USERS") {

  def * = (projectId, userId) <>(ProjectUser.tupled, ProjectUser.unapply)

  def projectId: Rep[Option[Int]] = column[Int]("PROJECT_ID")

  def userId: Rep[Option[Int]] = column[Int]("USER_ID")

  def project = foreignKey("PROJECT_FK", projectId, TableQuery[Projects])(_.id)

  def user = foreignKey("USER_FK", userId, TableQuery[Users])(_.id)
}

case class File(name: String, format: String, phase: String, projectId: Option[Int] = None, id: Option[Int] = None) {
  override def toString = name

  def path = s"src/main/resources/files/$projectId/$name.$format"
}

class Files(tag: Tag) extends Table[File](tag, "FILES") {

  def * = (name, format, phase, projectId, id) <>(File.tupled, File.unapply)

  def id: Rep[Option[Int]] = column[Option[Int]]("ID", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("NAME")

  def format: Rep[String] = column[String]("FORMAT")

  def phase: Rep[String] = column[String]("PHASE")

  def projectId: Rep[Option[Int]] = column[Int]("PROJECT_ID")

  def project = foreignKey("PROJECT_FK", projectId, TableQuery[Projects])(_.id)
}