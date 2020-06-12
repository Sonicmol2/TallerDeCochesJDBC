package persistencia;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Coche implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String matricula;
	
	private String marca;
	
	private String modelo;

	private Cliente cliente;

	private List<Revision> listaRevisiones;

	public Coche(String marca, String modelo, String matricula, Cliente cliente) {
		
		this.marca = marca;
		this.modelo = modelo;
		this.matricula = matricula;
		this.cliente = cliente;
		this.listaRevisiones = new ArrayList<Revision>();
	}

	public Coche() {
		this.listaRevisiones = new ArrayList<Revision>();
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) throws SQLException {

		if (marca.length() <= 0) {
			throw new SQLException("Error. Muy corto la marca.");
		}

		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) throws SQLException {

		if (modelo.length() <= 0) {
			throw new SQLException("Error. Muy corto el nombre del modelo.");
		}

		this.modelo = modelo;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) throws SQLException {
		this.matricula = matricula;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Revision> getListaRevisiones() {
		return listaRevisiones;
	}

	public void setListaRevisiones(List<Revision> listaRevisiones) {
		this.listaRevisiones = listaRevisiones;
	}

	@Override
	public String toString() {
		return "Coche: \n\tMarca: " + marca 
				+ " \n\tModelo: " + modelo 
				+ " \n\tMatricula:" + matricula
				+ " \n\tListaRevisiones: " + listaRevisiones + "\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matricula == null) ? 0 : matricula.hashCode());
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
		Coche other = (Coche) obj;
		if (matricula == null) {
			if (other.matricula != null)
				return false;
		} else if (!matricula.equals(other.matricula))
			return false;
		return true;
	}

}
