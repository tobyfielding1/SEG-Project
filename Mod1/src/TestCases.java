import static org.junit.Assert.*;

import org.junit.Test;

public class TestCases {
	/* **************** Regression Testing ************************* */
	@Test
	public void scenario1(){
		Obstacle ob = new Obstacle("Plane", -50, 3646, 0, 12);
		
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setObstacle(ob);
		assertEquals(3345, run1.getTORA());
		assertEquals(3345, run1.getTODA());
		assertEquals(3345, run1.getASDA());
		assertEquals(2985, run1.getLDA());
		
		Runway run2 = new Runway("27R", 3884, 3962, 3884, 3884);
		run2.setObstacle(ob);
		assertEquals(2986, run2.getTORA());
		assertEquals(2986, run2.getTODA());
		assertEquals(2986, run2.getASDA());
		assertEquals(3346, run2.getLDA());
	}
	@Test
	public void scenario2(){
		Obstacle ob = new Obstacle("Plane", 2853, 500, -20, 25);
		
		Runway run1 = new Runway("09R", 3660, 3660, 3660, 3353);
		run1.setObstacle(ob);
		assertEquals(1850, run1.getTORA());
		assertEquals(1850, run1.getTODA());
		assertEquals(1850, run1.getASDA());
		assertEquals(2553, run1.getLDA());
		
		Runway run2 = new Runway("27L", 3660, 3660, 3660, 3660);
		run2.setObstacle(ob);
		assertEquals(2860, run2.getTORA());
		assertEquals(2860, run2.getTODA());
		assertEquals(2860, run2.getASDA());
		assertEquals(1850, run2.getLDA());
	}
	@Test
	public void scenario3(){
		Obstacle ob = new Obstacle("Plane", 150, 3203, 60, 15);
		
		Runway run1 = new Runway("09R", 3660, 3660, 3660, 3353);
		run1.setObstacle(ob);
		assertEquals(2903, run1.getTORA());
		assertEquals(2903, run1.getTODA());
		assertEquals(2903, run1.getASDA());
		assertEquals(2393, run1.getLDA());
		
		Runway run2 = new Runway("27L", 3660, 3660, 3660, 3660);
		run2.setObstacle(ob);
		assertEquals(2393, run2.getTORA());
		assertEquals(2393, run2.getTODA());
		assertEquals(2393, run2.getASDA());
		assertEquals(2903, run2.getLDA());
	}
	@Test
	public void scenario4(){
		Obstacle ob = new Obstacle("Plane", 3546, 50, 20, 20);
		
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setObstacle(ob);
		assertEquals(2793, run1.getTORA());
		assertEquals(2793, run1.getTODA());
		assertEquals(2793, run1.getASDA());
		assertEquals(3246, run1.getLDA());
		
		Runway run2 = new Runway("27R", 3884, 3962, 3884, 3884);
		run2.setObstacle(ob);
		assertEquals(3534, run2.getTORA());
		assertEquals(3612, run2.getTODA());
		assertEquals(3534, run2.getASDA());
		assertEquals(2774, run2.getLDA());
	}
	/* ***************** End of regression testing ****************************/
	
	/* ***************** Boundary Testing ****************************/
		
	@Test
	public void BoundaryTestInNorth(){
		Obstacle ob = new Obstacle("Plane", 3546, 50, 74, 20);
		
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setObstacle(ob);
		assertEquals(2793, run1.getTORA());
		assertEquals(2793, run1.getTODA());
		assertEquals(2793, run1.getASDA());
		assertEquals(3246, run1.getLDA());
	}
	
	@Test
	public void BoundaryTestOnBoundaryNorth(){
		Obstacle ob = new Obstacle("Plane", 3546, 50, 75, 20);
		
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setObstacle(ob);
		assertEquals(2793, run1.getTORA());
		assertEquals(2793, run1.getTODA());
		assertEquals(2793, run1.getASDA());
		assertEquals(3246, run1.getLDA());
	}
	
	@Test
	public void BoundaryTestOutNorth(){
		Obstacle ob = new Obstacle("Plane", 3546, 50, 76, 20);
		
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setObstacle(ob);
		assertEquals(3902, run1.getTORA());
		assertEquals(3902, run1.getTODA());
		assertEquals(3902, run1.getASDA());
		assertEquals(3595, run1.getLDA());
	}
	
