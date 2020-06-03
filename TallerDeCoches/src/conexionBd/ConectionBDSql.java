package conexionBd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectionBDSql {
	private static final String URL = "jdbc:mysql://localhost/TallerDeCoches";
	private static final String USUARIO = "root";
	
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static Connection conexion;
	
	private ConectionBDSql() throws SQLException, ClassNotFoundException {
		Class.forName(DRIVER);
		conexion = DriverManager.getConnection(URL, USUARIO, "");
	}
	
	public static void iniciarTransaccion() {
		if(conexion != null) {
			try {
				conexion.setAutoCommit(false);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				new ConectionBDSql();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static Connection getConexion() throws SQLException, ClassNotFoundException{
		if(conexion == null) {
			new ConectionBDSql();
		}
		
		return conexion;
	}
	
	public static void hacerCommit() {
		try {
			if (conexion != null)
				conexion.commit();
		} catch (SQLException e) {
			System.err.println("No se ha podido insertar la transacción.");
		}
	}

	public static void hacerRollback() {
		try {
			if (conexion != null)
				conexion.rollback();
		} catch (SQLException e1) {
			System.err.println("Error");
		}
	}
	public static void cerrarConexion() throws SQLException {
		if (conexion != null) {
			conexion.close();
		}
	}
}
