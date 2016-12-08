#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <PubSubClient.h>

const char* SSID = "ssid"; //Seu SSID da Rede WIFI
const char* PASSWORD = "senha"; // A Senha da Rede WIFI
const char* MQTT_SERVER = "ip_servidor";//"localhost";//"test.mosquitto.org"; //Broker do Mosquitto.org

int value = 0; //Contador de é incrementado de -20 a 50
long lastMsg = 0;
char msg[50];
String serialMsg;
WiFiClient CLIENT;
PubSubClient MQTT(CLIENT);


//CONFIGURAÇÃO DA INTERFACE DE REDE
void setupWIFI() {
  WiFi.begin(SSID, PASSWORD);
  Serial.print("Conectando na rede: ");
  Serial.println(SSID);
  while (WiFi.status() != WL_CONNECTED) {
   Serial.print(".");
   delay(500);
  }
}
void setup(void) {
  Serial.begin(115200);
  setupWIFI(); 
  MQTT.setServer(MQTT_SERVER, 1883);
}
void reconectar() {
  while (!MQTT.connected()) {
    Serial.println("Conectando ao Broker MQTT.");
    if (MQTT.connect("ESP8266")) {
      Serial.println("Conectado com Sucesso ao Broker");
    } else {
      Serial.print("Falha ao Conectador, rc=");
      Serial.print(MQTT.state());
      Serial.println(" tentando se reconectar...");
      delay(3000);
    }
  }
}
void loop(void) {
  if (!MQTT.connected()) {
    reconectar();
  }
  MQTT.loop();
  
  if (Serial.available() > 0)
  {
    serialMsg = Serial.readString();
    serialMsg.toCharArray(msg,50);
    Serial.print("Mensagem a ser Puplicada: ");
    Serial.println(msg);
    MQTT.publish("arduino", msg);
  }
}
