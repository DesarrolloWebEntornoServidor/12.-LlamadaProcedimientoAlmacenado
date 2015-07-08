package es.dwes.auxiliar;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.dwes.pojos.*;


public class AccesoDatos {
	
	// JDBC driver name and database URL
	static final String DRIVER_JDBC = "com.mysql.jdbc.Driver";
	static final String ESQUEMA_BD = "banco";
	static final String URL_BD = "jdbc:mysql://localhost/" + ESQUEMA_BD;
	
	//  Database credentials
	static final String USUARIO_BD = "root";
	static final String CLAVE_USUARIO_BD = "root";
	   
	Connection conexion;
	PreparedStatement prpStmt;
	CallableStatement clbStmt;
	ResultSet rS;

	public AccesoDatos() {
		try {
			// registro del driver JDBC
			Class.forName(DRIVER_JDBC);
			// apertura de la conexión
			conexion = DriverManager.getConnection(URL_BD, USUARIO_BD, CLAVE_USUARIO_BD);
		} catch (ClassNotFoundException cnfE) {
			System.err.println("No se encontró el driver de la BD");	 
			cnfE.printStackTrace();
		} catch (SQLException sqlE) {
			System.err.println("No se pudo establecer la conexión");
			sqlE.printStackTrace();
		}	
	}
		   
