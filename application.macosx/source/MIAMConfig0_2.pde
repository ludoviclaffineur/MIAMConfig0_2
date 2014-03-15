
import processing.pdf.*;
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
float widthCoeff = (float) (displayWidth-100) /1920.0;
float heightCoeff = (float) (displayHeight-100) /1080.0;



void setup() { 

 // oscP5 = new OscP5(this, 1112);
  size(displayWidth-100, displayHeight-100);

  widthCoeff = (float) (displayWidth-100) /1920.0;
  heightCoeff = (float) (displayHeight-100) /1080.0;
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


void draw() {
  //long start = System.nanoTime();    

  if (ExportPDF) {
    beginRecord(pdf);
  }
  //smooth();

  background(#183152);
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
    fill(#E1E6FA);

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

void drawFaders() {
  text("Curseurs disponibles", width/2+20, 50);
  for (int i=0;i<Faders.length;i++) {
    Faders[i].draw();
  }
}

void drawOutputs() {
  text("Sorties carte son", width/2+20, outputs[0].y-20);
  for (int i=0;i< outputs.length;i++) {
    outputs[i].draw();
  }
}
void drawFiveDotOne() {
  text("Pistes audios", width/2+20, 50);
  for (int i =0;i<fiveDotOne.length;i++)
    fiveDotOne[i].draw();
}

void  drawOnglets() {
  stroke(#ABC8E2);
  fill(#E1E6FA);
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
    fill(#E1E6FA);
    textFont(fontA, 20);
    textAlign(LEFT);
    text(hpSelected[0].type, width/2+20, height/2-30);
    //text("Image", width/2, height/2+150);
    text("Orientation", width/2+150, height/2+170);
    text("Hauteur", width/2+300, height/2-30);
    image(hpSelected[0].imageHp, width/2+20, height/2);
    if (hpSelected[0].hasSymetric) {
      testButton[2].name="Symétrique";
      testButton[2].selected=true;
    }
    else {
      testButton[2].name="Symétrique";
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

void prepareMenu() {
  rotationSlider = new slider (0, 360, 1, width/2+150, height/2+200, "rotation", 200);
  testButton[0] = new RoundedSquare (40, 100, 20, width/2+450, height/2 +50, #375D81, "Douche", 0, null);
  testButton[1] = new RoundedSquare (40, 100, 20, width/2+450, height/2 +100, #375D81, "Fontaine", 0, null);
  testButton[2] = new RoundedSquare (40, 100, 20, width/2+150, height/2, #375D81, "Symétrique", 0, null);
  testButton[3] = new RoundedSquare (40, 100, 20, width/2+300, height/2 +0, #375D81, "Plafond", 0, null);
  testButton[3].shapeColor=#a51c1c;
  testButton[3].noChangeTextColor=true;
  testButton[4] = new RoundedSquare (40, 100, 20, width/2+300, height/2 +50, #375D81, "Balcon", 0, null);
  testButton[4].shapeColor=#bc6337;
  testButton[4].noChangeTextColor=true;
  testButton[5] = new RoundedSquare (40, 100, 20, width/2+300, height/2 +100, #375D81, "Sol", 0, null);
  testButton[5].shapeColor=#61bc4f;
  testButton[5].noChangeTextColor=true;
  testButton[6] = new RoundedSquare (40, 100, 20, width/2+450, height/2 +0, #375D81, "Frontal", 0, null);
  testButton[7] = new RoundedSquare (40, 100, 20, width/2+20, height/2+150 +0, #375D81, "Supprimer HP", 0, null);
  buttonGeneral[0] = new RoundedSquare (40, 100, 20, width-120, height-150, #375D81, "Charger", 0, null);
  buttonGeneral[1] = new RoundedSquare (40, 100, 20, width-230, height-150, #375D81, "Sauvegarder", 0, null);
  buttonGeneral[2] = new RoundedSquare (40, 100, 20, width-340, height-150 +0, #375D81, "Quitter", 0, null);

  buttonTab2[0]=new RoundedSquare (40, 100, 20, width/2+20, height/2-(int)(250*heightCoeff), #375D81, "Supp assign", 0, null);
  buttonTab2[1]=new RoundedSquare (40, 100, 20, width/2+20, height-(int)(300*heightCoeff) +0, #375D81, "Supp assign", 0, null);
  buttonTab3=new RoundedSquare (40, 100, 20, width/2+20, height-(int)(300*heightCoeff) +0, #375D81, "Supp assign", 0, null);
  int j=0;
  int k=0; 
  for (int i =0;i<fiveDotOne.length;i++) {
    if (width/2 +k*(int)(widthCoeff*60)>width-(int)(widthCoeff*80))
    {
      j++;
      k=0;
    }
    fiveDotOne[i] = new RoundedSquare ((int)(50*widthCoeff), (int)(50*widthCoeff), 10, width/2+20+k*(int)(widthCoeff*60), 80+j*(int)(widthCoeff*60), #375D81, ""+(i+1), 1, null);
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
    outputs[i] = new RoundedSquare ((int)(widthCoeff*50), (int)(widthCoeff*50), 10, width/2+20 +k*(int)(widthCoeff*60), height/2-80+j*(int)(widthCoeff*60), #375D81, ""+(i+1), 1, null);
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
    Faders[i] = new RoundedSquare ((int)(widthCoeff*50), (int)(widthCoeff*50), 10, 3*width/5 +k*(int)(widthCoeff*60), height/4-80+j*(int)(widthCoeff*60), #375D81, ""+(i+1), 1, null);
    k++;
  }
}
void loadXML() {

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
    hpTemp.rotation= (Float.parseFloat(rotationElements[i].getContent()))* 3.1415/180;
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

void loadHpInStock() {
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
      hpInStock.add(new RoundedSquare ((int)(50.0*heightCoeff), (int)(100.0*widthCoeff), 10, width/2 +20+k*(int)(110*widthCoeff), 60+j*(int)(60*heightCoeff), #375D81, marque[i].getContent()+" "+modele[i].getContent()+" "+numero[i].getContent(), Integer.parseInt(nombre[i].getContent()), 
      new hp(20, 20, 0, marque[i].getContent() +" "+ modele[i].getContent(), 0, "images/"+images[i].getContent(), Integer.parseInt(frequence_rep_min[i].getContent()), 
      Integer.parseInt(frequence_rep_max[i].getContent()), Float.parseFloat(impedance_nominale[i].getContent()), Integer.parseInt( puissance_rms[i].getContent()), 
      Integer.parseInt( puissance_crete[i].getContent()), Integer.parseInt(id[i].getString("id")), Integer.parseInt(parite[i].getContent()), Float.parseFloat(directivite[i].getContent())*3.1415/360, Integer.parseInt(numero[i].getContent()),nickname[i].getContent()) ) );
      k++;
    }
    else {
      if (Integer.parseInt(numero[i].getContent())%2!=0) {
        hpInStock.add(new RoundedSquareDHp ((int)(50.0*heightCoeff), (int)(100.0*widthCoeff), 10, width/2+20 +k*(int)(110*widthCoeff), 60+j*(int)(60*heightCoeff), #375D81, marque[i].getContent() +" "+ modele[i].getContent()+" "+numero[i].getContent(), marque[i].getContent() +" "+ modele[i].getContent()+" "+numero[i+1].getContent(), 
        new hp(20, 20, 0, marque[i].getContent() +" "+ modele[i].getContent(), 0, "images/"+images[i].getContent(), Integer.parseInt(frequence_rep_min[i].getContent()), 
        Integer.parseInt(frequence_rep_max[i].getContent()), Float.parseFloat(impedance_nominale[i].getContent()), Integer.parseInt( puissance_rms[i].getContent()), 
        Integer.parseInt( puissance_crete[i].getContent()), Integer.parseInt(id[i].getString("id")), Integer.parseInt(parite[i].getContent()), Float.parseFloat(directivite[i].getContent())*3.1415/360, Integer.parseInt(numero[i].getContent()),nickname[i].getContent()), 
        new hp(20, 20, 0, 
        marque[i].getContent() +" "+ modele[i].getContent(), 
        0, "images/"+images[i].getContent(), Integer.parseInt(frequence_rep_min[i].getContent()), 
        Integer.parseInt(frequence_rep_max[i].getContent()), Float.parseFloat(impedance_nominale[i].getContent()), Integer.parseInt( puissance_rms[i].getContent()), 
        Integer.parseInt( puissance_crete[i].getContent()), Integer.parseInt(id[i+1].getString("id")), Integer.parseInt(parite[i].getContent()), Float.parseFloat(directivite[i].getContent())*3.1415/360, Integer.parseInt(numero[i+1].getContent()),nickname[i+1].getContent()) ) );
        k++;
      }
    }
  }
}

void exportToTex() {

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


void exportToXML() {
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
    data+="\t\t<rotation>"+round(hpTemp.rotation*180/3.1415)+"</rotation>\n";
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

void keyPressed() {
  if (key=='s' || key=='S') {
    exportToXML();
  }
  if (key=='l') {
    loadXML();
  }
}

void selectHp(hp rHp, boolean wantSym)
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

  rotationSlider.currentPos=(int)(200f/(2*3.1415) * hpSelected[0].rotation)+rotationSlider.posX;
  if (rotationSlider.currentPos<rotationSlider.posX)
  {
    rotationSlider.currentPos+=100;
  }
}


boolean checkOnHpButtons(int x, int y) {
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

boolean checkHpOnStage(int x, int y, boolean wantSymetric, int pisteSelec) {
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

boolean checkOptionButtons(int x, int y) {
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

void looseFocus() {
  hpSelected[0].isSelected=false;
  if (hpSelected[0].hasSymetric) {
    int k= getIdOfSymetric(hpSelected[0]);
    hp hpTemp = (hp)hpOnStage.get(k);
    hpTemp.isSelected=false;
  }
  hpSelected[0]=null;
}


boolean checkButtonGeneral (int x, int y) {
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

void mousePressed() {
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

void checkCurseurs(int x, int y) {
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

void checkButtonsTab3(int x, int y) {
  if (buttonTab3.contains((float) x, (float) y)) {
    deleteLinkFaderHP();
  }
}

void deleteLinkFaderHP() {
  hpSelected[0].curseur=0;
}

void checkButtonsTab2(int x, int y) {
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

void deleteLinkPisteHp() {
  if (hpSelected[0]!=null) {
    int localPiste= Integer.parseInt(hpSelected[0].piste) -1;
    // fiveDotOne[localPiste].isDisable=false;
    hpSelected[0].piste=null;
  }
}


void deleteLinkOutputHp() {
  if (hpSelected[0]!=null) {
    int localOutput= hpSelected[0].sortie-1;
    if (localOutput!=-1) {
      outputs[localOutput].isDisable=false;
      hpSelected[0].sortie=0;
    }
  }
}
void checkPistes(int x, int y) {
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

void checkOutput(int x, int y) {
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

void clearSelectedHpsCur() {
  for (int i=0; i<listeHpSelCur.size();i++) {
    hp temp=(hp)listeHpSelCur.get(i);

    temp.OnFocus=false;
  }
  listeHpSelCur.clear();
}

void addSelectedInTheListCurseur(int curseur) {
  clearSelectedHpsCur();

  for (int i=0; i<hpOnStage.size()&& Faders[curseur].selected;i++) {
    hp temp=(hp)hpOnStage.get(i);
    if (temp.curseur==curseur+1) {
      temp.OnFocus=true;
      listeHpSelCur.add(temp);
    }
  }
}

void clearSelectedHps() {
  for (int i=0; i<listeHpSel.size();i++) {
    hp temp=(hp)listeHpSel.get(i);

    temp.OnFocus=false;
  }
  listeHpSel.clear();
}

void addSelectedInTheList(int piste) {

  clearSelectedHps();
  for (int i=0; i<hpOnStage.size()&& fiveDotOne[piste].selected;i++) {
    hp temp=(hp)hpOnStage.get(i);
    if (temp.piste!=null&&temp.piste.equals(Integer.toString(piste+1))) {
      temp.OnFocus=true;
      listeHpSel.add(temp);
    }
  }
}
void linkHpOutput (hp hpSel, int output) {
  if (hpSel!=null && outputs[output].selected && onglet==2 && hpSel.sortie==0) {
    hpSel.sortie=output+1;
    outputs[output].selected=false;
    outputs[output].isDisable=true;
  }
}

void disablePisteAndFaders() {
  Faders[curseurSelected].selected=false;
  fiveDotOne[pisteSelected].selected=false;
  clearSelectedHps();
  clearSelectedHpsCur();
}


boolean checkOnglets (int x, int y) {
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


void removeHp(hp hpSel) {


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
      int m= getPositionOfHpInStock(hpTemp.idHp); //ici ca pu encore amélio
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

void addSymetric(hp hpDepart)
{
  int idHp = getIdOfSymetric(hpDepart);
  // println("idHp "+idHp);
  if (idHp!=-1) {            //Est déjà présent sur la scène.
    hpDepart.hasSymetric=true;
    hp hpTemp=(hp) hpOnStage.get(idHp);
    hpTemp.hasSymetric=true;
    hpTemp.isSelected=true;
    hpTemp.position[2]=hpDepart.position[2];
    hpTemp.xRotation=hpDepart.xRotation;
    hpTemp.position[0]=width/2-hpDepart.position[0];
    hpTemp.position[1]=hpDepart.position[1];
    hpTemp.rotation=3.1415-hpDepart.rotation;
    hpTemp.calculateDraw();
  }
  else {
    hp rHp;
    int idFinal;
    if ( hpDepart.numero % 2!=0) {            //Numéros impaires
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
    else {                                       //numéros paires
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
    rHp.rotation=3.1415-hpDepart.rotation;
    rHp.xRotation=hpDepart.xRotation;
    hpDepart.hasSymetric=true;
    rHp.hasSymetric=true;
    rHp.isSelected=true;
    hpOnStage.add(rHp);
    rHp.calculateDraw();
  }
}

int getPositionOfHpInStock(int id) {
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

int getPositionOfHpInStock(String name, int numero) {

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


int getIdOfHpInStock(String name, int numero) {
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

int getIdOfSymetric(hp hpSel)
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

void setPositionSelectedHp(int x, int y) {
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


void setSliderRotation(int x, int y) {
  if (hpSelected[0]!=null && rotationSlider.isOnSlider(x, y)) {
    rotationSlider.currentPos=x-(x%(int)(rotationSlider.sWidth/24f));
    hpSelected[0].setRotation(((2*3.1415)/rotationSlider.sWidth * (x-rotationSlider.posX))-((2*3.1415)/rotationSlider.sWidth * (x-rotationSlider.posX)%(3.1415/12)));

    isOnSlider=true;
    if (hpSelected[0].hasSymetric) {
      int k=getIdOfSymetric(hpSelected[0]);
      hp hpTemp=(hp)hpOnStage.get(k);
      hpTemp.setRotation(3.1415 - hpSelected[0].rotation);
    }
  }
}

void mouseDragged() {
  if (onglet==1) {
    setPositionSelectedHp(mouseX-offsetX, mouseY-offsetY);
    setSliderRotation(mouseX, mouseY);
  }
}

void mouseReleased() {
  isOnSlider=false;
  moveAllRight=false;
}
int findHpWithName(String name, int numero) {
  for (int i=0;i<hpOnStage.size();i++) {
    hp temp=(hp) hpOnStage.get(i);
    // println (i+" "+temp.numero+" "+temp.type+ " "+ numero+" "+name);
    if (temp.type.equals(name) && temp.numero==numero) {
      return i;
    }
  }
  return -1;
}


int findHpWithPlacement(int Placement)
{
  for (int i=0;i<hpOnStage.size();i++) {
    hp temp=(hp) hpOnStage.get(i);
    if (temp.placement==Placement)
      return i;
  }
  return 0;
}

hp findHpWithId(int id)
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




RoundedSquare findRoundedSquareFromIdHp(int id) {
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
float getRotationBetweenTwoPoints(int x1, int y1, int x2, int y2) {
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
