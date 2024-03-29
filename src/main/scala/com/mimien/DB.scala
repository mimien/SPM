package com.mimien

import com.mimien.scenes.ManageProject.FileTable
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Awaitable, ExecutionContext, Future}
import scalafx.collections.ObservableBuffer

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

  private val files = TableQuery[Files] // query interface for Files table

  private val tablesExist: DBIO[Boolean] = MTable.getTables.map { tables =>
    val names = Vector("USERS", "PROJECTS", "PROJECTS_USERS")
    names.intersect(tables.map(_.name.name)) == names
  }
  private val create: DBIO[Unit] = (users.schema ++ projects.schema ++ projectsUsers.schema ++ files.schema).create
  private val insertUsers = users.map { u => (u.name, u.password, u.admin) } ++= Seq(("emilio", "cornejo", true), ("admin", "root", true))
  private val allUsers: DBIO[Seq[String]] = users.map(_.name).result
  private val createIfNotExist: DBIO[Unit] = tablesExist.flatMap { exist => if (exist) DBIO.seq(create, insertUsers) else DBIO.successful() }
  private val future = db.run(allUsers)
//    private val future = db.run(create >> insertUsers >> allUsers)

  awaitAndPrint(future)

  def awaitAndPrint[T](a: Awaitable[T])(implicit ec: ExecutionContext) = println(await(a))

  // selects user where user name equals given name
  def login(name: String): User = {
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

  def insertFile(name: String, format: String, phase: String, projectId: Option[Int]) = {
    await(db.run(files += File(name, format, phase, projectId)))
  }

  def listUsers = await(db.run(users.result))

  def listUserProjects(userId: Option[Int]): Seq[Project] = {
    val queryprojects = projectsUsers.filter(_.userId === userId).flatMap { up => up.project }
    await(db.run(queryprojects.result))
  }

  // Returns the id of the inserted project instead of the number of affected rows
  def addProject(name: String) = await(db.run((projects returning projects.map(_.id)) += Project(name)))

  def relateProjectUser(projectId: Option[Int], userId: Option[Int]) = {
    await(db.run(projectsUsers += ProjectUser(projectId, userId)))
  }

  def getFilesFrom(project: Project): Seq[File] = {
    val queryFiles = files.filter(_.projectId === project.id)
    await(db.run(queryFiles.result))
  }

}