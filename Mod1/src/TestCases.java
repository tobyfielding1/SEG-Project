import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCases {
	/* **************** Regression Testing ************************* */
	/*
	 * Checking all of the code from the previous increment is working
	 */
	@Test
	public void scenario1(){
		Obstacle ob = new Obstacle("Plane", -50, 3646, 0, 12);

		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(3345, run1.getTORA());
		assertEquals(3345, run1.getTODA());
		assertEquals(3345, run1.getASDA());
		assertEquals(2985, run1.getLDA());

		Runway run2 = new Runway("27R", 3884, 3962, 3884, 3884);
		run2.putObstacle(ob);
		assertEquals(2986, run2.getTORA());
		assertEquals(2986, run2.getTODA());
		assertEquals(2986, run2.getASDA());
		assertEquals(3346, run2.getLDA());
	}
	@Test
	public void scenario2(){
		Obstacle ob = new Obstacle("Plane", 2853, 500, -20, 25);

		Runway run1 = new Runway("09R", 3660, 3660, 3660, 3353);
		run1.putObstacle(ob);
		assertEquals(1850, run1.getTORA());
		assertEquals(1850, run1.getTODA());
		assertEquals(1850, run1.getASDA());
		assertEquals(2553, run1.getLDA());

		Runway run2 = new Runway("27L", 3660, 3660, 3660, 3660);
		run2.putObstacle(ob);
		assertEquals(2860, run2.getTORA());
		assertEquals(2860, run2.getTODA());
		assertEquals(2860, run2.getASDA());
		assertEquals(1850, run2.getLDA());
	}
	@Test
	public void scenario3(){
		Obstacle ob = new Obstacle("Plane", 150, 3203, 60, 15);

		Runway run1 = new Runway("09R", 3660, 3660, 3660, 3353);
		run1.putObstacle(ob);
		assertEquals(2903, run1.getTORA());
		assertEquals(2903, run1.getTODA());
		assertEquals(2903, run1.getASDA());
		assertEquals(2393, run1.getLDA());

		Runway run2 = new Runway("27L", 3660, 3660, 3660, 3660);
		run2.putObstacle(ob);
		assertEquals(2393, run2.getTORA());
		assertEquals(2393, run2.getTODA());
		assertEquals(2393, run2.getASDA());
		assertEquals(2903, run2.getLDA());
	}
	@Test
	public void scenario4(){
		Obstacle ob = new Obstacle("Plane", 3546, 50, 20, 20);

		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(2793, run1.getTORA());
		assertEquals(2793, run1.getTODA());
		assertEquals(2793, run1.getASDA());
		assertEquals(3246, run1.getLDA());

		Runway run2 = new Runway("27R", 3884, 3962, 3884, 3884);
		run2.putObstacle(ob);
		assertEquals(3534, run2.getTORA());
		assertEquals(3612, run2.getTODA());
		assertEquals(3534, run2.getASDA());
		assertEquals(2774, run2.getLDA());
	}
	/* ***************** End of regression testing ****************************/

	/* ***************** Boundary Testing ****************************/

	@Test
	public void BoundaryTestInNorth(){
		/*
		 * Obstacle inside (74m) cleared and graded to the north of centreline
		 * Values are original ones
		 */
		Obstacle ob = new Obstacle("Plane", 3546, 50, 74, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(2793, run1.getTORA());
		assertEquals(2793, run1.getTODA());
		assertEquals(2793, run1.getASDA());
		assertEquals(3246, run1.getLDA());
	}

	@Test
	public void BoundaryTestOnBoundaryNorth(){
		/*
		 * Obstacle on (75m) cleared and graded to the north of centreline
		 * Values are original ones
		 */
		Obstacle ob = new Obstacle("Plane", 3546, 50, 75, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(2793, run1.getTORA());
		assertEquals(2793, run1.getTODA());
		assertEquals(2793, run1.getASDA());
		assertEquals(3246, run1.getLDA());
	}

	@Test
	public void BoundaryTestOutNorth(){
		/*
		 * Obstacle outside (76m) cleared and graded to the north of centreline
		 * Values are original ones
		 */
		Obstacle ob = new Obstacle("Plane", 3546, 50, 76, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(3902, run1.getTORA());
		assertEquals(3902, run1.getTODA());
		assertEquals(3902, run1.getASDA());
		assertEquals(3595, run1.getLDA());
	}

	@Test 
	public void BoundaryTestInSouth(){
		/*
		 * Obstacle inside (-74m) cleared and graded to the south of centreline
		 * Values are original ones
		 */
		Obstacle ob = new Obstacle("Plane", 3546, 50, -74, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(2793, run1.getTORA());
		assertEquals(2793, run1.getTODA());
		assertEquals(2793, run1.getASDA());
		assertEquals(3246, run1.getLDA());
	}

	@Test
	public void BoundaryTestOnBoundarySouth(){
		/*
		 * Obstacle on (-75m) cleared and graded to the south of centreline
		 * Values are original ones
		 */
		Obstacle ob = new Obstacle("Plane", 3546, 50, -75, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(2793, run1.getTORA());
		assertEquals(2793, run1.getTODA());
		assertEquals(2793, run1.getASDA());
		assertEquals(3246, run1.getLDA());
	}

	@Test
	public void BoundaryTestOutBoundarySouth(){
		/*
		 * Obstacle outside (-76m) cleared and graded to the south of centreline
		 * Values are original ones
		 */
		Obstacle ob = new Obstacle("Plane", 3546, 50, -76, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(3902, run1.getTORA());
		assertEquals(3902, run1.getTODA());
		assertEquals(3902, run1.getASDA());
		assertEquals(3595, run1.getLDA());
	}
	@Test
	public void BoundaryTestInLeftofLowerThresh(){
		/*
		 * Obstacle inside (-59m) cleared and graded to the Left of lower threshold
		 * Values are original ones
		 */
		Obstacle ob = new Obstacle("Plane", -59, 50, 0, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(3354, run1.getTORA());
		assertEquals(3354, run1.getTODA());
		assertEquals(3354, run1.getASDA());
		assertEquals(2594, run1.getLDA());
	}

	@Test
	public void BoundaryTestOnLeftofLowerThresh(){
		/*
		 * Obstacle on (-60m) cleared and graded to the Left of lower threshold
		 * Values are redeclared
		 */
		Obstacle ob = new Obstacle("Plane", -60, 50, 0, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(3355, run1.getTORA());
		assertEquals(3355, run1.getTODA());
		assertEquals(3355, run1.getASDA());
		assertEquals(2595, run1.getLDA());
	}
	@Test
	public void BoundaryTestOutLeftofLowerThresh(){
		/*
		 * Obstacle outside (-61m) cleared and graded to the Left of lower threshold
		 * Values are original ones
		 */
		Obstacle ob = new Obstacle("Plane", -368, 50, 0, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(3902, run1.getTORA());
		assertEquals(3902, run1.getTODA());
		assertEquals(3902, run1.getASDA());
		assertEquals(3595, run1.getLDA());
	}

	@Test
	public void BoundaryTestInRightofLowerThresh(){
		/*
		 * Obstacle inside (59m) cleared and graded to the Right of lower threshold
		 * Values are redeclared
		 */
		Obstacle ob = new Obstacle("Plane", 3654, 50, 0, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(2901, run1.getTORA());
		assertEquals(2901, run1.getTODA());
		assertEquals(2901, run1.getASDA());
		assertEquals(3354, run1.getLDA());
	}

	@Test
	public void BoundaryTestOnRightofLowerThresh(){
		/*
		 * Obstacle on (60m) cleared and graded to the Right of lower threshold
		 * Values are redclared
		 */
		Obstacle ob = new Obstacle("Plane", 3655, 50, 0, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(2902, run1.getTORA());
		assertEquals(2902, run1.getTODA());
		assertEquals(2902, run1.getASDA());
		assertEquals(3355, run1.getLDA());
	}
	@Test
	public void BoundaryTestOutRightofLowerThresh(){
		/*
		 * Obstacle outside (61m) cleared and graded to the Right of lower threshold
		 * Values are original ones
		 */
		Obstacle ob = new Obstacle("Plane", 3656, 50, 0, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(3902, run1.getTORA());
		assertEquals(3902, run1.getTODA());
		assertEquals(3902, run1.getASDA());
		assertEquals(3595, run1.getLDA());
	}
	@Test
	public void BoundaryTestInRightofUpperThresh(){
		/*
		 * Obstacle inside (-59m) cleared and graded to the Right of upper threshold
		 * Values are original ones
		 */
		Obstacle ob = new Obstacle("Plane",3546 , -59, 0, 20);
		Runway run1 = new Runway("27L", 3660, 3660, 3660, 3660);
		run1.putObstacle(ob);
		assertEquals(3419, run1.getTORA());
		assertEquals(3419, run1.getTODA());
		assertEquals(3419, run1.getASDA());
		assertEquals(2659, run1.getLDA());
	}
	@Test
	public void BoundaryTestOnRightofUpperThresh(){
		/*
		 * Obstacle on (-60m) cleared and graded to the Right of upper threshold
		 * Values are redeclared
		 */
		Obstacle ob = new Obstacle("Plane",3546 , -60, 0, 20);
		Runway run1 = new Runway("27L", 3660, 3660, 3660, 3660);
		run1.putObstacle(ob);
		assertEquals(3420, run1.getTORA());
		assertEquals(3420, run1.getTODA());
		assertEquals(3420, run1.getASDA());
		assertEquals(2660, run1.getLDA());
	}
	@Test
	public void BoundaryTestOutRightofUpperThresh(){
		/*
		 * Obstacle outside (-61m) cleared and graded to the Right of upper threshold
		 * Values are original ones
		 */
		Obstacle ob = new Obstacle("Plane",3546 , -61, 0, 20);
		Runway run1 = new Runway("27L", 3660, 3660, 3660, 3660);
		run1.putObstacle(ob);
		assertEquals(3660, run1.getTORA());
		assertEquals(3660, run1.getTODA());
		assertEquals(3660, run1.getASDA());
		assertEquals(3660, run1.getLDA());
	}

	@Test
	public void BoundaryTestInLeftofUpperThresh(){
		/*
		 * Obstacle in (59m) cleared and graded to the Left of upper threshold
		 * Values are redeclared
		 */
		Obstacle ob = new Obstacle("Plane",3546 , 3719, 0, 20);
		Runway run1 = new Runway("27L", 3660, 3660, 3660, 3660);
		run1.putObstacle(ob);
		assertEquals(2659, run1.getTORA());
		assertEquals(2659, run1.getTODA());
		assertEquals(2659, run1.getASDA());
		assertEquals(3419, run1.getLDA());
	}

	@Test
	public void BoundaryTestonLeftofUpperThresh(){
		/*
		 * Obstacle on (60m) cleared and graded to the Left of upper threshold
		 * Values are redeclared
		 */
		Obstacle ob = new Obstacle("Plane",3546 , 3720, 0, 20);
		Runway run1 = new Runway("27L", 3660, 3660, 3660, 3660);
		run1.putObstacle(ob);
		assertEquals(2660, run1.getTORA());
		assertEquals(2660, run1.getTODA());
		assertEquals(2660, run1.getASDA());
		assertEquals(3420, run1.getLDA());
	}

	@Test
	public void BoundaryTestoutLeftofUpperThresh(){
		/*
		 * Obstacle out side (61m) cleared and graded to the Left of upper threshold
		 * Values are original ones
		 */
		Obstacle ob = new Obstacle("Plane",3546 , 3721, 0, 20);
		Runway run1 = new Runway("27L", 3660, 3660, 3660, 3660);
		run1.putObstacle(ob);
		assertEquals(3660, run1.getTORA());
		assertEquals(3660, run1.getTODA());
		assertEquals(3660, run1.getASDA());
		assertEquals(3660, run1.getLDA());
	}
	/* ***************************End of Boundary Testing ******************** */

	/* ***************************Extreme 1 ********************************** */
	@Test 
	public void Extereme1(){
		/*
		 * When obstacle set to null sets values to original
		 */
		Obstacle ob = null;
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(3902, run1.getTORA());
		assertEquals(3902, run1.getTODA());
		assertEquals(3902, run1.getASDA());
		assertEquals(3595, run1.getLDA());
	}

	@Test 
	public void Extreme2(){
		/*
		 * Obstacle too far so sets to original values
		 */
		Obstacle ob = new Obstacle("Plane", -10000, 50, 0, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(3902, run1.getTORA());
		assertEquals(3902, run1.getTODA());
		assertEquals(3902, run1.getASDA());
		assertEquals(3595, run1.getLDA());
	}
	
	@Test 
	public void Extreme3(){
		/*
		 * Obstacle too far so sets to original values
		 */
		Obstacle ob = new Obstacle("Plane", 1000000, 50, 0, 20);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(3902, run1.getTORA());
		assertEquals(3902, run1.getTODA());
		assertEquals(3902, run1.getASDA());
		assertEquals(3595, run1.getLDA());
	}

	@Test 
	public void Extreme4(){
		/*
		 * When heights is too small and large obstacle sets all value to 0
		 */
		Obstacle ob = new Obstacle("Plane", 50, 50, 0, 20);
		Runway run1 = new Runway("09L", 1, 1, 1, 1);
		run1.putObstacle(ob);
		assertEquals(0, run1.getTORA());
		assertEquals(0, run1.getTODA());
		assertEquals(0, run1.getASDA());
		assertEquals(0, run1.getLDA());
	}

	/* *************** End of extreme Testing *************** */

	/* *************** RESA/ALS TOCS Testing ***************** */
	@Test 
	public void RESA1(){
		/*
		 * ALS/TOCS slope too smaall and RESA larger so uses this instead
		 */
		Obstacle ob = new Obstacle("Plane", 50, 50, 0, 1);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.putObstacle(ob);
		assertEquals(3245, run1.getTORA());
		assertEquals(3245, run1.getTODA());
		assertEquals(3245, run1.getASDA());
		assertEquals(3245, run1.getLDA());
	}
	@Test
	public void RESA2(){
		/*
		 * Uses larger value of RESA
		 */
		Obstacle ob = new Obstacle("Plane", 50, 50, 0, 1);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setRESA(250);
		run1.putObstacle(ob);
		assertEquals(3245, run1.getTORA());
		assertEquals(3245, run1.getTODA());
		assertEquals(3245, run1.getASDA());
		assertEquals(3235, run1.getLDA());
	}

	@Test
	public void RESA3(){
		/*
		 * Uses blast allowance
		 */
		Obstacle ob = new Obstacle("Plane", 50, 50, 0, 1);
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setRESA(220);
		run1.putObstacle(ob);
		assertEquals(3245, run1.getTORA());
		assertEquals(3245, run1.getTODA());
		assertEquals(3245, run1.getASDA());
		assertEquals(3245, run1.getLDA());
	}
}