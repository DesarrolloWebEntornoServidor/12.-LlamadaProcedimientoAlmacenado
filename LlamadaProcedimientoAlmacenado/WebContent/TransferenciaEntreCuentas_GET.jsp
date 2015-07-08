<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.List, es.dwes.pojos.Cuenta"%>

<%
	List<Cuenta> listaCuentas = null;
	try {
		listaCuentas = (List<Cuenta>) request.getAttribute("listaCuentas");
	} catch (Exception e) {
		e.printStackTrace();
	}
%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script>
	function devuelveSaldo(org_dst) {
		nombreSelect = '#cuenta' + org_dst;
		nombreSpan = '#saldoCuenta' + org_dst;
		$.ajax({
            url : 'DevuelveSaldoCuentaAjax',
            method : 'GET',
            data : {
                cuenta : $(nombreSelect).val()
            },
            success : function(responseText) {
                $(nombreSpan).text(responseText);  // visualiza el saldo en un DIV
             // también visualiza el saldo en una ventana emergente
                var ventanitaSaldo = window.open("", "", "width=250, height=50", false);
				var cuenta = $(nombreSelect + " option:selected").text();
                ventanitaSaldo.document.write("Cuenta: " + cuenta + " <br />"); 
                ventanitaSaldo.document.write("Saldo = " + responseText);
            }
        });
	}
/*	
	$(document).ready(function() {
	    $('#saldoCOrg').click(function() {
	        $.ajax({
	            url : 'DevuelveSaldoCuentaAjax',
	            method : 'GET',
	            data : {
	                cuenta : $('#cuentaOrigen').val()
	            },
	            success : function(responseText) {
	                $('#saldoCuentaOrigen').text(responseText);
	            }
	        });
	    });
	});
*/	
	function devuelveMovimientos() {
		$.ajax({
        	url : 'DevuelveListaMovimientosAjax',
        	method : 'GET',
        	data : {},
        	success : function(respuesta) {
        		// Descomentar para visualizar en el DIV
            	// $("#listaMovimientos").text(respuesta);  // visualiza los movimientos en un DIV
            	
            	alert('Llega respuesta de DevuelveListaMovimientosAjax');
            	var obj = JSON && JSON.parse(respuesta) || $.parseJSON(respuesta);
				alert(obj.length);
				// AÑADIDO
				// generación de la fila de cabeceras de columna
				var columns = [];	
				var headerTr$ = $('<tr/>');
				for (var i = 0 ; i < obj.length ; i++) {
					var rowHash = obj[i];
				    for (var key in rowHash) {
				    	if ($.inArray(key, columns) == -1) {
				        	columns.push(key);
				            headerTr$.append($('<th/>').html(key));
				        }
				    }
				}
				$("#excelDataTable").append(headerTr$);
				for (var i = 0 ; i < obj.length ; i++) {
			        var row$ = $('<tr/>');
			        for (var colIndex = 0 ; colIndex < columns.length ; colIndex++) {
			            var cellValue = obj[i][columns[colIndex]];
			            if (cellValue == null) {
			            	cellValue = "";
				        }
			            row$.append($('<td/>').html(cellValue));
			        }
			        $("#excelDataTable").append(row$);
			    }
				// FIN AÑADIDO
			}
    	});
	}
</script>
</head>
<body onload="document.getElementById('cantidad').focus();">
<form name="fTransferencia" id="fTransferencia" method="POST" action="TransferenciaEntreCuentas" >
<table>
<tr>
  <td><label for="cuentaOrigen">Cuenta de origen</label></td>
  <td><label for="cantidad">Cantidad a transferir</label></td>
  <td><label for="cuentaDestino">Cuenta de destino</label></td>
</tr>
<tr>
  <td>
<select name="cuentaOrigen" id="cuentaOrigen" onchange="document.getElementById('saldoCuentaOrigen').innerHTML='';">
<option value="00000000000000000000">0000.0000.00.0000000000</option> <!-- Cuenta inexistente para pruebas -->
<%
	for (Cuenta unaCuenta : listaCuentas) {
		String entidad = unaCuenta.getEntidad();
		String sucursal = unaCuenta.getSucursal();
		String dc = unaCuenta.getDc();
		String cuenta = unaCuenta.getCuenta();
		double saldo = unaCuenta.getSaldo();
		String cuentaCompleta = unaCuenta.devuelveCuentaCompleta();		
%>		
	<option value="<%=cuentaCompleta %>"><%=entidad %>.<%=sucursal %>.<%=dc %>.<%=cuenta %></option> <!-- saldo = <%=saldo %> -->
<%	
	}
%>
</select>
</td>
<td><input type="text" name="cantidad" id="cantidad" /></td>
<td>
<%-- Sería más eficiente guardar en una variable el código del <select> e imprimirlo 2 veces --%>
<select name="cuentaDestino" id="cuentaDestino" onchange="document.getElementById('saldoCuentaDestino').innerHTML='';">
<option value="00000000000000000000">0000.0000.00.0000000000</option> <!-- Cuenta inexistente para pruebas -->
<%
	for (Cuenta unaCuenta : listaCuentas) {
		String entidad = unaCuenta.getEntidad();
		String sucursal = unaCuenta.getSucursal();
		String dc = unaCuenta.getDc();
		String cuenta = unaCuenta.getCuenta();
		double saldo = unaCuenta.getSaldo();
		String cuentaCompleta = unaCuenta.devuelveCuentaCompleta();		
%>		
	<option value="<%=cuentaCompleta %>"><%=entidad %>.<%=sucursal %>.<%=dc %>.<%=cuenta %></option> <!-- saldo = <%=saldo %> -->
<%	
	}
%>
</select>
  </td>
</tr>
<tr>
  <td><a href="javascript:devuelveSaldo('Origen');" id="saldoCOrg">Saldo</a> <span id="saldoCuentaOrigen"></span></td>
  <td><input type="submit" value="Transferir" /></td>
  <td><a href="javascript:devuelveSaldo('Destino');" id="saldoCDst">Saldo</a> <span id="saldoCuentaDestino"></span></td>
</tr>  
</table>
</form>
<a href="javascript:devuelveMovimientos();" id="listaMovs">Lista de movimientos</a>
<div id="listaMovimientos">
<table id="excelDataTable" border="1">
</table>
</div>
</body>
</html>