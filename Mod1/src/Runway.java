/**
 * Created by tobyf on 17/02/2017.
 */


public class Runway {
    public AvoidanceStrategy getAvoidanceStrategy() {
        return avoidanceStrategy;
    }

    public enum AvoidanceStrategy { TAKEOFFOVER_LANDINGTOWARD, LANDINGOVER_TAKEOFFAWAY}

    final String name;
    private int originalTORA;
    private int originalTODA;
    private int originalASDA;
    private int originalLDA;

    private int TORA;
    private int TODA;
    private int ASDA;
    private int LDA;

    private int blastAllowance = 300;
    private int stripEnd = 60;
    private int ALSTOCSSlope = 50;

    private Obstacle obstacle;
    private AvoidanceStrategy avoidanceStrategy = null;

    private String thresholdLabel;
    private String takeoffThresholdLabel;

    private String calculations;

    public int getOriginalTORA() {
        return originalTORA;
    }

    public void clearObstacle(){
        obstacle = null;
        this.TORA = originalTORA;
        this.TODA = originalTODA;
        this.ASDA = originalASDA;
        this.LDA = originalLDA;
        takeoffThresholdLabel = ""+getThreshold();
    }
    public int getOriginalTODA() {
        return originalTODA;
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

	public int getThreshold(){
        return originalTORA-LDA;
    }

    public int getTakeoffThreshold(){
        return originalTORA-TORA;}

    public String getThresholdLabel() {
        return thresholdLabel;
    }
    public String getTakeoffThresholdLabel() {
        return takeoffThresholdLabel;
    }
	public int getStopway(){return (ASDA - TORA);}
	
	public int getClearway(){return (TODA - TORA);}
	
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
        this.TORA = originalTORA = TORA;
        this.TODA = originalTODA  = TODA;
        this.ASDA  = originalASDA = ASDA;
        this.LDA  = originalLDA = LDA;
        takeoffThresholdLabel = ""+getThreshold();
    }

    public boolean setObstacle(Obstacle obstacle){
        this.obstacle = obstacle;

        //returns false if obstacle is far away enough not to redeclare
        if (obstacle.centerlineDist > 75)
            return false;
        
        //returns without redeclaration if obstacle is more than 60m from TORA and 75m from centreline
        if (this.getDirection() <= 18){
        	if (obstacle.dist1stThresh + getThreshold() < -60 || obstacle.dist2ndThresh < -60)
        		return false;
        }else if (obstacle.dist2ndThresh + getThreshold() < -60 || obstacle.dist1stThresh < -60)
        	return false;
        	
        calculations = "Runway: " + this.name;

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
                takeoffAway(obstacle.dist1stThresh);
                landingOver(obstacle.dist1stThresh);
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
                takeoffAway(obstacle.dist2ndThresh);
                landingOver(obstacle.dist2ndThresh);
            }
        }
        return true;
    }

    private void landingToward(int thresholdDist){
    	calculations = calculations.concat("\nLanding Toward: ");
        LDA = thresholdDist - obstacle.RESA - stripEnd;
        calculations = calculations.concat("\nLDA: "+thresholdDist+" - "+obstacle.RESA+" - "+stripEnd+" = "+LDA);
        thresholdLabel = obstacle.RESA+"(RESA)+"+stripEnd+"="+getThreshold();
    }

    private void landingOver(int thresholdDist){
    	calculations = calculations.concat("\nLanding Over: ");
    	
        int safeGroundDistance = blastAllowance;
        if ( obstacle.RESA + stripEnd > safeGroundDistance)
            safeGroundDistance = obstacle.RESA + stripEnd;
        int newLDA = LDA - thresholdDist - safeGroundDistance;

        int safeALSDistance = obstacle.height * ALSTOCSSlope + stripEnd;
        int tallObsLDA = LDA - thresholdDist - safeALSDistance;

        //chooses smallest new LDA and makes sure the new LDA is not bigger than the old
        if (tallObsLDA < newLDA && tallObsLDA < LDA){
        	calculations = calculations.concat("\nLDA: "+LDA+" - "+thresholdDist+" - "+obstacle.height+" x "+ALSTOCSSlope+" - "+stripEnd);
            LDA = tallObsLDA;
            calculations = calculations.concat(" = "+LDA);
            thresholdLabel = obstacle.height * ALSTOCSSlope+"(ALS)+"+stripEnd+"="+getThreshold();
        }
        else if (newLDA < LDA){
            LDA = newLDA;
            if ( obstacle.RESA + stripEnd > blastAllowance) {
                calculations = calculations.concat("\nLDA: " + thresholdDist + " + " + LDA + " - " + obstacle.RESA + " - " + stripEnd);
                thresholdLabel = obstacle.RESA + "(RESA)+" + stripEnd + "=" + getThreshold();
            }else {
                calculations = calculations.concat("\nLDA: " + thresholdDist + " + " + LDA + " - " + blastAllowance);
                thresholdLabel = "Blast Allowance=" + getThreshold();
            }
            calculations = calculations.concat(" = "+LDA);
        }
    }

    private void takeoffOver(int thresholdDist){
    	calculations = calculations.concat("\nTakeoff Over: ");

        int safeGroundDistance = obstacle.RESA + stripEnd;
        int newTORA = thresholdDist + getThreshold() - safeGroundDistance;

        int safeTOCSDistance = obstacle.height * ALSTOCSSlope + stripEnd;
        int tallObsTORA = thresholdDist + getThreshold() - safeTOCSDistance;

        if (tallObsTORA < newTORA){
        	calculations = calculations.concat("\nTORA, TODA, ASDA: "+thresholdDist+" + "+(TORA-LDA)+" - "+obstacle.height+" x "+ALSTOCSSlope+" - "+stripEnd);
            TORA = ASDA = TODA = tallObsTORA;
            calculations = calculations.concat(" = "+TORA);
            takeoffThresholdLabel = obstacle.height * ALSTOCSSlope+"(TOCS)+"+stripEnd+"="+getThreshold();
        }
        else{
        	calculations = calculations.concat("\nTORA, TODA, ASDA: "+thresholdDist+" + "+(TORA-LDA)+" - "+obstacle.RESA+" - "+stripEnd);
            TORA = ASDA = TODA = newTORA;
            calculations = calculations.concat(" = "+TORA);
            takeoffThresholdLabel = obstacle.RESA + "(RESA)+" + stripEnd + "=" + getThreshold();
        }
    }

    private void takeoffAway(int thresholdDist){
    	calculations = calculations.concat("\nTakeoff Away: ");
        //makes sure values can only decrease or stay the same
        if (thresholdDist + blastAllowance > 0) {
        	calculations = calculations.concat("\nTODA: "+TODA+" - "+thresholdDist+" - "+blastAllowance+" - "+getThreshold());
            TODA = TODA - thresholdDist - blastAllowance - getThreshold();
            calculations = calculations.concat(" = "+TODA);
            calculations = calculations.concat("\nASDA: "+ASDA+" - "+thresholdDist+" - "+blastAllowance+" - "+getThreshold());
            ASDA = ASDA - thresholdDist - blastAllowance - getThreshold();
            calculations = calculations.concat(" = "+ASDA);
            calculations = calculations.concat("\nTORA: "+TORA+" - "+thresholdDist+" - "+blastAllowance+" - "+getThreshold());
        	TORA = TORA - thresholdDist - blastAllowance - getThreshold();
        	calculations = calculations.concat(" = "+TORA);
            takeoffThresholdLabel = "Blast Allowance=" + getThreshold();
        }
    }

}
