import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.pdf.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MIAMConfig0_2 extends PApplet {



//import javax.media.opengl.*;
//import processing.opengl.*;
fader f;
boolean rotationActivated=false, moveAllRight=false, ExportPDF=false;
//String linkPlanSalle="planIsib.png"; 
String linkPlanSalle1="PlanSalleConfigBlanc.png";
int pisteSelected=0;
int curseurSelected=0;
int countFrame=0;
boolean moveTableMix=false;
slider rotationSlider;
int onglet=1;
int curseurPositionX, curseurPositionY, outputSelected=0;
ArrayList <hp> listeHpSel;
ArrayList<hp> listeHpSelCur;
RoundedSquare Faders[]=new RoundedSquare[64];
RoundedSquare testButton[]=new RoundedSquare[8];
RoundedSquare buttonGeneral [] = new RoundedSquare [3];
RoundedSquare fiveDotOne [] = new RoundedSquare [32];
RoundedSquare outputs[] = new RoundedSquare[64];
RoundedSquare buttonTab2 []= new RoundedSquare[2];
RoundedSquare buttonTab3;

int lastIdHpOnStage=1, offsetX, offsetY, positionInitX, positionInitY;
hp hpTest;

ArrayList <hp> hpOnStage = new ArrayList();
hp hpSelected []  =new hp [10]; 
fader tableMix[] = new fader[48];
ArrayList hpInStock = new ArrayList();
pdfLatex latex;

boolean isOnSlider=false;

int countBlob=0;
int heightTableMix = 200;
int widthPiseAudio=600;
int heightPiseAudio=height-heightTableMix;
int oldPositionTableMix=100;
PImage planSalle;
String nomSalle="ISIB";
PFont fontA;
PFont font;
RoundedSquareDHp HPtest;
RoundedSquare hpInData ;

PGraphicsPDF pdf;
float widthCoeff = (float) (displayWidth-100) /1920.0f;
float heightCoeff = (float) (displayHeight-100) /1080.0f;



public void setup() { 

 // oscP5 = new OscP5(this, 1112);
  size(displayWidth-100, displayHeight-100);

  widthCoeff = (float) (displayWidth-100) /1920.0f;
  heightCoeff = (float) (displayHeight-100) /1080.0f;
  //size(1366, 768);
  //size(1280, 800);

  font = createFont("BRLNSDB.TTF", 48);

  planSalle=loadImage(linkPlanSalle1);
  loadHpInStock();
  fontA = createFont("BAUHS93.TTF", 40);
  prepareMenu();
  tuioThread tt = new tuioThread();
  listeHpSel=new ArrayList();
  listeHpSelCur=new ArrayList();
 // tt.start();
  //size(1920, 1080,OPENGL);
  //fs = new FullScreen(this);
  //fs.enter();
  pdf = (PGraphicsPDF) createGraphics(width/2, height, PDF, "data/planSalle123.pdf");
  //println( hpInStock.get(1).getClass().getName());
  // createTableMix();
  frameRate(20);
}


public void draw() {
  //long start = System.nanoTime();    

  if (ExportPDF) {
    beginRecord(pdf);
  }
  //smooth();

  background(0xff183152);
  buttonGeneral[0].draw();
  buttonGeneral[1].draw();
  buttonGeneral[2].draw();
  // background(#001336);
  // background (#0b162f);




  image(planSalle, 0, 0, width/2, height);
  textFont(font, 16);
  for (int i = 0 ;i<hpOnStage.size();i++) {
    hp rTemp= (hp) (hpOnStage.get(i));
    rTemp.draw();
  }
  // print((System.nanoTime() - start)/1000000);
  drawOnglets();
  // print("  " + (System.nanoTime() - start)/1000000);
  if (onglet==1) {
    for (int i =0 ; i<hpInStock.size();i++)
    {
      if (hpInStock.get(i).getClass().getName()=="MIAMConfig0_2$RoundedSquare") {
        RoundedSquare rTemp= (RoundedSquare) (hpInStock.get(i));
        //rTemp.isDisable=true;
        rTemp.draw();
      }
      else {
        RoundedSquareDHp rTemp= (RoundedSquareDHp) (hpInStock.get(i));
        //rTemp.isDisable=true;
        rTemp.draw();
      }
    }
    drawMenu();
    fill(0xffE1E6FA);

    textFont(fontA, 20);
    textAlign(LEFT);
    text("Haut-parleurs disponibles", width/2+20, 40);
  }
  if (onglet==2) {
    textAlign(LEFT);
    textFont(fontA, 20);
    drawFiveDotOne();
    textAlign(LEFT);
    textFont(fontA, 20);
    drawOutputs();
    for (int i=0;i<buttonTab2.length;i++)
      buttonTab2[i].draw();
  }
  if (onglet==3)
  {
    textAlign(LEFT);
    textFont(fontA, 20);
    drawFaders();
    buttonTab3.draw();
  }
  //  println("  " + (System.nanoTime() - start)/1000000);
  if (ExportPDF) {
    endRecord();
    ExportPDF=false;
  }
  /* try{
   Thread.currentThread().sleep(50);
   }
   catch (Exception e){}*/
}

public void drawFaders() {
  text("Curseurs disponibles", width/2+20, 50);
  for (int i=0;i<Faders.length;i++) {
    Faders[i].draw();
  }
}

public void drawOutputs() {
  text("Sorties carte son", width/2+20, outputs[0].y-20);
  for (int i=0;i< outputs.length;i++) {
    outputs[i].draw();
  }
}
public void drawFiveDotOne() {
  text("Pistes audios", width/2+20, 50);
  for (int i =0;i<fiveDotOne.length;i++)
    fiveDotOne[i].draw();
}

public void  drawOnglets() {
  stroke(0xffABC8E2);
  fill(0xffE1E6FA);
  strokeWeight(1);
  line(width/2+10, height-70, width/2+10, 20);
  line(width/2+10, 20, width-10, 20);
  line(width-10, 20, width-10, height-70);
  text("Configuration salle", width*7/12, height-40);
  text("Assignation des pistes et sorties ", width*9/12, height-40);
  text("Assignation curseurs et HP", width*11/12, height-40);
  if (onglet==1) { 
    line(width/2+10, height-70, 3*width/6+10, height-70);
    line(4*width/6, height-70, width-10, height-70);
    line(3*width/6+10, height-70, 3*width/6+30, height-10);
    line(4*width/6, height-70, 4*width/6-20, height-10);
    line(3*width/6+30, height-10, 4*width/6-20, height-10);
  }
  if (onglet==2) {
    line(width/2+10, height-70, 4*width/6+10, height-70);
    line(5*width/6, height-70, width-10, height-70);
    line(4*width/6+10, height-70, 4*width/6+30, height-10);
    line(5*width/6, height-70, 5*width/6-20, height-10);
    line(4*width/6+30, height-10, 5*width/6-20, height-10);
  }
  if (onglet==3) {
    line(width/2+10, height-70, 5*width/6+10, height-70);
    //line(6*width/6, height-70, width-10, height-70);
    line(5*width/6+10, height-70, 5*width/6+30, height-10);
    line(6*width/6-10, height-70, 6*width/6-30, height-10);
    line(5*width/6+30, height-10, 6*width/6-30, height-10);
  }
  //line(width-10,height-20,width/2-10,height-20);
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
    fill(0xffE1E6FA);
    textFont(fontA, 20);
    textAlign(LEFT);
    text(hpSelected[0].type, width/2+20, height/2-30);
    //text("Image", width/2, height/2+150);
    text("Orientation", width/2+150, height/2+170);
    text("Hauteur", width/2+300, height/2-30);
    image(hpSelected[0].imageHp, width/2+20, height/2);
    if (hpSelected[0].hasSymetric) {
      testButton[2].name="Sym\u00e9trique";
      testButton[2].selected=true;
    }
    else {
      testButton[2].name="Sym\u00e9trique";
      testButton[2].selected=false;
    }
    switch (hpSelected[0].xRotation) {
      case(0):
      testButton[6].selected=true;
      testButton[0].selected=false;
      testButton[1].selected=false;
      break;
      case(1):
      testButton[6].selected=false;
      testButton[0].selected=true;
      testButton[1].selected=false;
      break;
      case(2):
      testButton[6].selected=false;
      testButton[0].selected=false;
      testButton[1].selected=true;
      break;
    }
    switch (hpSelected[0].position[2]) {
      case(0):
      testButton[5].selected=true;
      testButton[4].selected=false;
      testButton[3].selected=false;
      break;
      case(1):
      testButton[5].selected=false;
      testButton[4].selected=true;
      testButton[3].selected=false;
      break;
      case(2):
      testButton[5].selected=false;
      testButton[4].selected=false;
      testButton[3].selected=true;
      break;
    }
    for (int i=0;i<testButton.length;i++)
      testButton[i].draw();
    rotationSlider.draw();
  }
}

