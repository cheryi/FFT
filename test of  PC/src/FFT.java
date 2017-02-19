import java.io.File;

import org.jfree.ui.RefineryUtilities;

public class FFT {

  int n, m;

  // Lookup tables. Only need to recompute when size of FFT changes.
  double[] cos;
  double[] sin;

  public FFT(int n) {
      this.n = n;
      this.m = (int) (Math.log(n) / Math.log(2));

      // Make sure n is a power of 2
      if (n != (1 << m))
          throw new RuntimeException("FFT length must be power of 2");

      // precompute tables
      cos = new double[n / 2];
      sin = new double[n / 2];

      for (int i = 0; i < n / 2; i++) {
          cos[i] = Math.cos(-2 * Math.PI * i / n);
          sin[i] = Math.sin(-2 * Math.PI * i / n);
      }

  }

  public void fft(double[] x, double[] y) {
      int i, j, k, n1, n2, a;
      double c, s, t1, t2;

      // Bit-reverse
      j = 0;
      n2 = n / 2;
      for (i = 1; i < n - 1; i++) {
          n1 = n2;
          while (j >= n1) {
              j = j - n1;
              n1 = n1 / 2;
          }
          j = j + n1;

          if (i < j) {
              t1 = x[i];
              x[i] = x[j];
              x[j] = t1;
              t1 = y[i];
              y[i] = y[j];
              y[j] = t1;
          }
      }

      // FFT
      n1 = 0;
      n2 = 1;

      for (i = 0; i < m; i++) {
          n1 = n2;
          n2 = n2 + n2;
          a = 0;

          for (j = 0; j < n1; j++) {
              c = cos[a];
              s = sin[a];
              a += 1 << (m - i - 1);

              for (k = j; k < n; k = k + n2) {
                  t1 = c * x[k + n1] - s * y[k + n1];
                  t2 = s * x[k + n1] + c * y[k + n1];
                  x[k + n1] = x[k] - t1;
                  y[k + n1] = y[k] - t2;
                  x[k] = x[k] + t1;
                  y[k] = y[k] + t2;
              }
          }
      }
  }



  public static void main(String[] args) {
	   int N = 4096;//num of sample
	   int sec = 3;

	   FFT fft = new FFT(N);


	   double[] re = new double[N];
	   double[] im = new double[N];
	   double[] b = new double[sec*N];
	   
	   File file = new File("recordsound1.pcm");
	   //File file = new File("¾aªñ.pcm");
	   //File file = new File("»·Â÷.pcm");
		  //System.out.println(file.getAbsolutePath());
		  if(!file.exists()){
			  System.out.println("not found");
		  }
		  else{
			  long size = file.length();
			  System.out.println(size);
		  }
		  SoundDataUtils a = new SoundDataUtils();
		  b = a.load16BitPCMRawDataFileAsDoubleArray(file,N,sec);
	      
	   
	//test
		  for(int j=0;j<sec;j++)
		  {
			  System.out.println("\nTEST" + (j+1));
			  for(int i=0; i<N; i++) {
				  re[i] = b[i+(j*N)];
				  im[i] = 0;
			  }
			  double[] ans = beforeAfter(fft, re, im, N);
			  
			  //chart
			  /*
			  chart chart = new chart("Spectrum"+j, "Spectrum"+j, ans, N);
		      chart.pack( );          
		      RefineryUtilities.centerFrameOnScreen( chart );          
		      chart.setVisible( true ); 
		      */
		      //
		  }
	}



protected static double[] beforeAfter(FFT fft, double[] re, double[] im, int N) {
	  //System.out.println("Before: ");
	  //printReIm(re, im);
	  fft.fft(re, im);
	  //System.out.println("After: ");
	  //System.out.print("[");
	  
	  int sampleRate=44100;
	  double sum=0, max=0, high=80;
	  double[] ans = new double[re.length];
	  int[] frequence = new int[re.length/2];
	  int j=0;
	  //magnitude
	  System.out.print("magnitude : ");
	  for(int i=0;i<re.length;i++)
	  {
		  ans[i] = Math.pow(re[i], 2) + Math.pow(im[i], 2);
		  ans[i] = Math.sqrt(ans[i]);
		  
		  //all output
		  System.out.print(((int)(ans[i]*1000)/1000.0) + " ");
		  //
		  if(ans[i]>high)
			  frequence[j++]=i;
	  }
	  System.out.print("\n");
	  //
	  System.out.print("frequence (> "+ high +")= ");
	  for(int i=0;i<re.length/2;i++)
	  {
		  if(frequence[i]<re.length/2&&frequence[i]!=0)
		  System.out.print(" " + (int)(sampleRate/N*(frequence[i])));
	  }
	  System.out.print("\n");


	  //average and max
	  /*
	  for(int i=0;i<re.length/2;i++)
	  {
		  if(ans[i]!=0)
		  {
			  if(ans[i]>max)
				  max=ans[i];
		  sum = sum + ans[i];
		  }
	  }
	  System.out.println("\navg = " + sum/(re.length/2));
	  System.out.println("max = " + max);
	  */
	  return ans;
	}

protected static void printReIm(double[] re, double[] im) {
  System.out.print("Re: [");
  for(int i=0; i<re.length; i++)
    System.out.print(((int)(re[i]*1000)/1000.0) + " ");

  System.out.print("]\nIm: [");
  for(int i=0; i<im.length; i++)
    System.out.print(((int)(im[i]*1000)/1000.0) + " ");

  System.out.println("]");
}
}
