import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;

import java.awt.Color;
import java.lang.String;


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
	
	
	
	public RabbitsGrassSimulationAgent(int maxEnergy) {
		energy = maxEnergy;
		x = -1;
		y = -1;
		IDNUMBER++;
		ID = IDNUMBER;
		setVxVy();
	}
	
	
	public void draw(SimGraphics arg0) {
		arg0.drawFastRoundRect(Color.white);
	}
	
	public boolean tryMove(int newX, int newY) {
		return space.moveRabbit(x,y,newX,newY);
	}
	
	public void step() {
		energy--;
		int size = space.getSize();
		
		int newX = (x+vX+size)%size;
		int newY = (y+vY+size)%size;
		
		if(tryMove(newX,newY)) {
			energy += space.eatGrassAt(newX, newY);
		}
		else {
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
		vX = 0;
		vY = 0;
		while((vX==0) && (vY==0)) {
			vX = (int)Math.floor(Math.random()*3) - 1;
			vY = (int)Math.floor(Math.random()*3) - 1;
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
