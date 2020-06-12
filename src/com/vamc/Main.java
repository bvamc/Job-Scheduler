package com.vamc;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	private static long global_time = 0;
	private static int operationIndex = 0;
	private static Building selectedBuilding;

	/**
	 * Main function to execute the flow of the program. Takes filepath as the
	 * argument
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		boolean isInitialized = Boolean.FALSE;
		String line;
		String file = args[0];

		if (file == null || file.isEmpty()) {
			System.out.println("Failed to get filename. Please check code");
			return;
		}
		ArrayList<String> opType = new ArrayList<>();
		ArrayList<Integer> opTime = new ArrayList<>();

		// Read input
		BufferedReader reader = new BufferedReader(new FileReader(file));

		while ((line = reader.readLine()) != null) {
			opTime.add(Integer.valueOf(line.split(":")[0]));
			opType.add(line.split(":")[1]);
		}
		reader.close();

		// Prepare output
		BufferedWriter buffWriter = null;

		// Initialize minHeap and RedblackTree
		MinHeap minHeap = new MinHeap(2000);
		RBT redblackTree = new RBT();

		int daysOperated = 0; // Base case

		try {
			buffWriter = new BufferedWriter(new FileWriter("output_file.txt"));
			while (true) {
				if (isInitialized && minHeap.getSize() == 0 && operationIndex >= opTime.size()) {
					break;
				}

				if (!isInitialized) {
					// 1st Operation
					executeCommand(minHeap, redblackTree, opTime, opType, buffWriter);
					isInitialized = Boolean.TRUE;
					selectedBuilding = minHeap.getMinimumExecutedTimeBuilding();
					daysOperated = 0;
				} else {
					executeCommand(minHeap, redblackTree, opTime, opType, buffWriter);
					if (minHeap.getSize() == 1)
						selectedBuilding = minHeap.getMinimumExecutedTimeBuilding();
				}

				global_time++;

				if (daysOperated == 0) {
					// After insert, check heap for the best building
					minHeap.minHeapify();
					selectedBuilding = minHeap.getMinimumExecutedTimeBuilding();
				}
				// Work on the building
				if (selectedBuilding != null) {
					daysOperated++;
					selectedBuilding.setExecutedTime(selectedBuilding.getExecutedTime() + 1);
					if (selectedBuilding.getExecutedTime() == selectedBuilding.getTotalTime()) {
						print(selectedBuilding, global_time, buffWriter);
						minHeap.remove();
						redblackTree.removeNode(selectedBuilding);
						minHeap.minHeapify();
						selectedBuilding = minHeap.getMinimumExecutedTimeBuilding();
						daysOperated = 0;
					}
				}

				if (daysOperated == 5) {
					minHeap.minHeapify();
					selectedBuilding = minHeap.getMinimumExecutedTimeBuilding();
					daysOperated = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (buffWriter != null)
				buffWriter.close();
		}
	}

	/**
	 * Inserts the building object into minheap and redblackTree
	 * 
	 * @param minHeap
	 * @param redblackTree
	 * @param building
	 * @return
	 */
	public static boolean insert(MinHeap minHeap, RBT redblackTree, Building building) {
		if (!redblackTree.addNode(building)) {
			return false;
		}
		minHeap.insert(building);
		return true;
	}

	/**
	 * Prints the building number and time, throws exception if output stream is
	 * closed
	 * 
	 * @param building
	 * @param time
	 * @param bw
	 * @throws IOException
	 */
	private static void print(Building building, long time, BufferedWriter bw) throws IOException {
		bw.write("(" + building.getBuildingNumber() + "," + time + ")");
		bw.newLine();
	}

	/**
	 * Executes the operations based on the inputFile Takes heap, RBT, timeList and
	 * operationsList as parameters When global_time matches time in the input file,
	 * corresponding operation is performed
	 * 
	 * @param minHeap
	 * @param rbt
	 * @param timeList
	 * @param operation
	 * @param bw
	 * @throws IOException
	 */
	private static void executeCommand(MinHeap minHeap, RBT rbt, List<Integer> timeList, List<String> operation,
			BufferedWriter bw) throws IOException {
		if (operationIndex < timeList.size() && global_time == timeList.get(operationIndex)) {
			// doOperation
			if (operation.get(operationIndex).contains("Insert")) {
				String buildingNumber = operation.get(operationIndex).substring(
						operation.get(operationIndex).indexOf("(") + 1, operation.get(operationIndex).indexOf(","));
				String totalTime = operation.get(operationIndex).substring(
						operation.get(operationIndex).indexOf(",") + 1, operation.get(operationIndex).indexOf(")"));
				if (!insert(minHeap, rbt,
						new Building(Integer.parseInt(buildingNumber), 0, Integer.parseInt(totalTime)))) {
					return;
				}
			} else if (operation.get(operationIndex).contains("Print")) {
				String range = operation.get(operationIndex).substring(operation.get(operationIndex).indexOf("(") + 1,
						operation.get(operationIndex).indexOf(")"));
				if (range.contains(",")) {
					ArrayList<Building> list = RBT.searchBuildingNumberInRange(new ArrayList<>(), RBT.root,
							Integer.parseInt(range.split(",")[0]), Integer.parseInt(range.split(",")[1]));
					if (list.isEmpty()) {
						bw.write("(0,0,0)");
						bw.newLine();
					} else {
						StringBuilder result = new StringBuilder();
						for (Building building : list) {
							result.append("(" + building.getBuildingNumber() + "," + building.getExecutedTime() + ","
									+ building.getTotalTime() + ")" + ",");
						}
						bw.write(result.substring(0, result.length() - 1));
						bw.newLine();
					}
				} else {
					Building building = RBT.searchBuildingNumber(RBT.root, Integer.parseInt(range));
					if (building == null) {
						bw.write("(0,0,0)");
						bw.newLine();
					} else {
						bw.write("(" + building.getBuildingNumber() + "," + building.getExecutedTime() + ","
								+ building.getTotalTime() + ")");
						bw.newLine();
					}
				}
			}
			operationIndex++;
		}
	}
}
