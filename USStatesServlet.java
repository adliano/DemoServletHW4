/*
***************************************************************
   Author    : Adriano Alves (aalves3)
   Date      : Mar.20.2015
   Course    : CS211E Spring of 20115
   File Name : USStatesServlet.java
   Html File : usaStates.html
   Objective : HW4 Servlet program to work
               with tomcat , this program will
               recivie a name of a state or 
               capital from user using web browser
               http://[host]:8080/USAStates/usaState.html
               and it will display the answer

   How To Run : copy the mySQL connector to the directory
          $CATALINA_HOME/lib
            create a directories:
          $CATALINA_HOME/webapps/USAStates
            in this folder  place the html files
            and images (backgroung, .gif, etc)  
          $CATALINA_HOME/webapps/USAStates/WEB-INF
            in this folder place web.xml and porperty file 
          $CATALINA_HOME/webapps/USAStates/WEB-INF/classes
            in this folder place .class file
*****************************************************************
*/

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class USStatesServlet extends HttpServlet
{
   private static String urlHills, userId, passWord;
   private static final String PROPTFILE = "JDBC.prop";

   /******************************* doPost() *******************************/
   public void doPost(HttpServletRequest inReq, HttpServletResponse outRes)
                       throws ServletException, IOException
   {
      loadProperties(PROPTFILE);

      Connection conn = null;
      PrintWriter pw = null;
      Statement stmt;
      ResultSet rset;
      String strRequest = "";
      String strHtmlLine = "";

      outRes.setContentType("text/html");
      pw = outRes.getWriter();

      /******** HTML ********/
      pw.println("<html><head>");
      pw.println("<title>CS211E HW4 SERVLETS BY ADRIANO ALVES</title>");
      pw.println("<meta charset=UTF-8>");
      pw.println("<style type=text/css>");
      pw.println("body {background-image: url(flag_usa.jpg);}");
      pw.println("#hd1 {margin-top: 140px; color: red; font-size: 40px; font-weight: bolder;}");
      pw.println("#hd2 {color: red; font-size: 70px; font-weight: bolder;}");
      pw.println("#err {margin-top: 250px; color: red; font-size: 80px; font-weight: bolder;}");
      pw.println("</style>");
      pw.println("</head>");
      pw.println("<body>");

      try
      {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(urlHills, userId, passWord);
         stmt = conn.createStatement();

         strRequest = ""+ inReq.getParameter("user_input_text");
         rset = stmt.executeQuery("select capitals from usa where states='"
                                   +strRequest+"'");

         if( rset.next() )
         {
            pw.println("<h1 id='hd1'><strong><center>The Capital of "
                        +strRequest+" is : </strong></h1>");
            pw.println("<h1 id='hd2'><strong><center>"
                        +rset.getString(1)+"</strong></h1>");
         }
         else
         {
            rset = stmt.executeQuery("select states from usa where capitals='"
                                      +strRequest+"'");

            if( rset.next() )
            {
               pw.println("<h1 id='hd1'<strong><center>The state of "
                           +strRequest+" is : </strong></h1>");
               pw.println("<h1 id='hd2'><strong><center>"
                           +rset.getString(1)+"</strong></h1>");
            }
            else  pw.println("<h1 id='err'><strong><center>"
                             +"NO SUCH STATE OR CAPITAL</strong></h1>");
         }

         if( stmt != null ) stmt.close();

     } catch(Exception e)
       {
          pw.println("<h1 id=err>Exception: "+e.getMessage()+"</h1>");
       }
         pw.println("</body></html>");
         pw.close();
   }
   /******************************* doGet() **********************************/
   public void doGet(HttpServletRequest req, HttpServletResponse res)
                       throws ServletException, IOException
   {
      doPost(req, res);
   }
   /*********************** loadProperties() ******************************/
   public void loadProperties(String file)
   {
      Properties p = new Properties();

      try
      {
         p.load(getServletContext().getResourceAsStream("/WEB-INF/"+file));
         urlHills = p.getProperty("prop.URL");
         userId   = p.getProperty("prop.USERID");
         passWord = p.getProperty("prop.PSWD");
      } catch( Exception e ){}
   }
}
//********* END ************   
