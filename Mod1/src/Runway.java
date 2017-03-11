/**
 * Created by tobyf on 17/02/2017.
 */


public class Runway {

    public enum AvoidanceStrategy { TAKEOFFOVER,LANDINGTOWARD, LANDINGOVER,TAKEOFFAWAY}

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
    private AvoidanceStrategy takeoffStrategy = null;
    private AvoidanceStrategy landingStrategy = null;

    private String thresholdLabel;
    private String takeoffThresholdLabel;

    private String calculations;

    public int getOriginalTORA() {
        return originalTORA;
    }

    public void clearObstacle(){
        obstacle = null;
        takeoffStrategy = null;
        landingStrategy = null;
        this.TORA = originalTORA;
        this.TODA = originalTODA;
        this.ASDA = originalASDA;
        this.LDA = originalLDA;
        takeoffThresholdLabel = ""+getThreshold();
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
        if (obstacle.centerlineDist > 75) {
            takeoffStrategy = null;
            landingStrategy = null;
            return false;
        }
        
        /*
        //returns without redeclaration if obstacle is more than 60m from TORA and 75m from centreline
        if (this.getDirection() <= 18){
        	if (obstacle.dist1stThresh + getThreshold() < -60 || obstacle.dist2ndThresh < -60)
        		return false;
        }else if (obstacle.dist2ndThresh + getThreshold() < -60 || obstacle.dist1stThresh < -60)
        	return false;
        */
        calculations = "Runway: " + this.name;
        String wkngCalculations;
        int dist;
        int best;

        //if this is 1st runway (1-18)
        if (this.getDirection() <= 18)
            dist = obstacle.dist1stThresh;
        else
            dist = obstacle.dist2ndThresh;

        takeoffOver(dist);
        best = TORA;
        takeoffStrategy = AvoidanceStrategy.TAKEOFFAWAY;
        wkngCalculations=takeoffAway(dist);
        if(best>TORA){
            takeoffStrategy = AvoidanceStrategy.TAKEOFFOVER;
            wkngCalculations=takeoffOver(dist);
        }
        calculations+=wkngCalculations;

        landingToward(dist);
        best = LDA;
        landingStrategy = AvoidanceStrategy.LANDINGOVER;
        LDA = originalLDA;
        wkngCalculations=landingOver(dist);
        if(best>LDA){
            landingStrategy = AvoidanceStrategy.LANDINGTOWARD;
            wkngCalculations=landingToward(dist);
        }
        calculations+=wkngCalculations;

        if(TORA > originalTORA)
            TORA = originalTORA;
        if(ASDA > originalASDA)
            ASDA = originalASDA;
        if(TODA > originalTODA)
            TODA = originalTODA;
        if(LDA > originalLDA)
            LDA = originalLDA;

        return true;
    }

    private String landingToward(int thresholdDist){
        String calculations = "";
        landingStrategy = AvoidanceStrategy.LANDINGTOWARD;
    	calculations = calculations.concat("\nLanding Toward: ");
        LDA = thresholdDist - obstacle.RESA - stripEnd;
        calculations = calculations.concat("\nLDA: "+thresholdDist+" - "+obstacle.RESA+" - "+stripEnd+" = "+LDA);
        thresholdLabel = obstacle.RESA+"(RESA)+"+stripEnd+"="+(obstacle.RESA+stripEnd);
        return calculations;
    }