public void prepareMenu() {
  rotationSlider = new slider (0, 360, 1, width/2+150, height/2+200, "rotation", 200);
  testButton[0] = new RoundedSquare (40, 100, 20, width/2+450, height/2 +50, 0xff375D81, "Douche", 0, null);
  testButton[1] = new RoundedSquare (40, 100, 20, width/2+450, height/2 +100, 0xff375D81, "Fontaine", 0, null);
  testButton[2] = new RoundedSquare (40, 100, 20, width/2+150, height/2, 0xff375D81, "Sym\u00e9trique", 0, null);
  testButton[3] = new RoundedSquare (40, 100, 20, width/2+300, height/2 +0, 0xff375D81, "Plafond", 0, null);
  testButton[3].shapeColor=0xffa51c1c;
  testButton[3].noChangeTextColor=true;
  testButton[4] = new RoundedSquare (40, 100, 20, width/2+300, height/2 +50, 0xff375D81, "Balcon", 0, null);
  testButton[4].shapeColor=0xffbc6337;
  testButton[4].noChangeTextColor=true;
  testButton[5] = new RoundedSquare (40, 100, 20, width/2+300, height/2 +100, 0xff375D81, "Sol", 0, null);
  testButton[5].shapeColor=0xff61bc4f;
  testButton[5].noChangeTextColor=true;
  testButton[6] = new RoundedSquare (40, 100, 20, width/2+450, height/2 +0, 0xff375D81, "Frontal", 0, null);
  testButton[7] = new RoundedSquare (40, 100, 20, width/2+20, height/2+150 +0, 0xff375D81, "Supprimer HP", 0, null);
  buttonGeneral[0] = new RoundedSquare (40, 100, 20, width-120, height-150, 0xff375D81, "Charger", 0, null);
  buttonGeneral[1] = new RoundedSquare (40, 100, 20, width-230, height-150, 0xff375D81, "Sauvegarder", 0, null);
  buttonGeneral[2] = new RoundedSquare (40, 100, 20, width-340, height-150 +0, 0xff375D81, "Quitter", 0, null);

  buttonTab2[0]=new RoundedSquare (40, 100, 20, width/2+20, height/2-(int)(250*heightCoeff), 0xff375D81, "Supp assign", 0, null);
  buttonTab2[1]=new RoundedSquare (40, 100, 20, width/2+20, height-(int)(300*heightCoeff) +0, 0xff375D81, "Supp assign", 0, null);
  buttonTab3=new RoundedSquare (40, 100, 20, width/2+20, height-(int)(300*heightCoeff) +0, 0xff375D81, "Supp assign", 0, null);
  int j=0;
  int k=0; 
  for (int i =0;i<fiveDotOne.length;i++) {
    if (width/2 +k*(int)(widthCoeff*60)>width-(int)(widthCoeff*80))
    {
      j++;
      k=0;
    }
    fiveDotOne[i] = new RoundedSquare ((int)(50*widthCoeff), (int)(50*widthCoeff), 10, width/2+20+k*(int)(widthCoeff*60), 80+j*(int)(widthCoeff*60), 0xff375D81, ""+(i+1), 1, null);
    k++;
  }
  j=0;
  k=0;
  for (int i =0;i<outputs.length;i++) {

    //   println(Integer.parseInt(numero[i].getContent()));
    if (width/2 +k*(int)(widthCoeff*60)>width-(int)(widthCoeff*80))
    {
      j++;
      k=0;
    }
    outputs[i] = new RoundedSquare ((int)(widthCoeff*50), (int)(widthCoeff*50), 10, width/2+20 +k*(int)(widthCoeff*60), height/2-80+j*(int)(widthCoeff*60), 0xff375D81, ""+(i+1), 1, null);
    k++;
  }
  k=0;
  j=0;
  for (int i =0;i<Faders.length;i++) {

    //   println(Integer.parseInt(numero[i].getContent()));
    if (k%8==0)
    {
      j++;
      k=0;
    }
    Faders[i] = new RoundedSquare ((int)(widthCoeff*50), (int)(widthCoeff*50), 10, 3*width/5 +k*(int)(widthCoeff*60), height/4-80+j*(int)(widthCoeff*60), 0xff375D81, ""+(i+1), 1, null);
    k++;
  }
}
public void loadXML() {

  hpOnStage.clear();
  loadHpInStock();

  hpSelected[0]=null;
  XML config = null;
  try {
    config = loadXML("config_ISIB.xml");
  }
  catch(Exception e) {
  }
  XML[] x = config.getChildren("hp/x");
  XML[] y = config.getChildren("hp/y");
  XML[] z = config.getChildren("hp/z");
  XML[] idElements = config.getChildren("hp/idHp");
  XML[] placementElements = config.getChildren("hp/placement");
  XML[] xRotation = config.getChildren("hp/xRotation");
  XML[] hasSymetric = config.getChildren("hp/hasSymetric");
  XML[] rotationElements = config.getChildren("hp/rotation");
  XML[] curseurs = config.getChildren("hp/curseur");
  XML[] sorties = config.getChildren("hp/sortieMadi");
  XML[] pistes = config.getChildren("hp/pisteAudio");
  for (int i =0 ;i< idElements.length;i++)
  {
    lastIdHpOnStage=max(lastIdHpOnStage, Integer.parseInt(placementElements[i].getContent()));
    hp hpTemp = new hp(findHpWithId(Integer.parseInt(idElements[i].getContent())));
    //println("id " + Integer.parseInt(idElements[i].getContent()));
    hpTemp.placement=Integer.parseInt(placementElements[i].getContent());
    hpTemp.position[0]=(int)(Float.parseFloat(x[i].getContent())*displayWidth);
    hpTemp.position[1]=(int)(Float  .parseFloat(y[i].getContent())*displayHeight);
    hpTemp.position[2]=Integer.parseInt(z[i].getContent());
    hpTemp.rotation= (Float.parseFloat(rotationElements[i].getContent()))* 3.1415f/180;
    hpTemp.xRotation= (Integer.parseInt(xRotation[i].getContent()));
    hpTemp.curseur=(Integer.parseInt(curseurs[i].getContent()));
    hpTemp.sortie=(Integer.parseInt(sorties[i].getContent()));
    if (hpTemp.sortie!=0) {
      outputs[hpTemp.sortie-1].isDisable=true;
    }

    if (pistes[i].getContent().equals("null")) {
      hpTemp.piste=null;
    } 
    else {
      hpTemp.piste=pistes[i].getContent();
    }
    String hasSym= hasSymetric[i].getContent();
    int k= getPositionOfHpInStock(hpTemp.idHp);
    if (hpInStock.get(k).getClass().getName()=="MIAMConfig0_2$RoundedSquare") {
      RoundedSquare rTemp = (RoundedSquare) hpInStock.get(k);
      rTemp.isDisable=true;
    }
    else {
      RoundedSquareDHp rTemp =(RoundedSquareDHp) hpInStock.get(k);
      if (hpTemp.numero%2!=0)
      {
        rTemp.isDisable1=true;
      }
      else
      {
        rTemp.isDisable2=true;
      }
    }
    if (hasSym.equals("true")) {
      hpTemp.hasSymetric=true;
    }
    else {
      hpTemp.hasSymetric=false;
    }
    //    println(rTemp.hpLink.type+"   "+  rTemp.numberDispo);
    hpTemp.calculateDraw();
    hpOnStage.add(hpTemp);
  }
  lastIdHpOnStage=lastIdHpOnStage+1+lastIdHpOnStage%2;
}

public void loadHpInStock() {
  hpInStock.clear();
  boolean doubleHp=false;
  XML config = null;
  try {
    config = loadXML ("hp_database.xml");
  }
  catch (Exception e) {
  }
  XML[] id = config.getChildren("hp");
  XML[] marque = config.getChildren("hp/marque");
  XML[] nickname = config.getChildren("hp/nickname");
  XML[] modele = config.getChildren("hp/modele");
  XML[] numero = config.getChildren("hp/numero");
  XML[] images = config.getChildren("hp/image");
  XML[] directivite = config.getChildren("hp/directivite");
  XML[] frequence_rep_min = config.getChildren("hp/frequence_rep_min");
  XML[] frequence_rep_max = config.getChildren("hp/frequence_rep_max");
  XML[] impedance_nominale =  config.getChildren("hp/impedance_nominale");
  XML[] puissance_rms =  config.getChildren("hp/puissance_rms");
  XML[] puissance_crete =  config.getChildren("hp/puissance_crete");
  XML[] parite = config.getChildren("hp/parite");
  XML[] nombre = config.getChildren("hp/nombre_disponible");
  int j=0;
  int k=0;
  // print("marque.length");
  for (int i=0 ;i<marque.length;i++)
  {
    //   println(Integer.parseInt(numero[i].getContent()));
    if (width/2 +k*(int)(110*widthCoeff)>width-(int)(110*widthCoeff))
    {
      j++;
      k=0;
    }
    doubleHp=(Integer.parseInt(parite[i].getContent())==1);
    if (!doubleHp) {
      hpInStock.add(new RoundedSquare ((int)(50.0f*heightCoeff), (int)(100.0f*widthCoeff), 10, width/2 +20+k*(int)(110*widthCoeff), 60+j*(int)(60*heightCoeff), 0xff375D81, marque[i].getContent()+" "+modele[i].getContent()+" "+numero[i].getContent(), Integer.parseInt(nombre[i].getContent()), 
      new hp(20, 20, 0, marque[i].getContent() +" "+ modele[i].getContent(), 0, "images/"+images[i].getContent(), Integer.parseInt(frequence_rep_min[i].getContent()), 
      Integer.parseInt(frequence_rep_max[i].getContent()), Float.parseFloat(impedance_nominale[i].getContent()), Integer.parseInt( puissance_rms[i].getContent()), 
      Integer.parseInt( puissance_crete[i].getContent()), Integer.parseInt(id[i].getString("id")), Integer.parseInt(parite[i].getContent()), Float.parseFloat(directivite[i].getContent())*3.1415f/360, Integer.parseInt(numero[i].getContent()),nickname[i].getContent()) ) );
      k++;
    }
    else {
      if (Integer.parseInt(numero[i].getContent())%2!=0) {
        hpInStock.add(new RoundedSquareDHp ((int)(50.0f*heightCoeff), (int)(100.0f*widthCoeff), 10, width/2+20 +k*(int)(110*widthCoeff), 60+j*(int)(60*heightCoeff), 0xff375D81, marque[i].getContent() +" "+ modele[i].getContent()+" "+numero[i].getContent(), marque[i].getContent() +" "+ modele[i].getContent()+" "+numero[i+1].getContent(), 
        new hp(20, 20, 0, marque[i].getContent() +" "+ modele[i].getContent(), 0, "images/"+images[i].getContent(), Integer.parseInt(frequence_rep_min[i].getContent()), 
        Integer.parseInt(frequence_rep_max[i].getContent()), Float.parseFloat(impedance_nominale[i].getContent()), Integer.parseInt( puissance_rms[i].getContent()), 
        Integer.parseInt( puissance_crete[i].getContent()), Integer.parseInt(id[i].getString("id")), Integer.parseInt(parite[i].getContent()), Float.parseFloat(directivite[i].getContent())*3.1415f/360, Integer.parseInt(numero[i].getContent()),nickname[i].getContent()), 
        new hp(20, 20, 0, 
        marque[i].getContent() +" "+ modele[i].getContent(), 
        0, "images/"+images[i].getContent(), Integer.parseInt(frequence_rep_min[i].getContent()), 
        Integer.parseInt(frequence_rep_max[i].getContent()), Float.parseFloat(impedance_nominale[i].getContent()), Integer.parseInt( puissance_rms[i].getContent()), 
        Integer.parseInt( puissance_crete[i].getContent()), Integer.parseInt(id[i+1].getString("id")), Integer.parseInt(parite[i].getContent()), Float.parseFloat(directivite[i].getContent())*3.1415f/360, Integer.parseInt(numero[i+1].getContent()),nickname[i+1].getContent()) ) );
        k++;
      }
    }
  }
}

