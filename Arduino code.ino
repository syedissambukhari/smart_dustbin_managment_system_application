#include <Arduino.h>
#if defined(ESP32)
  #include <WiFi.h>
#elif defined(ESP8266)
  #include <ESP8266WiFi.h>
#endif
#include <Firebase_ESP_Client.h>

//Provide the token generation process info.
#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

// Insert your network credentials
#define WIFI_SSID "Poco_X3_Pro"
#define WIFI_PASSWORD "nawab5433"

// Insert Firebase project API Key
#define API_KEY "your firebase api key"
// Insert RTDB URL
#define DATABASE_URL "firestore database link"

//Define Firebase Data object
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;
bool signupOK = false;
int ultrasonicTriggerPin = D1;  // Ultrasonic sensor trigger pin
int ultrasonicEchoPin = D2;  // Ultrasonic sensor echo pin
int buzzerPin = D3;  // Buzzer pin

void setup() {
  Serial.begin(9600);
  pinMode(ultrasonicTriggerPin, OUTPUT);  // Ultrasonic sensor trigger pin as output
  pinMode(ultrasonicEchoPin, INPUT);  // Ultrasonic sensor echo pin as input
  pinMode(buzzerPin, OUTPUT);  // Buzzer pin as output

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  /* Assign the api key (required) */
  config.api_key = API_KEY;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;

  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")) {
    Serial.println("ok");
    signupOK = true;
  }
  else {
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h

  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
}

void loop() {
  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 5000 || sendDataPrevMillis == 0)) {
    sendDataPrevMillis = millis();

    // Perform your sensor checks and alerts here
    // For example, check the ultrasonic sensor
    digitalWrite(ultrasonicTriggerPin, LOW);
    delayMicroseconds(2);
    digitalWrite(ultrasonicTriggerPin, HIGH);
    delayMicroseconds(5);
    digitalWrite(ultrasonicTriggerPin, LOW);

    long duration = pulseIn(ultrasonicEchoPin, HIGH);
    float distance = duration * 0.034 / 2;
    int percentage = map(distance, 10, 100, 0, 100); // Map distance to percentage (10-100)
    if (distance < 10) {  // Set your desired threshold distance
      // Send an alert to Firebase or perform any required action
      Firebase.RTDB.setString(&fbdo, "Alert", "Your Dustbin Is Full");
      Serial.println("Your Dustbin Is Full");
Serial.print("Distance: ");
      Serial.print(distance);
      Serial.print(" cm");
      Serial.print(" (");
      Serial.print(percentage);
      Serial.println("%)");
      digitalWrite(buzzerPin, HIGH);  // Activate the buzzer
    }
    else {
      // No alert condition
      Firebase.RTDB.setString(&fbdo, "Alert", "No alert");
      Serial.println("No alert");
      Serial.print("Distance: ");
      Serial.print(distance);
      Serial.print(" cm");
      Serial.print(" (");
      Serial.print(percentage);
      Serial.println("%)");
      digitalWrite(buzzerPin, LOW);  // Deactivate the buzzer
    }
    Firebase.RTDB.setInt(&fbdo, "Ultrasonic Sensor/Percentage", percentage);
  }
}





