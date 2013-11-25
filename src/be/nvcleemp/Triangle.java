/*
 * Copyright (C) 2013 nvcleemp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package be.nvcleemp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class Triangle {
    
    private int uX = 0;
    private int uY = 0;
    
    private int sX = -100;
    private int sY = 100;
    
    private int tX = 100;
    private int tY = 100;
    
    private int number;

    public Triangle(int number) {
        this.number = number;
    }

    public int getuX() {
        return uX;
    }

    public void setuX(int uX) {
        this.uX = uX;
        fireTriangleChanged();
    }

    public int getuY() {
        return uY;
    }

    public void setuY(int uY) {
        this.uY = uY;
        fireTriangleChanged();
    }

    public int getsX() {
        return sX;
    }

    public void setsX(int sX) {
        this.sX = sX;
        fireTriangleChanged();
    }

    public int getsY() {
        return sY;
    }

    public void setsY(int sY) {
        this.sY = sY;
        fireTriangleChanged();
    }

    public int gettX() {
        return tX;
    }

    public void settX(int tX) {
        this.tX = tX;
        fireTriangleChanged();
    }

    public int gettY() {
        return tY;
    }

    public void settY(int tY) {
        this.tY = tY;
        fireTriangleChanged();
    }

    public int getNumber() {
        return number;
    }
    
    private List<TriangleListener> listeners = new ArrayList<TriangleListener>();
    
    public void addListener(TriangleListener listener){
        listeners.add(listener);
    }
    
    public void removeListener(TriangleListener listener){
        listeners.remove(listener);
    }
    
    private void fireTriangleChanged(){
        for (TriangleListener l : listeners) {
            l.triangleChanged();
        }
    }
}
