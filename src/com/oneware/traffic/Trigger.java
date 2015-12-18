/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oneware.traffic;

/**
 *
 * @author Mohamed Laptop
 */
public class Trigger {
    
    private int trigId;
    private int group1Standby;
    private int group2Standby;
    private int group3Standby;
    private int group4Standby;
    private int programId;
    private int stepId;
    private boolean triggered;
    private int waitingTime;
    
    public Trigger(int trigId, int group1Standby, int group2Standby, int group3Standby, int group4Standby, int programId, int stepId) {
        this.trigId = trigId;
        this.group1Standby = group1Standby;
        this.group2Standby = group2Standby;
        this.group3Standby = group3Standby;
        this.group4Standby = group4Standby;
        this.programId = programId;
        this.stepId = stepId;
    }

    /**
     * @return the trigId
     */
    public int getTrigId() {
        return trigId;
    }

    /**
     * @param trigId the trigId to set
     */
    public void setTrigId(int trigId) {
        this.trigId = trigId;
    }

    /**
     * @return the group1Standby
     */
    public int getGroup1Standby() {
        return group1Standby;
    }

    /**
     * @param group1Standby the group1Standby to set
     */
    public void setGroup1Standby(int group1Standby) {
        this.group1Standby = group1Standby;
    }

    /**
     * @return the group2Standby
     */
    public int getGroup2Standby() {
        return group2Standby;
    }

    /**
     * @param group2Standby the group2Standby to set
     */
    public void setGroup2Standby(int group2Standby) {
        this.group2Standby = group2Standby;
    }

    /**
     * @return the group3Standby
     */
    public int getGroup3Standby() {
        return group3Standby;
    }

    /**
     * @param group3Standby the group3Standby to set
     */
    public void setGroup3Standby(int group3Standby) {
        this.group3Standby = group3Standby;
    }

    /**
     * @return the group4Standby
     */
    public int getGroup4Standby() {
        return group4Standby;
    }

    /**
     * @param group4Standby the group4Standby to set
     */
    public void setGroup4Standby(int group4Standby) {
        this.group4Standby = group4Standby;
    }

    /**
     * @return the programId
     */
    public int getProgramId() {
        return programId;
    }

    /**
     * @param programId the programId to set
     */
    public void setProgramId(int programId) {
        this.programId = programId;
    }

    /**
     * @return the stepId
     */
    public int getStepId() {
        return stepId;
    }

    /**
     * @param stepId the stepId to set
     */
    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    /**
     * @return the triggered
     */
    public boolean isTriggered() {
        return triggered;
    }

    /**
     * @return the waitingTime
     */
    public int getWaitingTime() {
        return waitingTime;
    }

    /**
     * @param triggered the triggered to set
     */
    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    /**
     * @param waitingTime the waitingTime to set
     */
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
    
    
    
    
    
    
}
