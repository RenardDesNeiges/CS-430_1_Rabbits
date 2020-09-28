import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.ActionGroup;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.util.SimUtilities;
import uchicago.src.sim.analysis.DataSource;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;

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
 * (3) Ajouter le nom de l'attribut � la liste rendue par "getInitParam()"
 * Les getters et les setters sont tout en bas (vue que se sont les m�thodes les moins int�ressantes).
 * Enfin c'est pas oblig� (genre j'ai fait aucune des trois �tapes pour "grassSpace"), mais c'est 
 * assez bien comme routine je trouve.
 * 
 */

public class RabbitsGrassSimulationModel extends SimModelImpl {		
		
	
		//Attributs obligatoires (demand�s par le prof)
		private int gridSize = GRIDSIZE;
		private int numInitRabbits = NUM_INIT_RABBITS;
		private int numInitGrass = NUM_INIT_GRASS;
		private int grassGrowthRate = GRASSGROWTHRATE;
		private int birthTreshold = BIRTHTRESHOLD;
		private double grassEnergy = GRASSENERGY;
		
		//Valeurs par d�faults
		private static final int GRIDSIZE = 20;//Donn�e par le prof 
		private static final int NUM_INIT_RABBITS = 30;
		private static final int NUM_INIT_GRASS = 100;
		private static final int GRASSGROWTHRATE = 50;//Exemple donn� par le prof
		private static final int BIRTHTRESHOLD = 20;
		private static final int GRASSENERGY = 5;
		
		
		/*Attributs internes pour une simul'
		 * Pas de getter et setter pour ces attributs � priori
		 */
		private RabbitsGrassSimulationSpace grassSpace;
		private DisplaySurface displaySurf;
		private ArrayList<RabbitsGrassSimulationAgent> rabbitsList;
		private Schedule schedule;
		private OpenSequenceGraph amountOfGrassInSpace;
		
		class rabbitsInLand implements DataSource, Sequence{
			public Object execute() {
				double res = (double)getSValue();
				return res;
			}
			
			public double getSValue() {
				double res = (double)rabbitsList.size();
				return res;
			}
		}
		
		class grassInSpace implements DataSource, Sequence{
			
			public Object execute() {
				double res = (double)getSValue();
				return res;
			}
			
			public double getSValue() {
				double res = (double)grassSpace.getTotalGrass();
				return res;
			}
			
		}
		
		
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
			amountOfGrassInSpace.display();
			
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
			
			class GrassGrowth extends BasicAction{
				public void execute() {
					grassSpace.growGrass(grassGrowthRate);
				}
			}
			//schedule.scheduleActionBeginning(0, new GrassGrowth());
			
			class RabbitsStep extends BasicAction {
				public void execute() {
					SimUtilities.shuffle(rabbitsList);
					
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
			//schedule.scheduleActionBeginning(0, new RabbitsStep());
			
			class GraphRecord extends BasicAction{
				public void execute() {
					amountOfGrassInSpace.record();
				}
			}
			
			class StepByStep extends ActionGroup{
				public StepByStep() {
					addAction(new GrassGrowth());
					addAction(new RabbitsStep());
					addAction(new GraphRecord());
				}
			}
			schedule.scheduleActionBeginning(0, new StepByStep());
			
			class RabbitsGrassUpdateGrassInSpace extends BasicAction{
				public void execute() {
					amountOfGrassInSpace.step();
				}
			}
			schedule.scheduleActionAtInterval(5,new RabbitsGrassUpdateGrassInSpace());
			
			class ExportGraph extends BasicAction{
				public void execute() {
					amountOfGrassInSpace.writeToFile();
				}
			}
			schedule.scheduleActionAtEnd(new ExportGraph());
		}
		
		public void buildDisplay() {// Pas trop compris comment �a marche, j'avoue
			System.out.println("Running BuildDisplay");
			ColorMap map = new ColorMap();
			
			map.mapColor(1, new Color(0,(int) (1*8+127),0));
			map.mapColor(0,Color.black);
			
			Object2DDisplay displayRabbits = new Object2DDisplay(grassSpace.getRabbitsLand());
			displayRabbits.setObjectList(rabbitsList);
			
			Value2DDisplay displayGrass = new Value2DDisplay(grassSpace.getGrassLand(), map);
			
			displaySurf.addDisplayableProbeable(displayGrass, "Grass");
			displaySurf.addDisplayableProbeable(displayRabbits, "Rabbits");
			
			amountOfGrassInSpace.addSequence("Number of grass tile", new grassInSpace(), Color.green);
			amountOfGrassInSpace.addSequence("Number of rabbits", new rabbitsInLand(), Color.blue);
			
		}

		public void setup() {
			System.out.println("Running setup");
			grassSpace = null;
			
			//Tear down Displays
			if (displaySurf != null) {
				displaySurf.dispose();
			}
			displaySurf = null;
			
			if(amountOfGrassInSpace != null) {
				amountOfGrassInSpace.dispose();
			}
			amountOfGrassInSpace = null;
			
			//Creating Displays
			displaySurf = new DisplaySurface(this, "RabbitsGrass Mode Window 1");
			amountOfGrassInSpace = new OpenSequenceGraph("Amount Of Grass In Space",this);
			
			//Register Displays
			registerDisplaySurface("RabbitsGrass Model Window 1",displaySurf);
			this.registerMediaProducer("Plot", amountOfGrassInSpace);
			
			rabbitsList = new ArrayList<RabbitsGrassSimulationAgent>();
			schedule = new Schedule(1);
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
		
		//Getters pour chaque attribut (tenir � jour)
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
