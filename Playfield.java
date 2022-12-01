//rp2 needs width & height

public class Playfield 
{
	static int statewidth,stateheight,w=GameOfRose.w,t=GameOfRose.t, width, height, offset, hook, hook2;
	public static String type,geometry="z2";
	public static double cellsize;	
	
	public static int[] funDo(int x, int y)//gives the corresponding point in the fundamental domain only done rp2,moebius,cone
	{
		int[]out=new int[] {x,y};

		if(type.equals("torus"))
		{
			int[]modulo=new int[2], div=new int[2], dim= {width,height} ;
			
			for(int i=0;i<2;i++)
			{
				div[i]=(out[i]+dim[i]*1000000)/dim[i]-1000000;
				modulo[i]=(out[i]+dim[i]*1000000)%dim[i];
				
				
				out[i]=modulo[i];
				
				if(i==0/*div[i]%2!=0*/)
				{	
					
					out[1]-=offset*div[i];
				}
			}
		}
		else if (type.equals("rp2"))
		{
			int[]modulo=new int[2], div=new int[2], dim= {width,height} ;
			
			for(int i=0;i<2;i++)
			{
				div[i]=(out[i]+dim[i]*100)/dim[i]-100;
				modulo[i]=(out[i]+dim[i]*100)%dim[i];
				
				
				out[i]=modulo[i];
				
				if(div[i]%2!=0)
				{	int j=1-i;
					out[j]=dim[j]-1-out[j];
				}
			}
		}
		else if(type.equals("moebius"))
		{
			int div=(x+width*100)/width-100,
					modulo=(x+width*100)%width;
			out[0]=modulo;
			if(div%2!=0)
				out[1]=stateheight-1-out[1];
			
			//Rule.print(new int[][]{out});
		}
		else if(type.equals("cone"))
		{ int w=statewidth/2,h=stateheight/2;//System.out.print("x="+x+", y="+y);
			if(x<=w&&y<h) {out[0]=2*w-x;out[1]=2*h-y;}
			else if(x<w) {out[0]=w-h+y;out[1]=h+w-x;}
			else if(y<=h&& x>w) {out[0]=w+h-y;out[1]=h-w+x;}
		}
		else if (type.equals("bicone"))
		{
			double start=width/2.0,end=height/2,w=end-start;
			int xd=(int) ((x-start+100*w) /w)-100,yd=(int) ((y-start+100*w)/w)-100; double xm=(x-start)-w*xd,ym=(y-start)-w*yd;
			//System.out.println(x+"="+xd+"*"+w+"+"+xm+","+y+"="+yd+"*"+w+"+"+ym);
			if(xd%2==0&&yd%2==0)
			{
				out[0]=(int)(xm);out[1]=(int) (ym);
			}
			else if(yd%2==0)
			{
				out[1]=(int)(w-xm-0.5); out[0]=(int) (ym);
			}
			else if(xd%2==0)
			{
				out[1]=(int)(xm);out[0]=(int)(w-ym-0.5);
			}
			else 
			{
				out[0]=(int)(w-xm-0.5);out[1]=(int)(w-ym-0.5);
			}
			if(width!=0&&xm==0&&ym==0)
			{
				if(xd%2==0&&yd%2==1)
				{
					out[1]=(int)w;
				}
				else if((yd%2==0)&&(xd%2==1))
				{
					out[0]=(int)w;
				}
			}		
			if(w%1!=0&&ym==0&&xm!=0)	
			{
				out[0]=0;
				if(xd%2==0)	out[1]=(int)(w-xm);
				else		out[1]=(int)xm;
			}
		}
		//System.out.println("fundDo("+x+","+y+")=("+out[0]+","+out[1]+")");
		return out;
	}
	public static int[][] funDo(int[][] loc)
	{
		int[][]out=new int[loc.length][2];
		for(int i=0;i<loc.length;i++)
		{
			out[i]=funDo(loc[i][0],loc[i][1]);
		}
		return out;
	}
	public static int[][]portalneighbours(int[] loc){}
	
	public static int[][] z2neighbours(int[] loc)
	{
		int[][] out= new int[][] {{loc[0]-1,loc[1]},{loc[0]-1,loc[1]-1},{loc[0],loc[1]-1},{loc[0]+1,loc[1]-1},
						{loc[0]+1,loc[1]},{loc[0]+1,loc[1]+1},{loc[0],loc[1]+1},{loc[0]-1,loc[1]+1}};
		return funDo(out);
	}
	public static int[][] roseNeighbours(int[] loc,Penrose rose)
	{
		int[][]out=new int[4][2];
		
		for(int i=0;i<4;i++)
		{
			int x;
			if(loc[1]==0)
				x=rose.wneighbours[loc[0]][i];
			else
				x=rose.tneighbours[loc[0]][i];
				if(x<w)
				{
					out[i][0]=x;
					out[i][1]=0;
				}
				else if(x<w+t)
				{
					out[i][0]=x-w;
					out[i][1]=1;
				}
				else
				{
					out[i][0]=0;
					out[i][1]=2;
				}
		}
		return out;
	}
	
