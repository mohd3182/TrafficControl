package com.db;

public class TRFProgramDetail {

    public int programId;
    public int secDuration;
    public String group1Action;
    public String group2Action;
    public String group3Action;
    public String group4Action;
    public int stepId;
    public int nextStep;
    public int nextProgram;

    public TRFProgramDetail() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TRFProgramDetail(int programId, int secDuration,
            String group1Action, String group2Action, String group3Action,
            String group4Action, int stepId, int nextStep,int nextProgram) {
        super();
        this.programId = programId;
        this.secDuration = secDuration;
        this.group1Action = group1Action;
        this.group2Action = group2Action;
        this.group3Action = group3Action;
        this.group4Action = group4Action;
        this.stepId = stepId;
        this.nextStep = nextStep;
        this.nextProgram=nextProgram;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public int getSecDuration() {
        return secDuration;
    }

    public void setSecDuration(int secDuration) {
        this.secDuration = secDuration;
    }

    public String getGroup1Action() {
        return group1Action;
    }

    public void setGroup1Action(String group1Action) {
        this.group1Action = group1Action;
    }

    public String getGroup2Action() {
        return group2Action;
    }

    public void setGroup2Action(String group2Action) {
        this.group2Action = group2Action;
    }

    public String getGroup3Action() {
        return group3Action;
    }

    public void setGroup3Action(String group3Action) {
        this.group3Action = group3Action;
    }

    public String getGroup4Action() {
        return group4Action;
    }

    public void setGroup4Action(String group4Action) {
        this.group4Action = group4Action;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public int getNextStep() {
        return nextStep;
    }

    public void setNextStep(int nextStep) {
        this.nextStep = nextStep;
    }

    public int getNextProgram() {
        return nextProgram;
    }

    public void setNextProgram(int nextProgram) {
        this.nextProgram = nextProgram;
    }
}
