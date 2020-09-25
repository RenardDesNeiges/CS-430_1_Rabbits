/**
 * Class that implements the simulation space of the rabbits grass simulation.
 * @author 
 */

import uchicago.src.sim.space.Object2DGrid;
import java.lang.Math;

public class RabbitsGrassSimulationSpace {
	
	private Object2DGrid grassLand;
	private int size;
	
	public RabbitsGrassSimulationSpace(int size2) {
		size = size2;
		int zero = 0;
		grassLand = new Object2DGrid(size,size);
		for(int i = 0; i<size;i++) {
			for(int j=0;j<size;j++) {
				grassLand.putObjectAt(i, j,zero);
			}
		}
	}
	
	public int getGrassAt(int x, int y) {
		int i;
		
		if(grassLand.getObjectAt(x,y)!=null) {
			i = (int) grassLand.getObjectAt(x, y);
		}
		
		else {i = 0;}
		
		return i;
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
}
