package com.project.student.tracker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDBUtil studentDBUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource datasource;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		// Create our StudentDBUtil object and pass in the datasource (connection pool)
		try {
			studentDBUtil = new StudentDBUtil(datasource);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 try {
	            // read the "command" parameter
	            String command = request.getParameter("command");
	                    
	            // route to the appropriate method
	            switch (command) {               
	            case "ADD":
	                addStudent(request, response);
	                break;
	                                
	            default:
	                listStudents(request, response);
	            }
	                
	        }
	        catch (Exception e) {
	            throw new ServletException(e);
	        }
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// Read the command parameter "Add"/"List" etc
			String command = request.getParameter("command");
			
			// If the command parameter is empty, just list the students
			if (command == null) {
				command = "LIST";
			}
			
			// Route the request to appropriate method
			switch(command) {
			case "LIST":
				listStudents(request, response);
				break;
			
			case "LOAD":
				loadStudent(request, response);
				break;
			
			case "UPDATE":
				updateStudent(request, response);
				break;
				
			case "DELETE":
				deleteStudent(request, response);
				break;
				
			default:
				listStudents(request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Get the student details from update form data
		String id = request.getParameter("studentId");
		
		// Update student data from DB Util
		studentDBUtil.deleteStudent(id);
						
		// List the students by sending to main page
		listStudents(request, response);
	}


	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Get the student details from update form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// Create the Student object
		Student student = new Student(id, firstName, lastName, email);
				
		// Update student data from DB Util
		studentDBUtil.updateStudent(student);
				
		// List the students by sending to main page
		listStudents(request, response);
	}


	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		// Get the student ID param from form data
		String studentId = request.getParameter("studentId");
		
		// Get Student details from DB
		Student student = studentDBUtil.getStudent(studentId);
		
		// Place the Student in request attribute
		request.setAttribute("student", student);
		
		// Send to JSP page: update-student-form
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}


	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Get the student parameters from Form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// Create the Student object
		Student student = new Student(firstName, lastName, email);
		
		// Add student data from DB Util
		studentDBUtil.addStudent(student);
		
		// List the students by sending to main page
		// Send as REDIRECT to avoid multiple browser reload issue
		response.sendRedirect(request.getContextPath() + "/StudentControllerServlet?command=LIST");
	}


	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Get students from DB Util
		List<Student> studentList = studentDBUtil.getStudents();
		
		// Add students to the request
		request.setAttribute("student_list", studentList);
		
		// Send to JSP page (View)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}

}
