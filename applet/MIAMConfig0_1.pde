import oscP5.*;
import netP5.*;

import TUIO.*;
TuioProcessing tuioClient;
import fullscreen.*; 
import javax.media.opengl.*;
import processing.opengl.*;
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
void setup() {
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


void draw() {
    smooth();
   background(#183152);

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

void createTableMix() {
  for (int i = 0; i<48;i++) {
    tableMix[i]=new fader (100+i*50, height-50, ""+(i+1));
  }
}

void moveTableMix(int jump) {
  for (int i = 0; i<48;i++) {
    tableMix[i].posX= tableMix[i].posX+jump;
  }
}

void drawMenu() {
  if (hpSelected[0]!=null) {
    fill(#318CE7);
    rect(930, 50, 100, 50);
    rect(930, 120, 100, 50);
    rect(930, 190, 100, 50);
    fill(#000000);
    text("Test", 950, 70);
    text("Noise", 950, 140);
    text("La", 950, 210);
  }
}


void loadXML() {
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

void loadHpInStock(){
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
    hpInStock.add(new RoundedSquare (40,100, 20, width/2 +k*110, 20+j*50,#375D81, null, marque[i].getContent()+" "+modele[i].getContent()));
    k++;
  }

}

void exportToXML() {
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

void keyPressed() {
  if (key=='s') {
    exportToXML();
  }
  if (key=='l') {
    loadXML();
  }
}

void mousePressed() {
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

void mouseDragged() {
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

void mouseReleased() {

}

// called when a cursor is moved
void updateTuioCursor (TuioCursor tcur) {
}

// called after each message bundle
// representing the end of an image frame
void refresh(TuioTime bundleTime) {
}

void addTuioObject(TuioObject tobj) {
}

// called when an object is removed from the scene
void removeTuioObject(TuioObject tobj) {
}

// called when an object is moved
void updateTuioObject (TuioObject tobj) {
}

// called when a cursor is added to the scene

