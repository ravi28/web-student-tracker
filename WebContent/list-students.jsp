<%@ page import="java.util.*, com.project.student.tracker.*"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title>
			Student Tracker	
		</title>
		<link type="text/css" rel="stylesheet" href="css/style.css">
	</head>
	
	<body>
		<div id="wrapper">
			<div id="header">
				<h2>Udemy University</h2>
			</div>
		</div>
		
		<div id="container">
			<div id="content">
				<!-- New button to Add Student -->
				<input type="button" value="Add Student"
					onclick="window.location.href='add-student-form.jsp';return false;"
					class="add-student-button"
				/>
				<table border="1">
					<tr>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Email</th>
						<th>Action</th>
					</tr>
					
					<c:forEach var="student" items="${student_list}">
					<!-- Setup a update link for each student -->
					<c:url var="updateLink" value="StudentControllerServlet">
						<c:param name="command" value="LOAD"/>
						<c:param name="studentId" value="${student.id}"/>
					</c:url>
					<!-- Setup a delete link for each student -->
					<c:url var="deleteLink" value="StudentControllerServlet">
						<c:param name="command" value="DELETE"/>
						<c:param name="studentId" value="${student.id}"/>
					</c:url>
						<tr>
							<td>${student.firstName}</td>
							<td>${student.lastName}</td>
							<td>${student.email}</td>
							<td>
								<a href="${updateLink}">Update</a> 
								| 
								<a href="${deleteLink}"
								onclick="if (!(confirm('Are you sure you want to delete this student?'))) return false">
								Delete</a>
							</td>			
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</body>
</html>