	@Test 
	public void BoundaryTestInSouth(){
		Obstacle ob = new Obstacle("Plane", 3546, 50, -74, 20);
		
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setObstacle(ob);
		assertEquals(2793, run1.getTORA());
		assertEquals(2793, run1.getTODA());
		assertEquals(2793, run1.getASDA());
		assertEquals(3246, run1.getLDA());
	}
	
	@Test
	public void BoundaryTestOnBoundarySouth(){
		Obstacle ob = new Obstacle("Plane", 3546, 50, -75, 20);
		
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setObstacle(ob);
		assertEquals(2793, run1.getTORA());
		assertEquals(2793, run1.getTODA());
		assertEquals(2793, run1.getASDA());
		assertEquals(3246, run1.getLDA());
	}
	
	@Test
	public void BoundaryTestOutBoundarySouth(){
		Obstacle ob = new Obstacle("Plane", 3546, 50, -76, 20);
		
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setObstacle(ob);
		assertEquals(3902, run1.getTORA());
		assertEquals(3902, run1.getTODA());
		assertEquals(3902, run1.getASDA());
		assertEquals(3595, run1.getLDA());
	}
	@Test
	 public void BoundaryTestInLeftofLowerThresh(){
		 Obstacle ob = new Obstacle("Plane", -59, 50, 0, 20);
			
			Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
			run1.setObstacle(ob);
			assertEquals(3902, run1.getTORA());
			assertEquals(3902, run1.getTODA());
			assertEquals(3902, run1.getASDA());
			assertEquals(3595, run1.getLDA());
	 }
	
	 @Test
	 public void BoundaryTestOnLeftofLowerThresh(){
		 Obstacle ob = new Obstacle("Plane", -60, 50, 0, 20);
			
			Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
			run1.setObstacle(ob);
			assertEquals(3902, run1.getTORA());
			assertEquals(3902, run1.getTODA());
			assertEquals(3902, run1.getASDA());
			assertEquals(3595, run1.getLDA());
	 }
	 @Test
	 public void BoundaryTestOutLeftofLowerThresh(){
		 Obstacle ob = new Obstacle("Plane", -61, 50, 0, 20);
			
			Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
			run1.setObstacle(ob);
			assertEquals(3902, run1.getTORA());
			assertEquals(3902, run1.getTODA());
			assertEquals(3902, run1.getASDA());
			assertEquals(3595, run1.getLDA());
	 }
	 
	 @Test
	 public void BoundaryTestInRightofLowerThresh(){
		 Obstacle ob = new Obstacle("Plane", 3961, 50, 0, 20);
			
			Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
			run1.setObstacle(ob);
			assertEquals(3902, run1.getTORA());
			assertEquals(3902, run1.getTODA());
			assertEquals(3902, run1.getASDA());
			assertEquals(3595, run1.getLDA());
	 }
	 
	 @Test
	 public void BoundaryTestOnRightofLowerThresh(){
		 Obstacle ob = new Obstacle("Plane", 3962, 50, 0, 20);
			
			Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
			run1.setObstacle(ob);
			assertEquals(3902, run1.getTORA());
			assertEquals(3902, run1.getTODA());
			assertEquals(3902, run1.getASDA());
			assertEquals(3595, run1.getLDA());
	 }
	 @Test
	 public void BoundaryTestOutRightofLowerThresh(){
		 Obstacle ob = new Obstacle("Plane", 3963, 50, 0, 20);
			
			Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
			run1.setObstacle(ob);
			assertEquals(3902, run1.getTORA());
			assertEquals(3902, run1.getTODA());
			assertEquals(3902, run1.getASDA());
			assertEquals(3595, run1.getLDA());
	 }
	 @Test
	 public void BoundaryTestInRightofUpperThresh(){
		 Obstacle ob = new Obstacle("Plane",3546 , -59, -76, 20);
			
		 Runway run1 = new Runway("27L", 3660, 3660, 3660, 3660);
			run1.setObstacle(ob);
			assertEquals(3902, run1.getTORA());
			assertEquals(3902, run1.getTODA());
			assertEquals(3902, run1.getASDA());
			assertEquals(3595, run1.getLDA());
	 }
	 @Test
	 public void BoundaryTestOnRightofUpperThresh(){
		 Obstacle ob = new Obstacle("Plane",3546 , -60, -76, 20);
			
		 Runway run1 = new Runway("27L", 3660, 3660, 3660, 3660);
			run1.setObstacle(ob);
			assertEquals(3902, run1.getTORA());
			assertEquals(3902, run1.getTODA());
			assertEquals(3902, run1.getASDA());
			assertEquals(3595, run1.getLDA());
	 }
	 @Test
	 public void BoundaryTestOutRightofUpperThresh(){
		 Obstacle ob = new Obstacle("Plane",3546 , -61, -76, 20);
			
		 Runway run1 = new Runway("27L", 3660, 3660, 3660, 3660);
			run1.setObstacle(ob);
			assertEquals(3902, run1.getTORA());
			assertEquals(3902, run1.getTODA());
			assertEquals(3902, run1.getASDA());
			assertEquals(3595, run1.getLDA());
	 }
	 
