import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Random;
//274
//(3.349999999999972,3.349999999999972,-10.0,)
public class Neighbourhood 
{ 
	static boolean reset=false;//Whether or not the state should reset to the original initial state after the rule change
	static int it=200,// How many iterations should be done with no rule change
			n=2;//How far out do you look at the neighbourhoods
	
	static DecimalFormat df=new DecimalFormat("00");
	static int c=6, nr=n*(n+1)+1;
	public static String geometry="z2", type="z2";
	public static int start=0, t=start, ten=it, multiplicity=3,seesize=216,reach=2,fieldsize=1*(seesize/*+it*reach*/),
			width=29, offset=43,height=53,hook=0;
	static int[]eights= {24};//size of a neighbourhood(different sizes possible)
	public static double maximpuls=0.6/*/(n*n+n+2)*//nr/eights[0], maxa=2.5;//24.0/nr/eights[0];
	public static int[] colors= {new Color(1*255/c,0*255/c,5*255/c).getRGB(),new Color(5*255/c,1*255/c,0*255/c).getRGB(),new Color(0,5*255/c,1*255/c).getRGB()/*,new Color(0,102,102).getRGB(), new Color(0,255,255).getRGB()*/};
	 static String[]types= {"3820","90-9","9274","3921"};
	static double[] a0=new double[2+n*(n+1)],//
			impuls=new double[2+n*(n+1)]; //
	static double blurRatio=0.5,prob=0.7,prob1=0.001;
	final static double speed=1.0/nr/eights[0]/6; 
	public static String name0="multinghb_"+geometry+n+"_";
	public static boolean abort=false;
	
