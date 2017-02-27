/**
 * Created by tobyf on 17/02/2017.
 */


public class Runway {
    public AvoidanceStrategy getAvoidanceStrategy() {
        return avoidanceStrategy;
    }

    public enum AvoidanceStrategy { TAKEOFFOVER_LANDINGTOWARD, LANDINGOVER_TAKEOFFAWAY}

    final String name;
    private int TORA;
    private int TODA;
    private int ASDA;
    private int LDA;

    private int blastAllowance = 300;
    private int stripEnd = 60;
    private int ALSTOCSSlope = 50;

    private Obstacle obstacle;
    private AvoidanceStrategy avoidanceStrategy = null;
    
    private String calculations;

    public String getName() {
		return name;
	}

	public String getCalculations() {
		return calculations;
	}
	
	private void clearCalculations(){
		calculations = "";
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

    public int getDirection(){
        return Integer.parseInt(name.substring(0,2));
    }

    public Runway(String name, int TORA, int TODA, int ASDA, int LDA) {
        this.name = name;
        this.TORA = TORA;
        this.TODA = TODA;
        this.ASDA = ASDA;
        this.LDA = LDA;
    }

    public boolean setObstacle(Obstacle obstacle){
        this.obstacle = obstacle;

        //returns false if obstacle is far away enough not to redeclare
        if (obstacle.centerlineDist > 75)
            return false;
        
        clearCalculations();
        calculations.concat("Runway: " + this.name);

        //if this is 1st runway (1-18)
        if (this.getDirection() <= 18) {
            //if obstacle far end
            if (obstacle.dist1stThresh > obstacle.dist2ndThresh) {
                avoidanceStrategy = AvoidanceStrategy.TAKEOFFOVER_LANDINGTOWARD;
                takeoffOver(obstacle.dist1stThresh);
                landingToward(obstacle.dist1stThresh);
            //if obstacle near end
            }else{
                avoidanceStrategy = AvoidanceStrategy.LANDINGOVER_TAKEOFFAWAY;
                landingOver(obstacle.dist1stThresh);
                takeoffAway(obstacle.dist1stThresh);
            }
        //if this is 2nd runway (19 - 36)
        }else{
            //if obstacle far end
            if (obstacle.dist1stThresh < obstacle.dist2ndThresh) {
                avoidanceStrategy = AvoidanceStrategy.TAKEOFFOVER_LANDINGTOWARD;
                takeoffOver(obstacle.dist2ndThresh);
                landingToward(obstacle.dist2ndThresh);
            //if obstacle near end
            }else{
                avoidanceStrategy = AvoidanceStrategy.LANDINGOVER_TAKEOFFAWAY;
                landingOver(obstacle.dist2ndThresh);
                takeoffAway(obstacle.dist2ndThresh);
            }
        }
        return true;
    }

    private void landingToward(int thresholdDist){
    	calculations.concat("\nLanding Toward: ");
        LDA = thresholdDist - obstacle.RESA - stripEnd;
        calculations.concat("\nLDA: "+thresholdDist+" - "+obstacle.RESA+" - "+stripEnd+" = "+LDA);
    }

    private void landingOver(int thresholdDist){
    	calculations.concat("\nLanding Over: ");
    	
        int safeGroundDistance = blastAllowance;
        if ( obstacle.RESA + stripEnd > safeGroundDistance)
            safeGroundDistance = obstacle.RESA + stripEnd;
        int newLDA = TORA - thresholdDist - safeGroundDistance;

        int safeALSDistance = obstacle.height * ALSTOCSSlope + stripEnd;
        int tallObsLDA = TORA - thresholdDist - safeALSDistance;

        //chooses smallest new LDA and makes sure the new LDA is not bigger than the old
        if (tallObsLDA < newLDA && tallObsLDA < LDA){
        	calculations.concat("\nLDA: "+TORA+" - "+thresholdDist+" - "+obstacle.height+" x "+ALSTOCSSlope+" - "+stripEnd);
            LDA = tallObsLDA;
            calculations.concat(" = "+LDA);
        }
        else if (newLDA < LDA){
            if ( obstacle.RESA + stripEnd > blastAllowance)
            	calculations.concat("\nLDA: "+thresholdDist+" + "+TORA+" - "+obstacle.RESA+" - "+stripEnd);
            else
            	calculations.concat("\nLDA: "+thresholdDist+" + "+TORA+" - "+blastAllowance);
            
            LDA = newLDA;
            calculations.concat(" = "+LDA);
        }
    }

    private void takeoffOver(int thresholdDist){
    	calculations.concat("\nTakeoff Over: ");

        int safeGroundDistance = obstacle.RESA + stripEnd;
        int newTORA = thresholdDist + (TORA - LDA) - safeGroundDistance;

        int safeTOCSDistance = obstacle.height * ALSTOCSSlope + stripEnd;
        int tallObsTORA = thresholdDist + (TORA - LDA) - safeTOCSDistance;

        if (tallObsTORA < newTORA){
        	calculations.concat("\nTORA, TODA, ASDA: "+thresholdDist+" + "+TORA+" - "+LDA+" - "+obstacle.height+" x "+ALSTOCSSlope+" - "+stripEnd);
            TORA = ASDA = TODA = tallObsTORA;
            calculations.concat(" = "+TORA);
        }
        else{
        	calculations.concat("\nTORA, TODA, ASDA: "+thresholdDist+" + "+TORA+" - "+LDA+" - "+obstacle.RESA+" - "+stripEnd);
            TORA = ASDA = TODA = newTORA;
            calculations.concat(" = "+TORA);
        }
    }

    private void takeoffAway(int thresholdDist){
    	calculations.concat("\nTakeoff Away: ");
        //makes sure values can only decrease or stay the same
        if (thresholdDist + blastAllowance > 0) {
        	calculations.concat("\nTORA, TODA, ASDA: "+TORA+" - "+thresholdDist+" - "+blastAllowance);
            TORA = TORA - thresholdDist - blastAllowance;
            calculations.concat(" = "+TORA);
            TODA = TODA - thresholdDist - blastAllowance;
            ASDA = ASDA - thresholdDist - blastAllowance;
        }
    }

}
