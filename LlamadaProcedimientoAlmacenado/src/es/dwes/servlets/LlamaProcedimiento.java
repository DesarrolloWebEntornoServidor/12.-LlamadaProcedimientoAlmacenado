package es.dwes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LlamaProcedimiento")
public class LlamaProcedimiento extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	 // JDBC driver name and database URL
	   static final String DRIVER_JDBC = "com.mysql.jdbc.Driver";  
	   static final String URL_BD = "jdbc:mysql://localhost/scott";

	   //  Database credentials
	   static final String USUARIO_BD = "root";
	   static final String CLAVE_USUARIO_BD = "root";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		Connection conn = null;
		// Statement stmt = null;  // Or PreparedStatement if needed
		// PreparedStatement prpStmt = null;
		CallableStatement cllStmt = null;
		ResultSet rs = null;
		try {
			// Registro del driver JDBC, ¿obligatorio?
			Class.forName(DRIVER_JDBC);
			// apertura de la conexión
			conn = DriverManager.getConnection(URL_BD, USUARIO_BD, CLAVE_USUARIO_BD); 
			
			String llamada = "{call getEmpName (?, ?)}";
			cllStmt = conn.prepareCall(llamada);
			
			// bind IN parameter first, then bind OUT parameter
		    int empno = 7788;
		    cllStmt.setInt(1, empno); // This would set ID as 7788
		    // because second parameter is OUT so register it
		    cllStmt.registerOutParameter(2, java.sql.Types.VARCHAR);
		      
		    // Use execute method to run stored procedure.
		    cllStmt.execute();
			  
		    // Retrieve employee name with getXXX method
		    String ename = cllStmt.getString(2);
		    out.println("Emp Name with ID:" + empno + " is " + ename + " <br />");


		    llamada = "{call devuelveApellidos ()}";
		    cllStmt.close();
			cllStmt = null;
		    cllStmt = conn.prepareCall(llamada);
			boolean hadResults = cllStmt.execute();
		    while (hadResults) {
		    	rs = cllStmt.getResultSet();
		    	while (rs.next()) {
		               out.println(rs.getString(1) + " <br />");
		        }
		        hadResults = cllStmt.getMoreResults();
		    }
			
			
		    rs.close();
			rs = null;
			cllStmt.close();
			cllStmt = null;
			conn.close(); // Return to connection pool
			conn = null;  // Make sure we don't close it twice
		  } catch (SQLException e) {
			  // deal with errors
		  } catch (ClassNotFoundException cnfE) {
			 cnfE.printStackTrace();
		  } finally {
			  // Always make sure result sets and statements are closed,
			  // and the connection is returned to the pool
			  if (rs != null) {
				  try { 
					  rs.close(); 
				  } catch (SQLException e) {
					  ; 
				  }
				  rs = null;
			  }
			  if (cllStmt != null) {
				  try {
					  cllStmt.close(); 
				  } catch (SQLException e) {
					  ; 
				  }
				  cllStmt = null;
			  }
			  if (conn != null) {
				  try { 
					  conn.close(); 
				  } catch (SQLException e) { 
					  ; 
				  }
				  conn = null;
			  }
		  }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
