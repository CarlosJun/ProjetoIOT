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
package database;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos
 */
public class Database {
    
    DateFormat dateFormat; 
    Calendar calendar;
    
    String meanTemperature = "0.0";
    String meanHumidity = "0.0";
    String meanFlow = "0.0";
    String meanRain = "0.0";
    
    String action = "desligar";
    boolean app = false;
    
    ArrayList<String> temperature;
    ArrayList<String> humidity;
    ArrayList<String> flow;
    ArrayList<String> rain;
    
    String day;
    String filePath = "C:\\Users\\FranciscoCarlos\\Desktop\\moquette-master\\database\\";
    
    FileWriter fstream;
    BufferedWriter printFile;
    
    int noData = 0;
    
    public Database()
    {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();
        day = dateFormat.format(calendar.getTime());
        System.out.println(day);
        /*dateFormat = new SimpleDateFormat("HH:mm:ss");
        calendar = Calendar.getInstance();
        String hour = dateFormat.format(calendar.getTime()); 
        System.out.println(hour); */
        
        openFile();
        
        temperature = new ArrayList();
        humidity = new ArrayList();
        flow = new ArrayList();
        rain = new ArrayList();
    }
    
    public void updateDatabase()
    {
        updateHumidity();
        updateFlow();
        updateRain();
        updateTemperature();
        
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        calendar = Calendar.getInstance();
        String hour = dateFormat.format(calendar.getTime());
        
        String printMsg = hour + " umidade=" + meanHumidity + " chuva=" + meanRain + " temperatura=" + meanTemperature + " vazao=" + meanFlow + " acao=" + action + "\n";
        System.out.println("db msg = " + printMsg);
        
        
        try {
            printFile.write(printMsg);
            printFile.flush();
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        
        
        
    }
    
    public boolean getApp()
    {
        return this.app;
    }
    
    public void setApp(boolean state)
    {
        this.app = state;
    }
    
    public void setAction(String newAction)
    {
        this.action = newAction;
    }
    
    public String getAction()
    {
        return this.action;
    }
    
    public void openFile()
    {
        try  
        {
            fstream = new FileWriter(filePath + day + ".txt", true);
            printFile = new BufferedWriter(fstream);
        }
        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
        
    }
    
    public void addTemperature(String tempValue)
    {
        temperature.add(tempValue);
    }
    
    public void updateRain()
    {
        float mean = 0.0f;
        ArrayList<String> aux = new ArrayList(rain);
        int size = aux.size();
        for(String value: aux)
        {
            mean += Float.parseFloat(value);
        }
        if(size != 0)
            mean = mean/size;
        
        if(mean != 0.0f)
        {
            meanRain = Float.toString(mean);
            noData = 0;
        }
        else
            noData++;
        
        rain.clear();
    }
    
    public void updateHumidity()
    {
        float mean = 0.0f;
        ArrayList<String> aux = new ArrayList(humidity);
        int size = aux.size();
        for(String value: aux)
        {
            mean += Float.parseFloat(value);
        }
        if(size != 0)
            mean = mean/size;
        
        if(mean != 0.0f)
        {
            meanHumidity = Float.toString(mean);
            noData = 0;
        }
        else
            noData++;
        
        humidity.clear();
    }
    
    public void updateFlow()
    {
        float mean = 0.0f;
        ArrayList<String> aux = new ArrayList(flow);
        int size = aux.size();
        for(String value: aux)
        {
            mean += Float.parseFloat(value);
        }
        if(size != 0)
            mean = mean/size;
        
        if(mean != 0.0f)
        {
            meanFlow = Float.toString(mean);
            //noData = 0;
        }
        //else
            //noData++;
        
        flow.clear();
    }
    
    public void updateTemperature()
    {
        float mean = 0.0f;
        ArrayList<String> aux = new ArrayList(temperature);
        int size = aux.size();
        for(String value: aux)
        {
            mean += Float.parseFloat(value);
        }
        if(size != 0)
            mean = mean/size;
        
        if(mean != 0.0f)
        {
            meanTemperature = Float.toString(mean);
            //noData = 0;
        }
        //else
            //noData++;
        
        temperature.clear();
    }
    
    public void addRain(String tempValue)
    {
        rain.add(tempValue);
    }
    
    public void addHumidity(String tempValue)
    {
        humidity.add(tempValue);
    }
    
    public void addFlow(String tempValue)
    {
        flow.add(tempValue);
    }
    
    public void listTemperature()
    {
        System.out.print("\nListando temperaturas -> ");
        for(String e: temperature)
        {
            System.out.print(e);
        }
        System.out.print("\n");
    }
    
    public void listHumidity()
    {
        System.out.print("\nListando umidades -> ");
        for(String e: humidity)
        {
            System.out.print(e);
        }
        System.out.print("\n");
    }
    
    public String getTemperature ()
    {
        return meanTemperature;
    }
    
    public String getRain()
    {
        return meanRain;
    }
    
    public String getHumidity ()
    {
        return meanHumidity;
    }
    
    public String getFlow ()
    {
        return meanFlow;
    }
    
    
}
