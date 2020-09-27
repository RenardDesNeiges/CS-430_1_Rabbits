import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.util.SimUtilities;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Class that implements the simulation model for the rabbits grass
 * simulation.  This is the first class which needs to be setup in
 * order to run Repast simulation. It manages the entire RePast
 * environment and the simulation.
 *
 * @author 
 */
/*Lors du rajout d'un attribut, ne pas oublier de :
 * (1) Ajouter le getter,
 * (2) Ajouter le setter,
 * (3) Ajouter le nom de l'attribut à la liste rendue par "getInitParam()"
 * Les getters et les setters sont tout en bas (vue que se sont les méthodes les moins intéressantes).
 * Enfin c'est pas obligé (genre j'ai fait aucune des trois étapes pour "grassSpace"), mais c'est 
 * assez bien comme routine je trouve.
 * 
 */

public class RabbitsGrassSimulationModel extends SimModelImpl {		
		
	
		//Attributs obligatoires (demandés par le prof)
		private int gridSize = GRIDSIZE;
		private int numInitRabbits = NUM_INIT_RABBITS;
		private int numInitGrass = NUM_INIT_GRASS;
		private int grassGrowthRate = GRASSGROWTHRATE;
		private int birthTreshold = BIRTHTRESHOLD;
		private double grassEnergy = GRASSENERGY;
		
		//Valeurs par défaults
		private static final int GRIDSIZE = 20;//Donnée par le prof 
		private static final int NUM_INIT_RABBITS = 30;
		private static final int NUM_INIT_GRASS = 100;
		private static final int GRASSGROWTHRATE = 50;//Exemple donné par le prof
		private static final int BIRTHTRESHOLD = 20;
		private static final int GRASSENERGY = 5;
		
		
		//Attributs supplémentaires
		
		
		/*Attributs internes pour une simul'
		 * Pas de getter et setter pour ces attributs à priori
		 */
		private RabbitsGrassSimulationSpace grassSpace;
		private DisplaySurface displaySurf;
		private ArrayList<RabbitsGrassSimulationAgent> rabbitsList;
		private Schedule schedule;
		
		
		//Méthodes classiques d'un model
		public static void main(String[] args) {
			
			System.out.println("Rabbit skeleton");

			SimInit init = new SimInit();
			RabbitsGrassSimulationModel model = new RabbitsGrassSimulationModel();
			// Do "not" modify the following lines of parsing arguments
			if (args.length == 0) // by default, you don't use parameter file nor batch mode 
				init.loadModel(model, "", false);
			else
				init.loadModel(model, args[0], Boolean.parseBoolean(args[1]));
			
		}

		public void begin() {//Standard begin method layout
			buildModel();
			buildSchedule();
			buildDisplay();
			displaySurf.display();
			
		}
		
		public void buildModel() {
			System.out.println("Running BuildModel");
			grassSpace = new RabbitsGrassSimulationSpace(gridSize);
			grassSpace.growGrass(numInitGrass);
			
			for(int i = 0;i < numInitRabbits; i++) {
				addNewRabbits();
			}
		}
		
		public void buildSchedule() {
			System.out.println("Running BuildSchedule");
			
			class RabbitsGrassStep extends BasicAction {
				public void execute() {
					SimUtilities.shuffle(rabbitsList);
					
					grassSpace.growGrass(grassGrowthRate);
					
					for(int i = 0; i < rabbitsList.size(); i++) {
						RabbitsGrassSimulationAgent bunny = (RabbitsGrassSimulationAgent) rabbitsList.get(i);
						if(bunny.getEnergy() >= birthTreshold) {
							RabbitsGrassSimulationAgent baby = bunny.giveBirth();
							addRabbit(baby);
						}
						
						bunny.step(grassEnergy);
					}
					
					reapDeadRabbits();
					
					
					displaySurf.updateDisplay();
				}
				
			}
			schedule.scheduleActionBeginning(0, new RabbitsGrassStep());
			
			
			class RabbitsGrassCountLiving extends BasicAction {
				public void execute() {
					countLivingRabbits();
				}
			}
			schedule.scheduleActionBeginning(0, new RabbitsGrassCountLiving());
			
		}
		
