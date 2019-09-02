package Account
import java.sql._
import java.time.format.DateTimeFormatter

import Database.database

import scala.io.StdIn._
import scala.util._
import java.time.LocalDate


class paymentMode extends Enumeration{

  type payment = Value

  val atmPayment = Value("ATM")
  val netBanking = Value("NETBANKING")

  def otpGenerator(): Int = {
    val r = Random
    val start = 1000
    val otp = start + r.nextInt(8999)
    otp
  }

  def getCurrentTime: String ={
    val sdf : DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val d : LocalDate = LocalDate.now()
    val s :String = sdf.format(d)
    s
  }

}


class BankAccount(val userId : String) {

  val mode = new paymentMode
  val d = new database
  def createAccount():Unit ={
      if(!isAccountExist){
      val r = Random
      val start = 840000000
      val account = start + r.nextInt(1000000)
      val balance = 0.0

      if(d.open()){
        try{
          val statement = d.conn.createStatement()
          statement.execute(s"INSERT INTO account(userId,accountNumber,balance) VALUES('$userId',$account,$balance)")
          println("*********************************************************************\n" +
                  "Hey your account has opened Here is your accountNumber A/C = " + account
                  +"\n**********************************************************************\n")
        }catch {
          case e : SQLException => println("********* Sorry There is some Issue No account has opened. Please try Again :) ********** \n")
        }finally {
          d.close()
        }
      }
    }else{
        println("-------------------------------------------------------------------------------\n" +
                "Sorry you have already an account.\n" +
                "Please let us know if you are facing any problem in it\n" +
                "--------------------------------------------------------------------------------\n")
      }
  }

  def isAccountExist: Boolean = {
    var flag : Boolean = false
      if(d.open()){
        try {
          val statement = d.conn.createStatement()
          val res = statement.executeQuery(s"SELECT accountNumber FROM account WHERE userId = '$userId'")
          if(res.next()){
            flag = true
          }
        }catch {
          case e : SQLException => println("***** Sorry There is something wrong we are unable to access your details.*****")
        }finally {
          d.close()
        }
      }
    flag
  }




  def deposit():Unit= {

    if (isAccountExist) {
      println("----------------------------------------------------------------------\n" +
        "Enter the amount you want to deposit\n" +
        "----------------------------------------------------------------------\n")
      val amount = readDouble()
      if (d.open()) {
        try {
          val statement = d.conn.createStatement()
          val res = statement.executeQuery(s"SELECT balance FROM account WHERE userId = '$userId'")
          if (res.next()) {

            if (amount > 0) {
              val bal = amount + res.getDouble(1)
              statement.execute(s"UPDATE account SET balance = $bal WHERE userId = '$userId'")

              println("----------------------------------------------------------------------\n" +
                "your account is credited by Rs. " + amount + " on " + mode.getCurrentTime + "\nAvailable Balance is ₹ "  + bal + "\n" +
                "-----------------------------------------------------------------------\n")

            }
          }
        } catch {
          case e: SQLException => println("********* Sorry Link failed : We will get you back in sometime :) ********** \n")
        } finally {
          d.close()
        }
      }
    }else{
      println("*****Hey you have not open the account yet please open the account first.*****")
    }
  }


  def withdrawn(): Unit = {
    if (isAccountExist) {
      println("-----------------------------------------------------------------------------------\n" +
        "Select the mode to withdraw money " + "\n1 : " + mode.atmPayment + "\t 2 : " + mode.netBanking +
        "\n-------------------------------------------------------------------------------------")
      val choice = readInt()

      choice match {
        case 1 => {
          val otp = mode.otpGenerator()
          println("************** Here is your OTP < "+ otp + " > Please Enter the OTP ********************");
          val receivedOtp = readInt()
          if (otp == receivedOtp){
            println("-------------------------------------------------------------------------\n" +
              "Enter the amount you want to withdraw\n" +
              "-------------------------------------------------------------------------\n ")
            val amount = readDouble()

            if (d.open()) {
              try {
                val statement = d.conn.createStatement()
                val res = statement.executeQuery(s"SELECT balance FROM account WHERE userId = '$userId'")
                if (res.next()) {
                  val remainBal = res.getDouble(1) - amount
                  if (amount > 0 && remainBal >= 0) {

                    statement.execute(s"UPDATE account SET balance = $remainBal WHERE userId = '$userId'")
                    println("----------------------------------------------------------------------\n" +
                      "your account is debited by Rs. " + amount + " on " + mode.getCurrentTime + "\nAvailable Balance is ₹ "  + remainBal + "\n" +
                      "-----------------------------------------------------------------------\n")
                  } else {
                    println("******************** Sorry You have Exceeded your Limit ****************\n")
                  }
                }
              } catch {
                case e: SQLException => println("********* Sorry Link failed : We will get you back in sometime :) ********** \n")
              } finally {
                d.close()
              }
            } else {
              println("*****Hey you have not open the account yet please open the account first.*****")
            }
          }else{
            println(" ********** Sorry You have entered wrong OTP ************ ")
          }

        }

        case 2 => {

          println("-------------------------------------------------------------------------\n" +
            "Enter the amount you want to withdraw\n" +
            "-------------------------------------------------------------------------\n ")
          val amount = readDouble()

          if (d.open()) {
            try {
              val statement = d.conn.createStatement()
              val res = statement.executeQuery(s"SELECT balance FROM account WHERE userId = '$userId'")
              if (res.next()) {

                val remainBal = res.getDouble(1) - amount
                if (amount > 0 && remainBal >= 0) {

                  statement.execute(s"UPDATE account SET balance = $remainBal WHERE userId = '$userId'")
                  println("----------------------------------------------------------------------\n" +
                    "your account is debited by Rs. " + amount + " on " + mode.getCurrentTime + "\nAvailable Balance is ₹ "  + remainBal + "\n" +
                    "-----------------------------------------------------------------------\n")

                } else {
                  println("******************** Sorry You have Exceeded your Limit ****************\n")
                }
              }
            } catch {
              case e: SQLException => println("********* Sorry Link failed : We will get you back in sometime :) ********** \n")
            } finally {
              d.close()
            }
          } else {
            println("*****Hey you have not open the account yet please open the account first.*****")
          }

        }

        case _ => println("************ Sorry wrong Option ************* ")
      }


    }
  }

  def getAccountDetails(): Unit ={

    if(d.open()){
      try {
        val statement = d.conn.createStatement()
        val res = statement.executeQuery(s"SELECT * FROM account WHERE userId = '$userId'")
        if (res.next()) {
          println("---------------------------------------------------------------------------------------------------------\n" +
            "UserId : " + res.getString(1) + " | Account Number : " + res.getInt(2) + " | Balance : " +
            res.getDouble(3) + "\n" +
            "---------------------------------------------------------------------------------------------------------\n")
        }
      }catch {
        case e : SQLException => e.printStackTrace()
      }finally {
        d.close()
      }
    }

  }

}

