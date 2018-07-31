/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFProject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author curtr
 */
public class CFStates {
    private List<String> listStates = new ArrayList<>();
    @Override
    public String toString() {
        return "CFStates{" + "todo=" + todo + ", infinite=" + infinite + ", wip=" + wip + ", done=" + done + ", failed=" + failed + ", resting=" + resting + '}';
    }

    public boolean isTodo() {
        return todo;
    }

    public void setTodo(boolean todo) {
        this.todo = todo;
    }

    public boolean isInfinite() {
        return infinite;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    public boolean isWip() {
        return wip;
    }

    public void setWip(boolean wip) {
        if( wip == true) listStates.add("wip");
        this.wip = wip;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public boolean isResting() {
        return resting;
    }

    public void setResting(boolean resting) {
        this.resting = resting;
    }
    private boolean todo;
    private boolean infinite;
    private boolean wip;
    private boolean done;
    private boolean failed;
    private boolean resting;
    private boolean notDone; 
    CFStates(CFProjectApp projectApp) {
        todo = projectApp.getToDo();
        if ( todo == true) listStates.add("todo");
        infinite = projectApp.getInfinite();
        if ( infinite == true) listStates.add("infinite");
        wip = projectApp.getWip();
        if ( wip == true) listStates.add("wip");
        done = projectApp.getDone();
        if ( done == true) listStates.add("done");
        failed = projectApp.getFailed();
        if ( failed == true) listStates.add("failed");
        resting = projectApp.getResting();
        if ( resting == true) listStates.add("resting");
        notDone = projectApp.getNotDone();
        if ( notDone == true) listStates.add("notDone");
    }
    public CFStates(){
        //wip = true;
    }
    public int getNTrueStates(){
        int count = 0;
        if ( notDone) count++;
        if ( done) count++;
        if ( failed) count++;
        if( infinite) count++;
        if( resting) count++;
        if ( todo) count++;
        if( wip ) count++;
        return count;
    }
    public List getTrueStates(){
        List<String> list = new ArrayList<>();
        if ( done) list.add("done");
        if ( notDone) list.add("notDone");
        if ( failed) list.add("failed");
        if( infinite) list.add("infinite");
        if( resting) list.add("resting");
        if ( todo) list.add("todo");
        if( wip ) list.add("wip");
        return list;
    }

    public void setNotDone(boolean notDone) {
        this.notDone = notDone;
    }
}
