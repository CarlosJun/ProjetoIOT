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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;

/**
Esta classe ser� respons�vel por iniciar a conex�o.
Nesse primeiro tutorial iremos testar a conex�o com o cliente e entender
melhor a requisi��o do protocolo HTTP.
@author Djavan Fernando dos Santos (Djavan Ryuuzaki)
@since Tutorial: Criando um Servidor HTTP em Java. Parte 1
*/

public class HTTPServidor implements Runnable{
    
    Thread thread;
   
    Socket socket = null;

	private int porta;
        
        public void startThread()
        {
            thread.start();
        }
	
	public HTTPServidor() {
		this.porta = 8091;
                thread = new Thread(this);
		}
	
	
	public HTTPServidor(int porta) {
		this.porta = porta;
                thread = new Thread(this);
		}
	
	
	public void setPorta(int porta) {
		this.porta = porta;
		}
        
        @Override
        public void run(){
            try{
                iniciar();
            }catch(Exception e){}
        }
	
	
	public void iniciar() throws JSONException {
		ServerSocket socketServidor = null;
		System.out.println("Servidor Iniciando!");
		
		try {
			System.out.println("Tentando alocar a porta.");
			socketServidor = new ServerSocket(porta);
			}
		catch (IOException e) {
			System.out.println("Erro Fatal: "+e.getMessage());
			}
		catch (Exception e) {
			System.out.println("Erro Fatal: "+e.getMessage());
			}
		
		System.out.println("Servidor OK!");
		
		while (true) {
			System.out.println("Servidor Aguardando.");
			Socket socket = null;
			
			try {
				socket = socketServidor.accept();
				InetAddress infoCliente = socket.getInetAddress();
				System.out.println("Cliente: "+infoCliente.getHostName()+" conectou ao servidor!");
				//ler os dados
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //ler 
				//BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); //ler 
				
				//--parte 2
				//escrever os dados
				DataOutputStream output = new DataOutputStream(socket.getOutputStream()); //escreve
				
				HTTPRequest requisicaoCliente = new HTTPRequest();
				requisicaoCliente.httpHandler(input, output);
				
				HTTPResponse resposta = new HTTPResponse(output);
				resposta.setRequest(requisicaoCliente);
				resposta.enviaResposta();
				System.out.println(requisicaoCliente.getCabecalho());
				System.out.println("Depois do requisiçõ cliente");
				socket.close();
				//--parte 2
				
				//--parte 1 
				int linhaRequisicao = 0;	
				while (input.ready()) {
					System.out.println(linhaRequisicao+" "+input.readLine());
					System.out.println("no while");
					linhaRequisicao++;
					}
				socket.close();
				//--parte 1
				 
				}
			catch (IOException e) {
				System.out.println("Erro de conexao: "+e.getMessage());
				//--parte 2
				break;
				//--parte 2
				}
			}
		}
	}