package com.example.fastcar.Model.Distance;

import java.util.ArrayList;

public class DistanceMatrix {
    public ArrayList<Row> rows;

    public DistanceMatrix(ArrayList<Row> rows) {
        this.rows = rows;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Row> rows) {
        this.rows = rows;
    }

    public class Row{
        public ArrayList<Element> elements;

        public Row(ArrayList<Element> elements) {
            this.elements = elements;
        }

        public ArrayList<Element> getElements() {
            return elements;
        }

        public void setElements(ArrayList<Element> elements) {
            this.elements = elements;
        }
    }

    public class Distance{
        public String text;
        public int value;

        public Distance(String text, int value) {
            this.text = text;
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public class Duration{
        public String text;
        public int value;

        public Duration(String text, int value) {
            this.text = text;
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public class Element{
        public String status;
        public Duration duration;
        public Distance distance;

        public Element(String status, Duration duration, Distance distance) {
            this.status = status;
            this.duration = duration;
            this.distance = distance;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public Distance getDistance() {
            return distance;
        }

        public void setDistance(Distance distance) {
            this.distance = distance;
        }
    }
}
