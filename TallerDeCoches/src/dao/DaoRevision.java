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

	public void darAltaRevision(String fechaDate, String textoDescripcion, double precioRevision, String tipoRevision,
			String matricula) throws SQLException {

		PreparedStatement sentenciaNuevoCliente;

		sentenciaNuevoCliente = conexion.prepareStatement("insert into Revision values (?, ?, ?, ?, ?)");

		// Preguntar como poner el id
		sentenciaNuevoCliente.setString(2, textoDescripcion);
		sentenciaNuevoCliente.setString(3, fechaDate);
		sentenciaNuevoCliente.setDouble(4, precioRevision);
		sentenciaNuevoCliente.setString(5, tipoRevision);
		sentenciaNuevoCliente.setString(6, matricula);

		sentenciaNuevoCliente.executeUpdate();

		sentenciaNuevoCliente.close();

		System.out.println("\nRevisión creada correctamente.");

	}

	public void consultarTodasRevisionesEntreDosFechas(String fechaInicio, String fechaFinal) throws SQLException {

		Statement sentencia;
		ResultSet result;

		String cadenaSQL = "SELECT * FROM Revision WHERE Fecha BETWEEN '" + fechaInicio + "' AND '" + fechaFinal
				+ "' ORDER BY FECHA DESC";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		if (!result.next()) {// Preguntar si hacerlo con el try cacth
			System.out.println("Error. No hay revisiones entre esas fechas.");
		} else {
			while (result.next()) {
				System.out.println("Revisión: ");
				System.out.println("\tID Revisión: " + result.getString("idRevision"));
				System.out.println("\tDescripción " + result.getString("Descripcion"));
				System.out.println("\tFecha: " + result.getString("Fecha"));
				System.out.println("\tPrecio Revisión: " + result.getString("PrecioRevision"));
				System.out.println("\tTipo Revisión: " + result.getString("TipoRevision"));
				System.out.println("\tMatricula pertenece: " + result.getString("Matricula_Revisiones"));
			}
		}

		// Preguntar si cerrar asi o mediante un try catch
		result.close();
		sentencia.close();
	}


	public static void consultarRevisionesDeUnCliente(String dni) throws SQLException {

		String cadenaSQL = "select c.DNI as \"DNI Cliente\", co.Matricula as \"Matricula Coche\", r.idRevision as \"Cod. Revision\", "
				+ "r.Fecha as \"Fecha Revision\", r.Descripcion as \"Descripción\", r.PrecioRevision as \"Precio Revision\" "
				+ "from Cliente c inner join Coche co on c.DNI = co.ClientePertenece inner join Revision r on co.Matricula = r.Matricula_Revisiones "
				+ "where c.DNI = '" + dni + "'";

		Statement sentencia;
		ResultSet result;

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		while (result.next()) {
			System.out.println("Cliente: ");
			System.out.println("\tDni: " + result.getString("DNI"));
			System.out.println("\tMatricula: " + result.getString("Matricula"));
			System.out.println("\tId Revisión: " + result.getString("idRevision"));
			System.out.println("\tFecha: " + result.getString("Fecha"));
			System.out.println("\tDescripción: " + result.getString("Descripcion"));
			System.out.println("\tPrecio Revisión: " + result.getString("PrecioRevision"));

		}

		// Preguntar si cerrar asi o mediante un try catch
		result.close();
		sentencia.close();
	}


	public void hacerMediaPrecioRevisionesUnCoche(String matricula) throws SQLException {
		
		Statement sentencia;
		ResultSet result;

		String cadenaSQL = "SELECT avg(Precio_Revision) FROM Revision r WHERE r.Matricula_Revisiones = '" + matricula + "'";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		//PReguntar hacer la media 
		System.out.println("Coche con matrícula " + matricula + ", su media de precio de revisiones es: " + result.getDouble("media"));
		
		// Preguntar si cerrar asi o mediante un try catch
		result.close();
		sentencia.close();
		
		
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

	public void borrarRevisionPorSuId(int idRevision) throws SQLException {
		

		String cadenaSQL = "DELETE FROM Revision WHERE idRevision = '" + idRevision + "'";
		Statement sentenciaBorrarRevision;

		sentenciaBorrarRevision = conexion.createStatement();

		sentenciaBorrarRevision.executeUpdate(cadenaSQL);

		sentenciaBorrarRevision.close();
		
	}

	public Object buscarRevisionesDeUnCocheMatricula(String cadenaSQL) throws SQLException {
		
		Statement sentencia;
		ResultSet result;
		
		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);
		
		//Preguntar
		result.close();
		sentencia.close();
		
		return result;
	}

}