		public void buildDisplay() {// Pas trop compris comment ça marche, j'avoue
			System.out.println("Running BuildDisplay");
			ColorMap map = new ColorMap();
			
			for(int i = 1; i<16; i++) {
				map.mapColor(i, new Color(0,(int) (i*8+127),0));
			}
			
			Object2DDisplay displayRabbits = new Object2DDisplay(grassSpace.getRabbitsLand());
			displayRabbits.setObjectList(rabbitsList);
			
			map.mapColor(0,Color.black);
			Value2DDisplay displayGrass = new Value2DDisplay(grassSpace.getGrassLand(), map);
			
			displaySurf.addDisplayable(displayGrass, "Grass");
			displaySurf.addDisplayable(displayRabbits, "Rabbits");
			
		}

		public void setup() {
			System.out.println("Running setup");
			grassSpace = null;
			
			if (displaySurf != null) {
				displaySurf.dispose();
			}
			
			displaySurf = null;
			rabbitsList = new ArrayList<RabbitsGrassSimulationAgent>();
			schedule = new Schedule(1);
			
			displaySurf = new DisplaySurface(this, "RabbitsGrass Mode Window 1");
			registerDisplaySurface("RabbitsGrass Model Window 1",displaySurf);
		}
		
		private void addRabbit(RabbitsGrassSimulationAgent bunny) {
			if(grassSpace.addRabbit(bunny)) {
				rabbitsList.add(bunny);
			}
		}
		
		private void addNewRabbits() {
			RabbitsGrassSimulationAgent bunny = new RabbitsGrassSimulationAgent();
			if(grassSpace.addRabbit(bunny)) {
				rabbitsList.add(bunny);
			}
		}
		
		private int countLivingRabbits() {
			int livingRabbits = 0;
			for(int i = 0; i < rabbitsList.size(); i++) {
				RabbitsGrassSimulationAgent bunny = (RabbitsGrassSimulationAgent) rabbitsList.get(i);
				if(bunny.getEnergy() > 0) livingRabbits++;
			}
			
			
			return livingRabbits;
		}
		
		private int reapDeadRabbits() {
			int count = 0;
			for(int i = 0; i < rabbitsList.size(); i++) {
				RabbitsGrassSimulationAgent bunny = (RabbitsGrassSimulationAgent) rabbitsList.get(i);
				if(bunny.getEnergy() <= 0) {
					grassSpace.removeRabbitAt(bunny.getX(),bunny.getY());
					rabbitsList.remove(i);
					count++;
				}
			}
			return count;
		}
		
		//Setters pour chaque attribut
		public void setGridSize(int size) {
			gridSize = size;
		}
		
		public void setNumInitRabbits(int rab){
			numInitRabbits = rab;
		}
		
		public void setNumInitGrass(int grass) {
			numInitGrass = grass;
		}
		
		public void setGrassGrowthRate(int rate) {
			grassGrowthRate = rate;
		}
		
		public void setBirthTreshold(int treshold) {
			birthTreshold = treshold;
		}
		
		public void setGrassEnergy(double ge) {
			grassEnergy = ge;
		}
		
		//Getters pour chaque attribut (tenir à jour)
		public String getName() {
			return "RabbitsGrass Model";
		}

		public Schedule getSchedule() {
			return schedule;
		}
		
		public int getGridSize() {
			return gridSize;
		}
		
		public int getNumInitRabbits() {
			return numInitRabbits;
		}
		
		public int getNumInitGrass() {
			return numInitGrass;
		}
		
		public int getGrassGrowthRate() {
			return grassGrowthRate;
		}
		
		public int getBirthTreshold() {
			return birthTreshold;
		}
			
		public String[] getInitParam() { 
			String[] params = {"GridSize","NumInitRabbits","NumInitGrass", "GrassGrowthRate", "BirthTreshold", "GrassEnergy"};
			return params;
		}

		public double getGrassEnergy() {
			return grassEnergy;
		}
}
