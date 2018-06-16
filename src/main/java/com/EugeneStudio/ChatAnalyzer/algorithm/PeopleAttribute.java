package com.EugeneStudio.ChatAnalyzer.algorithm;

import java.util.ArrayList;

public class PeopleAttribute {
    public static class Attribute {
        private String attributeName;
        private double level;

        public Attribute(String attributeName, double level) {
            this.attributeName = attributeName;
            this.level = level;
        }

        public Attribute() {
        }

        public double getLevel() {
            return level;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public void setLevel(double level) {
            this.level = level;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }
    }

    private ArrayList<Attribute> attributes;

    public PeopleAttribute(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }
}
