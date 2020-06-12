package persistencia;

import java.io.Serializable;

public class Revision implements Serializable{

	private static final long serialVersionUID = 1L;

	private int codigoRevision;
	
	private String fecha;
	
	private String descripcion;

	private String tipoRevision;
	
	private double precioRevision;

	private Coche coche;

	public Revision(String fecha, String descripcion, String tipoRevision, double precioRevision, Coche coche) {
		
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.tipoRevision = tipoRevision;
		this.precioRevision = precioRevision;
		this.coche = coche;
	}
	
	public Revision() {
		
	}
	
	public Revision(int idRevision) {
		this.codigoRevision = idRevision;
	}
	
	public int getIdRevision() {
		return codigoRevision;
	}

	public void setIdRevision(int idRevision) {
		this.codigoRevision = idRevision;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipoRevision() {
		return tipoRevision;
	}

	public void setTipoRevision(String tipoRevision) {
		this.tipoRevision = tipoRevision;
	}
	
	public double getPrecioRevision() {
		return precioRevision;
	}

	public void setPrecioRevision(double precioRevision) {
		this.precioRevision = precioRevision;
	}

	public Coche getCoche() {
		return coche;
	}

	public void setCoche(Coche coche) {
		this.coche = coche;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coche == null) ? 0 : coche.hashCode());
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
		Revision other = (Revision) obj;
		if (coche == null) {
			if (other.coche != null)
				return false;
		} else if (!coche.equals(other.coche))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\nRevision: \n\tIdRevision: " + codigoRevision 
				+ " \n\tFecha: " + fecha 
				+ " \n\tDescripción: " + descripcion
				+ " \n\tTipo de Revision: " + tipoRevision 
				+ " \n\tPrecio Revisión: " + precioRevision + "\n" ;
	}
	
	
	
	
}