public void exportToTex() {

  String data ="\\documentclass[12pt,a4paper]{report}\n\\usepackage[utf8]{inputenc}\n\\usepackage[french]{babel}\n\\usepackage[T1]{fontenc}\n\\usepackage{amsmath}\n\\usepackage{amsfonts}\n\\usepackage{amssymb}\n\\usepackage{makeidx}\n\\usepackage{graphicx}\n\\usepackage[left=2cm,right=2cm,top=2cm,bottom=2cm]{geometry}\n\\author{Musiques et Recherches}\n\\title{Configuration salle : Studio 1 \\\\ Adresse :  Place Flagey}";
  data+="\n \\begin{document}\n\\maketitle\n\\chapter*{Vue d\'ensemble}";
  data+="\n \\begin{center}\n\\includegraphics[height=20cm]{data/planSalle123.pdf}\n\\end{center}";
  data+="\n \\chapter*{Association pistes audios - Curseur - Sortie}\n \\begin{center}\n \\begin{tabular}{|c|c|c|}\n \\hline \n Piste Audio & Curseur & Sortie MADI \\\\ \n\\hline \n";
  for (int i=0;i<hpOnStage.size();i++) {
    if ((i+1)%38==0) {
      data+="\n \\end{tabular} \\\\ \n \\begin{tabular}{|c|c|c|}\n \\hline \n Piste Audio & Curseur & Sortie MADI \\\\ \n\\hline \n ";
    }
    hp hpTemp = (hp) hpOnStage.get(i);
    data+=hpTemp.piste+" & "+hpTemp.curseur +" & "+hpTemp.sortie+" \\\\ \n \\hline \n" ;
  }
  data+="\n \\end{tabular}  \n";
  data+="\n \\end{center}";

  data+="\n \\chapter*{Association MADI - Ampli - haut-parleurs}\n \\begin{center}\n \\begin{tabular}{|c|c|c|}\n \\hline \n sortie MADI & Ampli & Haut-parleur \\\\ \n\\hline \n";
  for (int i=0;i<hpOnStage.size();i++) {
    if ((i+1)%38==0) {
      data+="\n \\end{tabular} \\\\ \n \\begin{tabular}{|c|c|c|}\n \\hline \n sortie MADI & Ampli & Haut-parleur \\\\ \n\\hline \n ";
    }
    hp hpTemp = (hp) hpOnStage.get(i);
    data+=hpTemp.sortie+" & TODO & "+hpTemp.type+" "+hpTemp.numero +" \\\\ \n \\hline \n" ;
  }
  data+="\n \\end{tabular}  \n";
  data+="\n \\end{center}";

  data+="\n \\end{document}  \n";

  String[]lineExport = new String[1];
  lineExport[0]=data;
  saveStrings("config_"+nomSalle+".tex", lineExport);
  /*  latex =new pdfLatex("config_"+nomSalle+".tex");
   latex.start();*/

  ExportPDF=true;
}


public void exportToXML() {
  String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<?xml-stylesheet href=\"XMLLatex.xsl\" type=\"text/xsl\" ?> \n<config>\n";
  data += "";
  data+="\t<salle>\n";
  data+="\t\t<nom>"+nomSalle+"</nom>\n"; 
  data+="\t\t<plan_salle>"+linkPlanSalle1+"</plan_salle>\n"; 
  data+="\t\t<adresse> 150, rue royale, 1000 Bruxelles </adresse> \n";
  data+="\t</salle>\n";
  for (int i=0;i<hpOnStage.size();i++) {
    hp hpTemp = (hp) hpOnStage.get(i);
    data+="\t<hp>\n";
    data+="\t\t<x>"+(float)(hpTemp.position[0])/displayWidth+"</x>\n";
    data+="\t\t<y>"+(float)(hpTemp.position[1])/displayHeight+"</y>\n";
    data+="\t\t<z>"+hpTemp.position[2]+"</z>\n";
    // println("idHpExport "+hpTemp.idHp);
    data+="\t\t<rotation>"+round(hpTemp.rotation*180/3.1415f)+"</rotation>\n";
    data+="\t\t<idHp>"+hpTemp.idHp+"</idHp>\n";
    data+="\t\t<placement>"+hpTemp.placement+"</placement>\n";
    data+="\t\t<xRotation>"+(hpTemp.xRotation)+"</xRotation>\n";
    data+="\t\t<hasSymetric>"+hpTemp.hasSymetric+"</hasSymetric>\n";
    data+="\t\t<curseur>"+hpTemp.curseur+"</curseur>\n";
    data+="\t\t<pisteAudio>"+hpTemp.piste+"</pisteAudio>\n";
    data+="\t\t<sortieMadi>"+hpTemp.sortie+"</sortieMadi>\n";
    data+="\t</hp>\n";
  }
  data+="</config>";
  String[]lineExport = new String[1];
  lineExport[0]=data;
  saveStrings("config_"+nomSalle+".xml", lineExport);
  exportToTex();
  println("Je lance l'export TEX");
}

public void keyPressed() {
  if (key=='s' || key=='S') {
    exportToXML();
  }
  if (key=='l') {
    loadXML();
  }
}

public void selectHp(hp rHp, boolean wantSym)
{
  if ( hpSelected[0]!=null) {
    hpSelected[0].isSelected=false;
    if (hpSelected[0].hasSymetric) {
      int k= getIdOfSymetric(hpSelected[0]);
      hp hpTemp = (hp)hpOnStage.get(k);
      hpTemp.isSelected=false;
    }
  }
  rHp.isSelected=true;

  hpSelected[0]=rHp;
  if (hpSelected[0].hasSymetric&&wantSym) {
    int k= getIdOfSymetric(hpSelected[0]);
    hp hpTemp = (hp)hpOnStage.get(k);
    hpTemp.isSelected=true;
  }

  rotationSlider.currentPos=(int)(200f/(2*3.1415f) * hpSelected[0].rotation)+rotationSlider.posX;
  if (rotationSlider.currentPos<rotationSlider.posX)
  {
    rotationSlider.currentPos+=100;
  }
}


public boolean checkOnHpButtons(int x, int y) {
  boolean onSomething=false;
  for (int i=0;i<hpInStock.size();i++) //check clics on buttons.
  {

    if (hpInStock.get(i).getClass().getName()=="MIAMConfig0_2$RoundedSquare") {
      RoundedSquare rTemp;
      rTemp = (RoundedSquare) hpInStock.get(i);
      if (rTemp.contains(x, y)&& !rTemp.isDisable)
      {
        rTemp.hpLink.placement=lastIdHpOnStage;
        //rTemp.numberDispo--;
        hp hpOnScreen=new hp(rTemp.hpLink);

        hpOnStage.add(hpOnScreen);
        /*if (hpOnScreen.useToBeTwo && rTemp.numberDispo>0) {
         addSymetric(hpOnScreen);
         rTemp.numberDispo--;
         }*/
        rTemp.isDisable=true;


        selectHp(hpOnScreen, true);
        // hpOnScreen.isSelected=true;
        onSomething=true;

        lastIdHpOnStage=lastIdHpOnStage+2;
      }
    }

    else {
      RoundedSquareDHp rTemp;
      rTemp = (RoundedSquareDHp) hpInStock.get(i);
      if (rTemp.contains(x, y) && (!rTemp.isDisable1 || !rTemp.isDisable2 ))
      {
        if (!rTemp.isDisable1 && !rTemp.isDisable2) {
          rTemp.hpLink1.placement=lastIdHpOnStage;
          rTemp.numberDispo1--;
          hp hpOnScreen=new hp(rTemp.hpLink1);
          hpOnStage.add(hpOnScreen);
          rTemp.isDisable1=true;
          addSymetric(hpOnScreen);
          /*if (hpOnScreen.useToBeTwo && rTemp.numberDispo>0) {
           addSymetric(hpOnScreen);
           rTemp.numberDispo--;
           }*/

          rTemp.isDisable2=true;

          selectHp(hpOnScreen, true);
          // hpOnScreen.isSelected=true;


          lastIdHpOnStage=lastIdHpOnStage+2;
        }
        else {
          if (!rTemp.isDisable1)
          {
            rTemp.hpLink1.placement=lastIdHpOnStage;
            lastIdHpOnStage++;
            hp hpOnScreen=new hp(rTemp.hpLink1);
            hpOnStage.add(hpOnScreen);
            rTemp.isDisable1=true;
          }
          else {
            rTemp.hpLink2.placement=lastIdHpOnStage;
            lastIdHpOnStage++;
            hp hpOnScreen=new hp(rTemp.hpLink2);
            hpOnStage.add(hpOnScreen);
            rTemp.isDisable2=true;
          }
        }
        onSomething=true;
      }
    }
  }
  return onSomething;
}

