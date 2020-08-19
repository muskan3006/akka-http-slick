package com.knoldus.model

case class Employee(employeeId: Int, name: String, salary: Double, designation: String)

//
//object Employee extends SprayJsonSupport with DefaultJsonProtocol{
//  implicit val format = jsonFormat2(Employee)
//}