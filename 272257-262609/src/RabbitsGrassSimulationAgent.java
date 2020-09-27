import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.awt.Color;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that implements the simulation agent for the rabbits grass simulation.

 * @author
 */
public class RabbitsGrassSimulationAgent implements Drawable {
	
	private RabbitsGrassSimulationSpace space; 
	private double energy;
	private int x;
	private int y;
	private int ID;
	private int vX;
	private int vY;
	private static int IDNUMBER = 0;
	private static String DIR = "NESW"; 
	
	public RabbitsGrassSimulationAgent() {
		energy = 20*Math.random();
		x = -1;
		y = -1;
		IDNUMBER++;
		ID = IDNUMBER;
		setVxVy();
	}

	public void draw(SimGraphics arg0) {
		arg0.drawFastOval(Color.white);
	}
	
	public boolean tryMove(int newX, int newY) {
		return space.moveRabbit(x,y,newX,newY);
	}
	
	public RabbitsGrassSimulationAgent giveBirth() {
		RabbitsGrassSimulationAgent bunny = new RabbitsGrassSimulationAgent();
		int i = (int)(Math.random()*4);
		char dir = DIR.charAt(i);
		ArrayList<Integer> dir2 = charToVector(dir);
		int count = 0;
		int size = space.getSize();
		int countLimit = 50;
		while(space.isCellOcuppied((x+dir2.get(0)+size)%size, (y+dir2.get(1)+size)%size) && count < countLimit){
			i = (int)(Math.random()*4);
			dir = DIR.charAt(i);
			dir2 = charToVector(dir);
			count++;
		}
		if(count < 10) {
			bunny.setXY((x+dir2.get(0)+size)%size, (y+dir2.get(1)+size)%size);
		}
		return bunny;
	
	}
	
	public ArrayList<Integer> charToVector(char dir) {
		ArrayList<Integer> res;
		if(dir == 'N') {
			res = new ArrayList<Integer>(Arrays.asList(0,1)); 
		}
		else if(dir == 'E') {
			res = new ArrayList<Integer>(Arrays.asList(1,0));
		}
		else if(dir == 'S') {
			res = new ArrayList<Integer>(Arrays.asList(0,-1));
		}
		else {
			res = new ArrayList<Integer>(Arrays.asList(-1,0));
		}
		return res;
	}
	
	public void step(double grassEnergy) {
		energy--;
		
		int size = space.getSize();
		
		int newX = (x+vX+size)%size;
		int newY = (y+vY+size)%size;
		
		if(tryMove(newX,newY)) {
			energy += space.eatGrassAt(newX, newY,grassEnergy);
			if(energy > 20) {
				energy = 20;
			}
		}
		setVxVy();
	}

	
	//Getters
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getID() {
		return ID;
	}
	
	public double getEnergy() {
		return energy;
	}
	
	public void report() {
		System.out.println(getID() + " at (" + x + ", "
									+ y + ") has "
									+ getEnergy() + " units of energy.");
	}
	
	//Setters
	public void setVxVy() {
		int dir = (int)(Math.random()*4);
		char dir2 = DIR.charAt(dir);
		if(dir2 == 'N') {
			vX = 0;
			vY = 1;
		}
		else if(dir2 == 'E') {
			vX = 1;
			vY = 0;
		}
		else if(dir2 == 'S') {
			vX = 0;
			vY = -1;
		}
		else {
			vX = -1;
			vY = 0;
		}
	}
	
	public void setSpace(RabbitsGrassSimulationSpace rgss) {
		space = rgss;
		
	}
	
	public void setXY(int newX,int newY) {
		x = newX;
		y = newY;
	}
}
