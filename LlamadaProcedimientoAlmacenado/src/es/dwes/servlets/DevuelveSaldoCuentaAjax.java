package es.dwes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.dwes.auxiliar.AccesoDatos;
import es.dwes.pojos.Cuenta;

@WebServlet("/DevuelveSaldoCuentaAjax")
public class DevuelveSaldoCuentaAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		// comprobación de solicitud vía AJAX
		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			String cuentaCompleta = (String) request.getParameter("cuenta");
			AccesoDatos aD = new AccesoDatos();
			Cuenta laCuenta= aD.devuelveCuenta(cuentaCompleta);
			double saldo = laCuenta.getSaldo();
			out.print(saldo);
		} else {  // no viene por AJAX
			out.println("ERROR: Esta ubicación sólo es accesible vía AJAX");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