	public static void main(String[] args)
	{
		types=allTypes(n);
		State.width=1080;State.height=1080;
		
		//For honey:
		if(geometry.equals("hive"))
		{Playfield.cellsize=State.height*2/Math.sqrt(3)/seesize;
		System.out.println("Cellsize="+Playfield.cellsize);
		double w=(State.height/Math.sqrt(3)+State.width)/Playfield.cellsize;
		State.max=new int[]{(int)((fieldsize+w+1)/2),(fieldsize+seesize+1)/2};
		State.min=new int[] {(int)((fieldsize-w)/2),((fieldsize-seesize)/2)};
		}
		//For z2:
		else
		{
			Playfield.cellsize=State.width*1.0/seesize;
			State.max=new int[] {(fieldsize+seesize+1)/2,(int)(fieldsize+seesize*State.height*1.0/State.width+1)/2};
			State.min=new int[] {(fieldsize-seesize)/2,(int)(fieldsize-seesize*State.height*1.0/State.width)/2};
		}
		Playfield.height=height;
		Rule.multiplicity=multiplicity; Rule.geometry=geometry;
		Rule.x=width; Playfield.width=width; Playfield.geometry=geometry; Playfield.type=type;Rule.type=type;
		Rule.offset=offset; Playfield.offset=offset;
		Rule.y=height; 
		Rule.z=hook;  
		Playfield.statewidth=fieldsize;
		Playfield.stateheight=fieldsize; 
		
		
		//Starting condition
		int l=5, x=fieldsize/2,y=fieldsize/2,k=start;
		//s.addHLine(2*l+1, x-l, y, 1);
		State s=distRandom();State	s1= s.copy();
		a0=randomA();
	
		for(int counter=0;counter<1000;counter++)
		{
		//if you want to reset for each rulechange:
		if(reset)	s1=s.copy();
		System.out.println(k);
		double[]a=new double[types.length+1]; //{1,-1,1,-1,0,-1,1,0};
		for(int i=0;i<a.length;i++)
		{	int m=1; if(i==0||i==a.length-1)m=eights[0];
			a0[i]+=impuls[i];
			a0[i]/=Math.max(1, Math.abs(a0[i]/maxa/m));
			a[i]=a0[i];
			
		}
	//	s1.draw(name0+start, colors[0]);
		Rule.print(a);
		while(t<start+it)
		{
			t++;
			
			int r=0;
			iterate(s1,a);
			//Rotating rule:
			/*	for(int i=2;i<5;i++)
				{for(int j=0;j<i;j++)
					a[2+r+j]=a0[2+r+(j+t)%i];
					r+=i;
				}*/
			/*int change=rand.nextInt(a.length);
			impuls[change]+=Math.signum(1-rand.nextDouble()*2-analyze(a));
			normalize(impuls);
			a=add(a,impuls);*/	
			if(t%ten==0)	
			{s1.draw(name0+k, colors[0]);
			k++;
			}
			
		//System.out.println(t);
		}t=start;chooseImpuls(s1,n);
		}
}
	private static double[] randomA() {
		double[]out=new double[2+n*(n+1)];
		Random rand=new Random();
		for(int i=0;i<2+n*(n+1);i++)
		{
			int k=1; if(i==0||i==1+n*(n+1))k=4;
			out[i]=rand.nextDouble(-maxa,maxa)*k;
		}
		return out;
	}
	private static void randomSpecialA(State s1)//For individual changing
	{
		int k=1,start=(fieldsize-11)/2,st=fieldsize/2;//System.out.println("speed="+speed);
		for (int i=1;i<=n;i++)
		for (int j=i;j>-i;j--) 
		{
			int stretch=(i+j);
			if(j%stretch==0&&stretch<12&&stretch>0)
			{
				int translate=-j/stretch;
				if(translate>-1&&translate<3)
			//System.out.print("before="+impuls[k]+", state="+s1.state[start+i][st+j]);
			{int e=1; if(i==0||i==1+n*(n+1))e=eights[0];
			a0[k]=maxa*(1-s1.state[start+i][st+j]);
		//	System.out.println(impuls[k]);
			}}
			if(stretch%3==0&&j*3%stretch==0&stretch<36&&stretch>0)
			{
				stretch/=3;
				int translate=1-j/stretch;
				if(translate>-1&&translate<3)
					//System.out.print("before="+impuls[k]+", state="+s1.state[start+i][st+j]);
					{a0[k]=maxa*(1-s1.state[start+i-stretch][st+j-stretch]);
					}
			}
			k++;
		}
		impuls[0]+=eights[0]*speed*(1-s1.state[start][st]);impuls[0]/=Math.max(1, Math.abs(impuls[0]/maximpuls/4));
		impuls[k]+=eights[0]*speed*(1-s1.state[start][st+1]);impuls[k]/=Math.max(1, Math.abs(impuls[k]/maximpuls/4));
	}
	private static String[] allTypes(int n) 
	{
		String[]out=new String[1+n*(n+1)];
		out[0]="00000";int k=1;
		for(int i=1;i<=n;i++)for(int j=i;j>-i;j--)
		{
			out[k]=df.format(i)+df.format(j);
			k++;
		}
		return out;
	}
	private static void chooseSpecialImpulse(State s1, double[] impuls, double speed,double maximpuls)//For individual changing
	{
		int k=1,i0=0,j0=0,counter=0,c=0,start=(fieldsize-11)/2,st=fieldsize/2;//System.out.println("speed="+speed);
		for (int i=1;i<=n;i++)
		for (int j=i;j>-i;j--) 
		{
			
			if(j>-1&&counter<9)
			{
				if(c==0) {i0=i;j0=j;}
			//System.out.print("before="+impuls[k]+", state="+s1.state[start+i][st+j]);
			{impuls[k]+=speed*(1-s1.state[start+i0][st+j0]);
			impuls[k]/=Math.max(1, Math.abs(impuls[k]/maximpuls));}
			c++;
			if(j>0&&j<i) 
			{
				{impuls[k+2*j]+=speed*(1-s1.state[start+i0][st+j0]);
					impuls[k+2*j]/=Math.max(1, Math.abs(impuls[k+2*j]/maximpuls));}
				c++;
			}
			}
				if(c>4) {c=0;counter++;}
			k++;
		}
		impuls[0]+=eights[0]*speed*(1-s1.state[start][st]);impuls[0]/=Math.max(1, Math.abs(impuls[0]/maximpuls/4));
		impuls[k]+=eights[0]*speed*(1-s1.state[start][st+1]);impuls[k]/=Math.max(1, Math.abs(impuls[k]/maximpuls/4));
	}
	
