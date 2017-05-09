
#include <Wire.h>
#include <SparkFunTSL2561.h>
SFE_TSL2561 light;
unsigned int ms;
boolean gain;
float temp;
static int aprint=0;
static int tprint=0;
static int lprint=0;
int templed=10;
int lightled=11;
int airled=12;



 float light_th=10;
float air_th=100;
float temp_th=40;


String input="";


void setup() 
{
 Serial.begin(9600); 
 Serial1.begin(9600);
 
 light.begin();
  gain = 0;
  unsigned char time = 2;
light.setTiming(gain,time,ms);
light.setPowerUp();
pinMode(templed, OUTPUT);
pinMode(lightled, OUTPUT);
pinMode(airled, OUTPUT);

}
void loop()
{
temp = (5.0 * analogRead(A0) * 100.0) / 1024;
Serial.println("temp:");
Serial.println(temp);
delay(500);


int air;
  
  air = analogRead(A1);
  Serial.println("now the Air quality sensor is :");
  Serial.println(air);
  delay(700);
  
  
  unsigned int data0, data1;
   light.getData(data0,data1);
    
 double lux;
 boolean good;
 good = light.getLux(gain,ms,data0,data1,lux);
  Serial.print(" lux: ");
    Serial.print(lux); 
    Serial.println(" "); 
    
   
set();


if(temp < 1 && temp > 100)
{
     Serial1.print("T:");
     Serial1.print("Sensor not Working");
     Serial1.print("~");
     
     delay(1000);
  
}

if(air < 1 && air > 400)
{
  
     Serial1.print("A:");
     Serial1.print("Sensor not Working");
     Serial1.print("~");
     
     delay(1000);
  
  
}

if(lux < 1 && lux > 1800)
{
  
     Serial1.print("L:");
     Serial1.print("Sensor not Working");
     Serial1.print("~");
     
     delay(1000);
  
  
}




  
if(temp>temp_th)
{
  
  
   Serial1.print("T:");
     Serial1.print(temp);
     Serial1.print("~");
    
 
tone(7, 25, 50); 
digitalWrite(templed, HIGH);

delay(1000);
}



else if(temp<temp_th)
{
   Serial1.print("T:");
     Serial1.print("NA");
     Serial1.print("~");
  
   digitalWrite(templed, LOW);
   delay(1000);
}



if(air>air_th)
  {
    
     Serial1.print("A:");
     Serial1.print(air);
     Serial1.print("~");
    
    
   tone(7, 494, 50); 
 digitalWrite(airled, HIGH);
 delay(1000);
  }


else if(air<air_th)
{
   Serial1.print("A:");
     Serial1.print("NA");
     Serial1.print("~");
  
   digitalWrite(airled, LOW);
   delay(1000);
}




if (lux>light_th)
   {
    // if(lprint==0)
     //{
      //Serial1.print("#");
     //Serial1.write("light:");
     Serial1.print("L:");
     Serial1.print(lux);
     Serial1.print("~");
     //Serial1.write(" ");
     //Serial1.print("000000000");
     //Serial1.print("~");
     //Serial1.print(" ");
     //lprint=lprint+1;
   //}
     tone(7, 494, 50);
     digitalWrite(lightled, HIGH);
     delay(1000);
   }


else if(lux<light_th)
{
   Serial1.print("L:");
     Serial1.print("NA");
     Serial1.print("~");
  
   digitalWrite(lightled, LOW);
   delay(1000);
}




delay(1000);
}



void set()
{
  char ch;
  
  int light_f=0;
  int air_f=0;
  int temp_f=0;
  int i=0;
  
  
  while(Serial1.available())
  {
    //delay(100);
    
    ch=(char)Serial1.read();
    input += ch;
    
  }
  
  
 if(input.substring(0,1)=="l")
 {
   
   

   
   
   String s=input.substring(2);
  // light_th=float(s);
   
   light_th=s.toInt();
   Serial.print(light_th);
 }
   
   
   if(input.substring(0,1)=="a")
 {
   String s=input.substring(2);
   air_th=s.toInt();
   Serial.print(air_th);
 }
   
  if(input.substring(0,1)=="t")
 {
   String s=input.substring(2);
   temp_th=s.toInt();
   Serial.print(temp_th);
 } 
   
   
 input="";
  
 // if(air_f==1)
 // air_th=input.toInt();
  
  //if(light_f=1)
  //light_th=input.toInt();
  
 // if(temp_f==1)
 // temp_th=input.toInt();
  
  
  
  
}



