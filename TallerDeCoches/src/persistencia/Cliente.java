package persistencia;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dni;
	
	private String nombre;
	
	private String apellidos;

	
	private List<Coche> listaCoches;

	public Cliente(String nombre, String apellidos, String dni) {

		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.listaCoches = new LinkedList<Coche>();
	}

	public Cliente() {
		this.listaCoches = new LinkedList<Coche>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public List<Coche> getListaCoches() {
		return listaCoches;
	}

	public void setListaCoches(List<Coche> listaCoches) {
		this.listaCoches = listaCoches;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (dni == null) {
			if (other.dni != null)
				return false;
		} else if (!dni.equals(other.dni))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cliente \n\tNombre: " + nombre + " \n\tApellidos: " + apellidos + " \n\tdni: " + dni
				+ " \n\tListaCoches: " + listaCoches + "\n";
	}

}
