package Admin
import java.sql.SQLException
import Account.AccountDetails
import scala.io.StdIn._
import Database.database

class Admin(val d : database) {

  def RegisterAdmin(): Unit = {
    println("--------------------------------------------------------\n" +
            "Enter name : Email : userId : Password and Mobile No \n" +
            "--------------------------------------------------------")

    val name: String = readLine()
    val email: String = readLine()
    val userId: String = readLine()
    val pass: String = readLine()
    val phone: Long = readLine().toLong
    d.Register(name, userId, pass, email, phone, "Admin")
  }

  def loginAdmin(): Unit = {
    println("-----------------------------------------------------\n" +
            "Enter UserId : Password\n" +
            "------------------------------------------------------")
    val userId: String = readLine()
    val pass: String = readLine()
    if (d.open()) {
      try {
        val statement = d.conn.createStatement()
        val res = statement.executeQuery(s"SELECT adminPass FROM Admin WHERE adminId = '$userId'")
        if (res.next()) {
          println("************** Hey you have login successfully  ***********************")
          d.close()
          moreToExploreAdmin(userId)
        }
      }catch {
        case e : SQLException => println("********* Sorry Invalid UserId-Password. Please try again :) ********** \n")
      }
      finally {
      d.close()
      }
    }
  }

  def profileDetails(userId : String):Unit = {

    if(d.open()){
      try{
        val statement = d.conn.createStatement()
        val res = statement.executeQuery(s"SELECT * FROM Admin WHERE adminId = '$userId' ")
        while (res.next()){
          println("----------------------------------------------------------------------------------------------------------------------------\n" +
            "Name : "+ res.getString(1)+" | UserId : "+ res.getString(2)+ " | Email : "+ res.getString(3)
            + " | Mob : "+ res.getLong(5)+"\n" +
            "-----------------------------------------------------------------------------------------------------------------------------")
        }
      }catch {
        case e : SQLException => println("************* Sorry Service Unavailable **************")
      }finally {
        d.close()
      }
    }

  }


  def customerDetails():Unit ={
    println("Do you want to see the customer details ? \n" +
            "----------------------------------------------------\n" +
            "For all customer  details press 1 else press 2 to search \n" +
            "----------------------------------------------------------\n")
          val choice = readInt()
    try {
      choice match {

        case 1 => if (d.open()) {
          val statement = d.conn.createStatement()
          val res = statement.executeQuery("SELECT * FROM Customer")
          while (res.next) {
            println("-------------------------------------------------------------------------------------------------------------\n" +
              "Name : " + res.getString(1) + " | UserId : " + res.getString(2) + " | Email : " +
              res.getString(3) + " | Password : " + res.getString(4) + " | Mob : " + res.getLong(5) +
              "\n-----------------------------------------------------------------------------------------------------------------\n")
          }
        }


        case 2 =>
          println("Enter the userId -------------->\n")
          val uId = readLine()
          if (d.open()) {
            val statement = d.conn.createStatement()
            val res = statement.executeQuery(s"SELECT * FROM Customer WHERE customerId = '$uId'")
            while (res.next) {
              println("-------------------------------------------------------------------------------------------------------------\n" +
                "Name : " + res.getString(1) + " | UserId : " + res.getString(2) + " | Email : " +
                res.getString(3) + " | Password : " + res.getString(4) + " | Mob : " + res.getLong(5) +
                "\n-----------------------------------------------------------------------------------------------------------------\n")
            }
          }
      }
    }catch {
      case e : SQLException => println("********* Sorry Service unavailable ***********")
    }finally {
      d.close()
    }
  }



  def moreToExploreAdmin(userId : String): Unit ={

    val details = new AccountDetails
    var flag : Boolean = true
    while(flag) {
      println(" Follow the given choice : and Press\n---------------------------------------------------------------\n" +
        " 1 : Get Account Details \t 2 : Get Customer Details \n 3 : Profile Details \t 4 : Exit \n " +
        "-------------------------------------------------------\n")
      val choice = readInt()
      choice match {
        case 1 => details.getAccountDetails()
        case 2 => customerDetails()
        case 3 => profileDetails(userId)
        case 4 => flag = false
        case _ => println("(*)(*)(*)(*)(*)(*)(*)(*)--> Sorry you have have entered wrong choice <--(*)(*)(*)(*)(*)(*)(*)(*) ")
      }
    }

  }

}
