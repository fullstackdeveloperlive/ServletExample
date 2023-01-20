package com.servlet.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

public class LoginUser extends HttpServlet{

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Login get Call");
		
		
		
		response.getWriter().append("true").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Login do Post call");
		Boolean status =false;
		
		String clientOrigin = request.getHeader("origin");
		System.out.println(clientOrigin);
		String body =request.getReader().lines().reduce("",String::concat);
		
		
		
		response.setHeader("Access-Control-Allow-Origin", clientOrigin);
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Max-Age", "86400");
		
		
		 response.setContentType("application/json");
	        response.setHeader("Cache-control", "no-cache, no-store");
	        response.setHeader("Pragma", "no-cache");
	        response.setHeader("Expires", "-1");
	        
		JSONParser parser = new JSONParser(body);
		try {
			  LinkedHashMap<String, Object> json= parser.parseObject();
			  String email =(String) json.get("email");
			  String password =(String) json.get("password");
			  
			 
			  System.out.println(email);
			 System.out.println(password);
			 
			 String url ="jdbc:mysql://localhost:3306/school?characterEncoding=latin1&useConfigs=maxPerformance"; 
			 try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection(url,"root","root");
				String query="Select * from user where email='"+email+"'";
				//orm
				System.out.println("Final Query :"+query);
				try (PreparedStatement ps = connection.prepareStatement(query)) {
					//ps.setString(1, email);
					ResultSet rs = ps.executeQuery(query);
					while(rs.next()) {
						System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3)+ " "+rs.getString(4));
						if(email.equals(rs.getString(2)) && password.equals(rs.getString(3))) {
							status=true;
						}
						
					}
				}
				connection.close();
				System.out.println("Got Connect with DB");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			 
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(body);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(status);
		out.flush();
		
	}

}
