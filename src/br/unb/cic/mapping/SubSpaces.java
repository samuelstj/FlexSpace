/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.mapping;

import java.io.Serializable;
import navigators.smart.reconfiguration.views.View;

/**
 *
 * @author samuelstj
 */
public class SubSpaces implements Serializable {

    private java.util.List<SubSpaceView> spaces = new java.util.LinkedList<SubSpaceView>();
    private SubSpaceView standby;

    public SubSpaces() {
    }

    public java.util.List<SubSpaceView> getSpaces() {
        return this.spaces;
    }

    public SubSpaceView getStandby() {
        return this.standby;
    }

    public void addStandby(int[] pStandby) {
        for (int i = 0; i < pStandby.length; i++) {
            this.standby.addProcess(pStandby[i]);
        }
    }

    public void clearStandby() {
        this.standby.clear();
    }

    public void createSubSpaces(View currentView) {
        spaces.clear();

        int n = currentView.getN();
        int f = currentView.getF();
        int procs[] = currentView.getProcesses();

        int tSubs = 2 * f + 1;
        int nSubs = n / tSubs;
        int nStandby = n % tSubs;

        int pos = 0;
        for (int i = 0; i < nSubs; i++) {
            int[] pSubs = new int[tSubs];
            for (int j = 0; j < pSubs.length; j++) {
                pSubs[j] = procs[pos];
                pos++;
            }
            spaces.add(new SubSpaceView(i, pSubs, f));
        }

        if (nStandby > 0) {
            int[] pStandby = new int[nStandby];
            for (int j = 0; j < pStandby.length; j++) {
                pStandby[j] = procs[pos];
                pos++;
            }
            this.standby = new SubSpaceView(0, pStandby, f);
        } else {
            this.standby = new SubSpaceView(0, null, f);
        }

        System.out.println("Numero de subspace: " + spaces.size());

        
        for (int i = 0; i < spaces.size(); i++) {
            System.out.println("SubSpace : " + spaces.get(i));
        }
        System.out.println("Standby: " + standby);
        

    }

    public void addSubspace(int[] pSubs, int f) {
        int id = this.spaces.size();
        spaces.add(new SubSpaceView(id, pSubs, f));
    }

    public void rmSubspace(int id) {
        spaces.remove(id);

        for (int i = 0; i < spaces.size(); i++) {
            spaces.set(i, new SubSpaceView(i, spaces.get(i).getProcesses(), spaces.get(i).getF()));
        }
    }

    public boolean hasStandby() {
        return this.standby.getProcesses() != null && this.standby.getProcesses().length > 0;
    }

    public int getSubSpacesNum() {
        return this.spaces.size();
    }
}
