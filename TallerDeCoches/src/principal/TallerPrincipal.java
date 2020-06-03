package principal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import dao.DaoCliente;
import dao.DaoCoche;
import dao.DaoRevision;

public class TallerPrincipal {

	private static Scanner teclado = new Scanner(System.in);
	private static final String PATRON_DNI = "[0-9]{8}[A-Z]";
	private static final String PATRON_MATRICULA = "[0-9]{4}[A-Z]{3}";

	public static void main(String[] args) {

		int opcion;

		// preguntar si hacer la conexion
		try {
			// DaoCliente clienteDAO = DaoCliente.getClienteDao();
			// DaoCoche cocheDAO = DaoCoche.getCocheDao();
			// DaoRevision revisionDao = DaoRevision.getRevisionDao();

			do {
				opcion = Menus.mostrarMenu();
				tratarMenu(opcion);

			} while (opcion != 4);

		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		} finally {

		}

	}

	private static void tratarMenu(int opcion) throws ClassNotFoundException, SQLException {
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

	private static void tratarMenuRevision(int opcionRevision) throws ClassNotFoundException, SQLException {
		DaoRevision revisionDao = DaoRevision.getRevisionDao();
		String textoDescripcion, tipoRevision, matricula, dni, fechaInicio, fechaFinal;
		int idRevision;
		double precioRevision;
		String fechaDate;
		boolean esCorrecto = false;
		boolean existe = false;

		try {
			switch (opcionRevision) {
			case 1:
				// Alta de revisión
				System.out.println("Vamos a introduir los datos de la revisión.");
				fechaDate = ValidacionesEntrada.solicitarCadena("Introduce la fecha de la revision(dd/mm/aaaa): ");
				textoDescripcion = ValidacionesEntrada.solicitarCadena("Introduce la descripción de la revisión: ");
				tipoRevision = ValidacionesEntrada
						.solicitarCadena("Introduce el tipo de revisión(Aceite, ruedas, motor, electronica): ");
				precioRevision = ponerPrecioRevision(tipoRevision);
				matricula = ValidacionesEntrada.solicitarCadena("Introduce la matricula del coche de la revisión: ");

				existe = DaoCoche.comprobarCochePorMatricula(matricula);

				if (existe == false) {
					System.out.println("Error. No hay coche con esa matrícula.");
				} else {
					revisionDao.darAltaRevision(fechaDate, textoDescripcion, precioRevision, tipoRevision, matricula);
				}

				break;
			case 2:
				// Consulta de todas las revisiones entre dos fechas(Preguntar por que está mal
				// hecho)
				System.out.println("Vamos a introducir dos fechas para mostrar revisiones entre las 2 fechas.");
				fechaInicio = ValidacionesEntrada.solicitarCadena("Introduce la primera fecha: ");
				fechaFinal = ValidacionesEntrada.solicitarCadena("Introduce la segunda fecha: ");

				// Comproabar el formato PREGUNTAR
				revisionDao.consultarTodasRevisionesEntreDosFechas(fechaInicio, fechaFinal);

				break;
			case 3:
				// Consultar las revisiones de un cliente
				dni = ValidacionesEntrada.solicitarCadena("Introduce el dni del cliente: ");

				existe = DaoCliente.buscarClientePorDni(dni);

				if (existe == false) {
					System.out.println("Error. No existe cliente con ese DNI.");
				} else {
					revisionDao.consultarRevisionesDeUnCliente(dni);
				}

				break;
			case 4:
				// Hacer la media de las revisiones
				matricula = ValidacionesEntrada.solicitarCadena("Introduce la matricula del coche: ");

				existe = DaoCoche.comprobarCochePorMatricula(matricula);

				if (existe) {
					System.out.println("Error. No hay coche con esa matrícula.");
				} else {
					revisionDao.hacerMediaPrecioRevisionesUnCoche(matricula);
				}
				break;
			case 5:
				// Modificar algun dato de la revision del coche
				matricula = ValidacionesEntrada.solicitarCadena("Introduce la matricula del coche: ");

				existe = DaoCoche.comprobarCochePorMatricula(matricula);

				if (existe == false) {
					System.out.println("Error. No hay coche con esa matrícula.");
				} else {
					idRevision = ValidacionesEntrada.solicitarNumero("Introduce el id de la revisión a modificar: ");

					existe = revisionDao.comprobarExisteRevisionId(idRevision);
					if (existe == false) {
						System.out.println("Error. No hay revisión con ese id.");
					} else {
						modificarDatoRevisionDeUnCoche(idRevision);

					}

				}

				break;
			case 6:
				// Borrar la revisión por su id, pero antes buscamos por matricula ese coche
				// para borrar la revisión
				matricula = ValidacionesEntrada.solicitarCadena("Introduce la matricula del coche: ");
				
				existe = DaoCoche.comprobarCochePorMatricula(matricula);
				
				if (existe == false) {
					System.out.println("Error. No hay coche con esa matrícula.");
				}else {
					
					ResultSet result;
					String cadenaSQL = "SELECT * FROM Revision WHERE Matricula_Revisiones = '" + matricula + "'";
					result = (ResultSet) revisionDao.buscarRevisionesDeUnCocheMatricula(cadenaSQL);
					
					//Mostramos la lista de revisiones del coche con esa matricula
					if(!result.next()) {
						System.out.println("Error. No hay revisiones de ese coche con esa matricula.");
					}else {
						while(result.next()) {
							System.out.println("Revisiones");
							System.out.println("\tId Revisión: " + result.getInt("idRevision"));
							System.out.println("\tDescripción: " + result.getInt("Descripcion"));
							System.out.println("\tFecha: " + result.getInt("Fecha"));
							System.out.println("\tPrecio Revisión: " + result.getInt("PrecioRevision"));
							System.out.println("\tTipo Revisión: " + result.getInt("TipoRevision"));
						}
						
						//Solicitamos el id para borrar
						idRevision = ValidacionesEntrada.solicitarNumero("Introduce el id de la revisión a borrar: ");
						revisionDao.borrarRevisionPorSuId(idRevision);
						System.out.println("Borrado correctamente.");
					}

					
					
				}
				
				break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private static void modificarDatoRevisionDeUnCoche(int idRevision) throws SQLException {

		int opcion;
		char confirmar;

		do {
			opcion = Menus.mostrarMenuCambiarDatosRevision();

			cambiarDatosRevision(opcion);

			confirmar = ValidacionesEntrada
					.solicitarConfirmacion("¿Quieres seguir modificando datos de la revisión?(S/N): ");
		} while (confirmar == 'S');

	}

	private static void cambiarDatosRevision(int opcion) throws SQLException {

		String nuevaDescripcion, nuevaTipoRevision;
		String nuevaFecha;
		String cadenaSQL;
		double precioNuevo;

		switch (opcion) {
		case 1:
			System.out.println("Introduce la nueva fecha.");
			nuevaFecha = ValidacionesEntrada.solicitarCadena("Introduce la nueva fecha(dd/mm/aaaa) o (dd-mm-aaaa): ");
			cadenaSQL = "UPDATE Revision set Fecha = '" + nuevaFecha + "'";
			DaoRevision.modificarDatosRevision(cadenaSQL);
			break;
		case 2:
			nuevaDescripcion = ValidacionesEntrada.solicitarCadena("Introduce la nueva descripción: ");
			cadenaSQL = "UPDATE Revision set Descripcion = '" + nuevaDescripcion + "'";
			DaoRevision.modificarDatosRevision(cadenaSQL);
			break;
		case 3:
			nuevaTipoRevision = ValidacionesEntrada.solicitarCadena("Introduce el nuevo tipo de revisión: ");
			precioNuevo = ponerPrecioRevision(nuevaTipoRevision);
			cadenaSQL = "UPDATE Revision set TipoRevision = '" + nuevaTipoRevision + "' AND PrecioRevision = "
					+ precioNuevo;
			DaoRevision.modificarDatosRevision(cadenaSQL);
			break;
		}

	}

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

	private static void tratarMenuCoches(int opcionCoche) throws ClassNotFoundException, SQLException {

		DaoCoche cocheDAO = DaoCoche.getCocheDao();
		DaoCliente clienteDAO = DaoCliente.getClienteDao();
		String marca, modelo, matricula;
		String dniCliente;
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
					System.out.println("Error. Formato Matricula incorrecto");
				} else {
					existe = cocheDAO.comprobarCochePorMatricula(matricula);

					if (existe == true) {
						System.out.println("Error. Ya existe un vehiculo con esa matrícula.");
					} else {
						existe = clienteDAO.buscarClientePorDni(dniCliente);

						if (existe = false) {
							System.out.println("Error. No existe ningún cliente con ese DNI.");
						} else {
							cocheDAO.darAltaCoche(marca, modelo, matricula, dniCliente);
						}
					}

				}

				break;
			case 2:
				// Consultar todos los coches, ordenados por su matrícula
				cocheDAO.consultarTodosLosCoches();
				break;
			case 3:
				// Consultar las revisiones de un coche en concreto, ordenados por la fecha
				matricula = ValidacionesEntrada
						.solicitarCadena("Introduce la matricula del coche(LLLL-NNN) para ver sus revisiones:");

				esCorrecto = ValidacionesEntrada.validarMediantePatron(matricula, PATRON_MATRICULA);

				if (esCorrecto == false) {
					System.out.println("Error. Formato Matricula incorrecto.");
				} else {
					// Preguntar si hacer esto de buscar la matricula o si devuelve el result vacio
					// tambien se controla
					existe = cocheDAO.comprobarCochePorMatricula(matricula);

					if (existe == false) {
						System.out.println("Error. No hay coche con esa matrícula.");
					} else {
						cocheDAO.consultarRevisionesDeUnCoche(matricula);
					}
				}
				break;
			case 4:
				// Consultar los coches que dispone un cliente.
				dniCliente = ValidacionesEntrada.solicitarCadena("Introduce el dni del cliente del coche:");

				esCorrecto = ValidacionesEntrada.validarMediantePatron(dniCliente, PATRON_DNI);

				if (esCorrecto == false) {
					System.out.println("Error. Formato Matricula incorrecto.");
				} else {
					// Preguntar si hacer esto de buscar la matricula o si devuelve el result vacio
					// tambien se controla
					cocheDAO.consultarCochesCliente(dniCliente);

				}
				break;
			case 5:
				// Modificar cualquier dato(Preguntar no se hacerlo)
				matricula = ValidacionesEntrada
						.solicitarCadena("Introduce la matricula del coche a modificar(LLLL-NNN):");

				esCorrecto = ValidacionesEntrada.validarMediantePatron(matricula, PATRON_MATRICULA);

				if (esCorrecto == false) {
					System.out.println("Error. Formato Matricula incorrecto.");
				} else {
					// Preguntar si hacer esto de buscar la matricula o si devuelve el result vacio
					// tambien se controla
					do {
						modificarDatoDeUnCoche(matricula);
						confirmar = ValidacionesEntrada
								.solicitarConfirmacion("¿Quieres seguir cambiando datos(S/N)?: ");
					} while (confirmar == 'S');

				}

				break;
			case 6:
				// Borrar el coche
				matricula = ValidacionesEntrada.solicitarCadena("Introduce la matricula del coche a borrar(LLLL-NNN):");

				esCorrecto = ValidacionesEntrada.validarMediantePatron(matricula, PATRON_MATRICULA);

				if (esCorrecto == false) {
					System.out.println("Error. Formato Matricula incorrecto.");
				} else {
					// Preguntar si hacer esto de buscar la matricula o si devuelve el result vacio
					// tambien se controla
					existe = cocheDAO.comprobarCochePorMatricula(matricula);

					if (existe == false) {
						System.out.println("Error. No existe coche con esa matrícula.");
					} else {
						cocheDAO.borrarCochePorMatricula(matricula);
					}

				}

				break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

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
				System.out.println("Error. No existe coche con esa matrícula.");
			} else {
				marcaNueva = ValidacionesEntrada
						.solicitarCadena("Introduce la nueva marca del vehículo para modificarla: ");
				cadenaSQL = "UPDATE Coche set Marca = '" + marcaNueva + "'";
				DaoCoche.modificarDatosCoche(cadenaSQL);
			}
			break;
		case 2:
			if (existe == false) {
				System.out.println("Error. No existe coche con esa matrícula.");
			} else {
				modeloNuevo = ValidacionesEntrada
						.solicitarCadena("Introduce el nuevo modelo del vehículo para modificar: ");
				cadenaSQL = "UPDATE Coche set Modelo = '" + modeloNuevo + "'";
				DaoCoche.modificarDatosCoche(cadenaSQL);
			}
			break;
		}

	}

	private static void tratarMenuCliente(int opcionCliente) throws ClassNotFoundException, SQLException {
		String nombreCliente, apellidosCliente, dni;
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
					System.out.println("Error. Formato DNI del cliente incorrecto.");
				} else {
					existe = clienteDAO.buscarClientePorDni(dni);

					if (existe == true) {
						System.out.println("Error. Ya existe un cliente con ese DNI.");
					} else {
						clienteDAO.nuevoCliente(nombreCliente, apellidosCliente, dni);
					}
				}

				break;

			case 2:
				// Consultar todos los clientes
				clienteDAO.consultarTodosClientes();
				break;
			case 3:
				// Consultar todos los clientes que contengan un apellido
				apellidosCliente = ValidacionesEntrada
						.solicitarCadena("Introduce el apellido del o de los clientes a consultar: ");
				clienteDAO.consultarClientesPorApellidos(apellidosCliente);
				break;
			case 4:
				// Modificar algun dato del cliente
				dni = ValidacionesEntrada.solicitarCadena("Introduce el dni del cliente a modificar: ");
				correcto = ValidacionesEntrada.validarMediantePatron(dni, PATRON_DNI);

				if (correcto == false) {
					System.out.println("Error. Formato DNI del cliente incorrecto.");
				} else {
					existe = clienteDAO.buscarClientePorDni(dni);

					if (existe == true) {
						System.out.println("Error. Ya existe un cliente con ese DNI.");
					} else {
						modificarDatosDeCliente(dni);
					}
				}

				break;
			case 5:
				// Borrar el cliente por su DNI, se borrará también la lista de coches del
				// cliente elegido
				dni = ValidacionesEntrada.solicitarCadena("Introduce el dni del cliente que quieres borrar: ");
				correcto = ValidacionesEntrada.validarMediantePatron(dni, PATRON_DNI);

				if (correcto == false) {
					System.out.println("Error. Formato DNI del cliente incorrecto.");
				} else {
					existe = clienteDAO.buscarClientePorDni(dni);

					if (existe == true) {
						System.out.println("Error. Ya existe un cliente con ese DNI.");
					} else {
						clienteDAO.borrarCliente(dni);
					}
				}
				break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

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
				System.out.println("Error. No hay cliente con ese DNI.");
			} else {
				nombreNuevo = ValidacionesEntrada.solicitarCadena("Introduce el nuevo nombre del cliente: ");
				cadenaSQL = "UPDATE Cliente set Nombre = '" + nombreNuevo + "' WHERE DNI = '" + dni + "'";
				// Pasarle aqui la sentencia sql ya con los datos que tiene que modificar
				DaoCliente.modificarDatosCliente(cadenaSQL);
			}
			break;
		case 2:// Pedimos los nuevos apellidos del cliente
			existe = DaoCliente.buscarClientePorDni(dni);

