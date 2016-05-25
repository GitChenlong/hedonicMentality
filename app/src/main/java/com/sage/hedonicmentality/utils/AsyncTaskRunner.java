package com.sage.hedonicmentality.utils;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.sage.hedonicmentality.bean.HRVData;


public class AsyncTaskRunner extends AsyncTask<float[][], String, String> {

	private int startindex=0;
	private int endindex=0;
	private float[][]indata=null;
	private float HRMax=0;
	private float HRMean=0;
	private float NNMax=0;
	private float NNMean=0;
	private float SDNN=0;
	private float RMSSD=0;
	private float TP=0;
	private float LF=0;
	private float HF=0;
	private float lfhf=0;
	private float SUMSDNN=0;
	private int analysistime=0; //1 for 1 minute 5 for fice minutes
	private int hrsize=0;
	private Fragment activity;
	private static final String TAG = "BioTrainingHRVDebug";
	private float hr;
	private AsyncTaskListener callback;

	public AsyncTaskRunner(Fragment act, int pstartindex, int pendindex, float hr, float[][] pdatatoprocess, int phrsize,float sumsdnn){
		this.activity = act;
		this.callback = (AsyncTaskListener)act;
		startindex = pstartindex;
		endindex = pendindex;
		this.hr=hr;
		indata= pdatatoprocess;
		hrsize = phrsize;
		this.SUMSDNN = sumsdnn;
	}



