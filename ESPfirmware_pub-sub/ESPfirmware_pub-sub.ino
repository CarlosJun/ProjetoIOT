#include <PubSubClient.h>
#include <ESP8266WiFi.h>

//////////////////////////Added toggling led with any topic publish "1"  /////
 
const char* ssid = "ssid";
const char* password = "senha";
 
char* topic = "chave";     //  using wildcard to monitor all traffic from mqtt server
char* server = "ip_servidor";  // Address of my server on my network, substitute yours!
char message_buff[100];   // initialise storage buffer (i haven't tested to this capacity.)

char msg[50];
String serialMsg;

WiFiClient wifiClient;
PubSubClient client(server, 1883, callback, wifiClient);
 
 
String macToStr(const uint8_t* mac)
{
  String result;
  for (int i = 0; i < 6; ++i) {
    result += String(mac[i], 16);
    if (i < 5)
      result += ':';
  }
  return result;
}
 
void setup() {
  Serial.begin(115200);
  delay(10);
  
    // prepare GPIO2 *********************************************************************
  pinMode(2, OUTPUT);   // i am using gpio2 as output to toggle an LED
  digitalWrite(2, 0);  //*****************************************************************
  
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
 
  //  connection to broker script.
  if (client.connect("arduinoClient")) {
    client.publish("outTopic","Hello Valvula!");
    client.subscribe(topic);   // i should probably just point this to the varialbe above.
  }
}

void reconectar() {
  while (!client.connected()) {
    //Serial.println("Conectando ao Broker MQTT.");
    if (client.connect("ESP8266")) {
      //Serial.println("Conectado com Sucesso ao Broker");
    } else {
      //Serial.print("Falha ao Conectador, rc=");
      //Serial.print(MQTT.state());
      //Serial.println(" tentando se reconectar...");
      delay(3000);
    }
  }
}

 
void loop() {
  if (!client.connected()) {
    reconectar();
  }
  client.loop();
   if (Serial.available() > 0)
  {
    serialMsg = Serial.readString();
    serialMsg.toCharArray(msg,50);
    //Serial.print("Mensagem a ser Puplicada: ");
    Serial.println(msg);
    client.publish("vazao", msg);
  }
  
}


void callback(char* topic, byte* payload, unsigned int length) {
  // Here is what i have been using to handle subscriptions. I took it as a snippet from elsewhere but i cannot credit author as i dont have reference!
    int i = 0;

  //Serial.println("Message arrived:  topic: " + String(topic));
  //Serial.println("Length: " + String(length,DEC));
  
  // create character buffer with ending null terminator (string)
  for(i=0; i<length; i++) {
    message_buff[i] = payload[i];
  }
  message_buff[i] = '\0';
  
  String msgString = String(message_buff);

  Serial.print(msgString);
  //Serial.println("Payload: " + msgString);
  
  int state = digitalRead(2);  // get the current state of GPIO1 pin
  if (msgString == "1"){    // if there is a "1" published to any topic (#) on the broker then:
    digitalWrite(2, !state);     // set pin to the opposite state 
    //Serial.println("Switching LED"); 
  }
}

