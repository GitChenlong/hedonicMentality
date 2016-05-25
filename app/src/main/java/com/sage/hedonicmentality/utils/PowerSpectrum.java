package com.sage.hedonicmentality.utils;

public class PowerSpectrum {
    
    /* Defines windowing of data */
    public static final int BARTLETT=1;
    public static final int WELCH=2;
    public static final int HANN=3;
    public static final int HAMM=4;
    private static final double Pi2=2*Math.PI;
          
    public double[] x;
    public double[] spectrum;
    public  int nSpectrum;
    private int winNum;
    private int n,nn;
    private double scale;
    private double[] data;
    private double[] winMult;
    private double floor=0.;      
    
//**********************************************************************      
/**
* Constructor
* @param    in_nn number of data points : must be power of 2!
* @param    in_winNum window for FFT: 1=BARTLETT 2=WELCH 3=HANN other=NONE
* @param    in_scale scale factor to convert from index to frequency 
*/          
//********************************************************************** 
    public PowerSpectrum(int in_nn, int in_winNum, double in_scale) {
      nn=in_nn;
      winNum=in_winNum;
      scale=in_scale;
      spectrum=new double[nn/2+1];
      n=2*nn;
      data = new double[n+1];
      nSpectrum=0;
      winMult=new double[nn];
      for(int i=0,j=1;i<nn;i++)
            winMult[i]=window(winNum,nn,i);                
    }
//**********************************************************************      
/**
* Transforms data and updates spectrum
* @param x array of length 2*in_nn containing time sequence of real and imaginary data.<br>
* On output x contains spectrum in format suitable for direct plotting in graph2D
* in first in_nn positions.
*/          
//**********************************************************************      
    public void transform(double[] x) {
          int i,j;
          double sumWindow=0;//nn*nn;
          
          System.arraycopy(x,1,data,1,n);
          if(winNum>0) {
              for(i=0,j=1;i<nn;i++) {
                      data[j]=data[j]*winMult[i];
                      j++;
                      data[j]=data[j]*winMult[i];
                      j++;
                      sumWindow=sumWindow+winMult[i]*winMult[i];
              }
          } else
          	sumWindow=1;

          four1(data,nn,1);
			//System.out.println("FFT real and img numbers");
			for(int k=0; k<nn; k++)
			{
				////System.out.println(k+"\t"+data[2*k+1]+"\t"+data[2*k+2]);	
			}
	
          nSpectrum++;
          x[0]=0; 
          
          //System.out.println("spectrum:");
          spectrum[0]=(data[0]*data[0]+data[1]*data[1])/sumWindow;
         // spectrum[0]=(data[0]*data[0]+data[1]*data[1]);
//          System.out.println(spectrum[0]);0
//          System.out.println(data[0] + "," + data[1]);
          for(i=1,j=2;i<nn/2;i++) {
                x[j]=i*scale;
                j++;
                spectrum[i]=2.0*(data[j]*data[j]+data[j+1]*data[j+1])/sumWindow;
                //spectrum[i]=(data[j]*data[j]+data[j+1]*data[j+1]);
               //// System.out.println(data[j] + "," + data[j+1]);
                j++;
                //System.out.println((double)i/nn + "," + spectrum[i]);
          }
          x[j]=(nn/2)*scale;
          j++;
          spectrum[nn/2]=(data[j]*data[j]+data[j+1]*data[j+1])/sumWindow;
          ////spectrum[nn/2]=(data[j]*data[j]+data[j+1]*data[j+1]);
         //// System.out.println(data[j] + "," + data[j+1]);
         //// System.out.println(spectrum[nn/2]);

//        System.out.println("sumWindow="+sumWindow);
    }
    
//**********************************************************************      
/**
* Number of spectra calculated
* @return   number of spectra
*/          
//**********************************************************************     
    public int getNumberOfSpectra() {
        return nSpectrum;
    }          
//**********************************************************************

//**********************************************************************      
/**
* Set floor for log functio
* @param input value of floor
*/          
//**********************************************************************     
    public void setFloor(double input) {
        floor=input;
    }          
//**********************************************************************
    
//**********************************************************************      
/**
* window function
* @param    winNum window for FFT: 1=BARTLETT 2=WELCH 3=HANN 0=NONE
* @param    nn number of data points
* @param    i of data point
* @return   value of window function 
*/          
//**********************************************************************      
    private double window(int winNum, int nn, int i) {
         double facm=nn/2;
         double facp=1./facm;
         switch (winNum) {
              case BARTLETT:
                      return 1.0-Math.abs(((i-1)-facm)*facp);                    
              case WELCH:
                      return 1.0-(((i-1)-facm)*facp)*(((i-1)-facm)*facp);
              case HANN:
              	 return 0.5*(1-Math.cos(Pi2*(i-1)*0.5*facp));
              case HAMM:
                  return 0.54 - 0.46*Math.cos(Pi2*i/facm);
              	
              default:
                      return 1.0;
         }
    }                                
    
//**********************************************************************      
/**
* FFT from Numerical recipes
* @param data array of nn data points<br>
* On return contains transform <br>
* For packing see Numerical Recipes
* @param    nn number of data points
* @param    isign 1 for forward; -1 for inverse
*/          
//**********************************************************************          
    private void four1(double data[], int nn, int isign) {
        int n,mmax,m,j,istep,i;
        double wtemp,wr,wpr,wpi,wi,theta;
        double tempr,tempi;
        double swap;
  
        n=2*nn;
        j=1;
        for (i=1;i<n;i+=2) {
              if (j > i) {
                    swap=data[j];
                    data[j]=data[i];
                    data[i]=swap;
                    
                    swap=data[j+1];
                    data[j+1]=data[i+1];
                    data[i+1]=swap;                  
              }
              m=n >> 1;
              while (m >= 2 && j > m) {
                    j -= m;
                    m >>= 1;
              }
              j += m;
        }
        mmax=2;
        while (n > mmax) {
              istep=2*mmax;
              theta=isign*(6.28318530717959/mmax);
              wtemp=Math.sin(0.5*theta);
              wpr = -2.0*wtemp*wtemp;
              wpi=Math.sin(theta);
              wr=1.0;
              wi=0.0;
              for (m=1;m<mmax;m+=2) {
                    for (i=m;i<=n;i+=istep) {
                          j=i+mmax;
                          tempr=wr*data[j]-wi*data[j+1];
                          tempi=wr*data[j+1]+wi*data[j];
                          data[j]=data[i]-tempr;
                          data[j+1]=data[i+1]-tempi;
                          data[i] += tempr;
                          data[i+1] += tempi;
                    }
                    wr=(wtemp=wr)*wpr-wi*wpi+wr;
                    wi=wi*wpr+wtemp*wpi+wi;
              }
              mmax=istep;
        }
   }


}