public boolean checkHpOnStage(int x, int y, boolean wantSymetric, int pisteSelec) {
  boolean onSomething=false;
  for (int i=0;i<hpOnStage.size();i++) //check click on HP added.
  {
    hp rHp = (hp) hpOnStage.get(i);
    if (rHp.isOnHp(x, y))
    {
      if (fiveDotOne[pisteSelec].selected&&rHp.piste==null&&  onglet==2) {
        rHp.piste=Integer.toString(pisteSelec+1);
        listeHpSel.add(rHp);
        rHp.OnFocus=true;
      }

      if (Faders[curseurSelected].selected&&rHp.curseur==0&&  onglet==3) {
        rHp.curseur=curseurSelected+1;
        listeHpSelCur.add(rHp);
        rHp.OnFocus=true;
      }
      println (outputSelected);
      linkHpOutput(rHp, outputSelected);



      selectHp(rHp, wantSymetric);
      onSomething=true;
      positionInitX=x;
      positionInitY=y;
      offsetX = x-rHp.position[0];
      offsetY = y-rHp.position[1];
    }
  }
  return onSomething;
}

public boolean checkOptionButtons(int x, int y) {
  boolean onSomething = false;
  for (int i=0 ;i<testButton.length;i++)
  {
    if (testButton[i].contains(x, y)) {
      onSomething=true;
      switch(i) {
      case 0:
        hpSelected[0].xRotation=1;
        if (hpSelected[0].hasSymetric) {
          int j=getIdOfSymetric(hpSelected[0]);
          hp hTemp=(hp) hpOnStage.get(j);
          hTemp.xRotation=1;
        }
        break;
      case 1:
        hpSelected[0].xRotation=2;
        if (hpSelected[0].hasSymetric) {
          int j=getIdOfSymetric(hpSelected[0]);
          hp hTemp=(hp) hpOnStage.get(j);
          hTemp.xRotation=2;
        }

        break;
      case 2:
        testButton[i].selected=!testButton[i].selected;//Sym
        if (testButton[i].selected) {
          addSymetric(hpSelected[0]);
        }
        else {
          int j=getIdOfSymetric(hpSelected[0]);
          hp hTemp=(hp) hpOnStage.get(j);
          hTemp.hasSymetric=false;
          hTemp.isSelected=false;
          hpSelected[0].hasSymetric=false;//soliste
          //hpOnStage.remove(j);
        }
        break;
      case 3:
        hpSelected[0].position[2]=2;
        if (hpSelected[0].hasSymetric) {
          int j=getIdOfSymetric(hpSelected[0]);
          hp hTemp=(hp) hpOnStage.get(j);
          hTemp.position[2]=2;
        }
        break;
      case 4:
        hpSelected[0].position[2]=1;
        if (hpSelected[0].hasSymetric) {
          int j=getIdOfSymetric(hpSelected[0]);
          hp hTemp=(hp) hpOnStage.get(j);
          hTemp.position[2]=1;
        }
        break;
      case 5:
        hpSelected[0].position[2]=0;
        if (hpSelected[0].hasSymetric) {
          int j=getIdOfSymetric(hpSelected[0]);
          hp hTemp=(hp) hpOnStage.get(j);
          hTemp.position[2]=0;
        }
        break;
      case 6:
        hpSelected[0].xRotation=0;
        if (hpSelected[0].hasSymetric) {
          int j=getIdOfSymetric(hpSelected[0]);
          hp hTemp=(hp) hpOnStage.get(j);
          hTemp.xRotation=0;
        }
        break;
      case 7:
        onSomething=false;

        removeHp(hpSelected[0]);
        hpSelected[0]=null;
        break;
      }
    }
  }
  return onSomething;
}

public void looseFocus() {
  hpSelected[0].isSelected=false;
  if (hpSelected[0].hasSymetric) {
    int k= getIdOfSymetric(hpSelected[0]);
    hp hpTemp = (hp)hpOnStage.get(k);
    hpTemp.isSelected=false;
  }
  hpSelected[0]=null;
}


public boolean checkButtonGeneral (int x, int y) {
  boolean onThat=false;
  for (int i =0; i<buttonGeneral.length;i++) {
    if (buttonGeneral[i].contains((float)x, (float)y)) {
      onThat=true;
      switch(i) {
      case 0:
        loadXML();
        break;

      case 1:
        if (!ExportPDF) {
          ExportPDF=true;
          exportToXML();
        }
        break;


      case 2:
        endRecord();
        exit();
        break;
      }
    }
  }
  return onThat;
}

public void mousePressed() {
  boolean onSomething=false;
  onSomething=  checkButtonGeneral(mouseX, mouseY);

  if (onglet==1) {
    onSomething=  checkOnHpButtons(mouseX, mouseY);
  }

  if ( onglet==1 && !onSomething) {
    onSomething = checkOptionButtons(mouseX, mouseY);
  }

  if (onglet==1 && !onSomething) {
    onSomething = rotationSlider.isOnSlider(mouseX, mouseY);
  }
  if (!onSomething) {
    onSomething= checkOnglets(mouseX, mouseY);
  }
  if (onglet==2) {
    //
    checkPistes(mouseX, mouseY);

    checkOutput(mouseX, mouseY);
    checkButtonsTab2(mouseX, mouseY);
  }
  if (onglet==3) {
    //
    checkCurseurs(mouseX, mouseY);
    checkButtonsTab3(mouseX, mouseY);
  }
  if (!onSomething) {
    onSomething = checkHpOnStage(mouseX, mouseY, (onglet==1), pisteSelected);
  }

  if (hpSelected[0]!=null && !onSomething && mouseX<width/2+20) {
    looseFocus();
  }
}

public void checkCurseurs(int x, int y) {
  for (int i =0;i<Faders.length;i++)
  {
    if (Faders[i].contains(x, y)) {
      Faders[i].selected=!Faders[i].selected;
      if (curseurSelected!=i) {
        Faders[curseurSelected].selected=false;
      }

      curseurSelected = i;
      addSelectedInTheListCurseur(curseurSelected);
      //hpSelected[0].piste=fiveDotOne[i].name;
    }
  }
}

public void checkButtonsTab3(int x, int y) {
  if (buttonTab3.contains((float) x, (float) y)) {
    deleteLinkFaderHP();
  }
}

public void deleteLinkFaderHP() {
  hpSelected[0].curseur=0;
}

public void checkButtonsTab2(int x, int y) {
  for (int i =0; i<buttonTab2.length;i++) {
    if (buttonTab2[i].contains((float)x, (float)y)) {

      switch(i) {
      case 0:
        deleteLinkPisteHp();
        break;

      case 1:
        deleteLinkOutputHp();
        break;
      }
    }
  }
}

public void deleteLinkPisteHp() {
  if (hpSelected[0]!=null) {
    int localPiste= Integer.parseInt(hpSelected[0].piste) -1;
    // fiveDotOne[localPiste].isDisable=false;
    hpSelected[0].piste=null;
  }
}


public void deleteLinkOutputHp() {
  if (hpSelected[0]!=null) {
    int localOutput= hpSelected[0].sortie-1;
    if (localOutput!=-1) {
      outputs[localOutput].isDisable=false;
      hpSelected[0].sortie=0;
    }
  }
}
public void checkPistes(int x, int y) {
  for (int i =0;i<fiveDotOne.length;i++)
  {
    if (fiveDotOne[i].contains(x, y)) {
      fiveDotOne[i].selected=!fiveDotOne[i].selected;
      if (pisteSelected!=i) {
        fiveDotOne[pisteSelected].selected=false;
      }

      pisteSelected = i;
      addSelectedInTheList(pisteSelected);
      //hpSelected[0].piste=fiveDotOne[i].name;
    }
  }
}

public void checkOutput(int x, int y) {
  for (int i =0;i<outputs.length;i++)
  {
    if (outputs[i].contains(x, y) &&!outputs[i].isDisable) {
      outputs[outputSelected].selected=false;
      outputs[i].selected=true;
      linkHpOutput( hpSelected[0], i);
      outputSelected=i;

      // hpSelected[0].sortie=Integer.parseInt(i);
    }
  }
}

