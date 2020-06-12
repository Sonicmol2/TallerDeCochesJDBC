package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import conexionBd.ConectionBDSql;

public class DaoCoche {

	private static DaoCoche daoCoche = null;
	private static Connection conexion = null;

	private DaoCoche() throws SQLException, ClassNotFoundException {
		conexion = ConectionBDSql.getConexion();

	}

	public static DaoCoche getCocheDao() throws SQLException, ClassNotFoundException {
		if (daoCoche == null)
			daoCoche = new DaoCoche();

		return daoCoche;
	}

	public void darAltaCoche(String marca, String modelo, String matricula, String dniCliente) throws SQLException {

		PreparedStatement sentenciaNuevoCoche;

		sentenciaNuevoCoche = conexion.prepareStatement("insert into Coche values (?, ?, ?, ?)");

		sentenciaNuevoCoche.setString(1, matricula);
		sentenciaNuevoCoche.setString(2, marca);
		sentenciaNuevoCoche.setString(3, modelo);
		sentenciaNuevoCoche.setString(4, dniCliente);

		sentenciaNuevoCoche.executeUpdate();

		sentenciaNuevoCoche.close();

	}

	public String consultarTodosLosCoches() throws SQLException {

		Statement sentencia;
		ResultSet result;
		StringBuilder datos = new StringBuilder();

		String cadenaSQL = "SELECT * FROM Coche ORDER BY Matricula";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		while (result.next()) {
			datos.append("Coche:\n\tMatrícula: " + result.getString("Matricula") + "\n\tMarca: "
					+ result.getString("Marca") + "\n\tModelo: " + result.getString("Modelo") + "\n\tDNI Pertenece: "
					+ result.getString("ClientePertenece") + "\n");

		}

		result.close();
		sentencia.close();

		return datos.toString();

	}

	public static boolean comprobarCochePorMatricula(String matricula) throws SQLException {

		boolean existe = false;
		Statement sentencia;
		ResultSet result;

		String cadenaSql = "SELECT * FROM Coche WHERE Matricula = '" + matricula + "'";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSql);

		if (result.next()) {
			existe = true;
		} else {
			existe = false;
		}

		result.close();
		sentencia.close();

		return existe;

	}

	public String consultarCochesCliente(String dniCliente) throws SQLException {

		ResultSet result;
		Statement sentencia;
		StringBuilder datos = new StringBuilder();

		String cadenaSQL = "SELECT * FROM Coche WHERE ClientePertenece = '" + dniCliente + "'";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		while (result.next()) {
			datos.append("Cliente: \n\tDNI: " + result.getString("ClientePertenece") + "\n\tCoche: \n\t\tMatrícula: "
					+ result.getString("Matricula") + "\n\t\tMarca: " + result.getString("Marca") + "\n\t\tModelo: "
					+ result.getString("Modelo") + "\n");
		}

		result.close();
		sentencia.close();

		return datos.toString();

	}

	public String consultarRevisionesDeUnCoche(String matricula) throws SQLException {

		ResultSet result;
		Statement sentencia;
		StringBuilder datos = new StringBuilder();

		String cadenaSQL = "SELECT * FROM Revision r WHERE Matricula_Revisiones = '" + matricula + "' ORDER BY Fecha";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		while (result.next()) {
			datos.append("Coche: \n\tMatrícula: " + result.getString("Matricula_Revisiones") + "\nRevisión: \n\tID: "
					+ result.getInt("idRevision") + "\n\tDescripción: " + result.getString("Descripcion")
					+ "\n\tFecha de la revisión: " + result.getString("Fecha") + "\n\tPrecio revisión: "
					+ result.getDouble("PrecioRevision") + "\n\tTipo de revisión: " + result.getString("TipoRevision")
					+ "\n");

		}

		result.close();
		sentencia.close();

		return datos.toString();
	}

	public static void modificarDatosCoche(String cadenaSQL) throws SQLException {

		Statement sentenciaModificarDatosCoche;

		sentenciaModificarDatosCoche = conexion.createStatement();
		sentenciaModificarDatosCoche.executeUpdate(cadenaSQL);

		sentenciaModificarDatosCoche.close();

	}

	public void borrarCochePorMatricula(String matricula) throws SQLException {

		String cadenaSQL = "DELETE FROM Coche WHERE Matricula = '" + matricula + "'";
		Statement sentenciaBorrarCoche;

		sentenciaBorrarCoche = conexion.createStatement();

		sentenciaBorrarCoche.executeUpdate(cadenaSQL);

		sentenciaBorrarCoche.close();

	}

	public String consultarCocheMatricula(String matricula) throws SQLException {
		ResultSet result;
		Statement sentencia;
		StringBuilder datos = new StringBuilder();
		
		String cadenaSql = "SELECT * FROM Coche WHERE Matricula = '" + matricula + "'";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSql);
		
		while (result.next()) {
			datos.append("Coche:\n\tMatrícula: " + result.getString("Matricula") + "\n\tMarca: "
					+ result.getString("Marca") + "\n\tModelo: " + result.getString("Modelo") + "\n\tDNI Pertenece: "
					+ result.getString("ClientePertenece") + "\n");

		}
		
		return datos.toString();
	}

}