	public List<Cuenta> devuelveTodasCuentas() {
		// PreparedStatement prpStmt = null;
		// ResultSet rs = null;
		List<Cuenta> listaCuentas = new ArrayList<Cuenta>();
		// listaCuentas = null;
		String consultaSQL = "SELECT * FROM Cuentas";
		try {
			prpStmt = conexion.prepareStatement(consultaSQL);
			rS = prpStmt.executeQuery();
			while (this.rS.next()) {
				Cuenta laCuenta = new Cuenta(rS.getString("entidad"), rS.getString("sucursal"),
											rS.getString("dc"), rS.getString("cuenta"),
											rS.getDouble("saldo"));
				listaCuentas.add(laCuenta);
			}
			rS.close();  // cerrar el ResultSet
			rS = null;  // eliminar el objeto
			prpStmt.close();  // cerrar la sentencia preparada
			prpStmt = null;  // eliminar el objeto
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaCuentas;		   
	}
	
	// devueve el objeto cuenta al recibir la cuenta completa
	public Cuenta devuelveCuenta(String cuentaCompleta) {
		String entidad = cuentaCompleta.substring(0, 4);
		String sucursal = cuentaCompleta.substring(4, 8);
		String dc = cuentaCompleta.substring(8, 10);
		String cuenta = cuentaCompleta.substring(10, 20);
		// PreparedStatement prpStmt = null;
		// ResultSet rs = null;
		Cuenta laCuenta = new Cuenta();
		// listaCuentas = null;
		String consultaSQL = "SELECT * FROM Cuentas WHERE entidad = ? AND sucursal = ? "
							+ "AND dc = ? AND cuenta = ?";
		
		try {
			prpStmt = conexion.prepareStatement(consultaSQL);
			prpStmt.setString(1, entidad);
			prpStmt.setString(2, sucursal);
			prpStmt.setString(3, dc);
			prpStmt.setString(4, cuenta);
			
			rS = prpStmt.executeQuery();
			if (rS.next()) {  // existe la cuenta
				laCuenta = new Cuenta(rS.getString("entidad"), rS.getString("sucursal"),
											rS.getString("dc"), rS.getString("cuenta"),
											rS.getDouble("saldo"));
			}
			rS.close();  // cerrar el ResultSet
			rS = null;  // eliminar el objeto
			prpStmt.close();  // cerrar la sentencia preparada
			prpStmt = null;  // eliminar el objeto
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return laCuenta;		   
	}
	
	// invoca al procedimiento almacenado
	public String ejecutaTransferencia(String cuentaOrigenCompleta, String cuentaDestinoCompleta, double cantidad) {
		String entidadOrg = cuentaOrigenCompleta.substring(0, 4);
		String sucursalOrg = cuentaOrigenCompleta.substring(4, 8);
		String dcOrg = cuentaOrigenCompleta.substring(8, 10);
		String cuentaOrg = cuentaOrigenCompleta.substring(10, 20);
		String entidadDst = cuentaDestinoCompleta.substring(0, 4);
		String sucursalDst = cuentaDestinoCompleta.substring(4, 8);
		String dcDst = cuentaDestinoCompleta.substring(8, 10);
		String cuentaDst = cuentaDestinoCompleta.substring(10, 20);
		
		String llamadaProcedimiento = "{call registraMovimiento (?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		/*
		PROCEDURE `registraMovimiento`(p_cantidad DECIMAL(8,2), 
				p_entidadOrg VARCHAR(4), p_sucursalOrg VARCHAR(4), p_dcOrg VARCHAR(2), p_cuentaOrg VARCHAR(10),
				p_entidadDst VARCHAR(4), p_sucursalDst VARCHAR(4), p_dcDst VARCHAR(2), p_cuentaDst VARCHAR(10))
		*/
		try  {
			clbStmt = conexion.prepareCall(llamadaProcedimiento);
			// binding de parámetros
			clbStmt.setDouble("p_cantidad", cantidad);
			clbStmt.setString("p_entidadOrg", entidadOrg);
			clbStmt.setString("p_sucursalOrg", sucursalOrg);
			clbStmt.setString("p_dcOrg", dcOrg);
			clbStmt.setString("p_cuentaOrg", cuentaOrg);
			clbStmt.setString("p_entidadDst", entidadDst);
			clbStmt.setString("p_sucursalDst", sucursalDst);
			clbStmt.setString("p_dcDst", dcDst);
			clbStmt.setString("p_cuentaDst", cuentaDst);
			
			// ejecutar el procedimiento almacenado
			clbStmt.execute();
			return "Transferencia realizada correctamente";
		} catch (SQLException sqlE) {
			if (sqlE.getErrorCode() == 1452) {  // intento de violación de FK
				// return "Cuenta de origen y/o destino inexistente";
				if (sqlE.getMessage().contains("FK_Movimientos_CuentaOrg")) {
					return "Cuenta de origen inexistente";
				} else {
					return "Cuenta de destino inexistente";
				}
			} else {
				return sqlE.getMessage(); // + " " + sqlE.getSQLState() + " " + sqlE.getErrorCode();
			}
			
		} catch (Exception sqlE) {
			return "Excepción indefinida";
		}		
	}

	public List<Movimiento> devuelveTodosMovimientos() {
		// PreparedStatement prpStmt = null;
		// ResultSet rs = null;
		List<Movimiento> listaMovimientos = new ArrayList<Movimiento>();
		// listaCuentas = null;
		String consultaSQL = "SELECT * FROM Movimientos ORDER BY fecha";
		try {
			prpStmt = conexion.prepareStatement(consultaSQL);
			rS = prpStmt.executeQuery();
			while (this.rS.next()) {
				Cuenta cuentaOrigen = new Cuenta(rS.getString("entidadOrg"), rS.getString("sucursalOrg"),
												rS.getString("dcOrg"), rS.getString("cuentaOrg"));
				Cuenta cuentaDestino = new Cuenta(rS.getString("entidadDst"), rS.getString("sucursalDst"),
												rS.getString("dcDst"), rS.getString("cuentaDst"));
				Movimiento elMovimiento = new Movimiento(rS.getInt("idMovimiento"), rS.getDouble("cantidad"),
											rS.getDate("fecha"), cuentaOrigen, cuentaDestino);
				listaMovimientos.add(elMovimiento);
			}
			rS.close();  // cerrar el ResultSet
			rS = null;  // eliminar el objeto
			prpStmt.close();  // cerrar la sentencia preparada
			prpStmt = null;  // eliminar el objeto
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaMovimientos;		   
	}

/*		   
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
*/		
	
}