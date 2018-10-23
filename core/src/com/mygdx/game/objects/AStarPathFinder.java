package com.mygdx.game.objects;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;

public class AStarPathFinder {
	
	Triangulator triangulator;
	ArrayList<Triangulator.TriangulatedNode> nodes;
	ArrayList<Vector2> points;
	
	public AStarPathFinder(ArrayList<Vector2> points) {
		this.points = Helper.vector2ListHardClone(points);
		triangulator = new Triangulator(this.points);
		triangulator.setThreshold(0.05f);
		nodes = triangulator.triangulate();
	}
	
	static class AStarNode{
		Triangulator.TriangulatedNode node;
		float g;
		float h;
		AStarNode parent;
		
		public AStarNode(Triangulator.TriangulatedNode node, float g, float h, AStarNode parent) {
			this.node = node;
			this.g = g;
			this.h = h;
			this.parent = parent;
		}
		
		public float getF() {
			return g + h;
		}
	}
	
	private static float getG(Triangulator.TriangulatedNode node,Triangulator.TriangulatedNode start) {
		return start.point.cpy().sub(node.point).len();
	}
	
	private static float getH(Triangulator.TriangulatedNode node,Triangulator.TriangulatedNode end) {
		return end.point.cpy().sub(node.point).len();
	}
	
	public Triangulator.TriangulatedNode findFromVector2(int index){
		for(Triangulator.TriangulatedNode n : nodes) {
			if(n.point == points.get(index)) {
				return n;
			}
		}
		
		return null;
	}
	
	public AStarNode lowestFFromList(ArrayList<AStarNode> list) {
		AStarNode lowest = list.get(0);
		for(AStarNode node : list) {
			if(node.getF() < lowest.getF()) {
				lowest = node;
			}
		}
		return lowest;
	}
	
	public ArrayList<Vector2> reconstructPath(AStarNode node){
		ArrayList<Vector2> path = new ArrayList<Vector2>();
		AStarNode current = node;
		while(current != null) {
			path.add(current.node.point);
			current = current.parent;
		}
		Collections.reverse(path);
		return path;
	}
	
	public boolean isNodeInStarList(Triangulator.TriangulatedNode node, ArrayList<AStarNode> list) {
		for(int i = 0; i < list.size(); i ++) {
			if(node.point == list.get(i).node.point){
				return true;
			}
		}
		return false;
	}
	
	public AStarNode getNodeInStarList(Triangulator.TriangulatedNode node, ArrayList<AStarNode> list) {
		for(int i = 0; i < list.size(); i ++) {
			if(node.point == list.get(i).node.point){
				return list.get(i);
			}
		}
		return null;
	}
	

	public ArrayList<Vector2> findPath(int indexStart, int indexEnd){
		Triangulator.TriangulatedNode startPosition = findFromVector2(indexStart);
		if(startPosition == null) return null;
		Triangulator.TriangulatedNode endPosition = findFromVector2(indexEnd);
		if(endPosition == null) return null;
		//inicia as listas
		ArrayList<AStarNode> openList = new ArrayList<AStarNode>();
		ArrayList<AStarNode> closedList = new ArrayList<AStarNode>();

		//Poe o primeiro ponto na lista aberta
		openList.add(new AStarNode(startPosition, 0, 0, null));
		
		int its = 0;
		while(openList.size() > 0) {
			
			
			//Pega o menor F da lista
			AStarNode q = lowestFFromList(openList);
			
			openList.remove(q);
						
			ArrayList<AStarNode> successors = new ArrayList<AStarPathFinder.AStarNode>();
						
			for(Triangulator.TriangulatedNode conn : q.node.connections) {
				AStarNode n = new AStarNode(conn, q.g + distance2(conn, q.node), getH(conn, endPosition), q);
				successors.add(n);
			}
			
			for(AStarNode succ : successors) {
				if(endPosition == succ.node) {
					return reconstructPath(succ);
				}
				
				//procura se já existe esse cara na lista com um F menor
				boolean skip = false;
				for(AStarNode a : openList) {
					if(a.node.point == succ.node.point && a.getF() < succ.getF()) {
						skip = true;
						break;
					}
				}
				if(skip) continue;
				
				//se já existe esse cara na lista fechada, com um f menor, skipa, senão adiciona na lista aberta
				skip = false;
				for(AStarNode a : closedList) {
					if(a.node.point == succ.node.point && a.getF() < succ.getF()) {
						skip = true;
						break;
					}
					
				}
				if(skip) continue;
				
				openList.add(succ);
			}
			closedList.add(q);
		}
		return null;
	}
	
	private float distance2(Triangulator.TriangulatedNode a, Triangulator.TriangulatedNode b) {
		return a.point.cpy().sub(b.point).len();
	}

	
	
	
}
