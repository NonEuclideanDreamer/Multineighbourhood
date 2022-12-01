import java.util.Random;

public class Rule //3states!!
{	public static int eight;
	public static String type,geometry;
	public static String relation;
	public static boolean adjacent=true;
	public static int x,y,z,offset,multiplicity=2,w=GameOfRose.w,t=GameOfRose.t;
	private boolean[][]used, tabbed;//To check wether the rule is used to its fullest. change for multiplicity
	public int[][] rule;
	public static String random2rule(int[][] out, int eight)
	{
		Random rand=new Random();
		int n;
		String name=("(");
		for(int i=0;i<multiplicity;i++)
		{int j;
			name=name+("(");
			for(j=0;j<eight+1;j++)
			{

			
					if(j>0)
						name+=(",");
					n=rand.nextInt((eight+1)*(multiplicity-1));
					if(n<multiplicity)
						out[i][j]=n;
					
					else if(n<2*multiplicity-1)
						out[i][j]=i;
					name+=(out[i][j]);
					
			}
			name+=(")");
			if(i<multiplicity-1)name+=(",");

		}
		name+=(")");System.out.println(name);
		return name;
	}
	
	public static void next(Rule regula, int[]index)
	{
		while(true)
		{
			if(regula.rule[index[0]][index[1]]==multiplicity-1)
			{
				regula.rule[index[0]][index[1]]=0;
				if(index[1]==regula.eight)
				{
					index[0]++;index[1]=0;
				}
				else
				{
					index[1]++;
				}
			}
			else{regula.rule[index[0]][index[1]]++; index[0]=0;index[1]=0;	return;}
		}
	}
	public static int[][][] random3rule(int eight)
	{
		Random rand=new Random();
		int n;
		int[][][]out=new int[multiplicity][eight+1][eight+1];
		System.out.print("{");
		for(int i=0;i<multiplicity;i++)
		{
			System.out.print("{");
			for(int j=0;j<eight+1;j++)
			{
				System.out.print("{");
				int k;
				for( k=0;k+j<eight+1;k++)
				{
					if(k>0)
						System.out.print(",");
					n=rand.nextInt(9*(multiplicity-1));
					if(n<multiplicity)
						out[i][j][k]=n;
					else if(n<2*multiplicity-1)
						out[i][j][k]=i;
					System.out.print(out[i][j][k]);
					
				}
				while(k<eight+1)
				{
					System.out.print(",0");
						k++;
				}
				System.out.print("}");
				if(j<eight)System.out.print(",");
			}
			System.out.print("}");
			if(i<multiplicity-1)System.out.println(",");

		}
		System.out.println("}");
		return out;
	}
	
	public static int[][][][]random4rule()
	{
		int n;Random rand=new Random();
		int[][][][]out=new int[4][eight+1][eight+1][eight+1];
		
		for(int i=0;i<multiplicity;i++)
		{
		
			for(int j=0;j<eight+1;j++)
			{
				
				int k;
				for( k=0;k+j<eight+1;k++)
				{
				
					for(int l=0;j+k+l<eight+1;l++)
					{
						n=rand.nextInt(9*(multiplicity-1));
						if(n<multiplicity)
							out[i][j][k][l]=n;
						else if(n<2*multiplicity-1)
							out[i][j][k][l]=i;
						
					}
					
				}
				
				
			}
			

		}
		
		return out;
		
	}
	
	public State stiterate(State current, int[][] rule,boolean abort)
	{
		boolean changes=false; //To be able to abort if we reach a constant state
		int last;
		int[][] state=new int[current.state.length][current.state[0].length];
	//	System.out.println("next State:");
		for(int i=0;i<state.length;i++)
		{
			for(int j=0;j<state[0].length;j++)
			{
				int[] counter=new int[multiplicity];
				int[][] neighbours=neighbours(i,j);
				
				for(int k=0;k<eight;k++)
				{
					try {counter[current.state[neighbours[k][0]][neighbours[k][1]]]++;}
					catch(ArrayIndexOutOfBoundsException e){/*System.out.println("at boundary");*/}//curst2todo
				}
				last=state[i][j];
				state[i][j]=rule[current.state[i][j]][counter[1]];
				if(last!=state[i][j])
					changes=true;
			}
			//System.out.println();
		}
		abort=changes;
		return new State(state, current.colors);
	}
	
