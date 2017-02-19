
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class SoundDataUtils {
	 public static double[] load16BitPCMRawDataFileAsDoubleArray(File file,int N, int sec) {
		    InputStream in = null;
		    if (file.isFile()) {
		    	long size = file.length();
		      try {
		        in = new FileInputStream(file);
		        return readStreamAsDoubleArray(in, size, N, sec);
		      } catch (Exception e) {
		      }
		    }
		    else
		    	System.out.println("not read file");
		    return null;
		  }

		  public static double[] readStreamAsDoubleArray(InputStream in, long size, int N, int sec)
		      throws IOException {
		    //int bufferSize = (int) (size / 2);
		    //double[] result = new double[bufferSize];
		    double[] result = new double[sec*N];
		    double[] notneed = new double[44100-N];
		    DataInputStream is = new DataInputStream(in);
		   
		    for (int j = 0; j < sec; j++) {
		    	for(int i=0;i<N;i++)
		    	{
		    		result[i+(j*N)] = is.readShort() / 32768.0;
		    	}
		    	for(int k=0;k<44100-N;k++)
		    	{
		    		notneed[k] = is.readShort() / 32768.0;
		    	}
		    	
		      //System.out.println(result[i]);
		    }
		    return result;
		  }
	  public static void main(String[] args) throws IOException {
		 
		  
		  /*File file = new File("recordsound1.pcm");
		  //System.out.println(file.getAbsolutePath());
		  if(!file.exists()){
			  System.out.println("not found");
		  }
		  else{
			  long size = file.length();
			  System.out.println(size);
		  }
		  
		  load16BitPCMRawDataFileAsDoubleArray(file);*/
	  }
}
