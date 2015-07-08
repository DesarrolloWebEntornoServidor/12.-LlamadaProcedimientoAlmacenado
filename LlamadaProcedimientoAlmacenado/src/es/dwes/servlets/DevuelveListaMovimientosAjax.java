package es.dwes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import es.dwes.auxiliar.AccesoDatos;
import es.dwes.pojos.*;

@WebServlet("/DevuelveListaMovimientosAjax")
public class DevuelveListaMovimientosAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AccesoDatos aD = new AccesoDatos();
		List<Movimiento> listaMovimientos = aD.devuelveTodosMovimientos();
		String json = new Gson().toJson(listaMovimientos);
		// response.setContentType("application/json;charset=UTF-8");
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
