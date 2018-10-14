package com.project.student.tracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDBUtil {
	
	private DataSource datasource;

	public StudentDBUtil(DataSource datasource) {
		this.datasource = datasource;
	}
	
	public List<Student> getStudents() throws Exception{
		List<Student> studentList = new ArrayList<>();
		
		Connection myconn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			// Get DB Connection
			myconn = datasource.getConnection();
			
			// Create a SQL statement
			String sql = "SELECT * FROM student ORDER BY LAST_NAME";
			stmt = myconn.createStatement();
			
			// Execute query
			rs = stmt.executeQuery(sql);
			
			// Process Result set
			while(rs.next()) {
				// retrieve data from result set row
				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				
				// Create a new Student object
				Student student = new Student(id, firstName, lastName, email);
				
				// Add it to list of students
				studentList.add(student);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			// Close the JDBC connection
			close(myconn, stmt, rs);
		}
		return studentList;
	}
	
	public void addStudent(Student student) throws Exception {
		Connection myconn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// Get DB Connection
			myconn = datasource.getConnection();
			
			// Create a SQL statement
			String sql = "INSERT INTO student "
						+ "(first_name, last_name, email) "
						+ "values (?, ?, ?)";
			
			stmt = myconn.prepareStatement(sql);
			
			// Set the param values for the student
			stmt.setString(1, student.getFirstName());
			stmt.setString(2,  student.getLastName());
			stmt.setString(3, student.getEmail());
			
			// Execute the statement
			stmt.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			// Close the JDBC connection
			close(myconn, stmt, null);
		}	
	}
	
	public Student getStudent(String studentId) throws SQLException {
		Connection myconn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Student student = null;
		try {
			// Get DB Connection
			myconn = datasource.getConnection();
			
			// Create a SQL statement
			String sql = "SELECT first_name, last_name, email "
						+ "FROM student "
						+ "WHERE id=" + studentId;
			
			stmt = myconn.createStatement();
			
			// Execute query
			rs = stmt.executeQuery(sql);
			
			// Process Result set
			while(rs.next()) {
				// retrieve data from result set row
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				int id = Integer.parseInt(studentId);
				// Create a new Student object
				student = new Student(id, firstName, lastName, email);
			} 
			return student;	
		}
		finally {
			// Close the JDBC connection
			close(myconn, stmt, null);
		}
	}
	
	public void updateStudent(Student student) {
		Connection myconn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// Get DB Connection
			myconn = datasource.getConnection();
			
			// Create a SQL statement
			String sql = "UPDATE student SET "
						+ "first_name=?, last_name=?, email=? "
						+ "WHERE id=?";
			
			stmt = myconn.prepareStatement(sql);
			
			// Set the param values for the student
			stmt.setString(1, student.getFirstName());
			stmt.setString(2,  student.getLastName());
			stmt.setString(3, student.getEmail());
			stmt.setInt(4, student.getId());
			
			// Execute the statement
			stmt.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			// Close the JDBC connection
			close(myconn, stmt, null);
		}	
	}
	
	public void deleteStudent(String id) {
		Connection myconn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int studentId = Integer.parseInt(id);
		
		try {
			// Get DB Connection
			myconn = datasource.getConnection();
			
			// Create a SQL statement
			String sql = "DELETE FROM student "
						+ "WHERE id=?";
			
			stmt = myconn.prepareStatement(sql);
			
			// Set the param values for the student
			stmt.setInt(1, studentId);
			
			// Execute the statement
			stmt.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			// Close the JDBC connection
			close(myconn, stmt, null);
		}
		
	}
	
	private void close(Connection myconn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) 
				rs.close();
			if (stmt != null)
				stmt.close();
			if (myconn != null)
				myconn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
