package com.knoldus

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.knoldus.model.{Employee, Employees}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.{Failure, Success}

object EmployeeRoute extends JsonSupport {
  implicit val system: ActorSystem = ActorSystem()
  //  implicit val materializer: ActorMaterializer.type = ActorMaterializer
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val route: Route = pathPrefix("employee") {
    concat(
      pathEnd {
        concat(
          get {
            val employees = EmployeeRegistry.get
            onSuccess(employees) { employee: Seq[Employee] =>
              val emp = Employees(employee.toList)
              complete(emp)
            }
          },
          post {
            entity(as[Employee]) { employee =>
              val newEmployee = EmployeeRegistry.insert(employee)
              onSuccess(newEmployee) { _ => complete(StatusCodes.OK, "Employee Added") }
            }
          }
        )
      },
      path(Segment) { id =>

        concat(
          get {
            val employee = EmployeeRegistry.getEmployeeById(id.toInt)
            onSuccess(employee) { employee: Seq[Employee] =>
              val emp = Employees(employee.toList)
              complete(emp)
            }
          },
          delete {
            val employee = EmployeeRegistry.delete(id.toInt)
            onSuccess(employee) { _ =>
              complete(StatusCodes.OK, "Employee Deleted")
            }
          },
          put {
            parameters("salary".as[Int], "name") { (salary, name) =>
              val updated = EmployeeRegistry.update(id.toInt, salary, name)
              onSuccess(updated) { _ =>
                complete(StatusCodes.OK, "Updated")
              }
            }
          })

      })
  }

  def main(args: Array[String]): Unit = {
    val table = EmployeeRegistry.createTable
    table onComplete {
      case Success(_) => val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
        println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
        StdIn.readLine() // let it run until user presses return
        bindingFuture
          .flatMap(_.unbind()) // trigger unbinding from the port
          .onComplete(_ => system.terminate()) // and shutdown when done
      case Failure(exception) => println(s"Cannot create the table $exception")
    }
  }
}