	public static int[][] secondRoseNeighbours(int[] loc, Penrose rose)//gibt doppelt 2. Nachbarn doppelt an
	{
		int[][]out =new int[16][2];
		int[][]nb=roseNeighbours(loc,rose);
		for(int i=0;i<4;i++) 
		{
			int[][]nghb=roseNeighbours(nb[i],rose);
			for(int j=0;j<4;j++)
			{
				out[4*i+j]=nghb[j];
			}
		}
		return out;
	}
	
	
	public static void toKlein(int[][] out, int width, int hook, int height)
	{
		for(int i=0;i<out.length;i++)
		{
			if(out[i][0]<0)out[i]=new int[] {out[i][0]+width, 2*hook-out[i][1]};
			else if(out[i][0]>statewidth-1)out[i]=new int[] {out[i][0]-width,2*hook-out[i][1]};

			while(out[i][1]<0) out[i][1]+=height;
			while(out[i][1]>stateheight-1)out[i][1]-=height;
		}
	}
	public static int[][] nthZ2Neighbours(int[]loc,int n, int m)
	{
		if (n==0) return new int[][]{loc};
		return new int[][] {{loc[0]-n,loc[1]+m},{loc[0]+m,loc[1]+n},{loc[0]+n,loc[1]-m},{loc[0]-m,loc[1]-n}};
		
	}

	
	public static void toMobius(int[][] out, int width, int hook)
	{
		for(int i=0;i<out.length;i++)
		{
			if(out[i][0]<0)out[i]=new int[] {out[i][0]+width, stateheight+hook-out[i][1]};
			else if(out[i][0]>=statewidth-1)out[i]=new int[] {out[i][0]-width,stateheight+hook-out[i][1]};
			
		}
	}
	public static void toCone(int[][] out)
	{
		for(int i=0;i<out.length;i++)
		{
			while(out[i][0]<0) out[i]=new int[] {out[i][1],-1-out[i][0]};
			if(out[i][1]<0) out[i]=new int[] {-1-out[i][1],out[i][0]};
		}
	}
 	public static double[] z2locate(int[] loc)
	{
		return new double[] {(loc[0]-State.min[0]+0.5)*cellsize,(loc[1]-State.min[1]+0.5)*cellsize};
	}
	public static double[] honeycombLocate(int[]loc)
	{
		double h=State.max[1]-State.min[1];//*1.5*State.width/h-State.width/2.0,Math.sqrt(3)*3*loc[1]*State.width/h
		double[] out={(loc[0]+0.5*(loc[1]-State.min[1]+1)-h/2-State.min[0])*cellsize,(Math.sqrt(3)*(loc[1]-State.min[1])+1)/2*cellsize};
		return out;
	}
	public static double[] roseLocate(int[] loc,Penrose rose)
	{
		if(loc[1]==0)
			return rose.wide[loc[0]].center;
		
		else
			return rose.thin[loc[0]].center;
	}
	public static int[][] z22ndNeighbours(int[] loc) 
	{
		int[][] out=new int[][] {{loc[0]-2,loc[1]},{loc[0]-2,loc[1]+1},{loc[0]-2,loc[1]+2},{loc[0]-1,loc[1]+2},{loc[0],loc[1]+2},{loc[0]+1,loc[1]+2},
			{loc[0]+2,loc[1]+2},{loc[0]+2,loc[1]+1},{loc[0]+2,loc[1]},{loc[0]+2,loc[1]-1},{loc[0]+2,loc[1]-2},{loc[0]+1,loc[1]-2},
			{loc[0],loc[1]-2},{loc[0]-1,loc[1]-2},{loc[0]-2,loc[1]-2},{loc[0]-2,loc[1]-1}};
			
			return funDo(out);
	}
	public static void toTorus(int[][] out, int x, int offset, int y) 
	{
		
		for(int i=0;i<out.length;i++)
		{
			if(out[i][0]<0)out[i]=new int[] {out[i][0]+x, out[i][1]-offset};
			else if(out[i][0]>=statewidth-1)out[i]=new int[] {out[i][0]-x,out[i][1]+offset};
			
			if(out[i][1]<0)out[i][1]=out[i][1]+y;
			else if(out[i][1]>=stateheight-1)out[i][1]=out[i][1]-y;
		}
		
	}
	public static int[][] diamondNeighbours(int[] loc) 
	{
		return new int[][] {{loc[0]-2,loc[1]},{loc[0]-1,loc[1]-1},{loc[0],loc[1]-2},{loc[0]+1,loc[1]-1},{loc[0]+2,loc[1]},{loc[0]+1,loc[1]+1},{loc[0],loc[1]+2},{loc[0]-1,loc[1]+1}};
	}
	
	//***************************************************************************************************
	// Honey comb neighbourhoods
	//**************************************************************************************************
	public static int[][] nthHoneyNeighbours(int[] loc,int n,int k)
	{
		if(n==0)return new int[][] {loc};
		else return new int[][] {{loc[0]+n-k,loc[1]+k},{loc[0]-k,loc[1]+n},{loc[0]-n,loc[1]+n-k},
								{loc[0]+k-n,loc[1]-k},{loc[0]+k,loc[1]-n},{loc[0]+n,loc[1]+k-n}};
		
	}
}