    private String landingOver(int thresholdDist){
        String calculations = "";
        landingStrategy = AvoidanceStrategy.LANDINGOVER;
    	calculations = calculations.concat("\nLanding Over: ");
    	
        int safeGroundDistance = blastAllowance;
        if ( obstacle.RESA + stripEnd > safeGroundDistance)
            safeGroundDistance = obstacle.RESA + stripEnd;
        int newLDA = originalLDA - thresholdDist - safeGroundDistance;

        int safeALSDistance = obstacle.height * ALSTOCSSlope + stripEnd;
        int tallObsLDA = originalLDA - thresholdDist - safeALSDistance;

        //chooses smallest new LDA and makes sure the new LDA is not bigger than the old
        if (tallObsLDA < newLDA && tallObsLDA < LDA){
        	calculations = calculations.concat("\nLDA: "+LDA+" - "+thresholdDist+" - "+obstacle.height+" x "+ALSTOCSSlope+" - "+stripEnd);
            LDA = tallObsLDA;
            calculations = calculations.concat(" = "+LDA);
            thresholdLabel = obstacle.height * ALSTOCSSlope+"(ALS)+"+stripEnd+"="+(obstacle.height*ALSTOCSSlope+stripEnd);
        }
        else if (newLDA < LDA){
            LDA = newLDA;
            if ( obstacle.RESA + stripEnd > blastAllowance) {
                calculations = calculations.concat("\nLDA: " + thresholdDist + " + " + LDA + " - " + obstacle.RESA + " - " + stripEnd);
                thresholdLabel = obstacle.RESA + "(RESA)+" + stripEnd + "=" + (obstacle.RESA + stripEnd);
            }else {
                calculations = calculations.concat("\nLDA: " + thresholdDist + " + " + LDA + " - " + blastAllowance);
                thresholdLabel = "Blast Allowance=" + (blastAllowance);
            }
            calculations = calculations.concat(" = "+LDA);
        }
        return calculations;
    }

    private String takeoffOver(int thresholdDist){
        String calculations = "";
        takeoffStrategy = AvoidanceStrategy.TAKEOFFOVER;
    	calculations = calculations.concat("\nTakeoff Over: ");

        int safeGroundDistance = obstacle.RESA + stripEnd;
        int newTORA = thresholdDist + getThreshold() - safeGroundDistance;

        int safeTOCSDistance = obstacle.height * ALSTOCSSlope + stripEnd;
        int tallObsTORA = thresholdDist + getThreshold() - safeTOCSDistance;

        if (tallObsTORA < newTORA){
        	calculations = calculations.concat("\nTORA, TODA, ASDA: "+thresholdDist+" + "+(TORA-LDA)+" - "+obstacle.height+" x "+ALSTOCSSlope+" - "+stripEnd);
            TORA = ASDA = TODA = tallObsTORA;
            calculations = calculations.concat(" = "+TORA);
            takeoffThresholdLabel = obstacle.height * ALSTOCSSlope+"(TOCS)+"+stripEnd+"="+(obstacle.height*ALSTOCSSlope+stripEnd);
        }
        else{
        	calculations = calculations.concat("\nTORA, TODA, ASDA: "+thresholdDist+" + "+(TORA-LDA)+" - "+obstacle.RESA+" - "+stripEnd);
            TORA = ASDA = TODA = newTORA;
            calculations = calculations.concat(" = "+TORA);
            takeoffThresholdLabel = obstacle.RESA + "(RESA)+" + stripEnd + "=" + (obstacle.RESA + stripEnd);
        }
        return calculations;
    }

    private String takeoffAway(int thresholdDist){
        takeoffStrategy = AvoidanceStrategy.TAKEOFFAWAY;
        String calculations = "";
    	calculations = calculations.concat("\nTakeoff Away: ");
        calculations = calculations.concat("\nTODA: "+TODA+" - "+thresholdDist+" - "+blastAllowance+" - "+getThreshold());
        TODA = Math.min(originalTODA - thresholdDist - blastAllowance - getThreshold(),originalTODA -obstacle.RESA + stripEnd);
        calculations = calculations.concat(" = "+TODA);
        calculations = calculations.concat("\nASDA: "+ASDA+" - "+thresholdDist+" - "+blastAllowance+" - "+getThreshold());
        ASDA = Math.min(originalASDA - thresholdDist - blastAllowance - getThreshold(),originalASDA -obstacle.RESA + stripEnd);
        calculations = calculations.concat(" = "+ASDA);
        calculations = calculations.concat("\nTORA: "+TORA+" - "+thresholdDist+" - "+blastAllowance+" - "+getThreshold());
        TORA = Math.min(originalTORA - thresholdDist - blastAllowance - getThreshold(),originalTORA -obstacle.RESA + stripEnd);
        calculations = calculations.concat(" = "+TORA);
        takeoffThresholdLabel = "Blast Allowance=" + (blastAllowance);
        return calculations;
    }
}
