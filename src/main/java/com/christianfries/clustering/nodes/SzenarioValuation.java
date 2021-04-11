package com.christianfries.clustering.nodes;


public class SzenarioValuation implements Comparable<SzenarioValuation> {
	private Long szenario;
	private Long value;

	public SzenarioValuation(Long szenario, Long value) {
		super();
		this.szenario = szenario;
		this.value = value;
	}

	public Long getSzenario() {
		return szenario;
	}

	public Long getValue() {
		return value;
	}

	@Override
	public int compareTo(SzenarioValuation o) {
		return this.szenario.compareTo(o.szenario);
	}
}