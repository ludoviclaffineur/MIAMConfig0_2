import processing.core.*; 
import processing.xml.*; 

import oscP5.*; 
import netP5.*; 
import TUIO.*; 
import fullscreen.*; 
import javax.media.opengl.*; 
import processing.opengl.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class MIAMConfig0_1 extends PApplet {





TuioProcessing tuioClient;
 


fader f;
boolean rotationActivated=false;
boolean moveTableMix=false;

FullScreen fs;
hp hpTest;
TuioCursor blobsOnHp [] = new TuioCursor [10];
TuioCursor moveTableMixCursor;
blobsLinked blobsNearHp [] = new blobsLinked [10];
Vector hpOnStage = new Vector();
hp hpSelected []  =new hp [10]; 
fader tableMix[] = new fader[48];
Vector hpInStock = new Vector();
int countBlob=0;
int heightTableMix = 200;
int widthPiseAudio=600;
int heightPiseAudio=height-heightTableMix;
int oldPositionTableMix=100;
PImage planSalle;
String nomSalle="ISIB";

RoundedSquare hpInData ;
OscP5 oscP5;
NetAddress distantLocation = new NetAddress("172.30.7.66", 1111);
public void setup() {
  oscP5 = new OscP5(this, 1112);
  size(1280, 800);
 // size(1920, 1080);
  planSalle=loadImage("planIsib.png");
  loadHpInStock();
  //tuioThread tt = new tuioThread();
 // tt.start();
  //size(1920, 1080,OPENGL);
  tuioClient  = new TuioProcessing(this); 
  fs = new FullScreen(this);
  fs.enter();
  // createTableMix();
}


public void draw() {
    smooth();
   background(0xff183152);

// background(#001336);
// background (#0b162f);
  image(planSalle, 0, 0, width/2, height);

  for(int i = 0 ;i<hpOnStage.size();i++){
    hp rTemp= (hp) (hpOnStage.elementAt(i));
    rTemp.draw();
  }
  for(int i =0 ; i<hpInStock.size();i++)
  {
    RoundedSquare rTemp= (RoundedSquare) (hpInStock.elementAt(i));
    rTemp.draw();
  }
}

public void createTableMix() {
  for (int i = 0; i<48;i++) {
    tableMix[i]=new fader (100+i*50, height-50, ""+(i+1));
  }
}

public void moveTableMix(int jump) {
  for (int i = 0; i<48;i++) {
    tableMix[i].posX= tableMix[i].posX+jump;
  }
}

public void drawMenu() {
  if (hpSelected[0]!=null) {
    fill(0xff318CE7);
    rect(930, 50, 100, 50);
    rect(930, 120, 100, 50);
    rect(930, 190, 100, 50);
    fill(0xff000000);
    text("Test", 950, 70);
    text("Noise", 950, 140);
    text("La", 950, 210);
  }
}


public void loadXML() {
  XMLElement config = new XMLElement(this, "config_ISIB.xml");
  XMLElement[] marque = config.getChildren("hp/marque");
  XMLElement[] x = config.getChildren("hp/x");
  XMLElement[] y = config.getChildren("hp/y");
  XMLElement[] z = config.getChildren("hp/z");
  XMLElement[] rotation = config.getChildren("hp/orientation");
  XMLElement[] opening = config.getChildren("hp/opening");
  for (int i =0 ;i< marque.length;i++)
  {
   
    hp hpTemp = new hp (Integer.parseInt(x[i].getContent()), Integer.parseInt(y[i].getContent()), Integer.parseInt(z[i].getContent()), marque[i].getContent(), i+1);
    
    hpTemp.rotation=Float.parseFloat(rotation[i].getContent());
    hpTemp.opening=Float.parseFloat(opening[i].getContent());
     hpOnStage.add(hpTemp);
  }
}

public void loadHpInStock(){
  XMLElement config = new XMLElement(this, "hp_database.xml");
  XMLElement[] marque = config.getChildren("hp/marque");
  XMLElement[] modele = config.getChildren("hp/modele");
   int j=0;
   int k=0;
   print("marque.length");
  for (int i=0 ;i<marque.length;i++)
  {
    
    if (width/2 +k*110>width-110)
    {
      j++;
      k=0;
    }
    hpInStock.add(new RoundedSquare (40,100, 20, width/2 +k*110, 20+j*50,0xff375D81, null, marque[i].getContent()+" "+modele[i].getContent()));
    k++;
  }

}

public void exportToXML() {
  String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><config>\n";
  data+="<salle>\n";
  data+="<nom>"+nomSalle+"</nom>"; 
  data+="<adresse>150, rue royale, 1000 Bruxelles</adresse>\n";
  data+="</salle>\n";
  for (int i=0;i<hpOnStage.size();i++) {
    hp hpTemp = (hp) hpOnStage.elementAt(i);
    data+="<hp>\n";
    data+="<marque>"+hpTemp.type+"</marque>\n";
    data+="<x>"+hpTemp.position[0]+"</x>\n";
    data+="<y>"+hpTemp.position[1]+"</y>\n";
    data+="<z>"+hpTemp.position[2]+"</z>\n";
    data+="<orientation>"+hpTemp.rotation+"</orientation>\n";
    data+="<opening>"+hpTemp.opening+"</opening>\n";
    data+="</hp>\n";
  }
  data+="</config>";
  String[]lineExport = new String[1];
  lineExport[0]=data;
  saveStrings("config_"+nomSalle+".xml", lineExport);
}

public void keyPressed() {
  if (key=='s') {
    exportToXML();
  }
  if (key=='l') {
    loadXML();
  }
}

public void mousePressed() {
  boolean onSomething=false;
  for (int i=0;i<hpInStock.size();i++) //check clics on buttons.
  {
    RoundedSquare rTemp = (RoundedSquare) hpInStock.elementAt(i);
    if (rTemp.contains(mouseX,mouseY))
    {
      hpOnStage.add(new hp (200,200, 0,rTemp.name, hpOnStage.size()+1));
    }
  }
   for (int i=0;i<hpOnStage.size();i++) //check click on HP added.
  {
    hp rHp = (hp) hpOnStage.elementAt(i);
    if (rHp.isOnHp(mouseX,mouseY))
    {
      if( hpSelected[0]!=null){
        hpSelected[0].isSelected=false;
      }
      rHp.isSelected=true;
       hpSelected[0]=rHp;
       onSomething=true;
    }
  }
 if(hpSelected[0]!=null && !onSomething){
    hpSelected[0].isSelected=false;
    hpSelected[0]=null;
  }
}

public void mouseDragged() {
  for (int i=0;i<hpOnStage.size();i++) //check click on HP added.
  {
    hp rHp = (hp) hpOnStage.elementAt(i);
    if (rHp.equals(hpSelected[0]))
    {
      rHp.position[0]=mouseX;
      rHp.position[1]=mouseY;
    }
  }
  
}

public void mouseReleased() {

}

// called when a cursor is moved
public void updateTuioCursor (TuioCursor tcur) {
}

// called after each message bundle
// representing the end of an image frame
public void refresh(TuioTime bundleTime) {
}

public void addTuioObject(TuioObject tobj) {
}

// called when an object is removed from the scene
public void removeTuioObject(TuioObject tobj) {
}

// called when an object is moved
public void updateTuioObject (TuioObject tobj) {
}

// called when a cursor is added to the scene


class RoundedSquare {
  int width=200;
  int diameter=100;
  int length=100, fadeout=0;
  int x=100, y=100, centerX, centerY;
  int fill=0xff6699FF;
  String name;
  boolean isFadeIn=true;
  PImage img;
  PFont font;
   PFont fontA;
  float alphaFill=0;

  RoundedSquare() {
    drawRoundedSquare();
  }

  RoundedSquare(int width, int length, int diameter, int x, int y, int fill, PImage im, String name) {
    this.width=width;
    this.font=font;
    this.length=length;
    this.diameter=diameter;
    this.x=x;
    this.y=y;
    this.fill=fill;
    this.img=im;
    this.name=name;
    centerX=x+(width/2);
    centerY=y+(length/2);
    font = createFont("SansSerif", 6);
    //fontA = loadFont("Ziggurat-HTF-Black-32.vlw");
     

    //  drawRoundedSquare();
  }

  public void draw() {
    drawRoundedSquare();
  }
  
  public boolean contains(float xPoint, float yPoint){
   
   return((xPoint < this.x+this.length) && (xPoint >this.x) && (yPoint >this.y) && (yPoint< this.y + this.width));
     
    
  }

  public void drawRoundedSquare() {
    centerX=x+(width/2);
    centerY=y+(length/2);
    stroke(fill,255);
    fill(fill,255);
    rect( x, y+diameter/2, length, width-diameter);
    rect( x+diameter/2, y, length-diameter, width);
    ellipse(x+diameter/2, y+diameter/2, diameter, diameter);
    ellipse(x-diameter/2+length, y+diameter/2, diameter, diameter);
    ellipse(x+diameter/2, y-diameter/2+width, diameter, diameter);
    ellipse(x-diameter/2+length, y-diameter/2+width, diameter, diameter);
  //  stroke(#B4AF91,255);
   // fill(#B4AF91,255);
    rect( x+10, y+diameter/2+10, length-20, width-diameter-20);
    rect( x+diameter/2+10, y+10, length-diameter-20, width-20);
    ellipse(x+diameter/2+10, y+diameter/2+10, diameter, diameter);
    ellipse(x-diameter/2+length-10, y+diameter/2+10, diameter, diameter);
    ellipse(x+diameter/2+10, y-diameter/2+width-10, diameter, diameter);
    ellipse(x-diameter/2+length-10, y-diameter/2-10+width, diameter, diameter);
    textFont(font, 10);
    fill(0xffE1E6FA,255);
    textAlign(CENTER, CENTER);
    text(name, x, y+width/2-15, 100,30);
    pushMatrix();
    
  /*  image(img, x+diameter/2, y+diameter/2);
      tint(255,alphaFill);*/
    
    popMatrix();
  }
}

class blobsLinked {
  TuioCursor blobOne;
  TuioCursor blobTwo;
  float alphaFromCos;
  float alphaFromSin;
  ;
  blobsLinked(TuioCursor blobOne, TuioCursor blobTwo) {
    this.blobOne=blobOne;
    this.blobTwo=blobTwo;
  }
  public float getRotation() {
    PVector p1 = new PVector(-blobOne.getX(), -blobOne.getY());
    PVector p2 = new PVector(blobTwo.getX(), blobTwo.getY());
    PVector p3 = PVector.add(p1, p2);
    if (p3.y <0) {
      return -PVector.angleBetween(new PVector(3, 0), p3);
    } 
    else {
      return PVector.angleBetween(new PVector(3, 0), p3);
    }
  }
}

class fader {
  int posX;
  int posY;
  String name;
  int lengthFader=100;
  fader(){}
  
   fader(int posX, int posY,String name){
     this.posX=posX;
     this.posY=posY;
     this.name=name;
   }
   
   public void draw(){
     
     line (posX, posY,posX, posY-lengthFader);
     fill(204, 102, 0);
     rect (posX-15,posY-lengthFader/2-5,30,10);
     fill(0xff000000);
     text (name,posX-4,posY+15);
   }
  
}
class hp {
  String type;
  boolean rotationActivated=false;
  boolean openingScale=false;
  boolean isSelected = false;
  int[] position = new int [3]; //x,y and Z. Z in { 0=low,1=middle, 2=high}
  int fillColor=0xff6699FF;
  int strokeColor = 0xff000000;
  int xLength=25;
  int coordCorners [][]=new int[10][2];
  int yLength=50;
  int arrowLength=25;
  int id;
  float pi=3.1415927f;
  float angleCorners=pi/3;
  float opening=pi/6;
  float rotation=pi/7;
  hp() {
  }

  hp(int x, int y, int z, String type, int id) {
    position[0]=x;
    position[1]=y;
    position[2]=z;
    this.type=type;
    this.id=id;
    calculateDraw();
  }
  
public boolean isOnHp(int x, int y){
     return(position[0]+xLength>x&&position[0]-xLength<x&&position[1]+yLength>y&&position[1]-yLength<y);
} 

public void calculateDraw(){
  //coord rect
  coordCorners[0][0]=(int)(position[0]+20*cos(rotation+angleCorners));
  coordCorners[1][0]=(int)(position[0]+20*cos(rotation+pi-angleCorners));
  coordCorners[2][0]=(int)(position[0]+20*cos(rotation+pi+angleCorners));
  coordCorners[3][0]=(int)(position[0]+20*cos(rotation-angleCorners));
  coordCorners[0][1]=(int)(position[1]+20*sin(rotation+angleCorners));
  coordCorners[1][1]=(int)(position[1]+20*sin(rotation+pi-angleCorners));
  coordCorners[2][1]=(int)(position[1]+20*sin(rotation+pi+angleCorners));
  coordCorners[3][1]=(int)(position[1]+20*sin(rotation-angleCorners));
  //Coord arrows up
  coordCorners[4][0]=(int)(position[0]+25*cos(rotation-opening));
  coordCorners[5][0]=(int)(position[0]+22*cos(rotation-0.1f-opening));
  coordCorners[6][0]=(int)(position[0]+22*cos(rotation+0.1f-opening));
  coordCorners[4][1]=(int)(position[1]+25*sin(rotation-opening));
  coordCorners[5][1]=(int)(position[1]+22*sin(rotation-0.1f-opening));
  coordCorners[6][1]=(int)(position[1]+22*sin(rotation+0.1f-opening));
  //coord arrow down
  coordCorners[7][0]=(int)(position[0]+25*cos(rotation+opening));
  coordCorners[8][0]=(int)(position[0]+22*cos(rotation-0.1f+opening));
  coordCorners[9][0]=(int)(position[0]+22*cos(rotation+0.1f+opening));
  coordCorners[7][1]=(int)(position[1]+25*sin(rotation+opening));
  coordCorners[8][1]=(int)(position[1]+22*sin(rotation-0.1f+opening));
  coordCorners[9][1]=(int)(position[1]+22*sin(rotation+0.1f+opening));
  
  
}
public void setRotation(float angle)
{
  rotation=angle;
  calculateDraw();
}
public void setOpening(float angle)
{
  opening=angle;
  calculateDraw();
}

  public void draw() {
    
    calculateDraw();
     stroke(0xffE1E6FA);
    if(isSelected){
      noFill();
      strokeWeight(5);
      stroke(0xffABC8E2);
      ellipse(position[0],position[1], 100,100);
      stroke(0xffE1E6FA);
      strokeWeight(1);
    }
    for (int i =0;i<3;i++){
      line(coordCorners[i][0],coordCorners[i][1],coordCorners[i+1][0],coordCorners[i+1][1]);
    }
    line(coordCorners[0][0],coordCorners[0][1],coordCorners[3][0],coordCorners[3][1]);
    
    line(position [0],position[1],coordCorners[4][0],coordCorners[4][1]);
    
    line(coordCorners[4][0],coordCorners[4][1],coordCorners[5][0],coordCorners[5][1]);
    line(coordCorners[4][0],coordCorners[4][1],coordCorners[6][0],coordCorners[6][1]);
    
   
    
    line(position [0],position[1],coordCorners[7][0],coordCorners[7][1]);
    
    line(coordCorners[7][0],coordCorners[7][1],coordCorners[8][0],coordCorners[8][1]);
    line(coordCorners[7][0],coordCorners[7][1],coordCorners[9][0],coordCorners[9][1]);
    fill(0xffE1E6FA);
    text(id,position[0]+20,position[1]);
    //setRotation(rotation+0.05);
    
    //imageMode(CENTER);
 /*  background (#C0C0C0);
    stroke(strokeColor);
    fill(fillColor);
    strokeWeight(2);
   translate(position[0], position[1]);
   
    rect(0, 0, xLength, yLength);
    line (xLength,yLength/2,xLength+arrowLength,yLength/2);
    fill(strokeColor);
    triangle(xLength+arrowLength,yLength/2-5,xLength+10+arrowLength,yLength/2,xLength+arrowLength,yLength/2+5);
    //line(position[0]+(xLength)*cos(rotation)-(yLength/2)*sin(rotation), position[1]+(xLength*sin(rotation))+(yLength/2)*cos(rotation), position[0]+xLength-3+arrowLength, position[1]+yLength/2+(arrowLength));
   rotation = rotation+=0.05;
    //  triangle(position[0]+xLength-3+arrowLength*cos(rotation),position[1]+yLength/2+arrowLength*sin(rotation));*/
  }
}

class tuioThread extends Thread {
  
boolean running = false;
private int wait; 

  tuioThread(){
  
  running = false;  
  
  }

public void start () {
    // Set running equal to true
    running = true;
    // Do whatever start does in Thread, don't forget this!
    super.start();
  }
  // We must implement run, this gets triggered by start()
public void run () {
    
    while (true) {
     
      
  try{

 //   println("TUIO Bridge Launched");
    Process p = Runtime.getRuntime().exec("C:\\Users\\Administrator\\Downloads\\Touch2Tuio_0.2\\Touch2Tuio\\Release\\Touch2Tuio.exe MIAMConfig");
    p.waitFor();
    }
   catch(Exception e){
             print(e);
           } 
      
      
      
    // Ok, let's wait for however long we should wait
   try {
      sleep((long)(wait));
      } catch (Exception e) {
      }
    }

  }

}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "MIAMConfig0_1" });
  }
}
