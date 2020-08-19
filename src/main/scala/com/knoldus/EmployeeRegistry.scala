package com.knoldus

import com.knoldus.model.Employee
import slick.jdbc.MySQLProfile.api._
import slick.lifted.ProvenShape

import scala.concurrent.Future

//import scala.util.{Failure, Success}
class EmployeeSchema(tag: Tag) extends Table[Employee](tag, "employee") {
  def * : ProvenShape[Employee] = (employeeId, name, salary, designation) <> (Employee.tupled, Employee.unapply)

  def employeeId: Rep[Int] = column[Int]("ID", O.PrimaryKey)

  def name: Rep[String] = column[String]("NAME")

  def salary: Rep[Double] = column[Double]("SALARY")

  def designation = column[String]("DESIGNATION")
}

object EmployeeRegistry extends TableQuery(new EmployeeSchema(_)) {
  val db = Database.forConfig("mydb")

  def createTable: Future[Unit] = {
    //   val query = MTable.getTables map (tables => {
    //      if (!tables.exists(_.name.name == this.baseTableRow.tableName))
    //        this.schema.create
    //      else ""
    //    })
    val query = TableQuery[EmployeeSchema].schema.createIfNotExists
    db.run(query)

  }


  def insert(employee: Employee): Future[Int] = {
    val query = this += employee
    db.run(query)
  }

  def delete(employeeId: Int): Future[Int] = {
    val query = this.filter(_.employeeId === employeeId).delete
    db.run(query)
  }

  def get: Future[Seq[Employee]] = {
    db.run(this.result)
  }

  def getEmployeeById(employeeId: Int): Future[Seq[Employee]] = {
    val query = this.filter(_.employeeId === employeeId).result
    db.run(query)
  }

  def update(employeeId: Int, newSalary: Double, name: String): Future[Int] = {
    val query = this.filter(_.employeeId === employeeId).map(employee => (employee.salary, employee.name)).update(newSalary, name)
    db.run(query)
  }
}