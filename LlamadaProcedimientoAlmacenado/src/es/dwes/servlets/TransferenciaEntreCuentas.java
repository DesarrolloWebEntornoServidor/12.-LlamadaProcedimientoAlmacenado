package es.dwes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.dwes.auxiliar.AccesoDatos;
import es.dwes.pojos.Cuenta;

@WebServlet("/TransferenciaEntreCuentas")
public class TransferenciaEntreCuentas extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		// PrintWriter out = response.getWriter();
		AccesoDatos aD = new AccesoDatos();
		List<Cuenta> listaCuentas= aD.devuelveTodasCuentas();
		request.setAttribute("listaCuentas", listaCuentas);

		String vistaDestino = "/TransferenciaEntreCuentas_GET.jsp";
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(vistaDestino);
		dispatcher.forward(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String cuentaOrigenCompleta = request.getParameter("cuentaOrigen");
		String cuentaDestinoCompleta = request.getParameter("cuentaDestino");
		double cantidad = 0.0;
		try {
			cantidad = Double.parseDouble(request.getParameter("cantidad"));
			AccesoDatos aD = new AccesoDatos();
			String resultado = aD.ejecutaTransferencia(cuentaOrigenCompleta, cuentaDestinoCompleta, cantidad);
			out.println(resultado);
		} catch (NumberFormatException nfE) {
			out.println("Cantidad errónea");
		}
		out.println("<br />");
		out.println("<a href=\"javascript:history.back();\">Volver</a>");
	}

}
