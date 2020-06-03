package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import conexionBd.*;

public class DaoCliente {

	private static DaoCliente daoCliente = null;
	private static Connection conexion = null;

	private DaoCliente() throws SQLException, ClassNotFoundException {
		conexion = ConectionBDSql.getConexion();

	}

	public static DaoCliente getClienteDao() throws SQLException, ClassNotFoundException {
		if (daoCliente == null)
			daoCliente = new DaoCliente();

		return daoCliente;
	}

	/**
	 * Método para dar de alta a un nuevo cliente
	 * 
	 * @param nombreCliente    del nuevo cliente
	 * @param apellidosCliente del nuevo cliente
	 * @param dniCliente       del nuevo cliente(¿Clave primaria?)
	 * @throws SQLException si hay algun error a la hora de insertar el nuevo
	 *                      cliente
	 */

	// Preguntar si tiene que devolver algo o así esta bien
	public static void nuevoCliente(String nombreCliente, String apellidosCliente, String dniCliente)
			throws SQLException {

		PreparedStatement sentenciaNuevoCliente;

		sentenciaNuevoCliente = conexion.prepareStatement("insert into Cliente values (?, ?, ?)");

		// Preguntar si tiene que tener id para clave primaria
		sentenciaNuevoCliente.setString(1, dniCliente);
		sentenciaNuevoCliente.setString(2, nombreCliente);
		sentenciaNuevoCliente.setString(3, apellidosCliente);

		sentenciaNuevoCliente.executeUpdate();

		sentenciaNuevoCliente.close();

		System.out.println("\nCliente creado correctamente.");

	}

	/**
	 * Método para consultar todos los clientes que se llamen iguales
	 * 
	 * @param nombre de los clientes a buscar
	 * @return una lista<String> con todos los clientes con ese nombre
	 * @throws SQLException
	 */

	// Preguntar si devolver una lista de objetos o lista string o mostrar
	// directamente
	public static void consultarClientesPorApellidos(String apellidos) throws SQLException {

		Statement sentencia;
		ResultSet result;

		String cadenaSQL = "SELECT * FROM Cliente WHERE Apellidos  LIKE'%" + apellidos + "%'";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		if (!result.next()) {// Preguntar si hacerlo con el try cacth
			System.out.println("Error. No hay clientes con ese apellidos en la base de datos.");
		} else {
			while (result.next()) {
				System.out.println("Cliente: ");
				System.out.println("\tDni: " + result.getString("DNI"));
				System.out.println("\tNombre: " + result.getString("NOMBRE"));
				System.out.println("\tApellidos: " + result.getString("APELLIDOS"));

			}
		}

		// Preguntar si cerrar asi o mediante un try catch
		result.close();
		sentencia.close();

	}

	/**
	 * Método para consultar todos los clientes
	 * 
	 * @return devuelve una lista con todos los clientes
	 * @throws SQLException
	 */
	public static void consultarTodosClientes() throws SQLException {

		Statement sentencia;
		ResultSet result;

		String cadenaSQL = "SELECT * FROM Cliente";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		if (!result.next()) {// Preguntar si hacerlo con el try cacth
			System.out.println("Error. No hay clientes aún en la base de datos.");
		} else {
			while (result.next()) {
				System.out.println("Cliente: ");
				System.out.println("\tDni: " + result.getString("DNI"));
				System.out.println("\tNombre: " + result.getString("NOMBRE"));
				System.out.println("\tApellidos: " + result.getString("APELLIDOS"));
			}

		}

		result.close();
		sentencia.close();

	}

	public static void modificarDatosCliente(String cadenaSql) throws SQLException {

		Statement sentenciaModificarDatosCliente;

		sentenciaModificarDatosCliente = conexion.createStatement();
		sentenciaModificarDatosCliente.executeUpdate(cadenaSql);

		sentenciaModificarDatosCliente.close();
	}

	/**
	 * Método para borrar un cliente por su dni
	 * 
	 * @param dniCliente del cliente a buscar para borrarlo
	 * @throws SQLException si no lo encuenntra
	 */
	// Preguntar si devolver el objeto cliente que ha borradp
	public static void borrarCliente(String dniCliente) throws SQLException {

		String cadenaSQL = "DELETE FROM Cliente WHERE DNI = '" + dniCliente + "'";
		Statement sentenciaBorrarCliente;

		sentenciaBorrarCliente = conexion.createStatement();

		sentenciaBorrarCliente.executeUpdate(cadenaSQL);

		sentenciaBorrarCliente.close();

	}

	/**
	 * Métoto para buscar un cliente por su dni
	 * 
	 * @param dni        del cliente a buscar
	 * @param sentencia2
	 * @param sentencia2
	 * @return devuelve el cliente con ese dni
	 * @throws SQLException
	 */
	public static boolean buscarClientePorDni(String dni) throws SQLException {

		boolean existe = false;
		Statement sentencia;
		ResultSet result;

		String cadenaSql = "SELECT * FROM Cliente WHERE DNI = '" + dni + "'";

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

}
