package Customer
import java.sql.SQLException
import java.util._
import Account.BankAccount
import Database.database

class Customer(val d : database) {
  var input = new Scanner(System.in)
  def RegisterCustomer(): Unit ={

    println("------------------------------------------------------------------\n" +
            "Enter name : Email : userId : Password and Mobile No \n" +
             "------------------------------------------------------------------\n")

    val name : String = input.nextLine()
    val email : String = input.nextLine()
    val userId : String = input.nextLine()
    val pass :String = input.nextLine()
    val phone : Long = input.nextLine().toLong
    d.Register(name,userId,pass,email,phone,"Customer")

  }

  def loginCustomer(): Unit ={
    println("-------------------------------------------------------\n" +
            "Enter UserId : Password\n" +
            "-------------------------------------------------------\n")
    val userId : String = input.nextLine()
    val pass: String = input.nextLine()
    if(d.open()){
      try {
        val statement = d.conn.createStatement()
        val res = statement.executeQuery(s"SELECT customerPass FROM Customer WHERE customerId = '$userId'")
        if (res.next()) {
          println("********************** Hey you have login successfully **********************")
          d.close()
          moreToExplore(userId)
        }
      }catch {
        case e : SQLException => println("********* Sorry Invalid UserId-Password. Please try again :) ********** \n")
      }finally {
        d.close()
      }
    }
  }

  def profileDetails(userId : String):Unit = {

    if(d.open()){
      try{
      val statement = d.conn.createStatement()
      val res = statement.executeQuery(s"SELECT * FROM Customer WHERE customerId = '$userId' ")
      while (res.next()){
        println("----------------------------------------------------------------------------------------------------------------------------\n" +
          "Name : "+ res.getString(1)+" | UserId : "+ res.getString(2)+ " | Email : "+ res.getString(3)
            + " | Mob : "+ res.getLong(5)+"\n" +
            "-----------------------------------------------------------------------------------------------------------------------------\n")
      }
    }catch {
        case e : SQLException => println("************* Sorry Service Unavailable **************")
      }finally {
        d.close()
      }
    }

  }

  def updateProfile(userId : String): Unit = {
    println("What do you want to update ? Follow the bellow instruction and press\n" +
            "-------------------------------------------------------------------------\n" +
            "1 : name \t 2 : Email \n3 : Mob No\n" +
            "--------------------------------------------------------------------------\n")
    val choice = input.nextInt()
    try {
      if(d.open()) {
        val statement = d.conn.createStatement()
        choice match {
          case 1 => println("Enter your new Name --> \n")
            val name = input.next()
            statement.execute(s"UPDATE Customer SET customerName = '$name' WHERE customerId = '$userId'")
            println("************* Hey your name updated successfully **************** ")

          case 2 => println("Enter your new Email --> \n")
            val email = input.next()
            statement.execute(s"UPDATE Customer SET customerEmail = '$email' WHERE customerId = '$userId'")
            println("************* Hey your Email updated successfully **************** ")

          case 3 => println("Enter your new Mob No --> \n")
            val mob = input.next().toLong
            statement.execute(s"UPDATE Customer SET customerMob = '$mob' WHERE customerId = '$userId'")
            println("************* Hey your Mobile No. updated successfully **************** ")

          case _ => println("Hey You have entered wrong choice")

        }
      }
    }catch {
      case e : SQLException => println("********* Sorry No update possible right now *********")
    }finally {
      d.close()
    }
  }


  def moreToExplore(userId : String): Unit ={
    val bank = new BankAccount(userId)
    var flag : Boolean = true
    while(flag) {
      println(" Follow the given choice : and Press\n---------------------------------------------------------------\n" +
        " 1 : CREATE NEW ACCOUNT \t 2 : DEPOSIT MONEY \t 3 : WITHDRAW \n 4 : CHECK ACCOUNT DETAILS\t 5 : PROFILE DETAILS \t 6 : UPDATE PROFILE \n " +
        "7 : EXIT \n-------------------------------------------------------\n")
      val choice = input.nextInt()
      choice match {
        case 1 => bank.createAccount()
        case 2 => bank.deposit()
        case 3 => bank.withdrawn()
        case 4 => bank.getAccountDetails()
        case 5 => profileDetails(userId)
        case 6 => updateProfile(userId)
        case 7 => flag = false
        case _ => println("(*)(*)(*)(*)(*)(*)(*)(*)--> Sorry you have have entered wrong choice <--(*)(*)(*)(*)(*)(*)(*)(*) ")
      }
    }
  }

}