			if (existe == false) {
				System.out.println("Error. No hay cliente con ese DNI.");
			} else {
				nuevosApellidos = ValidacionesEntrada.solicitarCadena("Introduce los nuevos apellidos del cliente: ");
				cadenaSQL = "UPDATE Cliente set Apellidos = '" + nuevosApellidos + "' WHERE DNI = '" + dni + "'";
				// Pasarle aqui la sentencia sql ya con los datos que tiene que modificar
				DaoCliente.modificarDatosCliente(cadenaSQL);
			}
			break;
		case 3:
			existe = DaoCliente.buscarClientePorDni(dni);

			if (existe == false) {
				System.out.println("Error. No hay cliente con ese DNI.");
			} else {
				nombreNuevo = ValidacionesEntrada.solicitarCadena("Introduce el nuevo nombre del cliente: ");
				nuevosApellidos = ValidacionesEntrada.solicitarCadena("Introduce los nuevos apellidos del cliente: ");
				cadenaSQL = "UPDATE Cliente set Nombre = '" + nombreNuevo + "' and Apellidos = '" + nuevosApellidos
						+ "' WHERE DNI = '" + dni + "'";
				// Pasarle aqui la sentencia sql ya con los datos que tiene que modificar
				DaoCliente.modificarDatosCliente(cadenaSQL);
			}
			break;
		}

	}

}
