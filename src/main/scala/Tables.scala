import slick.lifted.{Rep, Tag}

import DB.dbConfig.driver.api._

/**
 * @author emiliocornejo
 * @version 02/11/15
 *          @(#)Tables.scala
 */


case class User(id: Int, name: String, password: String, admin: Boolean)

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  // the * projection (e.g. select * ...) auto-transforms the tupled
  // column values to / from a User
  def * = (id, name, password, admin) <>(User.tupled, User.unapply)

  // Auto Increment the id primary key column
  def id: Rep[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("NAME") // can't be null

  def password: Rep[String] = column[String]("PASSWORD")

  def admin: Rep[Boolean] = column[Boolean]("ADMIN") // true if it has admin privileges

}

case class Project(id: Int, name: String)

class Projects(tag: Tag) extends Table[Project](tag, "project") {

  def * = (id, name) <>(Project.tupled, Project.unapply) // * projection

  def id: Rep[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("NAME")

  def password: Rep[String] = column[String]("PASSWORD")
}

