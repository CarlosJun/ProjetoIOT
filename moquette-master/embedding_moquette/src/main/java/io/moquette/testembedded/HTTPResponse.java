/*
 * Copyright 2016 Carlos.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.moquette.testembedded;

import java.io.DataOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import static io.moquette.testembedded.EmbeddedLauncher.database;
/**
Esta classe representa uma resposta do servidor
@author Djavan Fernando dos Santos (Djavan Ryuuzaki)
@since Tutorial: Criando um Servidor HTTP em Java. Parte 2
*/

public class HTTPResponse {
	
	private DataOutputStream output;
	private HTTPRequest request;

	
	public HTTPResponse(DataOutputStream output){
		this.output = output;
		}
	
	
	public void setRequest(HTTPRequest request){
		this.request = request;
		}
	
	
	public void enviaResposta() throws IOException, JSONException{
		JSONObject my_obj = new JSONObject();
		//int ano=2012;
                float hum = Float.parseFloat(database.getHumidity());
                float chuva = Float.parseFloat(database.getRain());
		my_obj.put("umidade",hum);
		my_obj.put("chuva", chuva);
		//my_obj.put("vazao", "teste");//database.getFlow());
		
		String json_string = my_obj.toString();
		
		
		String cabecalhoHTML = json_string;//"<html> <body>"+json_string+"</body>";
		//String corpoHTML = "<body> <h1> Pagina Encontrada </h1> Bem vindo! <br />";
		//corpoHTML += request.getCabecalho()+ "</body>";
		
		
		output.writeBytes(construirCabecalhoHTTP(200));
		//output.writeBytes(cabecalhoHTML+corpoHTML);
		output.writeBytes(cabecalhoHTML);
		output.close();
		}
	
	private String construirCabecalhoHTTP(int codigoRetorno) {
		String cabecalho = "HTTP/1.0 ";
		
		switch (codigoRetorno) {
		case 200:
			cabecalho += "200 OK";
			break;
			case 400:
				cabecalho += "400 Bad Request";
				break;
			case 403:
				cabecalho += "403 Forbidden";
				break;
			case 404:
				cabecalho += "404 Not Found";
				break;
			case 500:
				cabecalho += "500 Internal Server Error";
				break;
			case 501:
				cabecalho += "501 Not Implemented";
				break;
				}
		
		cabecalho += cabecalho + "\r\n";
		cabecalho += cabecalho + "Connection: close\r\n";
		cabecalho += cabecalho + "Server: Tutorial Servidor HTTP em Java v0\r\n";
		cabecalho += cabecalho + "Content-Type: text/html\r\n";
		cabecalho += cabecalho + "\r\n";
		return cabecalho;
		}
	
}