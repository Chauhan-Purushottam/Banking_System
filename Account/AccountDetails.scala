package Account
import scala.io.StdIn._
import Database.database
class AccountDetails {

  val d = new database

  def getAccountDetails(): Unit = {

    println("-----------------------------------------------------------------------------------\n" +
      "Do you want the details of accounts ? press 1 : for All account\t 2 : for particular one \n" +
      "------------------------------------------------------------------------------------\n")
    val choice = readInt()

    choice match {

      case 1 => if (d.open()) {
        val statement = d.conn.createStatement()
        val res = statement.executeQuery("SELECT * FROM account")
        while (res.next) {
          println("-----------------------------------------------------------------------------------------------\n" +
            "UserId : " + res.getString(1) + " | Account No : " + res.getInt(2) + " | Balance : " +
            res.getDouble(3) +
            "\n---------------------------------------------------------------------------------------------")
        }
        d.close()
      }


      case 2 => println("Enter the userId -------------->\n")
        val uId = readLine()
        if (d.open()) {
          val statement = d.conn.createStatement()
          val res = statement.executeQuery(s"SELECT * FROM account WHERE userId = '$uId'")
          while (res.next) {
            println("-----------------------------------------------------------------------------------------------\n" +
              "UserId : " + res.getString(1) + " | Account No : " + res.getInt(2) + " | Balance : " +
              res.getDouble(3) +
              "\n---------------------------------------------------------------------------------------------\n")
          }
          d.close()
        }
    }
  }
}