public void clearSelectedHpsCur() {
  for (int i=0; i<listeHpSelCur.size();i++) {
    hp temp=(hp)listeHpSelCur.get(i);

    temp.OnFocus=false;
  }
  listeHpSelCur.clear();
}

public void addSelectedInTheListCurseur(int curseur) {
  clearSelectedHpsCur();

  for (int i=0; i<hpOnStage.size()&& Faders[curseur].selected;i++) {
    hp temp=(hp)hpOnStage.get(i);
    if (temp.curseur==curseur+1) {
      temp.OnFocus=true;
      listeHpSelCur.add(temp);
    }
  }
}

public void clearSelectedHps() {
  for (int i=0; i<listeHpSel.size();i++) {
    hp temp=(hp)listeHpSel.get(i);

    temp.OnFocus=false;
  }
  listeHpSel.clear();
}

public void addSelectedInTheList(int piste) {

  clearSelectedHps();
  for (int i=0; i<hpOnStage.size()&& fiveDotOne[piste].selected;i++) {
    hp temp=(hp)hpOnStage.get(i);
    if (temp.piste!=null&&temp.piste.equals(Integer.toString(piste+1))) {
      temp.OnFocus=true;
      listeHpSel.add(temp);
    }
  }
}
public void linkHpOutput (hp hpSel, int output) {
  if (hpSel!=null && outputs[output].selected && onglet==2 && hpSel.sortie==0) {
    hpSel.sortie=output+1;
    outputs[output].selected=false;
    outputs[output].isDisable=true;
  }
}

public void disablePisteAndFaders() {
  Faders[curseurSelected].selected=false;
  fiveDotOne[pisteSelected].selected=false;
  clearSelectedHps();
  clearSelectedHpsCur();
}


public boolean checkOnglets (int x, int y) {
  boolean onOnglet = false;
  if (x> width/2 && x<4*width/6 && y>height-70 && y<height) {
    onOnglet=true;
    onglet=1;
    disablePisteAndFaders();
  }
  if (x>4*width/6 && x<5*width/6 && y>height-70 && y<height) {
    onOnglet=true;
    onglet=2;
    disablePisteAndFaders();
  }
  if (x>5*width/6 && x<width && y>height-70 && y<height) {
    onOnglet=true;
    onglet=3;
    disablePisteAndFaders();
  }
  return onOnglet;
}


public void removeHp(hp hpSel) {


  hpSel.isSelected=false;
  int l= getPositionOfHpInStock(hpSel.idHp);

  //println ("LAEZL"+ l);
  if (hpInStock.get(l).getClass().getName()=="MIAMConfig0_2$RoundedSquare") {
    RoundedSquare rTemp = (RoundedSquare) hpInStock.get(l);
    //rTemp.numberDispo++;
    rTemp.isDisable=false;

    if (hpSel.hasSymetric) {

      rTemp.numberDispo++;
      int j = getIdOfSymetric(hpSel);
      hp hpTemp = (hp)hpOnStage.get(j);
      int m= getPositionOfHpInStock(hpTemp.idHp); //ici ca pu encore am\u00e9lio
      RoundedSquare rTempHp = (RoundedSquare) hpInStock.get(m);
      // rTempHp.numberDispo++;
      rTempHp.isDisable=false;

      hpOnStage.remove(j);
    }

    int k = findHpWithName(hpSel.type, hpSel.numero);
    hpOnStage.remove(k);
  }
  else {
    RoundedSquareDHp rTemp = (RoundedSquareDHp) hpInStock.get(l);
    if (rTemp.hpLink1.numero==hpSel.numero) {
      rTemp.isDisable1=false;


      if (hpSel.hasSymetric) {
        int j = getIdOfSymetric(hpSel);
        rTemp.isDisable2=false;
        hpOnStage.remove(j);
      }
    }
    if (rTemp.hpLink2.numero==hpSel.numero) {
      rTemp.isDisable2=false;


      if (hpSel.hasSymetric) {
        int j = getIdOfSymetric(hpSel);
        rTemp.isDisable1=false;
        hpOnStage.remove(j);
      }
    }

    int k = findHpWithPlacement(hpSel.placement);
    hpOnStage.remove(k);
  }
}

public void addSymetric(hp hpDepart)
{
  int idHp = getIdOfSymetric(hpDepart);
  // println("idHp "+idHp);
  if (idHp!=-1) {            //Est d\u00e9j\u00e0 pr\u00e9sent sur la sc\u00e8ne.
    hpDepart.hasSymetric=true;
    hp hpTemp=(hp) hpOnStage.get(idHp);
    hpTemp.hasSymetric=true;
    hpTemp.isSelected=true;
    hpTemp.position[2]=hpDepart.position[2];
    hpTemp.xRotation=hpDepart.xRotation;
    hpTemp.position[0]=width/2-hpDepart.position[0];
    hpTemp.position[1]=hpDepart.position[1];
    hpTemp.rotation=3.1415f-hpDepart.rotation;
    hpTemp.calculateDraw();
  }
  else {
    hp rHp;
    int idFinal;
    if ( hpDepart.numero % 2!=0) {            //Num\u00e9ros impaires
      int l= getPositionOfHpInStock(hpDepart.type, (hpDepart.numero+1));
      //println ("positionReceive" +l);
      if (l==-1) return;
      if (hpInStock.get(l).getClass().getName()=="MIAMConfig0_2$RoundedSquare") {
        RoundedSquare rTemp = (RoundedSquare) hpInStock.get(l);
        rTemp.numberDispo=0;
        idFinal=rTemp.hpLink.idHp;
        rTemp.isDisable=true;
      }
      else {
        RoundedSquareDHp rTemp = (RoundedSquareDHp) hpInStock.get(l);
        if (!rTemp.isDisable1) {
          rTemp.isDisable1=true;
          // println("rTemp.hpLink1.idHp "+ rTemp.hpLink1.idHp);
          idFinal=rTemp.hpLink1.idHp;
        }
        else {
          rTemp.isDisable2=true;
          //println("rTemp.hpLink2.idHp "+ rTemp.hpLink2.idHp);
          idFinal=rTemp.hpLink2.idHp;
        }
      }
      rHp = new hp(hpDepart);
      rHp.idHp=idFinal;
      rHp.position[0]=width/2-hpDepart.position[0];
      rHp.xRotation=hpDepart.xRotation;
      rHp.placement=hpDepart.placement+1;
      rHp.numero = hpDepart.numero+1;
    }
    else {                                       //num\u00e9ros paires
      int l= getPositionOfHpInStock(hpDepart.type, hpDepart.numero-1);
      if (l==-1) return;
      // println(l);
      if (hpInStock.get(l).getClass().getName()=="MIAMConfig0_2$RoundedSquare") {
        RoundedSquare rTemp = (RoundedSquare) hpInStock.get(l);
        idFinal=rTemp.hpLink.idHp;
        rTemp.isDisable=true;
      }
      else {
        RoundedSquareDHp rTemp = (RoundedSquareDHp) hpInStock.get(l);
        if (!rTemp.isDisable1) {
          rTemp.isDisable1=true;
          //println("rTemp.hpLink1.idHp "+ rTemp.hpLink1.idHp);
          idFinal=rTemp.hpLink1.idHp;
        }
        else {
          rTemp.isDisable2=true;
          // println("rTemp.hpLink2.idHp "+ rTemp.hpLink2.idHp);
          idFinal=rTemp.hpLink2.idHp;
        }
      }
      rHp = new hp(hpDepart);
      rHp.position[0]=width/2-hpDepart.position[0];
      rHp.placement=hpDepart.placement-1;
      rHp.numero = hpDepart.numero-1;
    }
    rHp.rotation=3.1415f-hpDepart.rotation;
    rHp.xRotation=hpDepart.xRotation;
    hpDepart.hasSymetric=true;
    rHp.hasSymetric=true;
    rHp.isSelected=true;
    hpOnStage.add(rHp);
    rHp.calculateDraw();
  }
}

public int getPositionOfHpInStock(int id) {
  for (int i=0;i<hpInStock.size();i++) {
    if (hpInStock.get(i).getClass().getName()=="MIAMConfig0_2$RoundedSquare") {
      RoundedSquare temp=(RoundedSquare) hpInStock.get(i);
      //   println("temp.hpLink.idHp "+temp.hpLink.idHp+" id "+id);
      if (temp.hpLink.idHp==id)
        return i;
    }
    else {
      RoundedSquareDHp temp=(RoundedSquareDHp) hpInStock.get(i);
      if (temp.hpLink1.idHp==id || temp.hpLink2.idHp==id)
        return i;
    }
  }
  return -1;
}

public int getPositionOfHpInStock(String name, int numero) {

  for (int i=0;i<hpInStock.size();i++) {
    if (hpInStock.get(i).getClass().getName()=="MIAMConfig0_2$RoundedSquare") {
      RoundedSquare temp=(RoundedSquare) hpInStock.get(i);
      if (temp.hpLink.numero==numero && temp.hpLink.type.equals(name)) {
        return i;
      }
    }
    else {
      RoundedSquareDHp temp=(RoundedSquareDHp) hpInStock.get(i);
      if ((temp.hpLink1.numero==numero && temp.hpLink1.type.equals(name))||(temp.hpLink2.numero==numero && temp.hpLink2.type.equals(name)) )
        return i;
    }
  }
  return -1;
}


