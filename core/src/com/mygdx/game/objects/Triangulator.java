package com.mygdx.game.objects;

import java.util.ArrayList;

import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.states.State;

public class Triangulator {
	
	DelaunayTriangulator triangulator;
	ArrayList<Vector2> vectorpoints;	
	float threshold = 10000;
	
	class TriangleIndices{
		short a;
		short b;
		short c;
		public TriangleIndices(short a, short b, short c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}
	}
	
	
	public void setThreshold(float threshold) {
		this.threshold = threshold * State.PHYS_SCALE;
	}
	
	public class TriangulatedNode{
		public Vector2 point;
		public ArrayList<TriangulatedNode> connections;
		
		public TriangulatedNode(Vector2 point) {
			this.point = point;
			connections = new ArrayList<Triangulator.TriangulatedNode>();
		}
		
		public void addNode(TriangulatedNode node) {
			if(!connections.contains(node)) {
				if(point.cpy().sub(node.point).len2() < threshold*threshold) {
					connections.add(node);
				}
			}
		}
		
		public String toString() {
			return point + ", with " + connections.size() + " connections";
		}
	}
	
	public ArrayList<TriangulatedNode> triangulate(){
		float points[] = new float[vectorpoints.size() * 2];
		
		for(int i = 0; i < vectorpoints.size(); i ++) {
			points[i*2] = vectorpoints.get(i).x;
			points[i*2 + 1] = vectorpoints.get(i).y;

		}
				
		short s[] = triangulator.computeTriangles(points, false).items;
		
		ArrayList<TriangleIndices> trimmed = new ArrayList<TriangleIndices>();
		
		for(int i = 0; i < s.length/3; i ++) {
			short p1 = s[i*3];
			short p2 = s[i*3 + 1];
			short p3 = s[i*3 + 2];
			
			if(p1 > points.length/2f || p2 > points.length/2f || p3 > points.length/2f) {
				//nao pode
				continue;
			}
			if(p1 == p2 || p2 == p3 || p1 == p3) {
				continue;
			}
			
			trimmed.add(new TriangleIndices(p1, p2, p3));
		}
		
		ArrayList<TriangulatedNode> nodes = new ArrayList<Triangulator.TriangulatedNode>();
		
		for(TriangleIndices ti : trimmed) {
			
			TriangulatedNode a = getTriangulated(nodes, vectorpoints.get(ti.a));
			if(a == null) {
				a = new TriangulatedNode(vectorpoints.get(ti.a));
			}
			
			TriangulatedNode b = getTriangulated(nodes, vectorpoints.get(ti.b));
			if(b == null) {
				b = new TriangulatedNode(vectorpoints.get(ti.b));
			}
			
			TriangulatedNode c = getTriangulated(nodes, vectorpoints.get(ti.c));
			if(c == null) {
				c = new TriangulatedNode(vectorpoints.get(ti.c));
			}

			
			a.addNode(b);
			a.addNode(c);
			
			b.addNode(a);
			b.addNode(c);
			
			c.addNode(a);
			c.addNode(b);
			
			if(!nodes.contains(a)) {
				nodes.add(a);
			}
			if(!nodes.contains(b)) {
				nodes.add(b);
			}
			if(!nodes.contains(c)) {
				nodes.add(c);
			}
		}
		
		return nodes;
	}
	
	public Triangulator(ArrayList<Vector2> vectorpoints) {
		triangulator = new DelaunayTriangulator();
		this.vectorpoints = vectorpoints;
	}
	
	public Triangulator(float[] vertices) {
		triangulator = new DelaunayTriangulator();
		vectorpoints = new ArrayList<Vector2>();
		for(int i = 0; i < vertices.length / 2f; i ++) {
			vectorpoints.add(new Vector2(vertices[i*2], vertices[i*2+1]));
		}
	}
	
	TriangulatedNode getTriangulated(ArrayList<TriangulatedNode> list, Vector2 reference) {
		
		for(TriangulatedNode node : list) {
			if(node.point == reference) {
				return node;
			}
		}
		
		return null;
	}

}
