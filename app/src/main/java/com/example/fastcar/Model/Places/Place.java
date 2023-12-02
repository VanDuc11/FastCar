package com.example.fastcar.Model.Places;

import java.util.List;

public class Place {
    List<Predictions> predictions;
    String execution_time, status;

    public Place(List<Predictions> predictions, String execution_time, String status) {
        this.predictions = predictions;
        this.execution_time = execution_time;
        this.status = status;
    }

    public Place() {
    }

    public class Predictions {
        String description;
        Structured_formatting structured_formatting;
        String place_id;

        public Predictions(String description, Structured_formatting structured_formatting, String place_id) {
            this.description = description;
            this.structured_formatting = structured_formatting;
            this.place_id = place_id;
        }

        public Predictions() {
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Structured_formatting getStructured_formatting() {
            return structured_formatting;
        }

        public void setStructured_formatting(Structured_formatting structured_formatting) {
            this.structured_formatting = structured_formatting;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }
    }

    public class Structured_formatting {
        String main_text, secondary_text;

        public Structured_formatting(String main_text, String secondary_text) {
            this.main_text = main_text;
            this.secondary_text = secondary_text;
        }

        public Structured_formatting() {
        }

        public String getMain_text() {
            return main_text;
        }

        public void setMain_text(String main_text) {
            this.main_text = main_text;
        }

        public String getSecondary_text() {
            return secondary_text;
        }

        public void setSecondary_text(String secondary_text) {
            this.secondary_text = secondary_text;
        }
    }

    public List<Predictions> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Predictions> predictions) {
        this.predictions = predictions;
    }

    public String getExecution_time() {
        return execution_time;
    }

    public void setExecution_time(String execution_time) {
        this.execution_time = execution_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