public int getIdOfHpInStock(String name, int numero) {
  int k=-1;
  for (int i=0;i<hpInStock.size();i++) {
    if (hpInStock.get(i).getClass().getName()=="MIAMConfig0_2$RoundedSquare") {
      RoundedSquare temp=(RoundedSquare) hpInStock.get(i);
      if (temp.hpLink.numero==numero && temp.hpLink.type.equals(name)) {
        return temp.hpLink.idHp;
      }
    }
    else {
      RoundedSquareDHp temp=(RoundedSquareDHp) hpInStock.get(i);
      if (temp.hpLink1.numero==numero && temp.hpLink1.type.equals(name)) {
        return temp.hpLink1.idHp;
      }
      if (temp.hpLink2.numero==numero && temp.hpLink2.type.equals(name)) 
        return temp.hpLink2.idHp;
    }
  }
  return -1;
}

public int getIdOfSymetric(hp hpSel)
{
  int k=-1;
  if ( hpSel.numero % 2!=0) {
    k=findHpWithName(hpSel.type, hpSel.numero+1);
  }
  else {  
    k=findHpWithName(hpSel.type, hpSel.numero-1);
  }
  return k;
}

public void setPositionSelectedHp(int x, int y) {
  if (abs(x-positionInitX) > 20 || abs(y-positionInitY)>20) {
    moveAllRight=true;
  }
  if (moveAllRight && hpSelected[0]!=null && !rotationSlider.isOnSlider(x, y)&&!isOnSlider&& x<width/2) {
    hpSelected[0].position[0]=x;
    hpSelected[0].position[1]=y;
    hpSelected[0].calculateDraw();
    if (hpSelected[0].hasSymetric) {
      int k=getIdOfSymetric(hpSelected[0]);
      hp hpTemp=(hp)hpOnStage.get(k);
      hpTemp.position[0]=width/2-hpSelected[0].position[0];
      hpTemp.position[1]=hpSelected[0].position[1];
      hpTemp.calculateDraw();
    }
  }
}


public void setSliderRotation(int x, int y) {
  if (hpSelected[0]!=null && rotationSlider.isOnSlider(x, y)) {
    rotationSlider.currentPos=x-(x%(int)(rotationSlider.sWidth/24f));
    hpSelected[0].setRotation(((2*3.1415f)/rotationSlider.sWidth * (x-rotationSlider.posX))-((2*3.1415f)/rotationSlider.sWidth * (x-rotationSlider.posX)%(3.1415f/12)));

    isOnSlider=true;
    if (hpSelected[0].hasSymetric) {
      int k=getIdOfSymetric(hpSelected[0]);
      hp hpTemp=(hp)hpOnStage.get(k);
      hpTemp.setRotation(3.1415f - hpSelected[0].rotation);
    }
  }
}

public void mouseDragged() {
  if (onglet==1) {
    setPositionSelectedHp(mouseX-offsetX, mouseY-offsetY);
    setSliderRotation(mouseX, mouseY);
  }
}

public void mouseReleased() {
  isOnSlider=false;
  moveAllRight=false;
}
public int findHpWithName(String name, int numero) {
  for (int i=0;i<hpOnStage.size();i++) {
    hp temp=(hp) hpOnStage.get(i);
    // println (i+" "+temp.numero+" "+temp.type+ " "+ numero+" "+name);
    if (temp.type.equals(name) && temp.numero==numero) {
      return i;
    }
  }
  return -1;
}


public int findHpWithPlacement(int Placement)
{
  for (int i=0;i<hpOnStage.size();i++) {
    hp temp=(hp) hpOnStage.get(i);
    if (temp.placement==Placement)
      return i;
  }
  return 0;
}

public hp findHpWithId(int id)
{
  for (int i=0;i<hpInStock.size();i++) {
    if (hpInStock.get(i).getClass().getName()=="MIAMConfig0_2$RoundedSquare") {
      RoundedSquare temp=(RoundedSquare) hpInStock.get(i);
      if (temp.hpLink.idHp==id)
        return temp.hpLink;
    }
    else {
      RoundedSquareDHp rTemp = (RoundedSquareDHp) hpInStock.get(i);
      if (rTemp.hpLink1.idHp==id) {
        return rTemp.hpLink1;
      }
      else {
        if (rTemp.hpLink2.idHp==id) {
          return rTemp.hpLink2;
        }
      }
    }
  }
  return null;
}




public RoundedSquare findRoundedSquareFromIdHp(int id) {
  for (int i=0;i<hpInStock.size();i++) {
    RoundedSquare temp=(RoundedSquare) hpInStock.get(i);
    if (temp.hpLink.idHp==id)
      return temp;
  }
  return null;
}

/*void addTuioCursor(TuioCursor tcur) {
  boolean onSomething=false;
  int xPos = (int)(tcur.getX()*width);
  int yPos = (int)(tcur.getY()*height);


  if ( tcur.getCursorID()==0 ) {
    //KeyEvent event = new KeyEvent(this, KeyEvent.KEY_PRESSED, 1, 0, KeyEvent.VK_S);
    onSomething = checkButtonGeneral(xPos, yPos);
    if (onglet==1&&!onSomething) {
      onSomething=  checkOnHpButtons(xPos, yPos);
    }
    if (onglet==1 && !onSomething) {
      onSomething = checkOptionButtons(xPos, yPos);
    }

    if (onglet==1 && !onSomething) {
      onSomething = rotationSlider.isOnSlider(xPos, yPos);
    }

    if (!onSomething) {
      onSomething= checkOnglets(xPos, yPos);
    }
    if (onglet==2) {

      checkPistes(xPos, yPos);
      checkOutput(xPos, yPos);
      checkButtonsTab2(xPos, yPos);
    }
    if (onglet==3) {
      //
      checkCurseurs(xPos, yPos);
      checkButtonsTab3(xPos, yPos);
    }
    if (!onSomething) {
      onSomething = checkHpOnStage(xPos, yPos, (onglet==1), pisteSelected);
    }
    if (hpSelected[0]!=null && !onSomething &&xPos<width/2) {
      looseFocus();
    }
  }
  if ( onglet==1&& hpSelected[0]!=null&&tcur.getCursorID()==1) {
    hpSelected[0].rotationActivated =true;
  }
}

void removeTuioCursor(TuioCursor tcur) {
  isOnSlider=false;
  if (hpSelected[0]!=null && tcur.getCursorID()==1) {
    hpSelected[0].rotationActivated =false;
  }
  moveAllRight=false;
}

// called when a cursor is moved
void updateTuioCursor (TuioCursor tcur) {
  int xPos = (int)(tcur.getX()*width);
  int yPos = (int)(tcur.getY()*height);
  if (onglet==1&&tcur.getCursorID()==0) {

    setPositionSelectedHp(xPos-offsetX, yPos-offsetY);
    setSliderRotation(xPos, yPos);
  }
  if (hpSelected[0]!=null && hpSelected[0].rotationActivated)
  {
    float rotCurrent = getRotationBetweenTwoPoints(hpSelected[0].position[0], hpSelected[0].position[1], xPos, yPos);

    hpSelected[0].setRotation(rotCurrent-rotCurrent%(3.1415/12));
    if (hpSelected[0].hasSymetric) {
      int k=getIdOfSymetric(hpSelected[0]);
      hp hpTemp=(hp)hpOnStage.get(k);
      hpTemp.rotation=3.1415 - hpSelected[0].rotation;
    }
  }
}
*/
public float getRotationBetweenTwoPoints(int x1, int y1, int x2, int y2) {
  PVector p1 = new PVector(-x1, -y1);
  PVector p2 = new PVector(x2, y2);
  PVector p3 = PVector.add(p1, p2);
  if (p3.y <0) {
    return -PVector.angleBetween(new PVector(3, 0), p3);
  } 
  else {
    return PVector.angleBetween(new PVector(3, 0), p3);
  }
}
// called after each message bundle
// representing the end of an image frame
/*void refresh(TuioTime bundleTime) {
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
*/
class RoundedSquare {
  int width=200;
  int diameter=100;
  int length=100, fadeout=0;
  int x=100, y=100, centerX, centerY;
  int fill=0xff6699FF;
  String name;
  String linkPicture;
  boolean isFadeIn=true, selected=false;
  boolean isDisable=false;
  PImage img;
  PFont font;
  int shapeColor=0xff000000;
  PFont fontA;
  int numberDispo;
  boolean noChangeTextColor=false;
  float alphaFill=0;
  hp hpLink;
  RoundedSquare() {
    drawRoundedSquare();
  }

  RoundedSquare(int width, int length, int diameter, int x, int y, int fill, String name, int numberDispo, hp hpLink ) {
    this.width=width;
    this.font=font;
    this.numberDispo=numberDispo;
    this.length=length;
    this.diameter=diameter;
    this.x=x;
    this.y=y;
    this.fill=fill;
    this.name=name;
    this.hpLink=hpLink;
    this.linkPicture=linkPicture;
    centerX=x+(width/2);
    centerY=y+(length/2);
    font = createFont("SansSerif", (int)(6*widthCoeff));
    //fontA = loadFont("Ziggurat-HTF-Black-32.vlw");


    //  drawRoundedSquare();
  }

  public void draw() {
    drawRoundedSquare();
  }

  public boolean contains(float xPoint, float yPoint) {

    return((xPoint < this.x+this.length) && (xPoint >this.x) && (yPoint >this.y) && (yPoint< this.y + this.width));
  }

