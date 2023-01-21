package com.servlet.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

public class Singup extends HttpServlet{

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			System.out.println("Singup Servelt post method call");
			String body =request.getReader().lines().reduce("",String::concat);
			 PrintWriter out = response.getWriter();
			JSONParser parser = new JSONParser(body);
			LinkedHashMap<String, Object> json;
			try {
				json = parser.parseObject();
				String firstname =(String) json.get("firstname");
				String lastName=(String) json.get("lastName");
				
				String email =(String) json.get("email");
				String password =(String) json.get("password");
				String confromPassword =(String) json.get("confromPassword");
				boolean tns=(Boolean) json.get("tns");
			
				 String url ="jdbc:mysql://localhost:3306/school?characterEncoding=latin1&useConfigs=maxPerformance"; 
				
				 try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection connection = DriverManager.getConnection(url,"root","root");
					
					String query ="select * from user where email='"+email+"'";
					
					try (PreparedStatement ps = connection.prepareStatement(query)) {
						//ps.setString(1, email);
						ResultSet rs = ps.executeQuery(query);  
						
						if(rs.next()) {
							
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							out.print("User Alreay Exist");
							out.flush();
						}else {
							String querySava="insert into user(name,email,password) value('"+firstname+lastName+"','"+email+"','"+password+"')";
							System.out.println(querySava);
							int numres= ps.executeUpdate(querySava);  
							if(numres>0) {
								
								  response.setContentType("application/json");
								  response.setCharacterEncoding("UTF-8"); 
								  out.print("Got Register Successfully");
								  out.flush();
								 	
							}else {
								  response.setContentType("application/json");
								  response.setCharacterEncoding("UTF-8"); 
								  out.print("Please try after sometime");
								  out.flush();
							}
							
							
							
						}
					}
					//result return with 1 record  -alray exist
					//  
				 }catch (Exception e) {
					 	e.printStackTrace();
					 	  response.setContentType("application/json");
						  response.setCharacterEncoding("UTF-8"); 
						  out.print("Please try after sometime");
						  out.flush();
					 	
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				  response.setContentType("application/json");
				  response.setCharacterEncoding("UTF-8"); 
				  out.print("Please try after sometime");
				  out.flush();
			}
			 
			
			System.out.println(body);
			
			
			//Request data Read
			//DB connetion establish
			// we need to check user already present ->yes ->error msg
			//No -
			//insert into user () values() 
			//store yes -Success msg
			//No - Error Msg
			
			//Respose Possibilities
			//1.User Already exist
			//2.Error in saving user detail
			//3.Successfully Register
			
			
		
	}
}
