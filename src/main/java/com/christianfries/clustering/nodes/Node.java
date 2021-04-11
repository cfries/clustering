package com.christianfries.clustering.nodes;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Node {
	private static AtomicInteger nextID = new AtomicInteger(0);

	private final int id = nextID.getAndIncrement();
	private final List<Integer> mandatoryClustering;

	public Node(List<Integer> mandatoryClustering) {
		super();
		this.mandatoryClustering = mandatoryClustering;
	}

	public int getId() {
		return id;
	}

	public List<Integer> getMandatoryClustering() {
		return mandatoryClustering;
	}
}