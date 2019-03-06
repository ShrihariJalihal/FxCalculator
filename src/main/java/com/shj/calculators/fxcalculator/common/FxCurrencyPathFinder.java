package com.shj.calculators.fxcalculator.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FxCurrencyPathFinder {
	private final Logger LOG = LogManager.getLogger(FxCurrencyPathFinder.class.getName());
	
	private Map<FxCurrency, LinkedList<FxCurrency>> adjencyListMap;

	public FxCurrencyPathFinder() {
		this.adjencyListMap = new HashMap<>();
	}

	public void addEdge(FxCurrency fromVertex, FxCurrency toVertex) {
		adjencyListMap.putIfAbsent(fromVertex, new LinkedList<FxCurrency>());
		adjencyListMap.get(fromVertex).add(toVertex);
		LOG.info("Added Edge for vertex: "+fromVertex+"  to: "+toVertex);
	}

	private boolean hasPath(Stack<FxCurrency> trackerPath, FxCurrency fromVertex, FxCurrency toVertex) {
		if (trackerPath.contains(fromVertex) && trackerPath.contains(toVertex)) {
			if (trackerPath.indexOf(fromVertex) < trackerPath.indexOf(toVertex)) { // from must be before
				return true;
			}
		}
		return false;
	}

	/**
	 * using Depth First Search
	 * 
	 * @param fromVertex
	 * @param toVertex
	 * @return
	 */
	public List<FxCurrency> findPath(FxCurrency fromVertex, FxCurrency toVertex) {
		LOG.info("find path started");
		List<FxCurrency> currencyPath = null;
		Stack<FxCurrency> tracker = new Stack<>();
		Map<FxCurrency, Boolean> visited = new HashMap<>();

		Stack<FxCurrency> stack = new Stack<>();
		stack.add(fromVertex);

		visited.put(fromVertex, true);

		while (!stack.isEmpty()) {
			FxCurrency current = stack.pop();
			visited.put(current, true);

			tracker.add(current);

			if (hasPath(tracker, fromVertex, toVertex) && currencyPath == null) {
				currencyPath = new ArrayList<>(tracker);
				LOG.info("returning the path "+currencyPath.toString());
				return currencyPath;
			}

			// If we have exhausted all paths...
			if (adjencyListMap.get(current) == null) {
				adjencyListMap.put(current, new LinkedList<>());
			}
			Iterator<FxCurrency> neighborsVisited = adjencyListMap.get(current).listIterator();
			if (allNeighborsVisited(neighborsVisited, visited)) {
				// Pop until we find a node with neighbors
				// that haven't been visited
				popTillYouDrop(tracker, visited);
			}

			Iterator<FxCurrency> neighbors = adjencyListMap.get(current).listIterator();
			while (neighbors.hasNext()) {
				FxCurrency neighborVertex = neighbors.next();
				if (visited.get(neighborVertex) == null || !visited.get(neighborVertex)) {
					if (visited.get(neighborVertex) == null) {
						visited.put(neighborVertex, false);
					}

					stack.add(neighborVertex);
				}
			}

		}
		LOG.info("Not returning the path as not found");
		return null;
	}

	private boolean allNeighborsVisited(Iterator<FxCurrency> neighbors, Map<FxCurrency, Boolean> visited) {
		while (neighbors.hasNext()) {
			FxCurrency neighborVertex = neighbors.next();
			if (visited.get(neighborVertex) == null || !visited.get(neighborVertex)) {
				if (visited.get(neighborVertex) == null) {
					visited.put(neighborVertex, false);
				}

				return false;
			}
		}
		return true;
	}

	private void popTillYouDrop(Stack<FxCurrency> tracker, Map<FxCurrency, Boolean> visited) {
		if (tracker.isEmpty())
			return;

		FxCurrency current = tracker.peek();

		Iterator<FxCurrency> j = adjencyListMap.get(current).listIterator();

		if (allNeighborsVisited(j, visited)) {
			tracker.pop();
			popTillYouDrop(tracker, visited);
		}
	}
}
