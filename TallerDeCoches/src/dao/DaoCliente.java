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

	public void nuevoCliente(String nombreCliente, String apellidosCliente, String dniCliente) throws SQLException {

		PreparedStatement sentenciaNuevoCliente;

		sentenciaNuevoCliente = conexion.prepareStatement("insert into Cliente values (?, ?, ?)");

		sentenciaNuevoCliente.setString(1, dniCliente);
		sentenciaNuevoCliente.setString(2, apellidosCliente);
		sentenciaNuevoCliente.setString(3, nombreCliente);

		sentenciaNuevoCliente.executeUpdate();

		sentenciaNuevoCliente.close();

	}

	public String consultarClientesPorApellidos(String apellidos) throws SQLException {

		Statement sentencia;
		ResultSet result;
		StringBuilder datos = new StringBuilder();

		String cadenaSQL = "SELECT * FROM Cliente WHERE Apellidos LIKE '%" + apellidos + "%';";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		while (result.next()) {
			datos.append("Cliente:\n\tDNI: " + result.getString("DNI") + "\n\tNombre: " + result.getString("Nombre")
					+ "\n\tApellidos: " + result.getString("Apellidos") + "\n\n");
		}

		sentencia.close();

		return datos.toString();

	}

	public String consultarTodosClientes() throws SQLException {

		Statement sentencia;
		ResultSet result;
		StringBuilder datos = new StringBuilder();

		String cadenaSQL = "SELECT * FROM Cliente";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSQL);

		while (result.next()) {
			datos.append("Cliente:\n\tDNI: " + result.getString("DNI") + "\n\tNombre: " + result.getString("Nombre")
					+ "\n\tApellidos: " + result.getString("Apellidos") + "\n\n");
		}

		sentencia.close();
		return datos.toString();

	}

	public static void modificarDatosCliente(String cadenaSql) throws SQLException {

		Statement sentenciaModificarDatosCliente;

		sentenciaModificarDatosCliente = conexion.createStatement();
		sentenciaModificarDatosCliente.executeUpdate(cadenaSql);

		sentenciaModificarDatosCliente.close();
	}

	public void borrarCliente(String dniCliente) throws SQLException {

		String cadenaSQL = "DELETE FROM Cliente WHERE DNI = '" + dniCliente + "'";
		Statement sentenciaBorrarCliente;

		sentenciaBorrarCliente = conexion.createStatement();

		sentenciaBorrarCliente.executeUpdate(cadenaSQL);

		sentenciaBorrarCliente.close();

	}

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

	public String consultarClienteDNI(String dni) throws SQLException {

		StringBuilder datos = new StringBuilder();
		Statement sentencia;
		ResultSet result;

		String cadenaSql = "SELECT * FROM Cliente WHERE DNI = '" + dni + "'";

		sentencia = conexion.createStatement();
		result = sentencia.executeQuery(cadenaSql);

		while (result.next()) {
			datos.append("Cliente:\n\tDNI: " + result.getString("DNI") + "\n\tNombre: " + result.getString("Nombre")
					+ "\n\tApellidos: " + result.getString("Apellidos") + "\n\n");
		}

		result.close();
		sentencia.close();

		return datos.toString();
	}

}