	//looks at certain pixels in the state s1 and adjusts the impuls accordingly
	private static void chooseImpuls(State s1, int n) 
	{
		int k=1,start=(fieldsize-n)/2,st=fieldsize/2;
		for (int i=1;i<=n;i++)
		for (int j=i;j>-i;j--) 
		{
	
			impuls[k]+=speed*(1-s1.state[start+i][st+j]);
			impuls[k]/=Math.max(1, Math.abs(impuls[k]/maximpuls));
			k++;
		}
		impuls[0]+=eights[0]*speed*(1-s1.state[start][st]);impuls[0]/=Math.max(1, Math.abs(impuls[0]/maximpuls/4));
		impuls[k]+=eights[0]*speed*(1-s1.state[start][st+1]);impuls[k]/=Math.max(1, Math.abs(impuls[k]/maximpuls/4));
	}
	
	//Gives a random initial 3-values-state with probabilities changing over the playfield
	private static State distRandom() 
	{
		Random rand=new Random();
		State out=new State(fieldsize,fieldsize,(int[])colors.clone());
		for(int i=0;i<fieldsize;i++)
			for(int j=0;j<fieldsize;j++)
			{
				double[] p=new double[multiplicity];
				p[2]=Math.min(i*1.0, j)/fieldsize;
				p[1]=Math.max(i*1.0,j)/fieldsize-p[2];
				p[0]=1-p[2]-p[1];
				prob=rand.nextDouble();
				int k=0;
				while(prob>p[k])
				{
					//System.out.println(prob);
					prob-=p[k];
					k++;
				}
				out.state[i][j]=k;
			}
		return out;
	}	
	
	//Trying out my own pseudorandom generator, but it's not very random
	private static State distRandom(double seed) 
	{
		int t=0;
		State out=new State(fieldsize,fieldsize,(int[])colors.clone());
		for(int i=0;i<fieldsize;i++)
			for(int j=0;j<fieldsize;j++)
			{
				double[] p=new double[multiplicity];
				p[2]=Math.min(i*1.0, j)/fieldsize;
				p[1]=Math.max(i*1.0,j)/fieldsize-p[2];
				p[0]=1-p[2]-p[1];
				prob=Math.abs(100*Math.sin(t*seed))%1;t++;
				int k=0;
				while(prob>p[k])
				{
					//System.out.println(prob);
					prob-=p[k];
					k++;
				}
				out.state[i][j]=k;
			}
		return out;
	}
	private static void normalize(int[] b)
	{
		int sum=0;
		for(int i=0;i<b.length;i++)
			sum+=b[i]*b[i];
		if(sum>maximpuls)
			for(int i=0;i<b.length;i++)
				b[i]=(int) (b[i]*maximpuls/sum);
	}
	private static int[] add(int[] a, int[] b)
	{
		int[]out =new int[a.length];
		for(int i=0;i<a.length;i++)
		{
			out[i]=a[i]+b[i];
		}
		
		return out;
	}
	private static double analyze(int[]a)
	{
		int pos=0,neg=0;
		
		for(int i=0;i<a.length;i++)
		{
			if(a[i]>0)pos+=a[i]; else neg+=a[i];
		}
		return (pos+neg)/(double)Math.max(pos, -neg);
	}

