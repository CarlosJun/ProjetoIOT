    /*
 * Copyright (c) 2012-2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.moquette.testembedded;

import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.InterceptHandler;
import io.moquette.interception.messages.*;
import io.moquette.parser.proto.messages.AbstractMessage;
import io.moquette.parser.proto.messages.PublishMessage;
import io.moquette.server.Server;
import io.moquette.server.config.IConfig;
import io.moquette.server.config.ClasspathConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import static java.util.Arrays.asList;

import database.Database;
import io.moquette.parser.proto.messages.SubscribeMessage;

import java.io.*;
import java.net.*;
        
import io.moquette.testembedded.Servidor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

    public class EmbeddedLauncher {
        
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8080;
    private static final int BACKLOG = 1;

    private static final String HEADER_ALLOW = "Allow";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final int STATUS_OK = 200;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;

    private static final int NO_RESPONSE_LENGTH = -1;

    private static final String METHOD_GET = "GET";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_OPTIONS;
        
    public static Database database = new Database(); 
    
    static PublishMessage message;
 
    public static String pubMsg;
    public static boolean appPub = false;
    
    //static Servidor servidor = new Servidor();
   
    static class PublisherListener extends AbstractInterceptHandler {

        @Override
        public void onPublish(InterceptPublishMessage msg) {
            String msgIn = new String(msg.getPayload().array());
            System.out.println("Received on topic: " + msg.getTopicName() + " content: " + msgIn);
            
            if(msg.getTopicName().compareTo("vazao") == 0)
            {
                database.addFlow(msgIn);
                //System.out.println("Adicionando o valor " + msgIn + " em vazao");
            }
            if(msg.getTopicName().compareTo("hum") == 0)
            {
                database.addHumidity(msgIn);
            }
            if(msg.getTopicName().compareTo("arduino") == 0)
            {
                String[] parts = msgIn.split(";|=");
                int index = 0;
                for(String str:parts){
                    try{
                        if(str.compareTo("umidade") == 0)
                        {
                            database.addHumidity(parts[index+1]);
                            //System.out.println("Adicionando o valor " + parts[index+1] + " em umidade");
                        }
                        if(str.compareTo("chuva") == 0)
                        {
                            database.addRain(parts[index+1]);
                            //System.out.println("Adicionando o valor " + parts[index+1] + " em chuva");
                        }
                        if(str.compareTo("temperatura") == 0)
                        {
                            database.addTemperature(parts[index+1]);
                            //System.out.println("Adicionando o valor " + parts[index+1] + " em temperatura");
                        }
                    } catch(java.lang.ArrayIndexOutOfBoundsException e) {System.out.println("falha inciial de array");}
                    index++;
                }
                                              
            }
            if((new String(msg.getPayload().array()).compareTo("list") == 0))
            {
                database.listTemperature();
            } 
            if(msg.getTopicName().compareTo("solenoide") == 0)
            {
                if(msgIn.compareTo("ligar") == 0)
                {
                    database.setApp(true);
                    database.setAction("ligar");
                }
                if(msgIn.compareTo("desligar") == 0)
                {
                    database.setApp(true);
                    database.setAction("desligar");
                }
                if(msgIn.compareTo("desconectar") == 0)
                {
                    database.setApp(false);
                    appPub = false;
                }
            }
        }
    }
    
    static class SubscriberListener extends AbstractInterceptHandler {

        @Override
        public void onSubscribe(InterceptSubscribeMessage msg){
            //SubscribeMessage subMsg = new SubscribeMessage();
            //subMsg.addSubscription(new SubscribeMessage.Couple((byte) AbstractMessage.QOSType.MOST_ONE.ordinal(),msg.getTopicFilter()));
            System.out.println(msg.getClientID() + " foi inscrito no topico: " + msg.getTopicFilter());
            if(msg.getTopicFilter().compareTo("solenoide") == 0)
            {
                appPub = true;
            }
                          
        }
    } 
    
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    


    public static void main(String[] args) throws InterruptedException, IOException {
        
        
        final IConfig classPathConfig = new ClasspathConfig();
        
        //servidor.startThread();
        //servidor.run();
        HTTPServidor servidor = new HTTPServidor(8091);
        
        try {
            servidor.startThread();
        } catch(Exception e){}
        
        /*HttpServer server = HttpServer.create(new InetSocketAddress(8002), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();*/
        
        final Server mqttBroker = new Server();
        List<? extends InterceptHandler> userHandlers = asList(new PublisherListener());
        InterceptHandler userHandlers2 = new SubscriberListener();
        
        mqttBroker.startServer(classPathConfig, userHandlers);
        mqttBroker.addInterceptHandler(userHandlers2);    
        
        
        System.out.println("Broker started press [CTRL+C] to stop");
        //Bind  a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Stopping broker");
                mqttBroker.stopServer();
                System.out.println("Broker stopped");
            }
        });
        database.openFile();
        
        PublishMessage message = new PublishMessage();
        message.setQos(AbstractMessage.QOSType.LEAST_ONE);
        String action = null;
        
        while(true)
        {
            Thread.sleep(10000);
            float valorChuva = Float.parseFloat(database.getRain());
            float valorUmidade = Float.parseFloat(database.getHumidity());
            if(!database.getApp())
            {
                if (valorChuva < 600 || valorUmidade < 800 ) 
                {
                    database.setAction("desligar");
                }
                else
                {
                    database.setAction("ligar");
                }
            }
            
            if(appPub)
            {
                
                float umidade;
                float chuva;
                
                
                umidade = Float.valueOf(database.getHumidity());
                
                if(umidade<=400){
                    action = "Umidade alta";
                }
                else if(umidade<800 && umidade>400){
                    action = "Umidade moderada";
                }
                else if(umidade>=800 || umidade == 0){
                    action = "Umidade baixa";                   
                }
                message.setTopicName("umidade");
                message.setRetainFlag(true);
                message.setPayload(ByteBuffer.wrap(action.getBytes()));               
                mqttBroker.internalPublish(message);
                
                
                chuva = Float.valueOf(database.getRain());
                action = "teste";
                        
                if(chuva<=400){
                    action = "Chuva forte";
                }
                else if(chuva<600 && chuva>400){
                    action = "Chuva fraca";
                }
                else if(chuva>=600 || chuva == 0){
                    action = "Sem chuva";                   
                }
                message.setTopicName("chuva");
                message.setRetainFlag(true);
                message.setPayload(ByteBuffer.wrap(action.getBytes()));
                mqttBroker.internalPublish(message);
                
                action = database.getTemperature();
                message.setTopicName("temperatura");
                message.setRetainFlag(true);
                message.setPayload(ByteBuffer.wrap(action.getBytes()));
                mqttBroker.internalPublish(message);
                
                action = database.getFlow();
                message.setTopicName("vazao");
                message.setRetainFlag(true);
                message.setPayload(ByteBuffer.wrap(action.getBytes()));
                mqttBroker.internalPublish(message);
            }
            
            database.updateDatabase();
            message.setTopicName("chave");
            message.setRetainFlag(true);
            action = database.getAction();
            System.out.println("Publicando mensagem: " + action);
            message.setPayload(ByteBuffer.wrap(action.getBytes()));
            mqttBroker.internalPublish(message);
            
        }
        
        /*Thread.sleep(20000);
        System.out.println("Before self publish");
        PublishMessage message = new PublishMessage();
        message.setTopicName("arduino");
        message.setRetainFlag(true);
//        message.setQos(AbstractMessage.QOSType.MOST_ONE);
//        message.setQos(AbstractMessage.QOSType.LEAST_ONE);
        message.setQos(AbstractMessage.QOSType.EXACTLY_ONCE);
        message.setPayload(ByteBuffer.wrap("IniciaIrrigacao".getBytes()));
        mqttBroker.internalPublish(message);
        System.out.println("After self publish1");
        Thread.sleep(20000);
        message.setPayload(ByteBuffer.wrap("CancelaIrrigacao".getBytes()));
        mqttBroker.internalPublish(message);
        System.out.println("After self publish2");
        Thread.sleep(20000);
        message.setTopicName("arduino");
        message.setPayload(ByteBuffer.wrap("IniciaIrrigacao".getBytes()));
        mqttBroker.internalPublish(message);
        System.out.println("After self publish3");
        Thread.sleep(20000);
        message.setTopicName("arduino");
        message.setPayload(ByteBuffer.wrap("CancelaIrrigacao".getBytes()));
        mqttBroker.internalPublish(message);
        System.out.println("After self publish4"); */
        
    
    }
}