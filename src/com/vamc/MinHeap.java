package com.vamc;
/**
 * 
 * MinHeap Implementation for risingCity
 * @author Vamsidhar Bada
 *
 */
public class MinHeap {
	
	public static final int FRONT_POS = 1;
	
	private Building[] buildingHeap;
	private int heapSize;
	private int maxHeapSize;


	private MinHeap() {
		// Private constructor to avoid illegal object creation
	}
	
	/**
	 * Parameterized Constructor
	 * @param maxHeapSize
	 */
	public MinHeap(int maxHeapSize) {
		this.maxHeapSize = maxHeapSize;
		buildingHeap = new Building[this.maxHeapSize + 1];
		buildingHeap[0] = new Building(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
		this.heapSize = 0;
	}
	
	/**
	 * Returns the heaps size
	 * @return
	 */
	public int getSize() {
		return heapSize;
	}
	
	/**
	 * Returns the heaps maxsize
	 * @return
	 */
	public int getMaxHeapSize() {
		return maxHeapSize;
	}
	
	/**
	 * Function to return the position of the left child for the node currently at pos
	 * @param index
	 * @return
	 */
	private int getLeftChildIndex(int index) {
		return (2*index);
	}

	/**
	 * Function to return the position of the right child for the node currently at give index
	 * @param index
	 * @return
	 */
	private int getRightChildIndex(int index) {
		return (2*index) + 1;
	}

	/**
	 * Function to return the position of the parent for the node currently at position
	 * @param index
	 * @return
	 */
	private int getParentNodeIndex(int index) {
		return index / 2;
	}


	
	/**
	 * Function to build the minHeap using the minHeapify
	 */
	public void minHeapify() {
		int mid = heapSize/2;
		for (int index = mid; index >= 1; index--) {
			minHeapifyAtPosition(index);
		}
	}

	
	/**
	 * Function to heapify the node at given index
	 * @param index
	 */
	public void minHeapifyAtPosition(int index) {

		// If the node is a non-leaf node and greater than any of its child
		if (!isNodeALeaf(index)) {
			
			//If minHeap property is invalid
			if (buildingHeap[getLeftChildIndex(index)] != null && (buildingHeap[index].getExecutedTime() > buildingHeap[getLeftChildIndex(index)].getExecutedTime())
					|| (buildingHeap[getRightChildIndex(index)] != null && (buildingHeap[index].getExecutedTime() > buildingHeap[getRightChildIndex(index)].getExecutedTime()))) {

				// swapHeapNodes with the left child and heapify the left child
				if (buildingHeap[getLeftChildIndex(index)] == null) {
					swapHeapNodes(index, getRightChildIndex(index));
					minHeapifyAtPosition(getRightChildIndex(index));
				} else if (buildingHeap[getRightChildIndex(index)] == null) {
					swapHeapNodes(index, getLeftChildIndex(index));
					minHeapifyAtPosition(getLeftChildIndex(index));
				} else {
					if (buildingHeap[getLeftChildIndex(index)].getExecutedTime() < buildingHeap[getRightChildIndex(index)].getExecutedTime()) {
						swapHeapNodes(index, getLeftChildIndex(index));
						minHeapifyAtPosition(getLeftChildIndex(index));
					}
					// swapHeapNodes with the right child and heapify the right child
					else if (buildingHeap[getLeftChildIndex(index)].getExecutedTime() > buildingHeap[getRightChildIndex(index)].getExecutedTime()) {
						swapHeapNodes(index, getRightChildIndex(index));
						minHeapifyAtPosition(getRightChildIndex(index));
					} else {
						int minimumBuildingNumber = Math.min(buildingHeap[getLeftChildIndex(index)].getBuildingNumber(),
								buildingHeap[getRightChildIndex(index)].getBuildingNumber());
						if (buildingHeap[getLeftChildIndex(index)].getBuildingNumber() == minimumBuildingNumber) {
							swapHeapNodes(index, getLeftChildIndex(index));
							minHeapifyAtPosition(getLeftChildIndex(index));
						} else {
							swapHeapNodes(index, getRightChildIndex(index));
							minHeapifyAtPosition(getRightChildIndex(index));
						}
					}
				}
			} else {
				//If heap has only one child
				if (buildingHeap[getLeftChildIndex(index)] != null
						&& buildingHeap[index].getExecutedTime() == buildingHeap[getLeftChildIndex(index)].getExecutedTime()) {
					if (buildingHeap[index].getBuildingNumber() > buildingHeap[getLeftChildIndex(index)].getBuildingNumber()) {
						swapHeapNodes(index, getLeftChildIndex(index));
						minHeapifyAtPosition(getLeftChildIndex(index));
					}
				}

				if (buildingHeap[getRightChildIndex(index)] != null
						&& buildingHeap[index].getExecutedTime() == buildingHeap[getRightChildIndex(index)].getExecutedTime()) {
					if (buildingHeap[index].getBuildingNumber() > buildingHeap[getRightChildIndex(index)].getBuildingNumber()) {
						swapHeapNodes(index, getRightChildIndex(index));
						minHeapifyAtPosition(getRightChildIndex(index));
					}
				}
			}
		}
	}
	
	
	/**
	 * Function to insert a node into the heap
	 * @param building
	 */
	public void insert(Building building) {
		if (heapSize >= maxHeapSize) {
			return;
		}
		buildingHeap[++heapSize] = building;
	}


	/**
	 * Function to remove and return the minimum element from the heap
	 * @return
	 */
	public Building remove() {
		Building popped = buildingHeap[FRONT_POS];
		buildingHeap[FRONT_POS] = buildingHeap[heapSize];
		buildingHeap[heapSize--] = null;
		minHeapify();
		return popped;
	}


	/**
	 * Function that returns true if the passed node is a leaf node
	 * @param index
	 * @return
	 */
	private boolean isNodeALeaf(int index) {
		if (2 * index > heapSize && 2 * index + 1 > heapSize && index <= heapSize) 
			return true;
		else
			return false;
	}

	
	/**
	 * Function to swap two nodes of the heap 
	 * @param index1
	 * @param index2
	 */
	private void swapHeapNodes(int index1, int index2) {
		Building temp;
		temp = buildingHeap[index1];
		buildingHeap[index1] = buildingHeap[index2];
		buildingHeap[index2] = temp;
	}

	
	/**
	 * Function to print the contents of the heap
	 */
	public void printHeap() {
		for (int i = 1; i <= heapSize / 2; i++) {
			System.out.print("P : " + buildingHeap[i] + " LEFT CHILD : " + buildingHeap[2 * i] + " RIGHT CHILD :" + buildingHeap[2 * i + 1]);
			System.out.println();
		}
	}

	
	/**
	 * Function to get the min from heap
	 * @return
	 */
	public Building getMinimumExecutedTimeBuilding() {
		return buildingHeap[FRONT_POS];
	}


}
