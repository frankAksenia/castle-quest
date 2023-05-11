package clientData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// https://stackoverflow.com/questions/5269250/random-value-from-enum-with-probability
public enum EMapTerrain {

	WATER (7),
	GRASS (24),
	MOUNTAIN (5);
	
	private int weight;
	     
	private EMapTerrain(int weight) {
		this.weight = weight;
	}
	    
	private int getWeight() {
	    return weight;
	}
	    
	private static final List<EMapTerrain> TERRAINS =
	    Collections.unmodifiableList(Arrays.asList(values()));
	    
	private static int sumWeigts() {
		int sum = 0;
	    for(EMapTerrain value: TERRAINS) 
	       sum += value.getWeight();
	    return sum;
	}
	    
	private final static int SIZE = sumWeigts();
	private final static Random RANDOM = new Random();
	    
	public static EMapTerrain getRandomTerrain() {
		int randomNum = RANDOM.nextInt(SIZE);
	    int currentWeightSumm = 0;
	    EMapTerrain terrain = EMapTerrain.GRASS;
	    for(EMapTerrain currentValue: TERRAINS) {
	    	terrain = currentValue;
	    	if(randomNum > currentWeightSumm && randomNum <= (currentWeightSumm + currentValue.getWeight())) 
	    		break;
	        currentWeightSumm += currentValue.getWeight();
	    }
	        return terrain;
	}
}