	public static State penterate(State current, int[][] rule, Penrose rose)
	{//System.out.println("w="+w);
		int[][] state=new int[current.state.length][current.state[0].length];
		for(int i=0;i<w;i++)
		{
				int[] counter=new int[multiplicity];
				int[][] neighbours=Playfield.roseNeighbours(new int[] {i,0},rose);
				//ToDo
				for(int k=0;k<eight;k++)
				{
					try {counter[current.state[neighbours[k][0]][neighbours[k][1]]]++;}
					catch(ArrayIndexOutOfBoundsException e){/*System.out.println("at boundary");*/}//curst2todo
				}
				state[i][0]=rule[current.state[i][0]][counter[1]];
				
		//	System.out.println("wide "+i+" live neighbours:"+counter[1]);
		}
		for(int i=0;i<t;i++)
		{
				int[] counter=new int[multiplicity];
				int[][] neighbours=Playfield.roseNeighbours(new int[] {i,1},rose);
				//ToDo
				for(int k=0;k<eight;k++)
				{
					try {counter[current.state[neighbours[k][0]][neighbours[k][1]]]++;}
					catch(ArrayIndexOutOfBoundsException e){/*System.out.println("at boundary");*/}//curst2todo
				}
				state[i][1]=rule[current.state[i][1]][counter[1]];
				
			//System.out.println();
		}
		return new State(state, current.colors);
	}
	public State penterate(State current, int[][][] rule, Penrose rose, boolean abort)
	{//System.out.println("w="+w);
		int[][] state=new int[current.state.length][current.state[0].length];
		boolean changes=false; //To be able to abort if we reach a constant state
			int last;
		for(int i=0;i<w;i++)
		{
			
				int[] counter=new int[multiplicity];
				int[][] neighbours=Playfield.roseNeighbours(new int[] {i,0},rose);
				//ToDo
				for(int k=0;k<eight;k++)
				{
					try {counter[current.state[neighbours[k][0]][neighbours[k][1]]]++;}
					catch(ArrayIndexOutOfBoundsException e){/*System.out.println("at boundary");*/}//curst2todo
				}
				last=state[i][0];
				state[i][0]=rule[current.state[i][0]][counter[1]][counter[2]];
				if(last!=state[i][0])
				{
				changes=true;
				}
			used[current.state[i][0]][counter[1]][counter[2]]=true;
			/*used[1][counter[1]][counter[2]]=true;//Like that, the type of the cell itself doesn't matter, only the neighbours
			used[2][counter[1]][counter[2]]=true;*/
				
		//	System.out.println("wide "+i+" live neighbours:"+counter[1]);
		}
		for(int i=0;i<t;i++)
		{
				int[] counter=new int[multiplicity];
				int[][] neighbours=Playfield.roseNeighbours(new int[] {i,1},rose);
				//ToDo
				for(int k=0;k<eight;k++)
				{
					try {counter[current.state[neighbours[k][0]][neighbours[k][1]]]++;}
					catch(ArrayIndexOutOfBoundsException e){/*System.out.println("at boundary");*/}//curst2todo
				}
				last=state[i][1];
				state[i][1]=rule[current.state[i][1]][counter[1]][counter[2]];
				if(last!=state[i][1])
				{
				changes=true;
				}
			used[current.state[i][1]][counter[1]][counter[2]]=true;
			/*used[1][counter[1]][counter[2]]=true;//Like that, the type of the cell itself doesn't matter, only the neighbours
			used[2][counter[1]][counter[2]]=true;*/
			//System.out.println();
		}
		if(!changes)abort=true;
		return new State(state, current.colors);
	}
	public static State penterate(State current, int[][][][]rule,Penrose rose)
	{
		int[][] state=new int[current.state.length][current.state[0].length];
		for(int i=0;i<w;i++)
		{
				int[] counter=new int[multiplicity];
				int[][] neighbours=Playfield.roseNeighbours(new int[] {i,0},rose);
				//ToDo
				for(int k=0;k<eight;k++)
				{
					try {counter[current.state[neighbours[k][0]][neighbours[k][1]]]++;}
					catch(ArrayIndexOutOfBoundsException e){/*System.out.println("at boundary");*/}//curst2todo
				}
				state[i][0]=rule[current.state[i][0]][counter[1]][counter[2]][counter[3]];
				
		//	System.out.println("wide "+i+" live neighbours:"+counter[1]);
		}
		for(int i=0;i<t;i++)
		{
				int[] counter=new int[multiplicity];
				int[][] neighbours=Playfield.roseNeighbours(new int[] {i,1},rose);
				//ToDo
				for(int k=0;k<eight;k++)
				{
					try {counter[current.state[neighbours[k][0]][neighbours[k][1]]]++;}
					catch(ArrayIndexOutOfBoundsException e){/*System.out.println("at boundary");*/}//curst2todo
				}
				state[i][1]=rule[current.state[i][1]][counter[1]][counter[2]][counter[3]];
				
			//System.out.println();
		}
		return new State(state, current.colors);
	}
	
