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


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jws.soap.InitParam;

import org.json.JSONException;
import org.json.JSONObject;


/**
Esta classe representa a requisicao do Cliente
@author Djavan Fernando dos Santos (Djavan Ryuuzaki)
@since Tutorial: Criando um Servidor HTTP em Java. Parte 2
*/

public class HTTPRequest{

	private String cabecalho;
	JSONObject my_obj = new JSONObject();
	
	public HTTPRequest(){
		cabecalho = "";
		}
	
	
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	public void httpHandler(BufferedReader input, DataOutputStream output) throws IOException, JSONException{
		while (input.ready()){
			//int ano = input.read();

			cabecalho += input.readLine()+"\n";
			
			//System.out.println(Integer.parseInt(cabecalho));
			
			//int ano = cabecalho;
			
			//System.out.println(ano);
			//my_obj.put("ano", ano);

			}
		}
	
	
	
	
	//Setters and Getters
	public String getCabecalho(){
		return this.cabecalho;
		}
	
}