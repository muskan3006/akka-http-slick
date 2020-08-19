package com.knoldus

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.knoldus.model.{Employee, Employees}
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol{
  implicit val format = jsonFormat4(Employee)
  implicit val employeesFormat = jsonFormat1(Employees)
}