	public State iterate(State current, int[][][][] rule, boolean abort)
	{
		boolean changes=false; //To be able to abort if we reach a constant state
		int last;

		int[][] state=new int[current.state.length][current.state[0].length];
	//	System.out.println("next State:");
		for(int i=0;i<state.length;i++)
		{
			for(int j=0;j<state[0].length;j++)
			{
				int[] counter=new int[multiplicity];
				int[][] neighbours=neighbours(i,j);
				for(int k=0;k<8;k++)
				{
					try {counter[current.state[neighbours[k][0]][neighbours[k][1]]]++;}
					catch(ArrayIndexOutOfBoundsException e){/*System.out.println("at boundary");*/}//curst2todo
				}
				last=state[i][j];
				state[i][j]=rule[current.state[i][j]][counter[1]][counter[2]][counter[3]];
				if(last!=state[i][j])
				{
				changes=true;
				}
			used[current.state[i][j]][counter[1]]/*[counter[2]][counter[3]][counter[4]]*/=true;
			}
			//System.out.println();
		}
		
		return new State(state, current.colors);
	}
	
	public State iterate(State current, int[][][] rule, boolean abort)
	{
		boolean changes=false; //To be able to abort if we reach a constant state
		int last;

		int[][] state=new int[current.state.length][current.state[0].length];
	//	System.out.println("next State:");
		for(int i=0;i<state.length;i++)
		{
			for(int j=0;j<state[0].length;j++)
			{
				int[] counter=new int[multiplicity];
				int[][] neighbours=neighbours(i,j);
				for(int k=0;k<8;k++)
				{
					try {counter[current.state[neighbours[k][0]][neighbours[k][1]]]++;}
					catch(ArrayIndexOutOfBoundsException e){/*System.out.println("at boundary");*/}//curst2todo
				}
				last=state[i][j];
				state[i][j]=rule[current.state[i][j]][counter[1]][counter[2]];
				if(last!=state[i][j])
				{
				changes=true;
				}
			used[current.state[i][j]][counter[1]][counter[2]]/*[counter[3]][counter[4]]*/=true;			
			tabbed[current.state[i][j]][counter[1]][counter[2]]/*[counter[3]][counter[4]]*/=true;

			}
			//System.out.println();
			abort=!changes;
		}
		
		return new State(state, current.colors);
	}
	public static State iterate(State current, int[][][][]rule)
	{
		int[][] state=new int[current.state.length][current.state[0].length];
		//	System.out.println("next State:");
			for(int i=0;i<state.length;i++)
			{
				for(int j=0;j<state[0].length;j++)
				{
				
					int[] counter=new int[multiplicity];
					int[][] neighbours=neighbours(i,j);
					for(int k=0;k<eight;k++)
					{
						try {counter[current.state[neighbours[k][0]][neighbours[k][1]]]++;}
						catch(ArrayIndexOutOfBoundsException e){/*System.out.println("at boundary");*/}//curst2todo
					}
					state[i][j]=rule[current.state[i][j]][counter[1]][counter[2]][counter[3]];
				}
				//System.out.println();
			}
			
			return new State(state, current.colors);
	}
	
