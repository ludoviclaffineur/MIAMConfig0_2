class pdfLatex extends Thread {

  boolean running = false;
  private int wait; 
  String name;
  Process p;
  pdfLatex(String name) {
    this.name=name;
    running = false;
  }

  void start () {
    // Set running equal to true
    running = true;
    // Do whatever start does in Thread, don't forget this!
    super.start();
  }
  // We must implement run, this gets triggered by start()
  void run () {
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
