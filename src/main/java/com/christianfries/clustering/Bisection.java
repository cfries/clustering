package com.christianfries.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.christianfries.clustering.nodes.Node;
import com.christianfries.clustering.nodes.Preference;
import com.christianfries.clustering.nodes.SzenarioValuation;

public class Bisection {

	private final List<Node> nodes;
	private final int[][] mandatorySizes;
	private final Map<Node, List<Preference>> perferences;

	public Bisection(List<Node> nodes, int[][] mandatorySizes, Map<Node, List<Preference>> perferences) {
		super();
		this.nodes = nodes;
		this.mandatorySizes = mandatorySizes;
		this.perferences = perferences;
	}

	public static void main(String[] args) {

		// Create a class of 30 with some mandatory clustering
		final List<Node> nodes = new ArrayList<>();
		nodes.add(new Node(List.of(0,0)));
		nodes.add(new Node(List.of(1,0)));
		nodes.add(new Node(List.of(0,1)));
		nodes.add(new Node(List.of(0,0)));
		nodes.add(new Node(List.of(1,0)));
		nodes.add(new Node(List.of(1,1)));
		nodes.add(new Node(List.of(0,0)));
		nodes.add(new Node(List.of(0,2)));
		nodes.add(new Node(List.of(1,0)));
		nodes.add(new Node(List.of(0,0)));
		nodes.add(new Node(List.of(0,2)));
		nodes.add(new Node(List.of(0,1)));
		nodes.add(new Node(List.of(0,0)));
		nodes.add(new Node(List.of(0,0)));
		nodes.add(new Node(List.of(0,1)));
		nodes.add(new Node(List.of(0,0)));
		nodes.add(new Node(List.of(0,0)));
		nodes.add(new Node(List.of(1,0)));
		nodes.add(new Node(List.of(1,1)));
		nodes.add(new Node(List.of(1,1)));
		nodes.add(new Node(List.of(1,1)));
		nodes.add(new Node(List.of(0,2)));
		nodes.add(new Node(List.of(0,1)));
		nodes.add(new Node(List.of(0,1)));
		nodes.add(new Node(List.of(0,1)));
		nodes.add(new Node(List.of(1,0)));
		nodes.add(new Node(List.of(0,2)));
		nodes.add(new Node(List.of(0,2)));
		nodes.add(new Node(List.of(1,2)));

		final int[][] mandatorySizes = new int[][] { new int[] {9,6}, new int[] {8,6,1} };

		// Create a list of preferences
		final List<List<Integer>> learningGoups = List.of(
				List.of(10,16),
				List.of(7,12,26),
				List.of(1,5),
				List.of(2,6,14,15,20),
				List.of(0,4,8,17,19,23)
				);

		// Create preference form learning groups
		final Map<Node, List<Preference>> perferences = new HashMap<Node, List<Preference>>();
		for(int i=0; i<nodes.size(); i++) {
			for(final List<Integer> learningGroup: learningGoups) {
				if(learningGroup.contains(i)) {
					final int nodeIndex = i;
					final List<Preference> friends = learningGroup.stream().filter(k -> k != nodeIndex).map(k -> new Preference(nodes.get(k),1L)).collect(Collectors.toList());
					perferences.put(nodes.get(i), friends);
				}
			}
		}

		Bisection clustering = new Bisection(nodes, mandatorySizes, perferences);
		clustering.bisections();
	}

	private void bisections() {
		// Setup completed
		System.out.println("Working on the following input to the problem:");
		for(final Node node : nodes) {
			System.out.print(node.getId() + "\t Attributes:");
			for(final Integer cluster : node.getMandatoryClustering()) {
				System.out.print(cluster + " ");
			}
			System.out.print("\t Preferences:");
			final List<Preference> friends = perferences.get(node);
			if(friends != null) {
				for(final Preference friend : friends) {
					System.out.print(friend.getNode().getId() + " ");
				}
			}
			System.out.print("\n");
		}

		final List<SzenarioValuation> valuations = Collections.synchronizedList(new ArrayList<SzenarioValuation>());

		// Searching
		final AtomicLong numberOfAdmissibles = new AtomicLong(0);
		final AtomicLong bestValue = new AtomicLong(Long.MIN_VALUE);
		LongStream.range(0L, (long)Math.pow(2,nodes.size())).parallel().forEach(szenario -> {
			// Check mandatory
			boolean admissible = true;
			final int[][] mandatoryCounts = new int[][] { new int[2], new int[3] };
			for(int i=0; i<nodes.size();i++) {
				if((szenario & (1L << i)) != 0) {
					final List<Integer> mandatory = nodes.get(i).getMandatoryClustering();
					for(int j=0; j<mandatory.size(); j++) {
						final int k = mandatory.get(j);
						mandatoryCounts[j][k]++;
						if(mandatoryCounts[j][k] > mandatorySizes[j][k]) {
							admissible = false;
						}
					}
				}
				if(!admissible) {
					break;
				}
			}
			if(admissible) {
				for(int j=0; j<mandatoryCounts.length; j++) {
					for(int k=0; k<mandatoryCounts[j].length; k++) {
						if(mandatoryCounts[j][k] != mandatorySizes[j][k]) {
							admissible = false;
							break;
						}
					}
				}
			}
			if(admissible) {
				Long value = 0L;

				numberOfAdmissibles.incrementAndGet();
				// Value szenario
				for(final Node node : nodes) {
					final int i = nodes.indexOf(node);
					final List<Preference> friends = perferences.get(node);
					if(friends != null) {
						for(final Preference friend : friends) {
							final int k = nodes.indexOf(friend.getNode());
							if(((szenario & (1L << i)) != 0 ? 1 : 0)  == ((szenario & (1L << k))  != 0 ? 1 : 0)) {
								value += friend.getWeight();
							}
							else {
								value -= friend.getWeight();
							}
						}
					}
				}
				valuations.add(new SzenarioValuation(szenario, value));
				final Long currentValue = value;
				bestValue.getAndUpdate(v -> currentValue > v ? currentValue : v);
			}
			if(szenario % Math.pow(2, 24) == 0) {
				System.out.println("Current 10 best szenarios:");
				printBestSzenarios(10, valuations, nodes);
			}
		});


		System.out.println("_".repeat(79));
		System.out.println("_".repeat(79));
		System.out.println("Final Result");
		System.out.println("Szenarios matching size constrain..: " + numberOfAdmissibles.get());
		System.out.println("Best value achived by a szenario...: " + bestValue);
		System.out.println("_".repeat(79));
		System.out.println("The 100 best szenarios: (running number, value, ids of nodes that should be placed together in a group)");
		printBestSzenarios(100, valuations, nodes);
	}

	private static void printBestSzenarios(int numberOfSzenarios, List<SzenarioValuation> valuations, List<Node> nodes) {

		synchronized(valuations) {
			Collections.sort(valuations, (x,y) -> -x.getValue().compareTo(y.getValue()));
			final Iterator<SzenarioValuation> valuation = valuations.iterator();
			for(int i=1; i<=numberOfSzenarios; i++) {
				if(!valuation.hasNext()) {
					break;
				}
				final SzenarioValuation valuationSzenario = valuation.next();
				System.out.print(i + "\t" + valuationSzenario.getValue() + "\t");
				for(int k=0; k<nodes.size(); k++) {
					if((valuationSzenario.getSzenario() & (1L << k)) != 0) {
						System.out.print(k + " ");
					}
				}
				System.out.println();
			}
		}
		System.out.println("_".repeat(79));
	}
}