	public State iterate(State current, int[][] rule,boolean abort)
	{

		boolean changes=false; //To be able to abort if we reach a constant state
		int last;
		int[][] state=new int[current.state.length][current.state[0].length];
		//	System.out.println("next State:");
			for(int i=0;i<state.length;i++)
			{
				for(int j=0;j<state[0].length;j++)
				{
				
					int counter=0;
					int[][] neighbours=neighbours(i,j);
					for(int k=0;k<eight;k++)
					{
						try {counter+=current.state[neighbours[k][0]][neighbours[k][1]];}
						catch(ArrayIndexOutOfBoundsException e){/*System.out.println("at boundary");*/}//curst2todo
					}
					last=state[i][j];
					state[i][j]=rule[current.state[i][j]][counter]/*[counter[2]][counter[3]][counter[4]]*/;
					tabbed[current.state[i][j]][counter]=true;
					if(last!=state[i][j])
						{
						changes=true;
						}
					used[current.state[i][j]][counter]/*[counter[2]][counter[3]][counter[4]]*/=true;
				
				}
				//System.out.println();
			}
			abort=!changes;
			return new State(state, current.colors);
	}
	public static int[][] conway() 
	{
		return new int[][] {{0,0,0,1,0,0,0,0,0},{0,0,1,1,0,0,0,0,0}};
	}
	public static int[][] neighbours(int i, int j)
	{
		int[][]neighbours=new int[eight][2];
		if(Playfield.geometry.equals("hive"))
		{
			neighbours=Playfield.nthHoneyNeighbours(new int[] {i,j},Integer.parseInt(type.substring(0,2)),Integer.parseInt(type.substring(2,4)));
		}
		else if(Playfield.geometry.equals("z2"))
		{
			neighbours=Playfield.nthZ2Neighbours(new int[] {i,j},Integer.parseInt(type.substring(0,2)),Integer.parseInt(type.substring(2,type.length())));

		}
	
			else neighbours=Playfield.z2neighbours(new int[] {i,j});
		
			
			
			
		for(int n=0;n<neighbours.length;n++)
		{
			neighbours[n]=Playfield.funDo(neighbours[n][0],neighbours[n][1]);
		}
				
		
			
			
		return neighbours;
	}
	public static void print(int[][]t)
	{
		System.out.print("(");
		for(int i=0;i<t.length;i++)
		{
			System.out.print("(");
			for(int j=0;j<t[0].length;j++)
			//	for(int k=0;k<eight+1;k++)
			{
				System.out.print(t[i][j]/*[k]*/+",");
			}
			System.out.println("),");
		}
		System.out.println(")");
	}
	public void print()
	{
		System.out.print("(");
		for(int i=0;i<multiplicity;i++)
		{
			System.out.print("(");
			for(int j=0;j<eight+1;j++)
				for(int k=0;k<eight+1;k++)
			{
				System.out.print(tabbed[i][j]/*[k]*/+",");
			}
			System.out.println("),");
		}
		System.out.println(")");
	}
	public static boolean validIndex(int index)
	{
		int sum=0;
		for(int i=1;i<multiplicity;i++)
		{
			sum+=(index/Math.pow(eight+1, i-1))%(eight+1);
		}
		return (sum<eight+1);
	}
	public Rule(int[][][][] rule)
	{
		used=new boolean[multiplicity][eight+1][eight+1][eight+1];

		for(int i=0;i<multiplicity;i++)
			{for(int j=0;j<eight+1;j++)
				{for(int k=0;k<eight+1;k++)
					{for(int l=0;l<eight+1;l++)
						//{for(int m=0;m<eight+1;m++)
							{used[i][j][k][l]=(rule[i][j][k][l]==0);}}}}
	}
	public Rule(int[][][] rule)
	{
		used=new boolean[multiplicity][eight+1][eight+1];
		tabbed=new boolean[multiplicity][eight+1][eight+1];

		for(int i=0;i<multiplicity;i++)
			{for(int j=0;j<eight+1;j++)
				{for(int k=0;k<eight+1;k++)
					/*{for(int l=0;l<eight+1;l++)
						{for(int m=0;m<eight+1;m++)*/
							{used[i][j][k]=(rule[i][j][k]==0);tabbed[i][j][k]=false;}}}//}}
	}
	
