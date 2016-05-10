package hillbillies.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.som.annotate.Model;

public class Helper {
	
	/**
	 * Checks whether the value lies between a minimum value and a maximum value.
	 * 
	 * @pre		minValue is smaller than maxValue.
	 * 			| (minValue <= maxValue)
	 * @param 	value
	 * 			The value to be checked.
	 * @param 	minValue
	 * 			The minimum value.
	 * @param 	maxValue
	 * 			The maximum value.
	 * @return	Whether value is in the range between minValue and maxValue
	 * 			| result == (minValue <= value) && (value <= maxValue))
	 */
    @Model
	public static boolean inRange(int value, int minValue, int maxValue) {
		assert(minValue <= maxValue);
		return ((minValue <= value) && (value <= maxValue));
	}
    
	/**
	 * Clips the value between minValue and maxValue
	 * 
	 * @param 	value
	 * 			The value that needs to be clipped.
	 * @param 	minValue
	 * 			The minimum value.
	 * @param 	maxValue
	 * 			The maximum value.
	 * @return	The clipped value, i.e. the value if inRange(Value,minValue,maxValue)
	 * 			otherwise, the value in the range that is closest to value.
	 * 			| if (value > maxValue) then result == maxValue
	 * 			| else if (value < minValue) then result == minValue
	 * 			| else result == value
	 */
    @Model
	public static int clip(int value, int minValue, int maxValue){
		if (value > maxValue)
			value = maxValue;
		else if (value <minValue)
			value = minValue;
		return value;
	}
	
	public static boolean arrayListContainsIntArray(List<int[]> list, int[] elementToSearch){
		for (int[] elementFromList: list){
			if (Arrays.equals(elementFromList, elementToSearch))
				return true;
		}
		return false;
	}
	
	public static Integer[] getKeyArrayListInt(List<Integer[]> list, Integer[] elementToSearch){
		for (Integer[] elementFromList: list){
			if (Arrays.equals(elementFromList, elementToSearch))
				return elementFromList;
		}
		return null;
	}
	
	public static boolean mapContainsIntArray(Map<int[],Double> map, int[] elementToSearch){
		for (int[] elementFromList: map.keySet()){
			if (Arrays.equals(elementFromList, elementToSearch))
				return true;
		}
		return false;
	}
	
	public static int[] getKeyMapIntArray(Map<int[],Double> map, int[] elementToSearch){
		for (int[] elementFromList: map.keySet()){
			if (Arrays.equals(elementFromList, elementToSearch))
				return elementFromList;
		}
		return null;
	}

	public static Integer[] converter(int[] toConvert){
		Integer[] result = new Integer[toConvert.length];
		for (int i=0; i<toConvert.length; i++){
			result[i] = new Integer(toConvert[i]);
		}
		return result;
	}
	
	public static int[] converter(Integer[] toConvert){
		int[] result = new int[toConvert.length];
		for (int i=0; i<toConvert.length; i++){
			result[i] = toConvert[i].intValue();
		}
		return result;
	}
	
	public static boolean containsCube(List<Integer[]> list, int[] cubeToInspect){
		for (Integer[] cube: list){
			if (Arrays.equals(Helper.converter(cube),cubeToInspect))
				return true;
		}
		return false;
	}
	
}
