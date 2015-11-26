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

  // imports all the DSL goodies for the configured database
  import dbConfig.driver.api._

  private val db = dbConfig.db

  private val users = TableQuery[Users] // query interface for Users table

  private val projects = TableQuery[Projects] // query interface for Projects table

  private val projectsUsers = TableQuery[ProjectsUsers] // query interface for ProjectsUsers table

  private val tablesExist: DBIO[Boolean] = MTable.getTables.map { tables =>
    val names = Vector("USERS", "PROJECTS", "PROJECTS_USERS")
    //
    val bool = names.intersect(tables.map(_.name.name)) == names
    println("hola")
    bool
  }
  private val create: DBIO[Unit] = (users.schema ++ projects.schema ++ projectsUsers.schema).create
  private val insertUsers = users.map { u => (u.name, u.password, u.admin) } ++= Seq(("emilio", "cornejo", true), ("admin", "root", true))
  private val allUsers: DBIO[Seq[String]] = users.map(_.name).result
  private val createIfNotExist: DBIO[Unit] = tablesExist.flatMap { exist => if (exist) DBIO.seq(create, insertUsers) else DBIO.successful() }
  private val future = db.run(allUsers)
  //  private val future = db.run(create >> insertUsers >> allUsers)

  awaitAndPrint(future)

  def awaitAndPrint[T](a: Awaitable[T])(implicit ec: ExecutionContext) = println(await(a))

  // selects user where username equals given
  def login(name: String): User = {
    val query = users.filter(_.name === name)
    val action = query.result
    val future: Future[Seq[User]] = db.run(action)
    await(future).headOption.orNull
  }

  /** insert user to database
    * admin privileges are false for default
    * */
  def insertUser(name: String, password: String) = await(db.run(users += User(name, password)))

  def listUsers = await(db.run(users.result))

  def await[T](a: Awaitable[T])(implicit ec: ExecutionContext) = Await.result(a, Duration.Inf)

  def listUserProjects(userId: Option[Int]): Seq[Project] = {
    val queryprojects = projectsUsers.filter(_.userId === userId).flatMap { up => up.project }
    await(db.run(queryprojects.result))
  }

  // Returns the id of the inserted project instead of the number of affected rows
  def addProject(name: String) = await(db.run((projects returning projects.map(_.id)) += Project(name)))

  def relateProjectUser(projectId: Option[Int], userId: Option[Int]) = {
    await(db.run(projectsUsers += ProjectUser(projectId, userId)))
  }
}
