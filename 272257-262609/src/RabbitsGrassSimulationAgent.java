import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;

import java.awt.Color;
import java.lang.String;
import java.util.Vector;

/**
 * Class that implements the simulation agent for the rabbits grass simulation.

 * @author
 */
public class RabbitsGrassSimulationAgent implements Drawable {
	
	private RabbitsGrassSimulationSpace space; 
	private int energy;
	private int x;
	private int y;
	private int ID;
	private int vX;
	private int vY;
	private static int IDNUMBER = 0;
	private static String DIR = "NESW"; 
	
	public RabbitsGrassSimulationAgent() {
		energy = 20;
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
	
	public void giveBirth() {
		RabbitsGrassSimulationAgent bunny = new RabbitsGrassSimulationAgent();
		int i = (int)(Math.random()*4);
		char dir = DIR.charAt(i);
	
	}
	
	public Vector charToVector(char dir) {
		Vector res;
		if(dir == 'N') {
			res = new Vector(0,1); 
		}
		else if(dir == 'E') {
			res = new Vector(1,0);
		}
		else if(dir == 'S') {
			res = new Vector(0,-1);
		}
		else {
			res = new Vector(-1,0);
		}
		return res;
	}
	
	public void step(int treshold) {
		energy--;
		
		if(energy >= treshold) {
			giveBirth();
		}
		
		else {
			int size = space.getSize();
		
			int newX = (x+vX+size)%size;
			int newY = (y+vY+size)%size;
		
			if(tryMove(newX,newY)) {
				energy += space.eatGrassAt(newX, newY);
			}
			setVxVy();
		}
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
	
	public int getEnergy() {
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
