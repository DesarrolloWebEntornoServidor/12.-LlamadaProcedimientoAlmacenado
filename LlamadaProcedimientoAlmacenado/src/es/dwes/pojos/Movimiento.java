package es.dwes.pojos;

import java.util.Date;

public class Movimiento {

	private int id;
	private double cantidad;
	private Date fecha;
	private Cuenta cuentaOrigen;
	private Cuenta cuentaDestino;
	
	public Movimiento() {
	}
	
	public Movimiento(int id, double cantidad, Date fecha, Cuenta cuentaOrigen, Cuenta cuentaDestino) {
		this.id = id;
		this.cantidad = cantidad;
		this.fecha = fecha;
		this.cuentaOrigen = cuentaOrigen;
		this.cuentaDestino = cuentaDestino;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Cuenta getCuentaOrigen() {
		return cuentaOrigen;
	}

	public void setCuentaOrigen(Cuenta cuentaOrigen) {
		this.cuentaOrigen = cuentaOrigen;
	}

	public Cuenta getCuentaDestino() {
		return cuentaDestino;
	}

	public void setCuentaDestino(Cuenta cuentaDestino) {
		this.cuentaDestino = cuentaDestino;
	}
	
}
