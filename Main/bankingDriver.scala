package Main

import Admin.Admin
import Customer.Customer
import Database.database
import scala.io.StdIn._


object bankingDriver {

  def main(args: Array[String]): Unit = {
    while (true) {
      println("------------------------------------------------\n" +
        "Are you new User ? Please Register First\nPress 1 to Register \t  2 to signIn." +
        "\t 3 to Exit\n" +
        "--------------------------------------------------------")
      val choice = readInt()
      val d = new database
      val customer = new Customer(d)
      val admin = new Admin(d)
      choice match {

        case 1 => println("---------------------------------------------\n" +
          "Press 1 If you are Admin else for User press 2\n" +
          "---------------------------------------------\n")
          val innerChoice = readInt()
          innerChoice match {
            case 1 => admin.RegisterAdmin()
            case 2 => customer.RegisterCustomer()
            case _ => println("Invalid choice No Registration possible")
          }

        case 2 => println("---------------------------------------------\n" +
          "Press 1 If you are Admin else for User press 2\n" +
          "---------------------------------------------\n")
          val innerChoice = readInt()
          innerChoice match {
            case 1 => admin.loginAdmin()
            case 2 => customer.loginCustomer()
            case _ => println("Invalid choice No login possible")
          }
        case 3 => sys.exit(1)
        case _ => println("Hey You have entered wrong choice")
      }
    }
  }
}
