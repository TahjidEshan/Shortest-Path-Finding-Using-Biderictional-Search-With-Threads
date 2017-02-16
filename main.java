import java.io.*;
import java.util.*;

public class main{

  public static void main(String[]args){
   map m=new map();
   Thread t1=new Thread(m,"Forward");
   t1.start();
   Thread t2=new Thread(m,"Backward");
   t2.start();
  }
}