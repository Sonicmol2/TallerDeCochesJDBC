package principal;

import java.util.Scanner;

public class ValidacionesEntrada {
	// CONSTANTES
	private static final char RESPUESTA_NO = 'N';
	private static final char RESPUESTA_SI = 'S';
	static Scanner teclado = new Scanner(System.in);

	/**
	 * Solicita un número
	 * 
	 * @param msg texto que mostramos a la hora de solicitar
	 * @return numero introducido
	 */
	static int solicitarNumero(String msg) {

		int numero = 0;
		boolean esValido;

		do {
			try {
				System.out.println(msg);
				numero = Integer.parseInt(teclado.nextLine());
				esValido = true;
			} catch (NumberFormatException e) {
				System.out.println("Debe introducir un número mayor que 0.");
				esValido = false;
			}

		} while (!esValido && numero <= 0);

		return numero;
	}

	static double solicitarPrecio(String msg) {

		double numero = 0;
		boolean esValido;

		do {
			try {
				System.out.println(msg);
				numero = Double.parseDouble(teclado.nextLine());
				esValido = true;
			} catch (NumberFormatException e) {
				System.out.println("Debe introducir un número mayor que 0.");
				esValido = false;
			}

		} while (!esValido && numero <= 0);

		return numero;
	}

	/**
	 * Método que solicita una cadena
	 * 
	 * @param msg texto que mostramos a la hora de solicitar
	 * @return cadena introducida
	 */
	static String solicitarCadena(String msg) {

		String cadena;

		do {
			System.out.println(msg);
			cadena = teclado.nextLine();
		} while (cadena.length() == 0);

		return cadena;
	}

	static char solicitarConfirmacion(String msg) {

		char respuesta;

		System.out.println(msg);
		respuesta = teclado.nextLine().toUpperCase().charAt(0);

		return respuesta;
	}

	static boolean validarMediantePatron(String dnicliente, String patron) {

		boolean esCorrecto = false;

		if (dnicliente.matches(patron)) {
			esCorrecto = true;
		} else {
			esCorrecto = false;
		}

		return esCorrecto;
	}

}
