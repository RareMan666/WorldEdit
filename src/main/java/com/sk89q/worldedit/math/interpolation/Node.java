package com.sk89q.worldedit.math.interpolation;

import com.sk89q.worldedit.Vector;

public class Node {
   private Vector position;
   private double tension;
   private double bias;
   private double continuity;

   public Node() {
      this(new Vector(0, 0, 0));
   }

   public Node(Node other) {
      this.position = other.position;
      this.tension = other.tension;
      this.bias = other.bias;
      this.continuity = other.continuity;
   }

   public Node(Vector position) {
      this.position = position;
   }

   public Vector getPosition() {
      return this.position;
   }

   public void setPosition(Vector position) {
      this.position = position;
   }

   public double getTension() {
      return this.tension;
   }

   public void setTension(double tension) {
      this.tension = tension;
   }

   public double getBias() {
      return this.bias;
   }

   public void setBias(double bias) {
      this.bias = bias;
   }

   public double getContinuity() {
      return this.continuity;
   }

   public void setContinuity(double continuity) {
      this.continuity = continuity;
   }
}
