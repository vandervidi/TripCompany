package com.shenkar.tripcompany;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/controller/*")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Boolean db=false;
	Connection connection = null;
	
	String createTripTable = "CREATE TABLE trip ("
            +"        tripId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            +"        name VARCHAR(30), "
            +"        startDate VARCHAR(30), "
            +"        endDate VARCHAR(30), "
            +"        numOfTravelers INT(4), "
            +"        ratePerTraveler FLOAT(4) "
            +"        )";
	String createInstructorTable = "CREATE TABLE instructor ("
	        +"        instructorId VARCHAR(30) NOT NULL PRIMARY KEY, "
	        +"        name VARCHAR(30), "
	        +"        lastName VARCHAR(30), "
	        +"        address VARCHAR(30) "
	        +"        )";
	String createSiteTable = "CREATE TABLE site ("
	        +"        name VARCHAR(30) NOT NULL PRIMARY KEY, "
	        +"        instructorId VARCHAR(30), "
	        +"        duration VARCHAR(5) "
	        +"        )";
	String createTravelerTable = "CREATE TABLE traveler ("
	        +"        travelerId VARCHAR(30) NOT NULL PRIMARY KEY, "
	        +"        name VARCHAR(30), "
	        +"        lastName VARCHAR(30), "
	        +"        rate FLOAT(4) "                //maybe a mistake to have rate in traveler
	        +"        )";
	String createManagerTable = "CREATE TABLE manager ("
	        +"        managerId VARCHAR(30) NOT NULL PRIMARY KEY, "
	        +"        name VARCHAR(30), "
	        +"        lastName VARCHAR(30), "
	        +"        address VARCHAR(30), "                //maybe a mistake to have rate in traveler
	        +"        managingArea VARCHAR(30) "
	        +"        )";
	
	
	String dropProcedureTripsCheaperThenProcdure= "DELIMITER //"
	        + "DROP PROCEDURE IF EXISTS tripsCheaperThenProcdure //"
	        + "DELIMITER ;";
	
	String tripsCheaperThenProcdure = "DELIMITER // "
	        +" CREATE PROCEDURE getTripsCheaperThen(IN tripPrice FLOAT) "
	        +" BEGIN "
	        +" SELECT * FROM trip WHERE ratePerTraveler < tripPrice; "
	        +" END // "
	        +" DELIMITER ; ";
	
	String initTrigerBeforeDelete = "DELIMITER // "
			+" CREATE TRIGER initTrigerBeforeDelete"
			+" {BEFORE}"
			+" {DELETE}"
			+" ON trip"
			+" ON EACH ROW"
			+" @n= NEW.ID"
			+" @o= OLD.ID"
			+" @n=o"
			+" DELIMITER ; ";

	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        System.out.println("controller constructor");
        System.out.println("db="+db);
        try{
        	if (db==false){
	    		System.out.println("create tables");
	    		
		        //Create tables
	
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://localhost/tripcompany", "jaja", "gaga");
	        	Statement statement = connection.createStatement();
	              
	            //executing statements the create the main tables
				statement.executeUpdate(createTripTable);
				statement.executeUpdate(createInstructorTable);
	            statement.executeUpdate(createSiteTable);
	            statement.executeUpdate(createTravelerTable);
	            statement.executeUpdate(createManagerTable);
	            statement.close();
	            
	            //executing a procedure code
	            //statement.executeUpdate(dropProcedureTripsCheaperThenProcdure);
	            //statement.executeUpdate(tripsCheaperThenProcdure);
	            //statement.executeUpdate(initTrigerBeforeDelete);
	            db=true;		
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String str = request.getPathInfo();
		System.out.println("doGet");
		try {
		    
	    	

		    
////--------------Add------------------------------------------------ 
			//working!
	    	if (str.equals("/addTrip")){
				String tripName= request.getParameter("tripName");
				String startDate= request.getParameter("startDate");
				String endDate= request.getParameter("endDate");
				String ratePerTraveler= request.getParameter("ratePerTraveler");
				String numOfTravelers= request.getParameter("numOfTravelers");
				PreparedStatement prepstate = connection.prepareStatement
				("INSERT INTO `tripCompany`.`trip` (`name`, `startDate`, `endDate`, `numOfTravelers`, `ratePerTraveler`) "
						+ "VALUES (?, ?, ?, ?, ?)");
				prepstate.setString(1, tripName);
				prepstate.setString(2, startDate);
				prepstate.setString(3, endDate);
				prepstate.setInt(4, Integer.parseInt(numOfTravelers));
				prepstate.setDouble(5, Double.parseDouble(ratePerTraveler));
				prepstate.execute();
				prepstate.close();
					
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher("/views/index.jsp");
				dispatcher.forward(request, response);	
		    }
	    	//working!
		    else if(str.equals("/addInstructor")){
		    	String instructorId= request.getParameter("instructorId");
				String firstName= request.getParameter("firstName");
				String lastName= request.getParameter("lastName");
				String address= request.getParameter("address");
				PreparedStatement prepstate = connection.prepareStatement
				("INSERT INTO `tripCompany`.`instructor` (`instructorId`, `name`, `lastName`, `address`) "
						+ "VALUES (?, ?, ?, ?)");
				prepstate.setString(1, instructorId);
				prepstate.setString(2, firstName);
				prepstate.setString(3, lastName);
				prepstate.setString(4, address);
				prepstate.execute();
				prepstate.close();
				
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher("/views/index.jsp");
				dispatcher.forward(request, response);
		    }
	    	//working!
			else if(str.equals("/addSite")){
				String siteName= request.getParameter("siteName");
				String instructorId= request.getParameter("instructorId");
				// optional: check if instuctor-id exist in `tripcompany.instructor` table
				String duration= request.getParameter("duration");
				PreparedStatement prepstate = connection.prepareStatement
				("INSERT INTO `tripCompany`.`site` (`name`, `instructorId`, `duration`) "
						+ "VALUES (?, ?, ?)");
				prepstate.setString(1, siteName);
				prepstate.setString(2, instructorId);
				prepstate.setString(3, duration);
				prepstate.execute();
				prepstate.close();
				
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher("/views/index.jsp");
				dispatcher.forward(request, response);		
			}
		    

	    	
	    	
////---------preview (before update)----------------------------------------------------- 
			else if (str.equals("/tripUpdatePreview")){
                //Select in database the trip with the name passed to the controller
                //then pass the trip details to jsp preview form
				String tripUpdateName= request.getParameter("tripUpdateName");
				// Trip fields
				String tripName = null;
                String startDate= null;
                String endDate= null;
                double ratePerTraveler = 0;
                int numOfTravelers = 0;
                int id=0;
                try {
	                	PreparedStatement prepstate = connection.prepareStatement ("SELECT * FROM trip WHERE name=?");
	                    prepstate.setString(1, tripUpdateName);
	                    ResultSet rs = prepstate.executeQuery();
	                    while (rs.next())
	                    {
	                    	if(rs.getString("name").equals(tripUpdateName)){
	                            id=        rs.getInt("tripId");
	                            tripName = rs.getString("name");
	                            startDate = rs.getString("startDate");
	                            endDate = rs.getString("endDate");
	                            ratePerTraveler = rs.getDouble("ratePerTraveler");
	                            numOfTravelers = rs.getInt("numOfTravelers"); 
	                            break;
	                    	}
	                    }
	                    request.setAttribute("id", id);
	                    request.setAttribute("tripName", tripName);
	                    request.setAttribute("startDate", startDate);
	                    request.setAttribute("endDate", endDate);
	                    request.setAttribute("ratePerTraveler", ratePerTraveler);
	                    request.setAttribute("numOfTravelers", numOfTravelers);
                        
                } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                
                RequestDispatcher dispatcher = getServletContext()
                                .getRequestDispatcher("/views/tripUpdatePreview.jsp");
                dispatcher.forward(request, response);        
			}

			else if(str.equals("/instructorUpdatePreview")){
				String instructorUpdateId= request.getParameter("instructorUpdateId");
				// instructor fields
				String instructorId = null;
				String name = null;
				String lastName = null;
				String address = null;
                try {
                		PreparedStatement prepstate = connection.prepareStatement ("SELECT * FROM instructor WHERE instructorId=?");
                        prepstate.setString(1, instructorUpdateId);
                        ResultSet rs = prepstate.executeQuery();
                        while (rs.next())
                        {
                        	if(rs.getString("instructorId").equals(instructorUpdateId)){
                        		instructorId = rs.getString("instructorId");
                        		name = rs.getString("name");
                        		lastName = rs.getString("lastName");
                        		address = rs.getString("address");
                        		break;
                        	}
                        }
                        request.setAttribute("instructorId", instructorId);
                        request.setAttribute("name", name);
                        request.setAttribute("lastName", lastName);
                        request.setAttribute("address", address);
                        
                } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                
                RequestDispatcher dispatcher = getServletContext()
                                .getRequestDispatcher("/views/instructorUpdatePreview.jsp");
                dispatcher.forward(request, response);   
			}
	    	
			else if(str.equals("/siteUpdatePreview")){
				String siteUpdateName= request.getParameter("siteUpdateName");
				// Site fields
				String name = null;
				String instructorId = null;
				String duration = null;
                try {
                		PreparedStatement prepstate = connection.prepareStatement ("SELECT * FROM site WHERE name=?");
                        prepstate.setString(1, siteUpdateName);
                        ResultSet rs = prepstate.executeQuery();
                        while (rs.next())
                        {
                        	if(rs.getString("name").equals(siteUpdateName)){
                        		name = rs.getString("name");
                        		instructorId = rs.getString("instructorId");
                        		duration = rs.getString("duration");
                        		break;
                        	}
                        }
                        request.setAttribute("name", name);
                        request.setAttribute("instructorId", instructorId);
                        request.setAttribute("duration", duration);
                        
                } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                
                RequestDispatcher dispatcher = getServletContext()
                                .getRequestDispatcher("/views/instructorUpdatePreview.jsp");
                dispatcher.forward(request, response); 
		    }
	    	
	    	
		    
////------------Update-------------------------------------------------- 
			else if(str.equals("/updateTripAfterPreview")){
				
		    }
			else if(str.equals("/updateInstructorAfterPreview")){
				// TODO Auto-generated catch block	
			}
			else if(str.equals("/updateSiteAfterPreview")){
				// TODO Auto-generated catch block
		    }
	
		    
		    
		    
		    
		    
////---------Delete----------------------------------------------------- 
			else if(str.equals("/deleteTrip")){
				// TODO Auto-generated catch block
		    }
			else if(str.equals("/deleteInstructor")){
				// TODO Auto-generated catch block 	
			}
			else if(str.equals("/deleteSite")){
				// TODO Auto-generated catch block
		    }
	
			else if (str.equals("/about")){
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher("/views/about.jsp");
				dispatcher.forward(request, response);
			}
		    
		    
			else if(str.equals("/dropAll")){
				Statement statement = connection.createStatement();
				statement.execute("DROP TABLE trip");
				statement.execute("DROP TABLE traveler");
				statement.execute("DROP TABLE site");
				statement.execute("DROP TABLE manager");
				statement.execute("DROP TABLE instructor");
				statement.close();
				System.out.println("drop all completed.");
			}
		    
		    
		    //---------------------------------------------------------------

			else{
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher("/views/index.jsp");
				dispatcher.forward(request, response);
			}
	    	
		} catch (SQLException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