	@Override
	protected String doInBackground(float[][]... params) {
		String reps=null;
		try{
			HRMax=0;
			HRMean=0;
			NNMax=0;
			NNMean=0;
			SDNN=0;
			RMSSD=0;
			TP=0;
			LF=0;
			HF=0;
			lfhf=0;
			int recordCount=0;
			//如果要计算全部的SDNN recordCount = hrsize
			if(endindex<startindex){
				recordCount = endindex + hrsize - startindex;
			}
			else
				recordCount = endindex - startindex;

			float[][] data = new float[recordCount][2];
			//这里是把截取的部分值放入另一个数组
			for(int k=0;k<recordCount;k++){
				data[k][0]=indata[(k+startindex)%hrsize][0];
				data[k][1]=indata[(k+startindex)%hrsize][1]-indata[startindex][1];

			}
			//calculate HRMax, HRMean, NNMean, NNMax, SDNN
			float nn=0;
			float nnsum=0;
			float hrsum=0;
			float hr=0;
			//hrsum等于所有的hr值的和
			for(int k=0; k<recordCount;k++){
				hrsum +=data[k][0];
				nn=(float)60.0/data[k][0];
				nnsum +=nn;
				if(nn>NNMax)NNMax=nn;
				if(data[k][0]>HRMax)HRMax=data[k][0];
			}
			HRMean = hrsum/recordCount;
			NNMean = 60/HRMean;
			float sdnnsum=0;

			for(int k=0; k<recordCount;k++){
				sdnnsum += (60/data[k][0]-NNMean)*(60/data[k][0]-NNMean);
			}
			SDNN =1000*(float)Math.sqrt(sdnnsum/recordCount);


			double NNDiffSum=0;
			for(int k=0; k<recordCount-1;k++){
				NNDiffSum +=(60/data[k+1][0]-60/data[k][0])*(60/data[k+1][0]-60/data[k][0]);
			}
			RMSSD = 1000*(float)Math.sqrt(NNDiffSum/(recordCount-1));

			NNMean = 1000*NNMean;
			NNMax = 1000*NNMax;			//if data is one minute 
			//if data is five minutes

			float minutes = (indata[endindex][1]-indata[startindex][1])/(60000);
			int N=0;
			double itplrate=0.0;
			if(minutes>0 && minutes<2){//it is minute data
				analysistime=1;
				N=1024;
				itplrate=minutes*60.0/1024.0;

			}else if(minutes>2 && minutes<6){
				analysistime=5;
				N=4096;
				itplrate=minutes*60/4096;
			}

			//Log.d(TAG,"N value ="+N);
			//int n = recordcnt;
			double xx[] = new double[recordCount];
			double ff[] = new double[recordCount];
			double s, t;

			// Assign values for data arrays x and f. 
			//System.out.println("Spline.java function definition");
			for(int i=0; i<recordCount; i++)
			{
				xx[i] = data[i][1]/1000.0;
				ff[i] = 60.0/(data[i][0]);
			}
			// Calculate coefficients defining the cubic spline. 
			WikiCubicSpline s11 = new WikiCubicSpline();
			//int mynn=(int)(X/itplrate*1024.0/1024.0);
			//		System.out.println("Number of points="+nn);
			double xs[]=new double[N];
			double in=60.0/data[0][0];
			for(int i=0; i<N; i++)
			{
				t =itplrate*i+in;
				xs[i] = t;
				//		 System.out.println("x="+f1.format(t)+",  f(x)="+f1.format(s));
			}

			double[] result = s11.FitAndEval(xx, ff, xs, 0, 0, false);

			//System.out.println("interpolated values");

			//calculate mean
			double isum=0;
			for(int i=0; i<N; i++)
			{
				isum +=result[i];
				// System.out.println(xs[i]+","+result[i]);
			}
			double mean = isum/N;
			for(int i=0; i<N; i++)
			{
				result[i] = result[i]-mean;
				// System.out.println(xs[i]+","+result[i]);
			}


			//int NFFT=(int)Math.pow(2.0,log2(N)+1);
			//System.out.println ("NFFT="+NFFT);
			int NFFT=N;
			double RRFFTdata[]=new double[2*NFFT+1];

			// Storing x(n) in a complex array to make it work with four1. 
			//This is needed even though x(n) is purely real in this case. 
			for(int i=0; i<N; i++)
			{
				RRFFTdata[2*i+1] =result[i]; //make it ms.
				RRFFTdata[2*i+2] = 0.0;
			}
			// pad the remainder of the array with zeros (0 + 0 j)
			for(int i=N; i<NFFT; i++)
			{
				RRFFTdata[2*i+1] = 0.0;
				RRFFTdata[2*i+2] = 0.0;
			}

			//printf("\nInput complex sequence (padded to next highest power of 2):\n");
			for(int i=0; i<NFFT; i++)
			{
				//	System.out.println("RRFFTdata["+i+"]("+RRFFTdata[2*i+1]+"+j "+RRFFTdata[2*i+2]+")");
				//printf("x[%d] = (%.2f + j %.2f)\n", i, X[2*i+1], X[2*i+2]);
			}
			PowerSpectrum spe=new PowerSpectrum(NFFT, 3 , 1.0);
			//  public powerSpectrum(int in_nn, int in_winNum, double in_scale) {
			spe.transform(RRFFTdata);
			//print out x[i] and Spectrum[i]

			//TP, calculate LF/HF
			double pwr=0;
			double frq=0;
			double others=0;
			//System.out.println("x, spectrum");
			for(int i=0;i<NFFT/2;i++){
				pwr = spe.spectrum[i];
				//frequency f = i/(NFFT*delta)  delta = 0.25s for 4hz resampling rate. 
				//if((double)i/(itplrate*NFFT)<0.5)
				//   System.out.println((double)i/(itplrate*NFFT) + "," + spe.spectrum[i]);
				TP+=pwr;
				frq=(double)i/(itplrate*NFFT);
				if(frq>0.04 && frq<0.15)LF+=pwr;
				else if(frq>0.15 && frq<0.4)HF+=pwr;
				else
					others +=pwr;
			}
			lfhf = LF/HF;
			//Log.d(TAG, "LF/HF="+lfhf);
		}catch(Exception e){
			e.printStackTrace();
		}

		return reps;

	}

	@Override
	protected void onPostExecute(String result) {
		callback.onTaskComplete(new HRVData(5,0,hr,HRMax,HRMean,NNMax,NNMean,SDNN,RMSSD,LF/HF,LF,HF,TP,SUMSDNN));
		// callback.onTaskComplete(analysistime,startindex,endindex,hr,HRMax, HRMean, NNMax, NNMean, SDNN, RMSSD, TP, LF,HF);
	}

	static int log2(int x) {
		int pow = 0;
		if(x >= (1 << 16)) { x >>= 16; pow +=  16;}
		if(x >= (1 << 8 )) { x >>=  8; pow +=   8;}
		if(x >= (1 << 4 )) { x >>=  4; pow +=   4;}
		if(x >= (1 << 2 )) { x >>=  2; pow +=   2;}
		if(x >= (1 << 1 )) { x >>=  1; pow +=   1;}
		return pow;
	}
}
