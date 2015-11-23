package com.mimien

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.meta.MTable

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Awaitable, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author emiliocornejo
  * @version 02/11/15
  *          @(#)DB.scala
  */
object DB {
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("postgres")

  private val db = dbConfig.db

  import dbConfig.driver.api._

  // imports all the DSL goodies for the configured database

  private  val users = TableQuery[Users] // query interface for Users table

  private  val projects = TableQuery[Projects] // query interface for Projects table

  private  val tablesExist: DBIO[Boolean] = MTable.getTables.map { tables =>
    val names = Vector(users.baseTableRow.tableName, projects.baseTableRow.tableName)
    names.intersect(tables.map(_.name.name)) == names
  }
  private val create: DBIO[Unit] = (users.schema ++ projects.schema).create
  private val insertUsers = users.map { u => (u.name, u.password, u.admin) } ++= Seq(("emilio", "cornejo", true), ("admin", "root", true))
  private val createIfNotExist = tablesExist.flatMap(exist => if (!exist) create else DBIO.successful())
  private val allUsers: DBIO[Seq[String]] = users.map(_.name).result
  private val future = db.run(allUsers)

  def awaitAndPrint[T](a: Awaitable[T])(implicit ec: ExecutionContext) = println(await(a))

  def await[T](a: Awaitable[T])(implicit ec: ExecutionContext) = Await.result(a, Duration.Inf)

  // return password and admin boolean of given username
  def login(name: String): (String, Boolean) = {
    // SELECT (password, admin) FROM users WHERE name == (name input)
    val query = users.filter(_.name === name).map { u => (u.password, u.admin) }
    val action = query.result
    val future: Future[Seq[(String, Boolean)]] = db.run(action)
    await(future).headOption.orNull
  }

/**  insert user to database
   * admin privileges are false for default
   */
  def insertUser(name: String, password: String) = await(db.run(users += User(name, password)))

  def listUsers = await(future)
}
