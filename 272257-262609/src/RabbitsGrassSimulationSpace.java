/**
 * Class that implements the simulation space of the rabbits grass simulation.
 * @author 
 */

import uchicago.src.sim.space.Object2DGrid;

import java.lang.Math;

public class RabbitsGrassSimulationSpace {
	
	private Object2DGrid grassLand;
	private Object2DGrid rabbitsLand;
	private int size;//Note that both grassLand and rabbitsLand are squares of the same size
	
	//Méthodes ne concernant que l'espace
	public RabbitsGrassSimulationSpace(int size2) {
		size = size2;
		int zero = 0;
		grassLand = new Object2DGrid(size,size);
		rabbitsLand = new Object2DGrid(size,size);
		for(int i = 0; i<size;i++) {
			for(int j=0;j<size;j++) {
				grassLand.putObjectAt(i, j,zero);
			}
		}
		
	}
	
	public void growGrass(int numGrass) {
		
		for(int i = 0;i<numGrass;i++) {
			int x = (int)(Math.random()*size);
			int y = (int)(Math.random()*size);
			int currentGrass = getGrassAt(x,y);
			grassLand.putObjectAt(x, y, currentGrass+1);
		}
		
		/*Le reste n'est pas trop dûr à implémenter,
		 * mais je n'ai pas compris s'il pouvait y avoir
		 * plus d'une unité d'herbes en même temps sur la même case.
		 * Je suis parti du principe que oui, mais à vérifier
		 */
	}
	
	public boolean isCellOcuppied(int x,int y) {
		boolean res = false;
		if(rabbitsLand.getObjectAt(x, y)!=null) {
			res = true;
		}
		
		return res;
	}
	
	public boolean addRabbit(RabbitsGrassSimulationAgent bunny) {
		boolean added = false;
		int count = 0;
		int countLimit = 10*size*size;
		
		while((added == false) && (count < countLimit)) {
			int x = (int)(Math.random()*size);
			int y = (int)(Math.random()*size);
			if(isCellOcuppied(x,y) == false) {
				rabbitsLand.putObjectAt(x, y, bunny);
				added = true;
				bunny.setXY(x, y);
				bunny.setSpace(this);
			}
			count++;
		}
		return added;
	}
	
	
	//Méthodes faisant le lien avec les lapins
	public void removeRabbitAt(int x, int y) {
		rabbitsLand.putObjectAt(x,y, null);
	}
	
	public int eatGrassAt(int x,int y) {
		int grass = getGrassAt(x,y);
		
		if(grass > 0) {
			grassLand.putObjectAt(x, y, grass-1);
			grass = 1;
		}
		
		return grass;
	}
	
	public boolean moveRabbit(int x, int y, int newX, int newY) {
		boolean moved = false;
		if(!isCellOcuppied(x,y)) System.out.println("Un lapin a été perdu de vue.");
		
		if(!isCellOcuppied(newX,newY)) {
			RabbitsGrassSimulationAgent bunny = (RabbitsGrassSimulationAgent)rabbitsLand.getObjectAt(x, y);
			removeRabbitAt(x,y);
			rabbitsLand.putObjectAt(newX, newY, bunny);
			bunny.setXY(newX, newY);
			moved = true;
		}
		return moved;
	}
	
	//Getters
	public int getSize() {
		return size;
	}
	
	public int getGrassAt(int x, int y) {
		int i;
		
		if(grassLand.getObjectAt(x,y)!=null) {
			i = (int) grassLand.getObjectAt(x, y);
		}
		
		else {i = 0;}
		
		return i;
	}
	
	public Object2DGrid getGrassLand() {
		return grassLand;
	}
	
	public Object2DGrid getRabbitsLand() {
		return rabbitsLand;
	}
}
