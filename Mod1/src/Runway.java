public class Runway {

	public enum AvoidanceStrategy {TAKEOFFOVER, LANDINGTOWARD, LANDINGOVER, TAKEOFFAWAY}

	public Runway(){

	}

	private String name;
	private int originalTORA;
	private int originalTODA;
	private int originalASDA;
	private int originalLDA;

	public String getObstacleStr() {
		return obstacleStr;
	}

	public String getBefaf() {
		return befaf;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOriginalTORA(int originalTORA) {
		this.originalTORA = originalTORA;
	}

	public void setOriginalTODA(int originalTODA) {
		this.originalTODA = originalTODA;
	}

	public void setOriginalASDA(int originalASDA) {
		this.originalASDA = originalASDA;
	}

	public void setOriginalLDA(int originalLDA) {
		this.originalLDA = originalLDA;
	}

	public void setTORA(int TORA) {
		this.TORA = TORA;
	}

	public void setTODA(int TODA) {
		this.TODA = TODA;
	}

	public void setASDA(int ASDA) {
		this.ASDA = ASDA;
	}

	public void setLDA(int LDA) {
		this.LDA = LDA;
	}

	public void setTakeoffStrategy(AvoidanceStrategy takeoffStrategy) {
		this.takeoffStrategy = takeoffStrategy;
	}

	public void setLandingStrategy(AvoidanceStrategy landingStrategy) {
		this.landingStrategy = landingStrategy;
	}

	public void setThresholdLabel(String thresholdLabel) {
		this.thresholdLabel = thresholdLabel;
	}

	public void setTakeoffThresholdLabel(String takeoffThresholdLabel) {
		this.takeoffThresholdLabel = takeoffThresholdLabel;
	}

	public void setObstacleStr(String obstacleStr) {
		this.obstacleStr = obstacleStr;
	}

	public void setBefaf(String befaf) {
		this.befaf = befaf;
	}

	private int TORA;
	private int TODA;
	private int ASDA;
	private int LDA;

	private int blastAllowance = 300;
	private int stripEnd = 60;
	private int ALSTOCSSlope = 50;
	private int RESA = 240;

	private Obstacle obstacle;
	private AvoidanceStrategy takeoffStrategy = null;
	private AvoidanceStrategy landingStrategy = null;

	private String thresholdLabel;
	private String takeoffThresholdLabel;

	protected String calculations = "No Calculations - Obstacle not yet added";


	private String obstacleStr;
	private String befaf;

	public int getOriginalTORA() {
		return originalTORA;
	}

	public void clearObstacle() {
		obstacle = null;
		takeoffStrategy = null;
		landingStrategy = null;
		this.TORA = originalTORA;
		this.TODA = originalTODA;
		this.ASDA = originalASDA;
		this.LDA = originalLDA;
		takeoffThresholdLabel = "" + getThreshold();
	}

	public void setCalculations(String calculations) {
		this.calculations = calculations;
	}

	public int getRESA() {
		return RESA;
	}

	public void setRESA(int rESA) {
		RESA = rESA;
	}

	public int getOriginalTODA() {
		return originalTODA;
	}

	public AvoidanceStrategy getTakeoffStrategy() {
		return takeoffStrategy;
	}

	public AvoidanceStrategy getLandingStrategy() {
		return landingStrategy;
	}

	public int getOriginalASDA() {
		return originalASDA;
	}

	public int getOriginalLDA() {
		return originalLDA;
	}

	public String getName() {
		return name;
	}

	public String getCalculations() {
		return calculations;
	}

	public int getThreshold() {
		return originalTORA - LDA;
	}

	public int getOriginalThreshold(){
		return originalTORA -originalLDA;
	}

	public int getTakeoffThreshold() {
		return originalTORA - TORA;
	}

	public String getThresholdLabel() {
		return thresholdLabel;
	}

	public String getTakeoffThresholdLabel() {
		return takeoffThresholdLabel;
	}

	public int getStopway() {
		return (ASDA - TORA);
	}

	public int getClearway() {
		return (TODA - TORA);
	}

	private void clearCalculations() {
		calculations = "No Calculations - Obstacle not yet added";
	}

	public void setBlastAllowance(int blastAllowance) {
		this.blastAllowance = blastAllowance;
	}

	public void setStripEnd(int stripEnd) {
		this.stripEnd = stripEnd;
	}

	public void setALSTOCSSlope(int ALSTOCSSlope) {
		this.ALSTOCSSlope = ALSTOCSSlope;
	}

	public int getTORA() {
		return TORA;
	}

	public int getTODA() {
		return TODA;
	}

	public int getASDA() {
		return ASDA;
	}

	public int getLDA() {
		return LDA;
	}

	public int getBlastAllowance() {
		return blastAllowance;
	}

	public int getStripEnd() {
		return stripEnd;
	}

	public int getALSTOCSSlope() {
		return ALSTOCSSlope;
	}

	public Obstacle getObstacle() {
		return obstacle;
	}

	public int getDirection() {
		return Integer.parseInt(name.replaceAll("\\D+",""));
	}

	public Runway(String name, int TORA, int TODA, int ASDA, int LDA) {
		this.name = name;
		this.TORA = originalTORA = TORA;
		this.TODA = originalTODA = TODA;
		this.ASDA = originalASDA = ASDA;
		this.LDA = originalLDA = LDA;
		takeoffThresholdLabel = "" + getThreshold();
	}

	//  returns false if obstacle is far away enough not to redeclare

	public boolean setObstacle(Obstacle obstacle) {
		clearObstacle();
		this.obstacle = obstacle;
		if (obstacle == null || Math.abs(obstacle.centerlineDist) > 75) {
			takeoffStrategy = null;
			landingStrategy = null;
			return false;
		}
		
		if (this.getDirection() <= 18){
			if (obstacle.dist1stThresh + getThreshold() >getOriginalTORA()+60 || obstacle.dist1stThresh + getThreshold()<-60 - getOriginalThreshold() )
				return false;
		}
		if (this.getDirection() > 18){
			if (obstacle.dist2ndThresh + getThreshold() >getOriginalTORA()+60 || obstacle.dist2ndThresh + getOriginalThreshold()<-60 -getOriginalThreshold() )
				return false;
		}

		//returns without redeclaration if obstacle is more than 60m from TORA and 75m from centreline
		if (this.getDirection() <= 18){
			if (obstacle.dist1stThresh + getThreshold() < -60 || obstacle.dist2ndThresh < -60 - getOriginalThreshold())
				return false;
		}else if (obstacle.dist2ndThresh + getThreshold() < -60 || obstacle.dist1stThresh < -60 - getOriginalThreshold())
			return false;

		calculations = "Runway: " + this.name +"\n";
		String wkngCalculations;
		int dist;
		int best;

		//if this is 1st runway (1-18)
		if (this.getDirection() <= 18)
			dist = obstacle.getDist1stThresh();
		else
			dist = obstacle.getDist2ndThresh();
		if (dist < 0){
			befaf = "before";
		}else{
			befaf ="after";
		}
		obstacleStr = "\nObstacle Parameters: " + obstacle.height +"m tall, " + Math.abs(dist) + "m " + befaf + " the " + this.name + " threshold, " + obstacle.centerlineDist + "m from the centreline";  
		takeoffOver(dist);
		best = TORA;
		takeoffStrategy = AvoidanceStrategy.TAKEOFFAWAY;
		wkngCalculations = takeoffAway(dist);
		if (best > TORA) {
			takeoffStrategy = AvoidanceStrategy.TAKEOFFOVER;
			wkngCalculations = takeoffOver(dist);
		}
		calculations += obstacleStr;
		calculations += wkngCalculations;

		landingToward(dist);
		best = LDA;
		landingStrategy = AvoidanceStrategy.LANDINGOVER;
		LDA = originalLDA;
		wkngCalculations = landingOver(dist);
		if (best > LDA) {
			landingStrategy = AvoidanceStrategy.LANDINGTOWARD;
			wkngCalculations = landingToward(dist);
		}
		calculations += wkngCalculations;

		if (TORA > originalTORA)
			TORA = originalTORA;
		if (ASDA > originalASDA)
			ASDA = originalASDA;
		if (TODA > originalTODA)
			TODA = originalTODA;
		if (LDA > originalLDA)
			LDA = originalLDA;

		if (TORA < 0)
			TORA = 0;
		if (ASDA < 0)
			ASDA = 0;
		if (TODA < 0)
			TODA = 0;
		if (LDA < 0)
			LDA = 0;

		return true;
	}

	private String landingToward(int thresholdDist) {
		String calculations = "";
		landingStrategy = AvoidanceStrategy.LANDINGTOWARD;
		calculations = calculations.concat("\n \nLanding Toward: ");
		LDA = thresholdDist - RESA - stripEnd;
		calculations = calculations.concat("\nLDA: " + thresholdDist +"m " + " - " + RESA +"m "+ " - " + stripEnd +"m "+ " = " + Math.max(0, LDA) +"m");
		thresholdLabel = RESA + "(RESA)+" + stripEnd + "=" + (RESA + stripEnd);
		return calculations;
	}

	private String landingOver(int thresholdDist) {
		String calculations = "";
		landingStrategy = AvoidanceStrategy.LANDINGOVER;
		calculations = calculations.concat("\n \nLanding Over: ");

		int safeGroundDistance = blastAllowance;
		if (RESA + stripEnd > safeGroundDistance)
			safeGroundDistance = RESA + stripEnd;
		int newLDA = originalLDA - thresholdDist - safeGroundDistance;

		int safeALSDistance = obstacle.height * ALSTOCSSlope + stripEnd;
		int tallObsLDA = originalLDA - thresholdDist - safeALSDistance;

		//chooses smallest new LDA and makes sure the new LDA is not bigger than the old
		if (tallObsLDA < newLDA && tallObsLDA < LDA) {
			calculations = calculations.concat("\nLDA: " + LDA+"m " + " - " + thresholdDist+"m " + " - " + obstacle.height+"m " + " x " + ALSTOCSSlope+"m " + " - " + stripEnd+"m ");
			LDA = tallObsLDA;
			calculations = calculations.concat(" = " + Math.max(0, LDA)+"m");
			thresholdLabel = obstacle.height * ALSTOCSSlope + "(ALS)+" + stripEnd + "=" + (obstacle.height * ALSTOCSSlope + stripEnd);
		} else if (newLDA < LDA) {
			LDA = newLDA;
			if (RESA + stripEnd >= blastAllowance) {
				calculations = calculations.concat("\nLDA: " + thresholdDist+"m " + " + " + LDA+"m " + " - " + RESA+"m " + " - " + stripEnd+"m ");
				thresholdLabel = RESA + "(RESA)+" + stripEnd + "=" + (RESA + stripEnd);
			} else {
				calculations = calculations.concat("\nLDA: " + thresholdDist+"m " + " + " + LDA +"m "+ " - " + blastAllowance+"m ");
				thresholdLabel = "Blast Allowance=" + (blastAllowance);
			}
			calculations = calculations.concat(" = " + Math.max(0, LDA)+"m");
		}
		return calculations;
	}

	private String takeoffOver(int thresholdDist) {
		String calculations = "";
		takeoffStrategy = AvoidanceStrategy.TAKEOFFOVER;
		calculations = calculations.concat("\n \nTakeoff Over: ");

		int safeGroundDistance = RESA + stripEnd;
		int newTORA = thresholdDist + (originalTORA-originalLDA) - safeGroundDistance;

		int safeTOCSDistance = obstacle.height * ALSTOCSSlope + stripEnd;
		int tallObsTORA = thresholdDist + (originalTORA-originalLDA) - safeTOCSDistance;

		if (tallObsTORA < newTORA) {
			calculations = calculations.concat("\nTORA, TODA, ASDA: " + thresholdDist+"m " + " + " + (originalTORA-originalLDA)+"m " + " - " + obstacle.height+"m " + " x " + ALSTOCSSlope+"m " + " - " + stripEnd+"m ");
			TORA = ASDA = TODA = tallObsTORA;
			calculations = calculations.concat(" = " + Math.max(0, TORA)+"m");
			takeoffThresholdLabel = obstacle.height * ALSTOCSSlope + "(TOCS)+" + stripEnd + "=" + Math.max(0, obstacle.height * ALSTOCSSlope + stripEnd);
		} else {
			calculations = calculations.concat("\nTORA, TODA, ASDA: " + thresholdDist+"m " + " + " + (originalTORA-originalLDA)+"m " + " - " + RESA+"m " + " - " + stripEnd+"m ");
			TORA = ASDA = TODA = newTORA;
			calculations = calculations.concat(" = " + Math.max(0, TORA)+"m");
			takeoffThresholdLabel = RESA + "(RESA)+" + stripEnd + "=" + Math.max(0, RESA + stripEnd);
		}
		return calculations;
	}

	private String takeoffAway(int thresholdDist) {
		takeoffStrategy = AvoidanceStrategy.TAKEOFFAWAY;
		String calculations = "";
		calculations = calculations.concat("\n \nTakeoff Away: ");
		calculations = calculations.concat("\nTODA: " + originalTODA+"m " + " - " + thresholdDist+"m " + " - " + blastAllowance +"m "+ " - " + (originalTORA-originalLDA)+"m ");
		TODA = Math.min(originalTODA - thresholdDist - blastAllowance - (originalTORA-originalLDA), originalTODA - thresholdDist - (RESA + stripEnd));
		calculations = calculations.concat(" = " + Math.max(0, TODA)+"m");
		calculations = calculations.concat("\nASDA: " + originalASDA+"m " + " - " + thresholdDist+"m " + " - " + blastAllowance+"m " + " - " + (originalTORA-originalLDA)+"m ");
		ASDA = Math.min(originalASDA - thresholdDist - blastAllowance - (originalTORA-originalLDA), originalASDA - thresholdDist - (RESA + stripEnd));
		calculations = calculations.concat(" = " + Math.max(0, ASDA)+"m");
		calculations = calculations.concat("\nTORA: " + originalTORA+"m " + " - " + thresholdDist+"m " + " - " + blastAllowance+"m " + " - " + (originalTORA-originalLDA)+"m ");
		TORA = Math.min(originalTORA - thresholdDist - blastAllowance - (originalTORA-originalLDA), originalTORA - thresholdDist - (RESA + stripEnd));
		calculations = calculations.concat(" = " + Math.max(0, TORA)+"m");
		takeoffThresholdLabel = "Blast Allowance=" + Math.max(0, blastAllowance);
		return calculations;
	}
}
