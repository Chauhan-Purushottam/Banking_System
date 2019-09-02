package Database
import java.sql._
class database {

  var conn: Connection = _


   def open(): Boolean = {
    try{
//        println("--------------------------------------------------------------\n" +
//                "Trying to connect with Database .....\n" +
//                "--------------------------------------------------------------\n")
        conn = DriverManager.getConnection("jdbc:sqlite:/Users/apple/Desktop/BankingSystem/Banking.db")
        true
   }catch {
      case e : SQLException => println("************ Sorry There is an error in Database connection\n ")
        false
    }
   }


  def close(): Unit ={
    if(!conn.isClosed){
      conn.close()
    }
  }


  def create(): Unit ={

    if(open()){

      val statement = conn.createStatement()
      statement.execute("CREATE TABLE IF NOT EXISTS Customer(customerName TEXT, customerId TEXT," +
        "customerEmail TEXT, customerPass TEXT, customerMob INT, PRIMARY KEY(customerName, customerId))")

      statement.execute("CREATE TABLE IF NOT EXISTS Admin(adminName TEXT, adminId TEXT," +
        "adminEmail TEXT, adminPass TEXT, adminMob INT, PRIMARY KEY(adminName, adminId))")

      statement.execute("CREATE TABLE IF NOT EXISTS account(userId TEXT, accountNumber INT, balance REAL " +
        ", PRIMARY KEY(userId, accountNumber))")
      close()
    }

  }


  def Register(name : String,userId : String,pass : String,email : String,phone :Long, Type : String){

      if(open()){

        val statement = conn.createStatement()
        Type match {

        case "Customer" => statement.execute ("INSERT INTO Customer(customerName,customerId,customerEmail,customerPass,customerMob) " +
        s"VALUES('$name','$userId','$email','$pass',$phone)")
          close()


        case "Admin" => statement.execute ("INSERT INTO Admin(adminName,adminId,adminEmail,adminPass,adminMob) " +
          s"VALUES('$name','$userId','$email','$pass',$phone)")
            close()


        case _ => close()
        }
        println("***************** Registered Successfully ***********************\n")
      }
  }


}
