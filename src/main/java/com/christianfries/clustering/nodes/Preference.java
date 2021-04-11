package com.christianfries.clustering.nodes;

public class Preference {
	private final Node node;
	private final Long weight;

	public Preference(Node node, Long weight) {
		super();
		this.node = node;
		this.weight = weight;
	}

	public Node getNode() {
		return node;
	}

	public Long getWeight() {
		return weight;
	}

}