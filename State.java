import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class State 
{
	static int[] min= {0,0},max= {1,1};
	static int width,height,multiplicity;
	int [][] state;
	static double cellsize;
	int[] colors;
	
	public State(int x, int y,int[] colors0)
	{
		state=new int[x][y];
		colors=colors0;
		cellsize= Playfield.cellsize;//Math.min(width/(max[0]-min[0]), height/(max[1]-min[1]))+1;
	}
	
	
	public State(int[][] state0, int[] colors0)
	{
		state=state0;
		colors=colors0;
		cellsize=Playfield.cellsize+1;//Math.min(width/(max[0]-min[0]), height/(max[1]-min[1]))+1;
	}
	
	public void setColor(int i, int color)
	{
		colors[i]=color;
	}
	public void set(int[] point,int value)
	{
		state[point[0]][point[1]]=value;
	}
	
	public void addSquare(int size, int x,int y,int value)
	{
		addHLine(size,x,y,value);
		addHLine(size,x,y+size-1,value);
		addVLine(size,x,y,value);
		addVLine(size,x+size-1,y,value);
	}
	public void addDLine(int length, int xstart, int ystart, int value, boolean up)//left diagonal in hive when up==false
	{
		int sign=1,x,y; if(up)sign=-1;
		
		for(int i=0;i<length;i++)
		{
			x=xstart+i; y=ystart+sign*i;
			if(Math.min(x,y)>=0&&x<state.length&&y<state[0].length)
			{
				state[x][y]=value;
			}
		}
	}
	public void addHLine(int length, int xstart, int y,int value)//also works in Hive
	{
		if(y>=0 && y<state[0].length)
		for(int i=Math.max(0,xstart);i<Math.min(state.length, xstart+length);i++)
		{
			set(Playfield.funDo(i,y),value);
		}
	}
	public void addVLine(int length, int x, int ystart, int value)//right diagonal in hive
	{
		if(x>=0 && x<state.length)
		for(int j=Math.max(0, ystart);j<Math.min(ystart+length,state[0].length);j++)
		{
			set(Playfield.funDo(x,j),value);
		}
	}
	public void addDcross(int length,int xstart,int ystart,int value)
	{
		int x,y,y2; 
		
		for(int i=0;i<length;i++)
		{
			x=xstart+i; y=ystart+i;y2=ystart-i;
			if(Math.min(x,y)>=0&&x<state.length&&y<state[0].length)
			{
				set(Playfield.funDo(x, y),value);
			}
			if(Math.min(x,y2+length-1)>=0&&x<state.length&&y2+length-1<state[0].length)
			{
				set(Playfield.funDo(x, y2+length-1),value);
			}
		}
	}
	
	public void addDiamond(int length, int xstart, int ystart, int value)
	{
		int x,y,y2;
		for(int i=0;i<length+1;i++)
		{
			x=xstart+i;y=ystart-i;y2=ystart+i;
			
			set(Playfield.funDo(x, y),value);
			set(Playfield.funDo(x, y2),value);
			x=xstart+2*length-i;
			set(Playfield.funDo(x, y),value);
			set(Playfield.funDo(x, y2),value);
		}
	}
 
 	public void shadowDraw(BufferedImage image, double blurRatio)
	{
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
				image.setRGB(i, j, mixColors(image.getRGB(i, j),colors[0], blurRatio));
		for(int i=min[0];i<max[0];i++)
		{

		//	System.out.println("i="+i+", xloc=");
			for(int j=min[1];j<max[1];j++)
			{	
				double[]loc=locate(new int[] {i,j});
			//	double xloc=width*(loc[0]-min[0]+1)/(max[0]-min[0]+1);
			//	double yloc=height*(loc[1]-min[1]+1)/(max[1]-min[1]+1);		
				
				int color=colors[state[i][j]];if(state[i][j]!=0) {//System.out.println("j="+j+", i="+i+(loc[0])+", "+loc[1]);
				for(int x=(int)(loc[0]-cellsize/2+1);x<loc[0]+cellsize/2;x++)
				{											//System.out.println("x="+(x));
					int b=(int) Math.sqrt(cellsize*cellsize/4.0-(x-loc[0])*(x-loc[0]));
					for(int y=(int)(loc[1]-b+1);y<loc[1]+b;y++)
					{			
						//System.out.println("y="+(y));
						try{image.setRGB(x, y, color);}catch(ArrayIndexOutOfBoundsException e) {}
					}
		}}
	}
		}
	}
	
	public static int mixColors(int color1, int color2, double blurRatio)
	{
		Color col1=new Color(color1), col2=new Color(color2);
		Color out =new Color((int)(col1.getRed()*blurRatio+col2.getRed()*(1-blurRatio)),
						(int)(col1.getGreen()*blurRatio+col2.getGreen()*(1-blurRatio)),
						(int)(col1.getBlue()*blurRatio+col2.getBlue()*(1-blurRatio)));
		return out.getRGB();
	}
	public void randomState(int[] from, int[] to, double[] probability)
	{
		Random rand=new Random();
		
		for(int i=from[0]; i<to[0]; i++)
		{
			for(int j=from[1]; j<to[1];j++)
			{
				state[i][j]=0;
				double out=rand.nextDouble();
				while (out>probability[state[i][j]]) {out-=probability[state[i][j]]; state[i][j]++;}
			}
		}
	}
	public BufferedImage draw(String name, int background)
	{
		BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
				image.setRGB(i, j, background);
		for(int i=min[0];i<max[0];i++)
		for(int j=min[1];j<max[1];j++)
		{	
			double[]loc=locate(new int[] {i,j});	
			if(state[i][j]>0) 
			{	
				int color=colors[state[i][j]];
				for(int x=(int)(loc[0]-cellsize/2+1);x<loc[0]+cellsize/2;x++)
				{												
					int b=(int) Math.sqrt(cellsize*cellsize/4.0-(x-loc[0])*(x-loc[0]));
					for(int y=(int)(loc[1]-b+1);y<loc[1]+b;y++)
					{			
						try{image.setRGB(x, y, color);}catch(ArrayIndexOutOfBoundsException e) {}
					}
				}
			}
	
		}
		try 
		{
			ImageIO.write(image, "png", new File(name+".png"));
		} catch (IOException e) 
		{
			System.out.println("IOException: Problems drawing file "+name);
			e.printStackTrace();
		}
		return image;
	}
	public BufferedImage emptydraw(String name, int background)
	{
		BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
				image.setRGB(i, j, background);
		
		for(int i=min[0];i<max[0];i++)
		{
					//System.out.println("i="+i+", xloc="+xloc);
			for(int j=min[1];j<max[1];j++)
			{	
				double[]loc=locate(new int[] {i,j});
				double xloc=width*(loc[0]-min[0]+1)/(max[0]-min[0]+1);
				double yloc=height*(loc[1]-min[1]+1)/(max[1]-min[1]+1);		//System.out.println("j="+j+", yloc="+yloc);
				int color=colors[state[i][j]];
				for(int x=(int)(xloc-cellsize/2+1);x<xloc+cellsize/2;x++)
				{												//System.out.println("x="+(x+xloc));
					int b=(int) Math.sqrt(cellsize*cellsize/4-x*x);
					for(int y=(int)(yloc-b+1);y<yloc+b;y++)
					{			
					  // System.out.println("y="+(y+yloc));
						try{image.setRGB(x, y, color);}catch(ArrayIndexOutOfBoundsException e) {}
					}
				}
			}
		}
	
		return image;
	}
	
	public void spread()//Distribute pattern on torus et al
	{int[][]st=new int[state.length][state[0].length];
		for(int i=0;i<state.length;i++)
		{	
			for(int j=0;j<state[0].length;j++)
			{
				int[]pos=Playfield.funDo(i, j);
				st[i][j]=state[pos[0]][pos[1]];
			}
		}
		state=st;
	}

	public boolean notEmpty() 
	{
		for(int i=0;i<state.length;i++)
			{for(int j=0;j<state[0].length;j++)
				{if(state[i][j]!=0)
					return true;}}
		
		return false;
	}

//Drawing a 2-parameter State
	public BufferedImage draw(State s2, String name, int background) 
	{
		BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
				image.setRGB(i, j, background);
		
		for(int i=min[0];i<max[0];i++)
		{

			//System.out.println("i="+i+", xloc="+xloc);
	for(int j=min[1];j<max[1];j++)
	{	
		double[]loc=locate(new int[] {i,j});
		//double xloc=width*(loc[0]-min[0]+1)/(max[0]-min[0]+1);
		//double yloc=height*(loc[1]-min[1]+1)/(max[1]-min[1]+1);		//System.out.println("j="+j+", yloc="+yloc);
		int color=colors[state[i][j]];
		for(int x=(int)(loc[0]-cellsize/2+1);x<loc[0]+cellsize/2;x++)
		{												//System.out.println("x="+(x+xloc));
			int b=(int) Math.sqrt(cellsize*cellsize/4-x*x);
			for(int y=(int)(loc[1]-b+1);y<loc[1]+b;y++)
			{			
			  // System.out.println("y="+(y+yloc));
				try{image.setRGB(x, y, color);}catch(ArrayIndexOutOfBoundsException e) {}
			}
		}
	}
		}
		try 
		{
			ImageIO.write(image, "png", new File(name+".png"));
		} catch (IOException e) 
		{
			System.out.println("IOException: Problems drawing file "+name);
			e.printStackTrace();
		}
		return image;
	}
	double[]locate(int[]loc)
	{
		double[] out =new double[2];
		if (Playfield.geometry.equals("hive")) {out=Playfield.honeycombLocate(loc);}
		
		else if(Playfield.geometry.equals("z2")) out=Playfield.z2locate(loc);
		else out=new double[] {loc[0],loc[1]};
		return out;
	}


	
	//Special drawingfct for Langton and another one
	public void draw(String name, int background, boolean langton) 
	{
		BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
				image.setRGB(i, j, background);
		
		for(int i=min[0];i<max[0];i++)
		{

			//System.out.println("i="+i+", xloc="+xloc);
	for(int j=min[1];j<max[1];j++)
	{	int[] lo=new int[] {i,j};
		double[]loc=locate(lo);	
		if(state[i][j]>0||loc.equals(Langton.loc)) {	//Rule.print(loc);
		
	//	double xloc=width*(loc[0]-min[0]+1)/(max[0]-min[0]+1);
	//	double yloc=height*(loc[1]-min[1]+1)/(max[1]-min[1]+1);		//System.out.println("j="+j+", yloc="+yloc);
		int color=colors[state[i][j]];if(lo.equals(Langton.loc))color=colors[colors.length-1];
		for(int x=(int)(loc[0]-cellsize/2+1);x<loc[0]+cellsize/2;x++)
		{												//System.out.println("x="+(x+xloc));
			int b=(int) Math.sqrt(cellsize*cellsize/4.0-(x-loc[0])*(x-loc[0]));
			for(int y=(int)(loc[1]-b+1);y<loc[1]+b;y++)
			{			
			  // System.out.println("y="+(y+yloc));
				try{image.setRGB(x, y, color);}catch(ArrayIndexOutOfBoundsException e) {}
			}
		}}
	}
		}
		try 
		{
			ImageIO.write(image, "png", new File(name+".png"));
		} catch (IOException e) 
		{
			System.out.println("IOException: Problems drawing file "+name);
			e.printStackTrace();
		}
		
		
	}


	public void addGlider(int posx, int posy,int value) 
	{
		addHLine(3,posx,posy+2,value);
		state[posx+2][posy+1]=value;
		state[posx+1][posy]=value;
		
	}


	public void set(int k) 
	{
		for(int i=0;i<state.length;i++)
			for(int j=0;j<state[0].length;j++)
				state[i][j]=k;
		
	}


	public State copy() 
	{
		return new State(state.clone(),colors);
	}
}
