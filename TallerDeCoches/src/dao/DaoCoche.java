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

		// Preguntar
		System.out.println("\nCoche creado correctamente.");
	}

	public static void consultarTodosLosCoches() throws SQLException {

		Statement sentencia;
		ResultSet result;

		String cadenaSQL = "SELECT * FROM Coche ORDER BY Matricula";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		if (!result.next()) {// Preguntar si hacerlo con el try cacth
			System.out.println("Error. No hay coches aún en la base de datos.");
		} else {
			while (result.next()) {
				System.out.println("Coche: ");
				System.out.println("\tMatrícula: " + result.getString("Matricula"));
				System.out.println("\tMarca: " + result.getString("Marca"));
				System.out.println("\tModelo: " + result.getString("Modelo"));
				System.out.println("\tDNI_Pertenece: " + result.getString("ClientePertenece"));
			}

		}

		result.close();
		sentencia.close();

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

	public void consultarCochesCliente(String dniCliente) throws SQLException {

		Statement sentencia;
		ResultSet result;

		String cadenaSQL = "SELECT * FROM Coche WHERE ClientePertenece = '" + dniCliente + "'";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		if (!result.next()) {// Preguntar si hacerlo con el try cacth
			System.out.println("Error. Base de datos de coches vacía del cliente con ese DNI.");
		} else {
			while (result.next()) {
				System.out.println("Cliente: ");
				System.out.println("\tDNI: " + result.getString("ClientePertenece"));
				System.out.println("\tCoche: ");
				System.out.println("\t\tMatricula: " + result.getString("Matricula"));
				System.out.println("\t\tMarca: " + result.getString("Marca"));
				System.out.println("\t\tModelo: " + result.getString("Modelo"));
			}
		}

		// Preguntar si cerrar asi o mediante un try catch
		result.close();
		sentencia.close();
	}

	public void consultarRevisionesDeUnCoche(String matricula) throws SQLException {

		Statement sentencia;
		ResultSet result;

		String cadenaSQL = "SELECT * FROM Revision r WHERE Matricula_Revisiones = '" + matricula + "' ORDER BY Fecha";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		if (!result.next()) {// Preguntar si hacerlo con el try cacth
			System.out.println("Error. Base de datos de revisiones vacía.");
		} else {
			while (result.next()) {
				System.out.println("Coche: ");
				System.out.println("\tMatricula: " + result.getString("Matricula_Revisiones"));
				System.out.println("\tID: " + result.getString("idRevision"));
				System.out.println("\tDescripción: " + result.getString("Descripcion"));
				System.out.println("\tFecha revisión: " + result.getString("Fecha"));
				System.out.println("\tPrecio revision: " + result.getString("PrecioRevision"));
				System.out.println("\tTipo Revision: " + result.getString("TipoRevision"));
			}
		}
		// Preguntar si cerrar asi o mediante un try catch
		result.close();
		sentencia.close();
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

}