	 @Test
	 public void BoundaryTestInLeftofUpperThresh(){
		 Obstacle ob = new Obstacle("Plane",3546 , 3719, -76, 20);
			
		 Runway run1 = new Runway("27L", 3660, 3660, 3660, 3660);
			run1.setObstacle(ob);
			assertEquals(3902, run1.getTORA());
			assertEquals(3902, run1.getTODA());
			assertEquals(3902, run1.getASDA());
			assertEquals(3595, run1.getLDA());
	 }
	 
	 @Test
	 public void BoundaryTestonLeftofUpperThresh(){
		 Obstacle ob = new Obstacle("Plane",3546 , 3720, -76, 20);
			
		 Runway run1 = new Runway("27L", 3660, 3660, 3660, 3660);
			run1.setObstacle(ob);
			assertEquals(3902, run1.getTORA());
			assertEquals(3902, run1.getTODA());
			assertEquals(3902, run1.getASDA());
			assertEquals(3595, run1.getLDA());
	 }
	 
	 @Test
	 public void BoundaryTestoutLeftofUpperThresh(){
		 Obstacle ob = new Obstacle("Plane",3546 , 3721, -76, 20);
			
		 Runway run1 = new Runway("27L", 3660, 3660, 3660, 3660);
			run1.setObstacle(ob);
			assertEquals(3902, run1.getTORA());
			assertEquals(3902, run1.getTODA());
			assertEquals(3902, run1.getASDA());
			assertEquals(3595, run1.getLDA());
	 }
	 /* ***************************End of Boundary Testing ******************** */
	 
	 /* ***************************Extreme 1 ********************************** */
	@Test 
	public void Extereme1(){
		Obstacle ob = null;
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setObstacle(ob);
		assertEquals(3902, run1.getTORA());
		assertEquals(3902, run1.getTODA());
		assertEquals(3902, run1.getASDA());
		assertEquals(3595, run1.getLDA());
	}
	
	@Test 
	public void Extreme2(){
		Obstacle ob = new Obstacle("Plane", -10000, 50, 0, 20);
		
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setObstacle(ob);
		assertEquals(3902, run1.getTORA());
		assertEquals(3902, run1.getTODA());
		assertEquals(3902, run1.getASDA());
		assertEquals(3595, run1.getLDA());
	}
	
	@Test 
	public void Extreme3(){
		Obstacle ob = new Obstacle("Plane", 50, 50, 0, 20);
		
		Runway run1 = new Runway("09L", 1, 1, 1, 1);
		run1.setObstacle(ob);
		assertEquals(0, run1.getTORA());
		assertEquals(0, run1.getTODA());
		assertEquals(0, run1.getASDA());
		assertEquals(0, run1.getLDA());
	}
	
	/* *************** End of extreme Testing *************** */
	
	/* *************** RESA/ALS TOCS Testing ***************** */
	@Test 
	public void RESA1(){
		Obstacle ob = new Obstacle("Plane", 50, 50, 0, 1);
		
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setObstacle(ob);
		assertEquals(3245, run1.getTORA());
		assertEquals(3245, run1.getTODA());
		assertEquals(3245, run1.getASDA());
		assertEquals(3245, run1.getLDA());
	}
	@Test
	public void RESA2(){
		Obstacle ob = new Obstacle("Plane", 50, 50, 0, 1);
		
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setRESA(250);
		run1.setObstacle(ob);
		assertEquals(3245, run1.getTORA());
		assertEquals(3245, run1.getTODA());
		assertEquals(3245, run1.getASDA());
		assertEquals(3235, run1.getLDA());
	}
	
	@Test
	public void RESA3(){
		Obstacle ob = new Obstacle("Plane", 50, 50, 0, 1);
		
		Runway run1 = new Runway("09L", 3902, 3902, 3902, 3595);
		run1.setRESA(220);
		run1.setObstacle(ob);
		assertEquals(3245, run1.getTORA());
		assertEquals(3245, run1.getTODA());
		assertEquals(3245, run1.getASDA());
		assertEquals(3245, run1.getLDA());
	}
	
	
}