	public Rule(int[][] rule)
	{
		used=new boolean[multiplicity][eight+1];
		tabbed=new boolean[multiplicity][eight+1];
		for(int i=0;i<multiplicity;i++)
			{for(int j=0;j<eight+1;j++)
					{used[i][j]=(rule[i][j]==0);tabbed[i][j]=false;}}
	}
	
	
	public boolean fullyValid() //To change with multiplicity
	{
		for(int i=0;i<multiplicity;i++)
			for(int j=0;j<eight+1;j++)
				/*for(int k=0;k<eight+1;k++)
					for(int l=0;l<eight+1;l++)
						for(int m=0;m<eight+1;m++)*/
							{{{{{if(used[i][j]/*[k][l]*/==false)return false;}}}}}
							
		return true;
	}

	public static void next(int[][][] rule, int[] index) 
	{
		while(true)
		{
			if(rule[index[0]][index[1]][index[2]]==multiplicity-1)
			{
				rule[index[0]][index[1]][index[2]]=0;
				if(index[2]+index[1]==eight)
				{
					if(index[1]==eight)
					{
						index[0]++;index[1]=0;index[2]=0;
					}
					else
					{
						index[1]++;index[2]=0;
					}
				}
				else
				{
					index[2]++;
				}
			}
			else{rule[index[0]][index[1]][index[2]]++; index[0]=0;index[1]=0;index[2]=0;	return;}
		}
		
	}

	public static State iterate(State s1, State s2, int[][][] rule1, int[][][] rule2, boolean abort) 
	{
		boolean changes=false; //To be able to abort if we reach a constant state
		int last;

	//	System.out.println("next State:");
		for(int i=0;i<s1.state.length;i++)
		{
			for(int j=0;j<s1.state[0].length;j++)
			{
				int[] counter=new int[multiplicity];
				int[][] neighbours=neighbours(i,j);
				for(int k=0;k<8;k++)
				{
					try {counter[s1.state[neighbours[k][0]][neighbours[k][1]]]++;}
					catch(ArrayIndexOutOfBoundsException e){/*System.out.println("at boundary");*/}//curst2todo
				}
				last=state[i][j];
				state[i][j]=rule[current.state[i][j]][counter[1]][counter[2]];
				if(last!=state[i][j])
				{
				changes=true;
				}
			used[current.state[i][j]][counter[1]][counter[2]]/*[counter[3]][counter[4]]*/=true;			
			tabbed[current.state[i][j]][counter[1]][counter[2]]/*[counter[3]][counter[4]]*/=true;

			}
			//System.out.println();
			abort=!changes;
		}
		
		return new State(state, current.colors);
	}

	public static void print(int[] t)
	{
		System.out.print("(");
		for(int i=0;i<t.length;i++)
		{
			//System.out.print("(");
			//for(int j=0;j<t[0].length;j++)
			//	for(int k=0;k<eight+1;k++)
			{
				System.out.print(t[i]/*[j][k]*/+",");
			}
			//System.out.println("),");
		}
		System.out.println(")");
	}

	public static void print(double[] t) 
	{
		System.out.print("(");
		for(int i=0;i<t.length;i++)
		{
			//System.out.print("(");
			//for(int j=0;j<t[0].length;j++)
			//	for(int k=0;k<eight+1;k++)
			{
				System.out.print(t[i]/*[j][k]*/+",");
			}
			//System.out.println("),");
		}
		System.out.println(")");
	}
}

//cool r2-rules:
//{{1,0,0,0,0,0,0,1,0},{0,0,0,0,0,0,0,0,1}}