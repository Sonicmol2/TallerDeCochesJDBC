package principal;

import java.util.Scanner;

public class Menus {
	private static Scanner teclado = new Scanner(System.in);

	static int mostrarMenu() {
		int opcion = 0;

		do {
			System.out.println("Proyecto JDBC");
			System.out.println("\t[1] Cliente");
			System.out.println("\t[2] Coche");
			System.out.println("\t[3] Revision");
			System.out.println("\t[4] Salir");

			opcion = Integer.parseInt(teclado.nextLine());

		} while (opcion < 1 || opcion > 4);

		return opcion;
	}
	
	static int mostrarMenuCliente() {

		int opcion = 0;

		do {
			System.out.println("[1] Alta nuevo cliente.");
			System.out.println("[2] Consultar todos los clientes.");
			System.out.println("[3] Consultar clientes por un apellido: ");
			System.out.println("[4] Modificar nombre o apellidos de un cliente:");
			System.out.println("[5] Borrar un cliente por el dni.");
			System.out.println("[6] Salir.");

			opcion = Integer.parseInt(teclado.nextLine());

		} while (opcion < 1 || opcion > 6);
		
		return opcion;
	}
	
	static int mostrarOpcionesCambiarDatosCliente() {
		int opcion = 0;

		do {
			System.out.println("¿Que quieres modificar?\n");
			System.out.println("\t|1| Cambiar solamente el nombre del cliente: ");
			System.out.println("\t|2| Cambiar solamente los apellidos del cliente: ");
			System.out.println("\t|3| Cambiar nombre y apellidos: ");
			System.out.println("\nIntroduce la opción: ");

			opcion = Integer.parseInt(teclado.nextLine());

		} while (opcion < 1 || opcion > 3);
		return opcion;
	}
	
	static int mostrarMenuCoche() {
		int opcion = 0;

		do {
			System.out.println("[1] Alta nuevo coche.");
			System.out.println("[2] Consultar todos los coches.");
			System.out.println("[3] Consulta lista de revisiones de un coche por matrícula.");
			System.out.println("[4] Consultar los coches que dispone un cliente.");
			System.out.println("[5] Modificar modelo u otro dato de un coche.");
			System.out.println("[6] Borrar coche por su matrícula.");
			System.out.println("[7] Salir");

			opcion = Integer.parseInt(teclado.nextLine());

		} while (opcion < 1 || opcion > 7);
		return opcion;
	}
	
	static int mostrarCambiosDatosCoche() {

		int opcion = 0;

		do {
			System.out.println("¿Que quieres modificar del coche?\n");
			System.out.println("\t|1| Cambiar solamente la marca del coche: ");
			System.out.println("\t|2| Cambiar solamente el modelo del coche: ");
			System.out.println("\nIntroduce la opción: ");

			opcion = Integer.parseInt(teclado.nextLine());

		} while (opcion < 1 || opcion > 2);
		return opcion;
	}

	public static int mostrarMenuRevision() {
		
		int opcion = 0;

		do {
			System.out.println("[1] Alta nueva revisión.");
			System.out.println("[2] Consultar todas las revisiones entre 2 fechas.");
			System.out.println("[3] Consultar revisiónes de un cliente.");
			System.out.println("[4] Hacer la media del precio de las revisiones de un coche.");
			System.out.println("[5] Modificar fecha o algún otro dato de la revisión.");
			System.out.println("[6] Borrar revision.");
			System.out.println("[7] Salir");

			opcion = Integer.parseInt(teclado.nextLine());
		} while (opcion < 1 || opcion > 7);

		return opcion;
	}

	public static int mostrarMenuCambiarDatosRevision() {
		
		int opcion = 0;

		do {
			System.out.println("¿Que quieres modificar de la revision?\n");
			System.out.println("\t|1| Cambiar la fecha de la revisión: ");
			System.out.println("\t|2| Cambiar la descripción de la revisión: ");
			System.out.println("\t|3| Cambiar tipo de Revisión(Al cambiarse se cambiara también el precio): ");
			System.out.println("\nIntroduce la opción: ");

			opcion = Integer.parseInt(teclado.nextLine());

		} while (opcion < 1 || opcion > 3);

		return opcion;
	}
	
	
}
