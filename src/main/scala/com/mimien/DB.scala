package com.mimien

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Awaitable, ExecutionContext, Future}

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

  private val users = TableQuery[Users] // query interface for Users table

  private val projects = TableQuery[Projects] // query interface for Projects table

  private val projectsUsers = TableQuery[ProjectsUsers] // query interface for ProjectsUsers table

  private val tablesExist: DBIO[Boolean] = MTable.getTables.map { tables =>
    val names = Vector(users.baseTableRow.tableName, projects.baseTableRow.tableName)
    names.intersect(tables.map(_.name.name)) == names
  }
  private val create: DBIO[Unit] = (users.schema ++ projects.schema ++ projectsUsers.schema).create
  private val insertUsers = users.map { u => (u.name, u.password, u.admin) } ++= Seq(("emilio", "cornejo", true), ("admin", "root", true))
  private val createIfNotExist = tablesExist.flatMap(exist => if (!exist) create else DBIO.successful())
  private val allUsers: DBIO[Seq[String]] = users.map(_.name).result
  private val allProjects: DBIO[Seq[String]] = projects.map(_.name).result
  private val future = db.run(allUsers)
//  private val future = db.run(create >> insertUsers >> allUsers)

  awaitAndPrint(future)

  def awaitAndPrint[T](a: Awaitable[T])(implicit ec: ExecutionContext) = println(await(a))

  // return password and admin boolean of given username
  def login(name: String): User = {
    // SELECT (password, admin) FROM users WHERE name == (name input)
    val query = users.filter(_.name === name)
    val action = query.result
    val future: Future[Seq[User]] = db.run(action)
    await(future).headOption.orNull
  }

  def await[T](a: Awaitable[T])(implicit ec: ExecutionContext) = Await.result(a, Duration.Inf)

  /** insert user to database
    * admin privileges are false for default
    * */
  def insertUser(name: String, password: String) = await(db.run(users += User(name, password)))

  def listUsers = await(db.run(allUsers))

  def listUserProjects(userId: Option[Int]): Seq[Project] = {
    val queryprojects = projectsUsers.filter(_.userId === userId).flatMap { up => up.project }
    await(db.run(queryprojects.result))
  }

  def addProject(name: String) = await(db.run(projects += Project(name)))

  def relateProjectUser(projectId: Option[Int], username: String) = {
    val queryUId = users.filter(_.name === username).map { u => u.id }
    val userId = await(db.run(queryUId.result))
    await(db.run(projectsUsers += ProjectUser(projectId, userId.head)))
  }
}
