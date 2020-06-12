package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import conexionBd.ConectionBDSql;

public class DaoRevision {

	private static DaoRevision daoRevision = null;
	private static Connection conexion = null;

	private DaoRevision() throws SQLException, ClassNotFoundException {
		conexion = ConectionBDSql.getConexion();

	}

	public static DaoRevision getRevisionDao() throws SQLException, ClassNotFoundException {
		if (daoRevision == null)
			daoRevision = new DaoRevision();

		return daoRevision;
	}

	public void darAltaRevision(String fechaDate, String textoDescripcion, float precioRevision, String tipoRevision,
			String matricula) throws SQLException {

		try (PreparedStatement sentenciaNuevoCliente = conexion
				.prepareStatement("INSERT INTO Revision VALUES (?, ?, ?, ?, ?)");) {
			sentenciaNuevoCliente.setString(1, textoDescripcion);
			sentenciaNuevoCliente.setString(2, fechaDate);
			sentenciaNuevoCliente.setFloat(3, precioRevision);
			sentenciaNuevoCliente.setString(4, tipoRevision);
			sentenciaNuevoCliente.setString(5, matricula);

			sentenciaNuevoCliente.executeUpdate();
		}

	}

	public String consultarTodasRevisionesEntreDosFechas(String fechaInicio, String fechaFinal) throws SQLException {

		Statement sentencia;
		ResultSet result;

		StringBuilder datos = new StringBuilder();

		String cadenaSQL = "SELECT * FROM Revision WHERE Fecha BETWEEN '" + fechaInicio + "' AND '" + fechaFinal
				+ "' ORDER BY Fecha DESC";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		while (result.next()) {
			datos.append("Revision: \n\tID: " + result.getInt("idRevision") + "\n\tDescripcion: "
					+ result.getString("Descripcion") + "\n\tFecha: " + result.getString("Fecha")
					+ "\n\tPrecio Revisión: " + result.getFloat("PrecioRevision") + "\n\tTipo Revisión: "
					+ result.getString("TipoRevision") + "\n\tMatricula: " + result.getString("Matricula_Revisiones"));

		}

		result.close();
		sentencia.close();

		return datos.toString();

	}

	public String consultarRevisionesDeUnCliente(String dni) throws SQLException {

		String cadenaSQL = "SELECT c.DNI, co.Matricula, r.idRevision, r.Fecha ,r.Descripcion, r.PrecioRevision "
				+ "FROM Cliente c INNER JOIN Coche co ON c.DNI = co.ClientePertenece INNER JOIN Revision r ON co.Matricula = r.Matricula_Revisiones "
				+ "WHERE c.DNI = '" + dni + "'";

		Statement sentencia;
		ResultSet result;

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);
		StringBuilder datos = new StringBuilder();

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		while (result.next()) {

			datos.append("Cliente: \n\tDNI: " + result.getString("DNI") + "\n\tCoche: \n\t\tMatricula: "
					+ result.getString("Matricula") + "\n\t\tRevision con ID: " + result.getString("idRevision")
					+ "\n\t\tFecha: " + result.getString("Fecha") + "\n\t\tDescripción: "
					+ result.getString("Descripcion") + "\n\t\tPrecio de la revisión: "
					+ result.getFloat("PrecioRevision") + "\n");

		}

		result.close();
		sentencia.close();

		return datos.toString();

	}

	public Float hacerMediaPrecioRevisionesUnCoche(String matricula) throws SQLException {

		Statement sentencia;
		ResultSet result;
		Float media;

		String cadenaSQL = "SELECT AVG(PrecioRevision) FROM Revision WHERE Matricula_Revisiones = '" + matricula + "'";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		result.next();
		media = result.getFloat(1);

		result.close();
		sentencia.close();

		return media;

	}

	public boolean comprobarExisteRevisionId(int idRevision) throws SQLException {
		boolean existe = false;
		Statement sentencia;
		ResultSet result;

		String cadenaSql = "SELECT * FROM Revision WHERE idRevision = '" + idRevision + "'";

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

	public static void modificarDatosRevision(String cadenaSQL) throws SQLException {

		Statement sentenciaModificarDatosRevision;

		sentenciaModificarDatosRevision = conexion.createStatement();
		sentenciaModificarDatosRevision.executeUpdate(cadenaSQL);

		sentenciaModificarDatosRevision.close();

	}

	public void borrarRevisionPorSuId(int idRevision, String matricula) throws SQLException {

		// Preguntar si esta bien asi con el where matrícula
		String cadenaSQL = "DELETE FROM Revision WHERE idRevision = '" + idRevision + "' AND Matricula_Revisiones = '"
				+ matricula + "'";
		Statement sentenciaBorrarRevision;

		sentenciaBorrarRevision = conexion.createStatement();

		sentenciaBorrarRevision.executeUpdate(cadenaSQL);

		sentenciaBorrarRevision.close();

	}

	public String buscarRevisionesDeUnCocheMatricula(String matricula) throws SQLException {

		Statement sentencia;
		ResultSet result;
		String cadenaSQL = "SELECT * FROM Revision WHERE Matricula_Revisiones = '" + matricula + "'";
		StringBuilder datos = new StringBuilder();

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		while (result.next()) {
			datos.append("Revisión: \n\tID: " + result.getInt("idRevision") + "\n\tDescripción: "
					+ result.getString("Descripcion") + "\n\tFecha: " + result.getString("Fecha")
					+ "\n\tPrecio revisión: " + result.getFloat("PrecioRevision") + "\n\tTipo revisión: "
					+ result.getString("TipoRevision") + "\n\tMatricula pertenece: "
					+ result.getString("Matricula_Revisiones") + "\n");
		}

		result.close();
		sentencia.close();

		return datos.toString();
	}

}
