package com.paras.setgo.Models;

public class TaskItemModel {
    private String taskName,totalDuration;
    private Integer sets,reps;
    private Long duration,rest,date;

    public TaskItemModel(String taskName, String totalDuration, Integer sets, Integer reps, Long duration, Long rest,Long date) {
        this.taskName = taskName;
        this.totalDuration = totalDuration;
        this.sets = sets;
        this.reps = reps;
        this.duration = duration;
        this.rest = rest;
        this.date = date;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public Integer getSets() {
        return sets;
    }

    public Integer getReps() {
        return reps;
    }

    public Long getDuration() {
        return duration;
    }

    public Long getRest() {
        return rest;
    }
    public Long getDate() {
        return date;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void setRest(Long rest) {
        this.rest = rest;
    }
    public void setDate(Long date) {
        this.date = date;
    }
}
