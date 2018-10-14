package com.project.student.tracker;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


/**
 * Servlet implementation class TestDBConn
 */
@WebServlet("/TestDBConn")
public class TestDBConn extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Define datasource/connection pool for Resource injection
	@Resource(name="jdbc/web_student_tracker")
	private DataSource datasource;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Step 1 - Get the PrintWriter
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		
		// Step 2 - Get Connection to database
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = datasource.getConnection();
			
			// Step 3 - Create SQL statement(s)
			String sql = "SELECT * FROM student";
			myStmt = myConn.createStatement();
			
			// Step 4 - Execute SQL query
			myRs = myStmt.executeQuery(sql);
			
			// Step 5 - Process the resultSet
			while(myRs.next()) {
				String email = myRs.getString("email");
				out.println(email);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