  public void drawRoundedSquare() {
    centerX=x+(width/2);
    centerY=y+(length/2);
    if (selected) {
      fill=0xffABC8E2;
    }
    else {
      if (isDisable)
      {
        fill=0xff5c6f81;
      }
      else fill=0xff375D81;
    }
    stroke(fill, 255);
    fill(fill, 255);
    if ( noChangeTextColor) {
      stroke(shapeColor, 255);
      fill(shapeColor, 255);
    }
    rect( x, y+diameter/2, length, width-diameter);
    rect( x+diameter/2, y, length-diameter, width);
    ellipse(x+diameter/2, y+diameter/2, diameter, diameter);
    ellipse(x-diameter/2+length, y+diameter/2, diameter, diameter);
    ellipse(x+diameter/2, y-diameter/2+width, diameter, diameter);
    ellipse(x-diameter/2+length, y-diameter/2+width, diameter, diameter);
    //  stroke(#B4AF91,255);
    // fill(#B4AF91,255);

    stroke(fill, 255);
    fill(fill, 255);
    rect( x+2, y+diameter/2+2, length-4, width-diameter-4);
    rect( x+diameter/2+2, y+2, length-diameter-4, width-4);
    ellipse(x+diameter/2+2, y+diameter/2+2, diameter, diameter);
    ellipse(x-diameter/2+length-2, y+diameter/2+2, diameter, diameter);
    ellipse(x+diameter/2+2, y-diameter/2+width-2, diameter, diameter);
    ellipse(x-diameter/2+length-2, y-diameter/2-2+width, diameter, diameter);
    textFont(font, 12);

    if (selected) {
      fill(0xff000000);
    }
    else {
      if (isDisable) {
        fill(0xff3e4b57);
      }
      else fill(0xffE1E6FA, 255);
    }

    textAlign(CENTER, CENTER);
    if (hpLink == null){
      text(name, x, y+width/2-15, length, width/2+10); 
    }
    else{
      text(hpLink.nickname + hpLink.numero, x, y+width/2-15, length, width/2+10); 
    }
  }
}

class RoundedSquareDHp {
  int width=200;
  int diameter=100;
  int length=100, fadeout=0;
  int x=100, y=100, centerX, centerY;
  int fill=0xff6699FF;
  String name1, name2;
  String linkPicture; 
  boolean isFadeIn=true, selected=false;
  boolean isDisable1=false;
  boolean isDisable2=false;
  PImage img;  
  PFont font;
  PFont fontA;
  int numberDispo1, numberDispo2;
  float alphaFill=0;
  hp hpLink1, hpLink2;
  RoundedSquareDHp() {
    drawRoundedSquare();
  }

  RoundedSquareDHp(int width, int length, int diameter, int x, int y, int fill, String name1, String name2, hp hpLink1, hp hpLink2 ) {
    this.width=width;
    this.font=font;
    this.numberDispo1=1;
    this.numberDispo2=1;
    this.length=length;
    this.diameter=diameter;
    this.x=x;
    this.y=y;
    this.fill=fill;
    this.name1=name1;
    this.name2=name2;
    this.hpLink1=hpLink1;
    this.hpLink2=hpLink2;
    this.linkPicture=linkPicture;
    centerX=x+(width/2);
    centerY=y+(length/2);
    font = createFont("SansSerif", (int)(6*widthCoeff));
    //fontA = loadFont("Ziggurat-HTF-Black-32.vlw");


    //  drawRoundedSquare();
  }

  public void draw() {
    drawRoundedSquare();
  }

  public boolean contains(float xPoint, float yPoint) {

    return((xPoint < this.x+this.length) && (xPoint >this.x) && (yPoint >this.y) && (yPoint< this.y + this.width));
  }

  public void drawRoundedSquare() {
    centerX=x+(width/2);
    centerY=y+(length/2);
    if (selected) {
      fill=0xffABC8E2;
    }
    else {
      if (isDisable1)
      {
        fill=0xff5c6f81;
      }
      else fill=0xff375D81;
    }
    stroke(fill, 255);
    fill(fill, 255);
    rect( x, y+diameter/2, length, (width-10)/2);
    rect( x+diameter/2, y, length-diameter, width/2);

    ellipse(x+diameter/2, y+diameter/2, diameter, diameter);
    ellipse(x-diameter/2+length, y+diameter/2, diameter, diameter);


    if (isDisable2)
    {
      fill=0xff5c6f81;
    }
    else fill=0xff375D81;

    stroke(fill, 255);
    fill(fill, 255);
    rect( x, y+diameter/2+(width-10)/2, length, (width-10)/2);
    rect( x+diameter/2, y+(width)/2, length-diameter, width/2);
    ellipse(x+diameter/2, y-diameter/2+width, diameter, diameter);
    ellipse(x-diameter/2+length, y-diameter/2+width, diameter, diameter);
    //  stroke(#B4AF91,255);
    // fill(#B4AF91,255);




    textAlign(CENTER, CENTER);
    textFont(font, 12);
    if (selected) {
      fill(0xff000000);
    }
    else {
      if (isDisable1) {
        fill(0xff3e4b57);
      }
      else fill(0xffE1E6FA, 255);
    }


    if (hpLink1 == null){
        text(name1, x, y-2, (int)(100*widthCoeff), (int)(30*heightCoeff));
    }
    else{
       text(hpLink1.nickname + hpLink1.numero, x, y-2, (int)(100*widthCoeff), (int)(30*heightCoeff));
    }
    if (isDisable2) {
      fill(0xff3e4b57);
    }
    else fill(0xffE1E6FA, 255);
     if (hpLink2 == null){
       text(name2, x, y+width/2-2, (int)(100*widthCoeff), (int)(30*heightCoeff));
    }
    else{
       text(hpLink2.nickname + hpLink2.numero, x, y+width/2-2, (int)(100*widthCoeff), (int)(30*heightCoeff));
    }
  }
}

class fader {
  int posX;
  int posY;
  String name;
  int lengthFader=100;
  fader() {
  }

  fader(int posX, int posY, String name) {
    this.posX=posX;
    this.posY=posY;
    this.name=name;
  }

  public void draw() {

    line (posX, posY, posX, posY-lengthFader);
    fill(204, 102, 0);
    rect (posX-15, posY-lengthFader/2-5, 30, 10);
    fill(0xff000000);
    text (name, posX-4, posY+15);
  }
}

class hp {
  String type;
  boolean hasSymetric;
  boolean rotationActivated=false;
  boolean openingScale=false;
  boolean isSelected = false;
  PImage imageHp;
  int[] position = new int [3]; //x,y and Z. Z in { 0=low,1=middle, 2=high}
  int fillColor=0xff6699FF;
  int strokeColor = 0xff000000;
  int xLength=25;
  int coordCorners [][]=new int[10][2];
  int yLength=50;
  int arrowLength=25;
  String linkPicture;
  int idHp;
  float pi=3.1415927f;
  float angleCorners=pi/3;
  float opening=pi/6;
  float rotation=pi/2.0f;
  int numero;
  int placement;
  int frequence_rep_min;
  String nickname;
  int frequence_rep_max; 
  float impedance_nominale; 
  int puissance_rms;
  int puissance_crete;
  String piste;
  int sortie=0;
  int curseur=0;
  boolean OnFocus=false;

  boolean useToBeTwo;
  PFont font_hp;
  int xRotation = 0; // 0= frontal 1=douche 2=fontaine;
  hp() {
    font_hp = createFont("Arial",32);
  }


  hp(hp hpCopy) {
    this();
    this.nickname = hpCopy.nickname;
    this.position[0]=hpCopy.position[0];
    this.position[1]=hpCopy.position[1];
    this.position[2]=hpCopy.position[2];
    this.type=hpCopy.type;
    this.idHp=hpCopy.idHp;
    this.placement=hpCopy.placement;
    calculateDraw();
    this.linkPicture=hpCopy.linkPicture;
    this.imageHp=hpCopy.imageHp;
    this.frequence_rep_min=hpCopy.frequence_rep_min;
    this.frequence_rep_max=hpCopy.frequence_rep_max; 
    this.impedance_nominale=hpCopy.impedance_nominale; 
    this.puissance_rms=hpCopy.puissance_rms;
    this.puissance_crete=hpCopy.puissance_crete;
    this.rotation=hpCopy.rotation;
    this.opening=hpCopy.opening;
    this.useToBeTwo = hpCopy.useToBeTwo;
    this.numero=hpCopy.numero;
  }

  hp(int x, int y, int z, String type, int placement, String linkPicture, int frequence_rep_min, int frequence_rep_max, float impedance_nominale, int puissance_rms, int puissance_crete, int idHp, int useToBeTwo, float opening, int numero, String nickname) {
    this();
    position[0]=x;
    position[1]=y;
    position[2]=z;
    this.nickname = nickname;
    this.type=type;
    this.idHp=idHp;
    this.placement=placement;
    this.opening=opening;
    calculateDraw();
    this.linkPicture=linkPicture;
    imageHp=loadImage(linkPicture);
    imageHp.resize(100, 0);
    this.numero=numero;
    this.frequence_rep_min=frequence_rep_min;
    this.frequence_rep_max=frequence_rep_max; 
    this.impedance_nominale=impedance_nominale; 
    this.puissance_rms=puissance_rms;
    this.puissance_crete=puissance_crete;
    if (useToBeTwo==1) {
      this.useToBeTwo=true;
    }
  }

  public boolean isOnHp(int x, int y) {
    return(position[0]+xLength>x&&position[0]-xLength<x&&position[1]+yLength>y&&position[1]-yLength<y);
  } 

