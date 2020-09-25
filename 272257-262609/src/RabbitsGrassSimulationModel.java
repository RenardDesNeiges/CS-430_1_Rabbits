import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;

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
		private int gridSize;
		private int numInitRabbits;
		private int numInitGrass;
		private double grassGrowthRate;
		private int birthTreshold;
		
		//Valeurs par défaults
		private static final int GRIDSIZE = 20;
		
		//Attributs supplémentaires
		private int numGrass; //(Pas sûr qu'il soit utile)
		private int numRabbits;//(Same)
		
		//Attributs internes dont l'utilisateur n'a pas besoin 
		private RabbitsGrassSimulationSpace grassSpace;
		private DisplaySurface displaySurf;
		
		
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
			
		}
		
		public void buildModel() {
			System.out.println("Running BuildModel");
			grassSpace = new RabbitsGrassSimulationSpace(gridSize);
			grassSpace.growGrass(numInitGrass);
			
		}
		
		public void buildSchedule() {
			System.out.println("Running BuildSchedule");
		}
		
		public void buildDisplay() {
			System.out.println("Running BuildDisplay");
		}

		public void setup() {
			System.out.println("Running setup");
			grassSpace = null;
			
			if (displaySurf != null) {
				displaySurf.dispose();
			}
			displaySurf = null;
			
			displaySurf = new DisplaySurface(this, "RabbitsGrass Mode Window 1");
			registerDisplaySurface("RabbitsGrass Model Window 1",displaySurf);
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
		
		public void setNumRabbits(int num) {
			numRabbits =num;
		}
		
		public void setNumGrass(int num) {
			numGrass = num;
		}
		
		
		
		//Getters pour chaque attribut (tenir à jour)
		public String getName() {
			// TODO Auto-generated method stub
			return "RabbitsGrass Model";
		}

		public Schedule getSchedule() {
			// TODO Auto-generated method stub
			return null;
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
		
		public double getGrassGrowthRate() {
			return grassGrowthRate;
		}
		
		public int getBirthTreshold() {
			return birthTreshold;
		}
		
		public int getNumRabbits() {
			return numRabbits;
		}
		
		public int getNumGrass() {
			return numGrass;
		}
		
		public String[] getInitParam() { 
			String[] params = { "GridSize", "NumInitRabbits", "NumInitGrass", "GrassGrowthRate", "BirthThreshold",
								"NumRabbits", "NumGrass"};
			return params;
		}
}
