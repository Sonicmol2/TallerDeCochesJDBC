package conexionBd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectionBDSqlite {

	private static final String URL = "jdbc:sqlite:baseDatos/";
	private static final String CONTRASENNA = "";
	private static final String USUARIO = "root";

	private static Connection conexion;

	private ConectionBDSqlite(String nombreBaseDatosSQLite) throws SQLException {
			conexion = DriverManager.getConnection(URL + nombreBaseDatosSQLite + ".db", USUARIO, CONTRASENNA);
			conexion.setAutoCommit(false);//Hasta que no haga commit no se hará ningún cambio
		}

	public static void cambiarAutoCommit(boolean autoCommi) throws SQLException {
		if (conexion != null) {
			conexion.commit();
		}
	}

	public static void hacerCommit() throws SQLException {
		if (conexion != null) {
			conexion.commit();
		}
	}

	public static void hacerRollBack() {
		if (conexion != null) {
			try {
				conexion.rollback();
			} catch (SQLException e) {
				System.out.println("Error grave al hacer el rollback.");
				e.printStackTrace();
			}
		}
	}

	public static Connection getConnection(String nombreBaseDatosSQLite) throws SQLException {
		if (conexion == null) {
			new ConectionBDSqlite(nombreBaseDatosSQLite);
		}

		return conexion;
	}

	public static void cerrarConexion() throws SQLException {
		if (conexion != null) {
			conexion.close();
		}
	}
}