  public void calculateDraw() {
    //coord rect
    coordCorners[0][0]=(int)(position[0]+20*widthCoeff*cos(rotation+angleCorners));
    coordCorners[1][0]=(int)(position[0]+20*widthCoeff*cos(rotation+pi-angleCorners));
    coordCorners[2][0]=(int)(position[0]+20*widthCoeff*cos(rotation+pi+angleCorners));
    coordCorners[3][0]=(int)(position[0]+20*widthCoeff*cos(rotation-angleCorners));
    coordCorners[0][1]=(int)(position[1]+20*widthCoeff*sin(rotation+angleCorners));
    coordCorners[1][1]=(int)(position[1]+20*widthCoeff*sin(rotation+pi-angleCorners));
    coordCorners[2][1]=(int)(position[1]+20*widthCoeff*sin(rotation+pi+angleCorners));
    coordCorners[3][1]=(int)(position[1]+20*widthCoeff*sin(rotation-angleCorners));
    //Coord arrows up
    coordCorners[4][0]=(int)(position[0]+25*widthCoeff*cos(rotation-opening));
    coordCorners[5][0]=(int)(position[0]+22*widthCoeff*cos(rotation-0.1f-opening));
    coordCorners[6][0]=(int)(position[0]+22*widthCoeff*cos(rotation+0.1f-opening));
    coordCorners[4][1]=(int)(position[1]+25*widthCoeff*sin(rotation-opening));
    coordCorners[5][1]=(int)(position[1]+22*widthCoeff*sin(rotation-0.1f-opening));
    coordCorners[6][1]=(int)(position[1]+22*widthCoeff*sin(rotation+0.1f-opening));
    //coord arrow down
    coordCorners[7][0]=(int)(position[0]+25*widthCoeff*cos(rotation+opening));
    coordCorners[8][0]=(int)(position[0]+22*widthCoeff*cos(rotation-0.1f+opening));
    coordCorners[9][0]=(int)(position[0]+22*widthCoeff*cos(rotation+0.1f+opening));
    coordCorners[7][1]=(int)(position[1]+25*widthCoeff*sin(rotation+opening));
    coordCorners[8][1]=(int)(position[1]+22*widthCoeff*sin(rotation-0.1f+opening));
    coordCorners[9][1]=(int)(position[1]+22*widthCoeff*sin(rotation+0.1f+opening));
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

//    calculateDraw();

    //Couleur noire
    textFont(font_hp, 12*widthCoeff);
    strokeWeight(10);
    stroke(0xffffffff);
    

    for (int i =0;i<3;i++) {
      line(coordCorners[i][0], coordCorners[i][1], coordCorners[i+1][0], coordCorners[i+1][1]);
    }

    line(coordCorners[0][0], coordCorners[0][1], coordCorners[3][0], coordCorners[3][1]);
    if (xRotation==0) { 
      line(position [0], position[1], coordCorners[4][0], coordCorners[4][1]);

      line(coordCorners[4][0], coordCorners[4][1], coordCorners[5][0], coordCorners[5][1]);
      line(coordCorners[4][0], coordCorners[4][1], coordCorners[6][0], coordCorners[6][1]);



      line(position [0], position[1], coordCorners[7][0], coordCorners[7][1]);

      line(coordCorners[7][0], coordCorners[7][1], coordCorners[8][0], coordCorners[8][1]);
      line(coordCorners[7][0], coordCorners[7][1], coordCorners[9][0], coordCorners[9][1]);
    }
    if (xRotation==1) { 
      line(coordCorners[0][0], coordCorners[0][1], coordCorners[2][0], coordCorners[2][1]);

      line(coordCorners[1][0], coordCorners[1][1], coordCorners[3][0], coordCorners[3][1]);
    }
    else if (xRotation==2) { 
      fill(0xffE1E6FA);
      ellipse(position[0], position[1], 4, 4);
    }
    if (piste!=null) {
      fill(0xffffffff);
      ellipse(position[0]+20, position[1]-20, 15, 15);
    }
    if (sortie!=0) {
      fill(0xffffffff);
      ellipse(position[0]+20, position[1]+20, 15, 15);
    }
    if (curseur!=0) {
      fill(0xffffffff);
      ellipse(position[0]-20, position[1]-20, 15, 15);
    } 

    if (OnFocus) {
      noStroke();
      fill(0xffABC8E2);
      ellipse(position[0], position[1], 70, 70);
    }
    noStroke();
    fill(0xff000000);

    if (piste!=null) {

      fill(0xff000000);
      text(piste, position[0]-15, position[1]-16);
      
    }
    if (sortie!=0) {

      fill(0xff000000);
      text(sortie, position[0]+15, position[1]-16);
    }
    if (curseur!=0) {

      fill(0xff000000);
      text(curseur, position[0], position[1]-32);
    }
    //Couleur Au dessus
    stroke(0xffE1E6FA);
    if (isSelected) {
      noFill();
      strokeWeight(5);
      stroke(0xffABC8E2);

      ellipse(position[0], position[1], 100, 100);
      stroke(0xffE1E6FA);
    }
    fill(0xff000000);
    strokeWeight(1);
    //text(type+" "+numero, position[0], position[1]-32);
    strokeWeight(2);
    if (position[2]==1)
    {
      stroke(0xffbc6337);
    }
    if (position[2]==0)
    {
      stroke(0xff61bc4f);
    }
    if (position[2]==2)
    {
      stroke(0xffa51c1c);
    }

    for (int i =0;i<3;i++) {
      line(coordCorners[i][0], coordCorners[i][1], coordCorners[i+1][0], coordCorners[i+1][1]);
    }

    line(coordCorners[0][0], coordCorners[0][1], coordCorners[3][0], coordCorners[3][1]);
    if (xRotation==0) { 
      line(position [0], position[1], coordCorners[4][0], coordCorners[4][1]);

      line(coordCorners[4][0], coordCorners[4][1], coordCorners[5][0], coordCorners[5][1]);
      line(coordCorners[4][0], coordCorners[4][1], coordCorners[6][0], coordCorners[6][1]);



      line(position [0], position[1], coordCorners[7][0], coordCorners[7][1]);

      line(coordCorners[7][0], coordCorners[7][1], coordCorners[8][0], coordCorners[8][1]);
      line(coordCorners[7][0], coordCorners[7][1], coordCorners[9][0], coordCorners[9][1]);
    }
    if (xRotation==1) { 
      line(coordCorners[0][0], coordCorners[0][1], coordCorners[2][0], coordCorners[2][1]);

      line(coordCorners[1][0], coordCorners[1][1], coordCorners[3][0], coordCorners[3][1]);
    }
    fill(0xff000000);
    if (xRotation==2) { 

      fill(0xffE1E6FA);
      ellipse(position[0], position[1], 4, 4);
    }

    fill(0xff000000);
    strokeWeight(1);
    
    text(nickname+numero, position[0], position[1]-25);
    /* if(!OnFocus && piste!=null){
     noStroke();
     fill(#ffffff,200);
     ellipse(position[0], position[1], 100, 100);
     }*/

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
     textFont(font, 16*widthCoeff);
  }
}

class pdfLatex extends Thread {

  boolean running = false;
  private int wait; 
  String name;
  Process p;
  pdfLatex(String name) {
    this.name=name;
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
    try {
      Thread.sleep(3000);

      Runtime runtime = Runtime.getRuntime();
      Process proc = runtime.exec(
      "C:/Users/Luds/Dropbox/Programmes/Processing/MIAMConfig0_1/compile.bat" );//dos command
    }

    catch(Exception e) {
      // print(e);
    } 



    // Ok, let's wait for however long we should wait
    try {
      sleep((long)(wait));
    } 
    catch (Exception e) {
    }

    //}
  }
}
class slider {

  int val_min, val_max, increment, posX, posY, currentPos, sWidth;

  String name;
  slider(  int val_min, int val_max, int increment, int posX, int posY, String name, int sWidth)
  {
    this.val_min=val_min;
    this.val_max=val_max;
    this.increment=increment;
    this.posX=posX;
    this.sWidth=sWidth;
    this.posY=posY;
    this.name=name;
    this.currentPos=posX;
  }

  public void draw() {
    strokeWeight(10);
    stroke(0xffE1E6FA);
    line (posX, posY, posX+sWidth, posY);
    strokeWeight(1);
    fill(0xffE1E6FA);
    text(val_min, posX, posY+15);
    for (int i=1;i<4;i++)
      text((int)((val_min+val_max)*i/4), (int)((posX)+sWidth*i/4), posY+15 );
    text(val_max, posX+sWidth, posY+15);
    stroke(0xff375D81);
    strokeWeight(1);
    fill(0xff375D81);
    ellipse(currentPos, posY, 20, 20);
  }

  public boolean isOnSlider(int x, int y) {
    return (x<posX+sWidth && x>posX-10 && y<posY+10 && y>posY-10);
  }
}

class tuioThread extends Thread {

  boolean running = false;
  private int wait; 

  tuioThread() {

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


      try {

        //println("TUIO Bridge Launched");
        Process p = Runtime.getRuntime().exec("C:\\Touch2Tuio\\Touch2Tuio.exe MIAMConfig");
        // Process p =  Runtime.getRuntime().exec("C:\\Users\\Administrator\\Downloads\\Touch2Tuio_0.2\\Touch2Tuio\\Release\\Touch2Tuio.exe MIAMConfig");
        p.waitFor();
      }
      catch(Exception e) {
        // print(e);
      } 



      // Ok, let's wait for however long we should wait
      try {
        sleep((long)(wait));
      } 
      catch (Exception e) {
      }
    }
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MIAMConfig0_2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