	private static void iterate(State s, double[] a) 
	{	Random rand=new Random();
		int[][]out=new int[fieldsize][fieldsize];
		for(int i=0;i<fieldsize;i++)
			for(int j=0;j<fieldsize;j++)
			{	int[]loc=new int[] {i,j};
				double value=a[a.length-1];
				for(int k=0;k<a.length-1;k++)if(a[k]!=0)
				{	
					Rule.type=types[k];
					int[][] neighbours=Rule.neighbours(i,j);
					for(int l=0;l<neighbours.length;l++)
					{
					try {	value+=s.state[neighbours[l][0]][neighbours[l][1]]*a[k];}
					catch(ArrayIndexOutOfBoundsException w) {value+=(a[k]*(neighbours[l][0]+neighbours[l][1])/fieldsize);}
					}
				}
				if(value<-5)
				out[i][j]=0;
				else if (value >5)
					out[i][j]=2;
				else
					out[i][j]=1;
			}
		s.state=out;
	}
	//Interesting parameter values for hive
	static int[][]hiverules= {{-1,-1,-1,-1,-1,-1,0,1,1,1,1,1},{-1,-1,-1,-1,-1,-1,0,1,1,1,1,0},{-1,-1,-1,-1,-1,-1,0,1,1,1,1,-1},
							 {-1,-1,-1,-1,-1,-1,1,1,1,1,1,-1},{-1,-1,-1,-1,-1,-1,1,1,1,1,1,0},{-1,-1,-1,-1,-1,-1,1,1,1,1,1,1},
							 {-1,-1,-1,-1,-1,-1,1,1,1,1,0,1},{-1,-1,-1,-1,-1,-1,1,1,1,1,0,0},{-1,-1,-1,-1,-1,-1,1,1,1,1,0,-1},
							 {-1,-1,-1,-1,-1,0,1,1,1,1,0,-1}, {-1,-1,-1,-1,-1,0,1,1,1,1,0,0}, {-1,-1,-1,-1,-1,0,1,1,1,1,0,1},
							 {-1,-1,-1,-1,-1,0,1,1,1,1,-1,1}, {-1,-1,-1,-1,-1,0,1,1,1,1,-1,0},{-1,-1,-1,-1,-1,0,1,1,1,1,-1,-1},
							 {-1,-1,-1,-1,-1,1,1,1,1,1,-1,-1},{-1,-1,-1,-1,-1,1,1,1,1,1,-1,0},{-1,-1,-1,-1,-1,1,1,1,1,1,-1,1},
							 {-1,-1,-1,-1,-1,1,1,1,1,1,0,1},{-1,-1,-1,-1,-1,1,1,1,1,1,0,0},{-1,-1,-1,-1,-1,1,1,1,1,1,0,-1},
							 {-1,-1,-1,-1,-1,1,1,1,1,1,1,-1},{-1,-1,-1,-1,-1,1,1,1,1,1,1,0},{-1,-1,-1,-1,-1,1,1,1,1,1,1,1},
							 {-1,-1,-1,-1,-1,1,1,1,1,0,1,1},{-1,-1,-1,-1,-1,1,1,1,1,0,1,0},{-1,-1,-1,-1,-1,1,1,1,1,0,1,-1},
							 {-1,-1,-1,-1,-1,1,1,1,1,0,0,-1},{-1,-1,-1,-1,-1,1,1,1,1,0,0,0},{-1,-1,-1,-1,-1,1,1,1,1,0,0,1},
							 {-1,-1,-1,-1,-1,1,1,1,1,0,-1,1},{-1,-1,-1,-1,-1,1,1,1,1,0,-1,0},{-1,-1,-1,-1,-1,1,1,1,1,0,-1,-1},
							 {-1,-1,-1,-1,-1,1,1,1,1,-1,-1,-1},};
	static int[][] hiveas= {{0,1,0,1,1,-1,0,-1,-1,-1,1,0},/*{-1,1,0,1,1,-1,0,-1,-1,-1,1,0},{-1,0,0,1,1,-1,0,-1,-1,-1,1,0},{-1,0,0,0,1,-1,0,-1,-1,-1,1,0},{-1,0,0,0,0,-1,0,-1,-1,-1,1,0},{-1,0,0,0,0,0,0,-1,-1,-1,1,0},{-1,0,0,0,0,1,0,-1,-1,-1,1,0},{-1,0,0,0,0,1,-1,-1,-1,-1,1,0},
			*/{-1,0,0,0,0,1,-1,1,-1,1,1,1},
			{0,1,-1,1,1,0,-1,0,1,0,-1,-1},{0,1,1,1,-1,-1,0,1,0,-1,-1,-1},{1,1,1,1,1,0,1,0,-1,0,-1,0},{1,1,-1,0,1,0,-1,1,0,0,1,1},
			{0,-1,0,-1,1,1,0,-1,0,-1,1,1},{1,1,1,0,-1,-1,1,0,0,-1,-1,1},{1,1,1,1,0,-1,0,-1,0,-1,1,0},{0,0,-1,-1,-1,1,-1,-1,1,1,1,0},
			{-1,0,0,0,-1,1,0,0,0,-1,1,-1},{1,1,0,0,0,0,-1,1,-1,-1,0,1},{0,-1,1,1,0,0,1,1,-1,1,-1,-1},{1,0,1,0,-1,0,1,0,-1,1,1,0},
			{1,1,1,-1,1,1,-1,-1,1,0,-1,-1}/*37*/,{1,-1,1,-1,1,-1,-1,-1,1,0,1,1},{0,0,-1,1,0,-1,0,1,-1,0,1,0},{1,1,1,1,0,0,-1,1,1,1,1,-1}};
}