class tuioThread extends Thread {

  boolean running = false;
  private int wait; 

  tuioThread() {

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

