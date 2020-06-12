package principal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import conexionBd.ConectionBDSql;
import dao.DaoCliente;
import dao.DaoCoche;
import dao.DaoRevision;

public class TallerPrincipal {

	private static final String PATRON_DNI = "[0-9]{8}[A-Z]";
	private static final String PATRON_MATRICULA = "[0-9]{4}-[A-Z]{3}";

	public static void main(String[] args) {

		int opcion;

		try {

			do {
				opcion = Menus.mostrarMenu();
				tratarMenu(opcion);

			} while (opcion != 4);

		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				ConectionBDSql.cerrarConexion();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Método para tratar los menús
	 * 
	 * @param opcion elegida del menú principal
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static void tratarMenu(int opcion) throws SQLException, ClassNotFoundException {
		int opcionCliente, opcionCoche, opcionRevision;

		switch (opcion) {
		case 1: {
			do {
				opcionCliente = Menus.mostrarMenuCliente();
				tratarMenuCliente(opcionCliente);
			} while (opcionCliente != 6);
			break;
		}
		case 2: {
			do {
				opcionCoche = Menus.mostrarMenuCoche();
				tratarMenuCoches(opcionCoche);
			} while (opcionCoche != 7);
			break;
		}
		case 3: {
			do {
				opcionRevision = Menus.mostrarMenuRevision();
				tratarMenuRevision(opcionRevision);
			} while (opcionRevision != 7);
			break;
		}
		}
	}

	/**
	 * Método para tratar las opciones del menú de revisiones
	 * 
	 * @param opcionRevision elegida
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private static void tratarMenuRevision(int opcionRevision) throws SQLException, ClassNotFoundException {
		
		DaoRevision revisionDao = DaoRevision.getRevisionDao();
		DaoCoche cocheDAO = DaoCoche.getCocheDao();
		DaoCliente clienteDAO = DaoCliente.getClienteDao();
		String textoDescripcion, tipoRevision, matricula, dni, fechaInicio, fechaFinal, datosRevision;
		int codigoRevision;
		float precioRevision;
		String fechaDate;
		char confirmacion;
		boolean existe = false;

		try {
			switch (opcionRevision) {
			case 1:
				// Alta de revisión
				System.out.println("Vamos a introduir los datos de la revisión.");
				codigoRevision = ValidacionesEntrada.solicitarNumero("Introduce el codigo de la revisión: ");
				fechaDate = ValidacionesEntrada.solicitarCadena("Introduce la fecha de la revision(dd-mm-aaaa): ");
				textoDescripcion = ValidacionesEntrada.solicitarCadena("Introduce la descripción de la revisión: ");
				tipoRevision = ValidacionesEntrada
						.solicitarCadena("Introduce el tipo de revisión(Aceite, ruedas, motor, electronica): ");
				precioRevision = (float) ponerPrecioRevision(tipoRevision);
				matricula = ValidacionesEntrada.solicitarCadena("Introduce la matricula del coche de la revisión: ");

				existe = DaoCoche.comprobarCochePorMatricula(matricula);

				if (existe == false) {
					throw new SQLException("Error. No hay coche con esa matrícula.");
				} else {
					revisionDao.darAltaRevision(codigoRevision, fechaDate, textoDescripcion, precioRevision,
							tipoRevision, matricula);

					System.out.println("\nRevisión creada correctamente.");
				}

				break;
			case 2:
				// Consulta de todas las revisiones entre dos fechas(Preguntar por que está mal
				// hecho)
				System.out.println("Vamos a introducir dos fechas para mostrar revisiones entre las 2 fechas.");
				fechaInicio = ValidacionesEntrada.solicitarCadena("Introduce la primera fecha: ");
				fechaFinal = ValidacionesEntrada.solicitarCadena("Introduce la segunda fecha: ");

				consultarTodasRevisionesEntreDosFechas(fechaInicio, fechaFinal, revisionDao);

				break;
			case 3:
				// Consultar las revisiones de un cliente
				dni = ValidacionesEntrada.solicitarCadena("Introduce el dni del cliente: ");

				existe = DaoCliente.buscarClientePorDni(dni);

				if (existe == false) {
					throw new SQLException("Error. No existe cliente con ese DNI.");
				} else {
					consultarRevisionesDeUnCliente(dni, revisionDao);
				}

				break;
			case 4:
				// Hacer la media de las revisiones
				matricula = ValidacionesEntrada.solicitarCadena("Introduce la matricula del coche: ");

				existe = DaoCoche.comprobarCochePorMatricula(matricula);

				if (existe == false) {
					throw new SQLException("Error. No hay coche con esa matrícula.");
				} else {

					hacerMediaPrecioRevisionesUnCoche(matricula, revisionDao);
				}
				break;
			case 5:
				// Modificar algun dato de la revision del coche
				matricula = ValidacionesEntrada.solicitarCadena("Introduce la matricula del coche: ");

				existe = DaoCoche.comprobarCochePorMatricula(matricula);

				if (existe == false) {
					throw new SQLException("Error. No hay coche con esa matrícula.");
				} else {

					modificarDatoDeUnaRevisionDeUnCoche(matricula, revisionDao);
					
					System.out.println("Se terminó de cambiar datos de la revisión.");

				}

				break;
			case 6:
				// Borrar la revisión por su id, pero antes buscamos por matricula ese coche
				// para borrar la revisión
				matricula = ValidacionesEntrada.solicitarCadena("Introduce la matricula del coche: ");

				existe = DaoCoche.comprobarCochePorMatricula(matricula);

				if (existe == false) {
					throw new SQLException("Error. No hay coche con esa matrícula.");
				} else {

					String cadenaResultado;
					cadenaResultado = revisionDao.buscarRevisionesDeUnCocheMatricula(matricula);

					// Mostramos la lista de revisiones del coche con esa matricula
					if (cadenaResultado.isEmpty()) {
						throw new SQLException("Error. No hay revisiones de ese coche con esa matricula.");
					} else {
						System.out.println(cadenaResultado);

						// Solicitamos el id para borrar
						codigoRevision = ValidacionesEntrada
								.solicitarNumero("Introduce el código de la revisión a borrar: ");
						datosRevision = revisionDao.consultarRevisionPorCodigo(codigoRevision, matricula);
						confirmacion = ValidacionesEntrada
								.solicitarConfirmacion("¿Seguro que quieres borrar la revision?: ");
						if (confirmacion == 'S') {
							revisionDao.borrarRevisionPorSuCodigo(codigoRevision, matricula);
							System.out.println(datosRevision);
							System.out.println("Borrado correctamente.");
						}

					}

				}

				break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Método para modificar los datos de una revisison
	 * 
	 * @param matricula   del coche para mostrar las revisiones y modificar
	 * @param revisionDao
	 * @throws SQLException error, si aun no hay revisiones o la matricula
	 *                      incorrecta o no existe el coche
	 */
	private static void modificarDatoDeUnaRevisionDeUnCoche(String matricula, DaoRevision revisionDao)
			throws SQLException {

		int codigoRevision;
		boolean existe = false;

		String cadenaResultado;
		cadenaResultado = revisionDao.buscarRevisionesDeUnCocheMatricula(matricula);

		if (cadenaResultado.isEmpty()) {
			throw new SQLException("Error. Ese coche no tienes revisiones.");
		} else {
			System.out.println(cadenaResultado);

			codigoRevision = ValidacionesEntrada.solicitarNumero("Introduce el codigo de la revisión a modificar: ");

			existe = revisionDao.comprobarExisteRevisionId(codigoRevision);
			if (existe == false) {
				throw new SQLException("Error. No hay revisión con ese id.");
			} else {
				modificarDatoRevisionDeUnCoche(codigoRevision, matricula);
			}

		}

	}

	/**
	 * Método para hacer la media del precio de revisiones de un coche en concreto
	 * 
	 * @param matricula   del coche que queremos hacer la media del precio de las
	 *                    revisiones
	 * @param revisionDao
	 * @throws SQLException error, si matricula incorrecto, coche no existe o que
	 *                      aún no tiene revisiones ese coche
	 */
	private static void hacerMediaPrecioRevisionesUnCoche(String matricula, DaoRevision revisionDao)
			throws SQLException {

		double media = 0;

		media = revisionDao.hacerMediaPrecioRevisionesUnCoche(matricula);

		if (media == 0) {
			throw new SQLException("Coche con matricula '" + matricula + "', no tiene media de revisiones.");
		} else {
			System.out.println("Coche con matrícula " + matricula + ", su media de precio de revisiones es: " + media);
		}

	}

	/**
	 * Método para consultar las revisiones de un cliente
	 * 
	 * @param dni         del cliente para ver las revisiones que tiene
	 * @param revisionDao
	 * @throws SQLException error, si dni incorrecto o que el cliente no existe con
	 *                      el dni indicado
	 */
	private static void consultarRevisionesDeUnCliente(String dni, DaoRevision revisionDao) throws SQLException {

		String cadenaResultado;

		cadenaResultado = revisionDao.consultarRevisionesDeUnCliente(dni);

		if (cadenaResultado.isEmpty()) {
			throw new SQLException("Ese cliente no tiene revisiones de ninguno de sus coches.");
		} else {
			System.out.println(cadenaResultado);
		}

	}

	/**
	 * Método para consultar revisiones entre dos fechas indicadas
	 * 
	 * @param fechaInicio fecha desde donde empezar a buscar
	 * @param fechaFinal  fecha terminar de buscar
	 * @param revisionDao
	 * @throws SQLException error, si no hay revisiones entre las fechas indicadas o
	 *                      fechas introducidas incorrectas
	 */
	private static void consultarTodasRevisionesEntreDosFechas(String fechaInicio, String fechaFinal,
			DaoRevision revisionDao) throws SQLException {

		String cadenaResultado;

		cadenaResultado = revisionDao.consultarTodasRevisionesEntreDosFechas(fechaInicio, fechaFinal);

		if (cadenaResultado.isEmpty()) {
			throw new SQLException("No hay revisiones entre las fechas indicadas.");
		} else {
			System.out.println(cadenaResultado);
		}

	}

	/**
	 * Método para modificar los datos de una revisión
	 * 
	 * @param idRevision revision a modificar
	 * @param matricula  del coche para traer sus revisiones
	 * @throws SQLException error si idRevision incorecto o matricula incorrecta
	 */
	private static void modificarDatoRevisionDeUnCoche(int codigoRevision, String matricula) throws SQLException {

		int opcion;
		char confirmar;

		do {
			opcion = Menus.mostrarMenuCambiarDatosRevision();

			cambiarDatosRevision(opcion, codigoRevision, matricula);

			confirmar = ValidacionesEntrada
					.solicitarConfirmacion("¿Quieres seguir modificando datos de la revisión?(S/N): ");
		} while (confirmar == 'S');

	}

	/**
	 * Método para cambiar datos de una revisión en concreto
	 * 
	 * @param opcion     elegida por el usuario para modificar
	 * @param idRevision de la revisón que queremos modificar
	 * @param matricula  del coche, para que traiga las revisiones de ese coche en
	 *                   concreto
	 * @throws SQLException si la opcion del menú incorrecta, matricula incorrecta o
	 *                      no hay coche con esa matrícula, o idRevision incorrecta
	 *                      o no existe
	 */
	private static void cambiarDatosRevision(int opcion, int codigoRevision, String matricula) throws SQLException {

		String nuevaDescripcion, nuevaTipoRevision;
		String nuevaFecha;
		String cadenaSQL;
		double precioNuevo;

		switch (opcion) {
		case 1:
			// Preguntar de todas formas si esta bien en el where de la matricula
			System.out.println("Introduce la nueva fecha.");
			nuevaFecha = ValidacionesEntrada.solicitarCadena("Introduce la nueva fecha(dd-mm-aaaa): ");
			cadenaSQL = "UPDATE Revision set Fecha = '" + nuevaFecha + "' WHERE CodigoRevision = " + codigoRevision
					+ " AND Matricula_Revisiones = '" + matricula + "'";
			DaoRevision.modificarDatosRevision(cadenaSQL);

			System.out.println("\nFecha de la revisión del coche modificada correctamente\n");
			break;
		case 2:
			nuevaDescripcion = ValidacionesEntrada.solicitarCadena("Introduce la nueva descripción: ");
			cadenaSQL = "UPDATE Revision set Descripcion = '" + nuevaDescripcion + "' WHERE CodigoRevision = "
					+ codigoRevision + " AND Matricula_Revisiones = '" + matricula + "'";
			DaoRevision.modificarDatosRevision(cadenaSQL);

			System.out.println("\nLa descripción de la revisión del coche modificada correctamente\n");
			break;
		case 3:
			nuevaTipoRevision = ValidacionesEntrada.solicitarCadena("Introduce el nuevo tipo de revisión: ");
			precioNuevo = ponerPrecioRevision(nuevaTipoRevision);
			cadenaSQL = "UPDATE Revision set TipoRevision = '" + nuevaTipoRevision + "' , PrecioRevision = "
					+ precioNuevo + " WHERE CodigoRevision = " + codigoRevision + " AND Matricula_Revisiones = '"
					+ matricula + "'";
			DaoRevision.modificarDatosRevision(cadenaSQL);

			System.out.println("\nTipo de revisión y precio de la revisión del coche modificados correctamente\n");
			break;
		}

	}

	/**
	 * Método para poner precio a la revisión, según el tipo de revisión elegida.
	 * 
	 * @param tipoRevision de revisión elegida por el usuario
	 * @return precio
	 */
	private static double ponerPrecioRevision(String tipoRevision) {

		double precio = 0;

		switch (tipoRevision) {
		case "aceite":
			precio = 60;
			break;
		case "ruedas":
			precio = 80;
			break;
		case "motor":
			precio = 100;
			break;
		case "electronica":
			precio = 130;
			break;
		}

		return precio;
	}

	/**
	 * Método para tratar el menú de los coches
	 * 
	 * @param opcionCoche elegida por el usuario del menú
	 * @throws ClassNotFoundException
	 * @throws SQLException           error, si las validaciones no son correctas,
	 *                                una opción elegida incorrecta
	 */
	private static void tratarMenuCoches(int opcionCoche) throws ClassNotFoundException, SQLException {

		DaoCoche cocheDAO = DaoCoche.getCocheDao();
		DaoCliente clienteDAO = DaoCliente.getClienteDao();
		String marca, modelo, matricula;
		String dniCliente;
		String cadenaResultado;
		char confirmacion;
		boolean existe = false;
		boolean esCorrecto = false;
		char confirmar = 0;

		try {
			switch (opcionCoche) {
			case 1:
				// Alta a un nuevo coche
				marca = ValidacionesEntrada.solicitarCadena("Introduce la marca del coche: ");
				modelo = ValidacionesEntrada.solicitarCadena("Introduce el modelo del coche: ");
				matricula = ValidacionesEntrada.solicitarCadena("Introduce la matricula del coche(LLLL-NNN):");
				dniCliente = ValidacionesEntrada.solicitarCadena("Introduce el dni del cliente del coche:");

				esCorrecto = ValidacionesEntrada.validarMediantePatron(matricula, PATRON_MATRICULA);

				if (esCorrecto == false) {
					throw new SQLException("Error. Formato Matricula incorrecto");
				} else {
					existe = cocheDAO.comprobarCochePorMatricula(matricula);

					if (existe == true) {
						throw new SQLException("Error. Ya existe un vehiculo con esa matrícula.");
					} else {
						existe = clienteDAO.buscarClientePorDni(dniCliente);

						if (existe = false) {
							throw new SQLException("Error. No existe ningún cliente con ese DNI.");
						} else {
							cocheDAO.darAltaCoche(marca, modelo, matricula, dniCliente);

							System.out.println("\nCoche creado correctamente.");
						}
					}

				}

				break;
			case 2:
				// Consultar todos los coches, ordenados por su matrícula
				consultarTodosLosCoches(cocheDAO);
				break;
			case 3:
				// Consultar las revisiones de un coche en concreto, ordenados por la fecha
				matricula = ValidacionesEntrada
						.solicitarCadena("Introduce la matricula del coche(LLLL-NNN) para ver sus revisiones:");

				esCorrecto = ValidacionesEntrada.validarMediantePatron(matricula, PATRON_MATRICULA);

				if (esCorrecto == false) {
					System.out.println("Error. Formato Matricula incorrecto.");
				} else {

					existe = cocheDAO.comprobarCochePorMatricula(matricula);

					if (existe == false) {
						throw new SQLException("Error. No hay coche con esa matrícula.");
					} else {
						consultarRevisionesDeUnCoche(matricula, cocheDAO);
					}
				}
				break;
			case 4:
				// Consultar los coches que dispone un cliente.
				dniCliente = ValidacionesEntrada.solicitarCadena("Introduce el dni del cliente del coche:");

				esCorrecto = ValidacionesEntrada.validarMediantePatron(dniCliente, PATRON_DNI);

				if (esCorrecto == false) {
					throw new SQLException("Error. Formato Matricula incorrecto.");
				} else {
					consultarCochesCliente(dniCliente, cocheDAO);

				}
				break;
			case 5:
				// Modificar cualquier dato(Preguntar no se hacerlo)
				matricula = ValidacionesEntrada
						.solicitarCadena("Introduce la matricula del coche a modificar(LLLL-NNN):");

				esCorrecto = ValidacionesEntrada.validarMediantePatron(matricula, PATRON_MATRICULA);

				if (esCorrecto == false) {
					throw new SQLException("Error. Formato Matricula incorrecto.");
				} else {
					cadenaResultado = cocheDAO.consultarCocheMatricula(matricula);

					do {
						System.out.println(cadenaResultado);

						modificarDatoDeUnCoche(matricula);
						confirmar = ValidacionesEntrada
								.solicitarConfirmacion("¿Quieres seguir cambiando datos(S/N)?: ");

						cadenaResultado = cocheDAO.consultarCocheMatricula(matricula);
					} while (confirmar == 'S');
					System.out.println("Se terminó  de modificar.\n");
				}

				break;
			case 6:
				// Borrar el coche
				matricula = ValidacionesEntrada.solicitarCadena("Introduce la matricula del coche a borrar(LLLL-NNN):");

				esCorrecto = ValidacionesEntrada.validarMediantePatron(matricula, PATRON_MATRICULA);

				if (esCorrecto == false) {
					throw new SQLException("Error. Formato Matricula incorrecto.");
				} else {

					existe = cocheDAO.comprobarCochePorMatricula(matricula);

					if (existe == false) {
						throw new SQLException("Error. No existe coche con esa matrícula.");
					} else {
						cadenaResultado = cocheDAO.consultarCocheMatricula(matricula);

						System.out.println(cadenaResultado);

						confirmacion = ValidacionesEntrada
								.solicitarConfirmacion("¿Seguro que quieres borrar el coche?: ");
						if (confirmacion == 'S') {
							cocheDAO.borrarCochePorMatricula(matricula);
							System.out.println(cadenaResultado);
							System.out.println("\nCoche borrado correctamente.\n");
						}

					}

				}

				break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Método para consultar los coches que tiene un cliente
	 * 
	 * @param dniCliente del cliente a buscar para ver sus coches
	 * @param cocheDAO
	 * @throws SQLException error, si no hay cliente con ese dni o que ese cliente
	 *                      no tenga aún coches.
	 */
	private static void consultarCochesCliente(String dniCliente, DaoCoche cocheDAO) throws SQLException {

		String cadenaResultado;

		cadenaResultado = cocheDAO.consultarCochesCliente(dniCliente);
		if (cadenaResultado.isEmpty()) {
			throw new SQLException("El cliente con DNI: " + dniCliente + ", no tiene coches.");
		} else {
			System.out.println(cadenaResultado);
		}

	}

	/**
	 * Método para consultar las revisiones de un coche en concreto
	 * 
	 * @param matricula del coche a buscar
	 * @param cocheDAO
	 * @throws SQLException error, si no hay coches con esa matricula o que aún ese
	 *                      coche no tiene revisiones.
	 */
	private static void consultarRevisionesDeUnCoche(String matricula, DaoCoche cocheDAO) throws SQLException {

		String cadenaResultado;

		cadenaResultado = cocheDAO.consultarRevisionesDeUnCoche(matricula);

		if (cadenaResultado.isEmpty()) {
			throw new SQLException("Coche con matricula: " + matricula + ", no tiene revisiones aún.");
		} else {
			System.out.println(cadenaResultado);
		}

	}

	/**
	 * Método para consultar todos los coches
	 * 
	 * @param cocheDAO
	 * @throws SQLException error, si no hay coches aún en la base de datos(Está
	 *                      vacía)
	 */
	private static void consultarTodosLosCoches(DaoCoche cocheDAO) throws SQLException {

		String cadenaResultado;

		cadenaResultado = cocheDAO.consultarTodosLosCoches();

		if (cadenaResultado.isEmpty()) {
			throw new SQLException("Base de datos de los coches vacia.");
		} else {
			System.out.println(cadenaResultado);
		}

	}

	/**
	 * Método para modificar datos de un coche
	 * 
	 * @param matricula del coche para buscarlo y para modificar
	 * @throws SQLException error si no hay ningún coche con esa matrícula
	 */
	private static void modificarDatoDeUnCoche(String matricula) throws SQLException {

		String cadenaSQL;
		int opcion;
		boolean existe = false;
		String marcaNueva, modeloNuevo;

		opcion = Menus.mostrarCambiosDatosCoche();

		existe = DaoCoche.comprobarCochePorMatricula(matricula);

		switch (opcion) {
		case 1:

			if (existe == false) {
				throw new SQLException("Error. No existe coche con esa matrícula.");
			} else {
				marcaNueva = ValidacionesEntrada
						.solicitarCadena("Introduce la nueva marca del vehículo para modificarla: ");
				cadenaSQL = "UPDATE Coche set Marca = '" + marcaNueva + "' WHERE Matricula = '" + matricula + "'";
				DaoCoche.modificarDatosCoche(cadenaSQL);

				System.out.println("\nMarca del coche modificada correctamente.\n");
			}
			break;
		case 2:
			if (existe == false) {
				throw new SQLException("Error. No existe coche con esa matrícula.");
			} else {
				modeloNuevo = ValidacionesEntrada
						.solicitarCadena("Introduce el nuevo modelo del vehículo para modificar: ");
				cadenaSQL = "UPDATE Coche set Modelo = '" + modeloNuevo + "' WHERE Matricula = '" + matricula + "'";
				DaoCoche.modificarDatosCoche(cadenaSQL);
				System.out.println("\nModelo del coche modificado correctamente.\n");
			}
			break;
		}

	}

	/**
	 * Método para tratar el menú de clientes
	 * 
	 * @param opcionCliente elegida del menú cliente
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static void tratarMenuCliente(int opcionCliente) throws ClassNotFoundException, SQLException {
		String nombreCliente, apellidosCliente, dni, cadenaResultado;
		char confirmacion;
		boolean existe = false;
		boolean correcto = false;
		DaoCliente clienteDAO = DaoCliente.getClienteDao();

		try {
			switch (opcionCliente) {
			case 1:
				// Alta de un nuevo cliente

				nombreCliente = ValidacionesEntrada.solicitarCadena("Introduce el nombre del cliente: ");
				apellidosCliente = ValidacionesEntrada.solicitarCadena("Introduce los apellidos del nuevo cliente: ");
				dni = ValidacionesEntrada.solicitarCadena("Introduce el dni del nuevo cliente: ");

				correcto = ValidacionesEntrada.validarMediantePatron(dni, PATRON_DNI);

				if (correcto == false) {
					throw new SQLException("Error. Formato DNI del cliente incorrecto.");
				} else {
					existe = clienteDAO.buscarClientePorDni(dni);

					if (existe == true) {
						throw new SQLException("Error. Ya existe un cliente con ese DNI.");
					} else {
						clienteDAO.nuevoCliente(nombreCliente, apellidosCliente, dni);

						System.out.println("\nCliente creado correctamente.");
					}
				}

				break;

			case 2:
				// Consultar todos los clientes

				consultarTodosClientes(clienteDAO);
				break;
			case 3:
				// Consultar todos los clientes que contengan un apellido
				apellidosCliente = ValidacionesEntrada
						.solicitarCadena("Introduce el apellido del o de los clientes a consultar: ");

				consultarClientesPorApellidos(apellidosCliente, clienteDAO);
				break;
			case 4:
				// Modificar algun dato del cliente
				dni = ValidacionesEntrada.solicitarCadena("Introduce el dni del cliente a modificar: ");
				correcto = ValidacionesEntrada.validarMediantePatron(dni, PATRON_DNI);

				if (correcto == false) {
					throw new SQLException("Error. Formato DNI del cliente incorrecto.");
				} else {
					existe = clienteDAO.buscarClientePorDni(dni);

					if (existe == false) {
						throw new SQLException("Error. No existe un cliente con ese DNI.");
					} else {

						cadenaResultado = clienteDAO.consultarClienteDNI(dni);

						do {

							System.out.println(cadenaResultado);

							modificarDatosDeCliente(dni);

							confirmacion = ValidacionesEntrada
									.solicitarConfirmacion("¿Quieres seguir modificando datos del cliente?");

							cadenaResultado = clienteDAO.consultarClienteDNI(dni);
						} while (confirmacion == 'S');
						System.out.println("Terminó de modificar datos.");
					}
				}

				break;
			case 5:
				// Borrar el cliente por su DNI, se borrará también la lista de coches del
				// cliente elegido
				dni = ValidacionesEntrada.solicitarCadena("Introduce el dni del cliente que quieres borrar: ");
				correcto = ValidacionesEntrada.validarMediantePatron(dni, PATRON_DNI);

				if (correcto == false) {
					throw new SQLException("Error. Formato DNI del cliente incorrecto.");
				} else {
					existe = clienteDAO.buscarClientePorDni(dni);

					if (existe == false) {
						throw new SQLException("Error. No existe un cliente con ese DNI.");
					} else {

						cadenaResultado = clienteDAO.consultarClienteDNI(dni);

						System.out.println(cadenaResultado);

						confirmacion = ValidacionesEntrada
								.solicitarConfirmacion("¿Seguro que quieres borrar el cliente?: ");
						if (confirmacion == 'S') {
							//DaoCoche.getCocheDao().borrarCochePorMatricula(matricula);
							clienteDAO.borrarCliente(dni);
							System.out.println(cadenaResultado);
							System.out.println("\nCliente borrado correctamente.");
						}

					}
				}
				break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Método para consultar los cliente que contengan unos apellidos indicados
	 * 
	 * @param apellidosCliente para buscar
	 * @param clienteDAO
	 * @throws SQLException si no hay clientes con esos apellidos
	 */
	private static void consultarClientesPorApellidos(String apellidosCliente, DaoCliente clienteDAO)
			throws SQLException {

		String resultadoConsulta;

		resultadoConsulta = clienteDAO.consultarClientesPorApellidos(apellidosCliente);

		if (resultadoConsulta.isEmpty()) {
			throw new SQLException("No hay clientes con esos apellidos.");
		} else {
			System.out.println(resultadoConsulta);
		}

	}

	/**
	 * Método para consultar todos los clientes
	 * 
	 * @param clienteDAO
	 * @throws SQLException si no hay clientes aún en la base de datos(Está vacía)
	 */
	private static void consultarTodosClientes(DaoCliente clienteDAO) throws SQLException {

		String resultadoConsulta;

		resultadoConsulta = clienteDAO.consultarTodosClientes();

		if (resultadoConsulta.isEmpty()) {
			throw new SQLException("Error. No hay clientes aún en la base de datos.");
		} else {
			System.out.println(resultadoConsulta);
		}

	}

	/**
	 * Método para modificar datos de un cliente
	 * 
	 * @param dni del cliente a buscar que queremos modificar datos
	 * @throws SQLException error si no encuentra cliente con el dni indicado
	 */
	private static void modificarDatosDeCliente(String dni) throws SQLException {
		String nombreNuevo, nuevosApellidos;
		String cadenaSQL;
		int opcion;
		boolean existe = false;

		opcion = Menus.mostrarOpcionesCambiarDatosCliente();

		switch (opcion) {
		case 1:// Pedimos el nuevo nombre del cliente

			existe = DaoCliente.buscarClientePorDni(dni);

			if (existe == false) {
				throw new SQLException("Error. No hay cliente con ese DNI.");
			} else {
				nombreNuevo = ValidacionesEntrada.solicitarCadena("Introduce el nuevo nombre del cliente: ");
				cadenaSQL = "UPDATE Cliente set Nombre = '" + nombreNuevo + "' WHERE DNI = '" + dni + "'";
				// Pasarle aqui la sentencia sql ya con los datos que tiene que modificar
				DaoCliente.modificarDatosCliente(cadenaSQL);

				System.out.println("\nNombre del cliente modificado correctamente.");
			}
			break;
		case 2:// Pedimos los nuevos apellidos del cliente
			existe = DaoCliente.buscarClientePorDni(dni);

			if (existe == false) {
				throw new SQLException("Error. No hay cliente con ese DNI.");
			} else {
				nuevosApellidos = ValidacionesEntrada.solicitarCadena("Introduce los nuevos apellidos del cliente: ");
				cadenaSQL = "UPDATE Cliente set Apellidos = '" + nuevosApellidos + "' WHERE DNI = '" + dni + "'";
				// Pasarle aqui la sentencia sql ya con los datos que tiene que modificar
				DaoCliente.modificarDatosCliente(cadenaSQL);

				System.out.println("\nApellidos del cliente modificado correctamente.");
			}
			break;
		case 3:
			existe = DaoCliente.buscarClientePorDni(dni);

			if (existe == false) {
				throw new SQLException("Error. No hay cliente con ese DNI.");
			} else {
				nombreNuevo = ValidacionesEntrada.solicitarCadena("Introduce el nuevo nombre del cliente: ");
				nuevosApellidos = ValidacionesEntrada.solicitarCadena("Introduce los nuevos apellidos del cliente: ");
				cadenaSQL = "UPDATE Cliente set Nombre = '" + nombreNuevo + "' , Apellidos = '" + nuevosApellidos
						+ "' WHERE DNI = '" + dni + "'";
				// Pasarle aqui la sentencia sql ya con los datos que tiene que modificar
				DaoCliente.modificarDatosCliente(cadenaSQL);

				System.out.println("\nNombre y apellidos del cliente modificados correctamente.");
			}
			break;
		}

	}

}
