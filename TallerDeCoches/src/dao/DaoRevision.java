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

	public void darAltaRevision(int codigoRevision, String fechaDate, String textoDescripcion, float precioRevision, String tipoRevision,
			String matricula) throws SQLException {

		// INSERT INTO Revision (Descripcion, Fecha, PrecioRevision, TipoRevision,
		// Matricula_Revisiones) VALUES ('sdghdfh', '19-06-2020', 80, 'ruedas',
		// '1430-CNV');

		try (PreparedStatement sentenciaNuevoRevision = conexion.prepareStatement(
				"INSERT INTO Revision (CodigoRevision, Descripcion, Fecha, PrecioRevision, TipoRevision, Matricula_Revisiones) VALUES (?, ?, ?, ?, ?, ?);");) {
			
			sentenciaNuevoRevision.setInt(1, codigoRevision);
			sentenciaNuevoRevision.setString(2, textoDescripcion);
			sentenciaNuevoRevision.setString(3, fechaDate);
			sentenciaNuevoRevision.setFloat(4, precioRevision);
			sentenciaNuevoRevision.setString(5, tipoRevision);
			sentenciaNuevoRevision.setString(6, matricula);

			sentenciaNuevoRevision.executeUpdate();

			sentenciaNuevoRevision.close();
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
			datos.append("Revision: \n\tCodigo Revision: " + result.getInt("CodigoRevision") + "\n\tDescripcion: "
					+ result.getString("Descripcion") + "\n\tFecha: " + result.getString("Fecha")
					+ "\n\tPrecio Revisión: " + result.getFloat("PrecioRevision") + "\n\tTipo Revisión: "
					+ result.getString("TipoRevision") + "\n\tMatricula: " + result.getString("Matricula_Revisiones")
					+ "\n");

		}

		result.close();
		sentencia.close();

		return datos.toString();

	}

	public String consultarRevisionesDeUnCliente(String dni) throws SQLException {

		Statement sentencia;
		ResultSet result;
		StringBuilder datos = new StringBuilder();

		String cadenaSQL = "SELECT c.DNI, co.Matricula, r.CodigoRevision, r.Fecha ,r.Descripcion, r.PrecioRevision "
				+ "FROM Cliente c INNER JOIN Coche co ON c.DNI = co.ClientePertenece INNER JOIN Revision r ON co.Matricula = r.Matricula_Revisiones "
				+ "WHERE c.DNI = '" + dni + "'";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		while (result.next()) {
			datos.append("Cliente: \n\tDNI: " + result.getString("DNI") + "\n\tCoche: \n\t\tMatricula: "
					+ result.getString("Matricula") + "\n\t\tRevision con Codigo: " + result.getString("CodigoRevision")
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

	public boolean comprobarExisteRevisionId(int codigoRevision) throws SQLException {
		boolean existe = false;
		Statement sentencia;
		ResultSet result;

		String cadenaSql = "SELECT * FROM Revision WHERE CodigoRevision = " + codigoRevision;

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

	public void borrarRevisionPorSuCodigo(int codigoRevision, String matricula) throws SQLException {

		// Preguntar si esta bien asi con el where matrícula
		String cadenaSQL = "DELETE FROM Revision WHERE CodigoRevision = '" + codigoRevision + "' AND Matricula_Revisiones = '"
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
			datos.append("Revisión: \n\tCodigo: " + result.getInt("CodigoRevision") + "\n\tDescripción: "
					+ result.getString("Descripcion") + "\n\tFecha: " + result.getString("Fecha")
					+ "\n\tPrecio revisión: " + result.getFloat("PrecioRevision") + "\n\tTipo revisión: "
					+ result.getString("TipoRevision") + "\n\tMatricula pertenece: "
					+ result.getString("Matricula_Revisiones") + "\n");
		}

		result.close();
		sentencia.close();

		return datos.toString();
	}

	public String consultarRevisionPorCodigo(int codigoRevision, String matricula) throws SQLException {
		
		Statement sentencia;
		ResultSet result;
		StringBuilder datos = new StringBuilder();
		String cadenaSQL = "SELECT * FROM Revision WHERE CodigoRevision = " + codigoRevision + " AND Matricula_Revisiones = '" + matricula + "'";
		
		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		while (result.next()) {
			datos.append("Revisión: \n\tCodigo: " + result.getInt("CodigoRevision") + "\n\tDescripción: "
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
