package es.dwes.pojos;

public class Cuenta {

	private String entidad;
	private String sucursal;
	private String dc;
	private String cuenta;
	private double saldo;
	
	public Cuenta() {
		this.entidad = "";
		this.sucursal = "";
		this.dc = "";
		this.cuenta = "";
		this.saldo = 0.0;
	}
	
	public Cuenta(String entidad, String sucursal, String dc, String cuenta) {
		this.entidad = entidad;
		this.sucursal = sucursal;
		this.dc = dc;
		this.cuenta = cuenta;
		this.saldo = 0.0;
	}
	
	public Cuenta(String entidad, String sucursal, String dc, String cuenta, double saldo) {
		this(entidad,sucursal,dc,cuenta);
		this.saldo = saldo;
	}
	
	public Cuenta(String cuentaCompleta) {
		this.entidad = cuentaCompleta.substring(0, 4);
		this.sucursal = cuentaCompleta.substring(4, 8);
		this.dc = cuentaCompleta.substring(8, 10);
		this.cuenta = cuentaCompleta.substring(10, 20);
		this.saldo = 0.0;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getDc() {
		return dc;
	}

	public void setDc(String dc) {
		this.dc = dc;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	// devuelve un String con la cuenta completa
	public String devuelveCuentaCompleta() {
		return entidad + sucursal + dc + cuenta;
	}
	
}
