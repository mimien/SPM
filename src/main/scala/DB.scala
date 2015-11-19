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

  val db = dbConfig.db

  import dbConfig.driver.api._

  // imports all the DSL goodies for the configured database

  val users = TableQuery[Users] // query interface for Users table

  val projects = TableQuery[Projects] // query interface for Projects table

  val tablesExist: DBIO[Boolean] = MTable.getTables.map { tables =>
    val names = Vector(users.baseTableRow.tableName, projects.baseTableRow.tableName)
    names.intersect(tables.map(_.name.name)) == names
  }
  val create: DBIO[Unit] = (users.schema ++ projects.schema).create
  val insertUsers = users.map { u => (u.name, u.password, u.admin) } ++= Seq(("emilio", "cornejo", true), ("admin", "root", true))
  val createIfNotExist = tablesExist.flatMap(exist => if (!exist) create else DBIO.successful())
  val listUsers: DBIO[Seq[String]] = users.map(_.name).result
  val future = db.run(listUsers)

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

  awaitAndPrint(future)
}
