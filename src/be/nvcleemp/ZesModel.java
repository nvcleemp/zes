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
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author nvcleemp
 */
public class ZesModel {
    
    private final TriangleListener triangleListener = new TriangleListener() {

        @Override
        public void triangleChanged() {
            fireDrawingChanged();
            verifyConstraints();
        }
    };
    
    private final List<ZesListener> listeners = new ArrayList<>();
    
    private final List<Triangle> triangles = new ArrayList<>();
    
    private final List<String> notSatisfied = new ArrayList<>();
    
    private int scale = 5;
    
    public void addListener(ZesListener listener){
        listeners.add(listener);
    }
    
    public void removeListener(ZesListener listener){
        listeners.remove(listener);
    }
    
    private void fireTriangleAdded(Triangle triangle){
        for (ZesListener l : listeners) {
            l.triangleAdded(triangle);
        }
    }
    
    private void fireDrawingChanged(){
        for (ZesListener l : listeners) {
            l.drawingChanged();
        }
    }
    
    private void fireConstraintsChanged(){
        for (ZesListener l : listeners) {
            l.constraintsChanged();
        }
    }
    
    private int circleRadius = 60;

    public int getCircleRadius() {
        return circleRadius;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        if(scale > 0 && this.scale!=scale){
            this.scale = scale;
            fireDrawingChanged();
        }
    }
    
    public void incrementScale(){
        scale++;
        fireDrawingChanged();
    }
    
    public void decrementScale(){
        if(scale>1){
            scale--;
            fireDrawingChanged();
        }
    }

    public List<Triangle> getTriangles() {
        return Collections.unmodifiableList(triangles);
    }

    public List<String> getNotSatisfied() {
        return notSatisfied;
    }
    
    public Triangle createNewTriangle(){
        Triangle t = new Triangle(triangles.size()+1);
        triangles.add(t);
        t.addListener(triangleListener);
        fireTriangleAdded(t);
        fireDrawingChanged();
        verifyConstraints();
        return t;
    }
    
    private void verifyConstraints(){
        notSatisfied.clear();
        int xy = 4*circleRadius*circleRadius;
        for (Triangle triangle : triangles) {
            int st = getDistance2(triangle.getsX(), triangle.getsY(), triangle.gettX(), triangle.gettY());
            int su = getDistance2(triangle.getsX(), triangle.getsY(), triangle.getuX(), triangle.getuY());
            int tu = getDistance2(triangle.getuX(), triangle.getuY(), triangle.gettX(), triangle.gettY());
            int sx = getDistance2(triangle.getsX(), triangle.getsY(), -circleRadius, 0);
            int ty = getDistance2(triangle.gettX(), triangle.gettY(), circleRadius, 0);
            int n = triangle.getNumber();
            if(st < xy || st < su || st < tu){
                notSatisfied.add("s" + n + "t" + n + 
                        " > max(xy, s" + n + "u" + n + ", t" + n + "u" + n + ")");
            }
            if(sx < xy){
                notSatisfied.add("s" + n + "x > xy");
            }
            if(ty < xy){
                notSatisfied.add("t" + n + "y > xy");
            }
        }
        for (int i = 0; i < triangles.size()-1; i++) {
            Triangle t1 = triangles.get(i);
            int n1 = t1.getNumber();
            for (int j = i+1; j < triangles.size(); j++) {
                Triangle t2 = triangles.get(j);
                int n2 = t2.getNumber();
                int s1t2 = getDistance2(t1.getsX(), t1.getsY(), t2.gettX(), t2.gettY());
                int s1s2 = getDistance2(t1.getsX(), t1.getsY(), t2.getsX(), t2.getsY());
                int t1t2 = getDistance2(t1.gettX(), t1.gettY(), t2.gettX(), t2.gettY());
                int s1u1 = getDistance2(t1.getsX(), t1.getsY(), t1.getuX(), t1.getuY());
                int s2u2 = getDistance2(t2.getsX(), t2.getsY(), t2.getuX(), t2.getuY());
                int t1u1 = getDistance2(t1.gettX(), t1.gettY(), t1.getuX(), t1.getuY());
                int t2u2 = getDistance2(t2.gettX(), t2.gettY(), t2.getuX(), t2.getuY());
                int s2y = getDistance2(t2.getsX(), t2.getsY(), circleRadius, 0);
                int t1x = getDistance2(t1.gettX(), t1.gettY(), -circleRadius, 0);
                int max_s2y_t1x = Math.max(s2y, t1x);
                if(s1t2 < xy || s1t2 < s1u1 || s1t2 < t2u2){
                    notSatisfied.add("s" + n1 + "t" + n2 + 
                        " > max(xy, s" + n1 + "u" + n1 + ", t" + n2 + "u" + n2 + ")");
                }
                if(s1s2 < xy || s1s2 < s1u1 || s1s2 < s2u2){
                    notSatisfied.add("s" + n1 + "s" + n2 + 
                        " > max(xy, s" + n1 + "u" + n1 + ", s" + n2 + "u" + n2 + ")");
                }
                if(t1t2 < xy || t1t2 < t1u1 || t1t2 < t2u2){
                    notSatisfied.add("t" + n1 + "t" + n2 + 
                        " > max(xy, t" + n1 + "u" + n1 + ", t" + n2 + "u" + n2 + ")");
                }
                if(max_s2y_t1x < xy || max_s2y_t1x < t1u1 || max_s2y_t1x < s2u2){
                    notSatisfied.add("max(s" + n2 + "y, t" + n1 + "x)" + 
                        " > max(xy, t" + n1 + "u" + n1 + ", s" + n2 + "u" + n2 + ")");
                }
            }
        }
        fireConstraintsChanged();
    }
    
    public String saveToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("scale: ").append(scale).append("\n");
        sb.append("x: ").append(-circleRadius).append(" 0\n");
        sb.append("y: ").append(circleRadius).append(" 0\n");
        for (Triangle t : triangles) {
            int n = t.getNumber();
            sb.append("s").append(n).append(": ").append(t.getsX()).append(" ").append(t.getsY()).append("\n");
            sb.append("u").append(n).append(": ").append(t.getuX()).append(" ").append(t.getuY()).append("\n");
            sb.append("t").append(n).append(": ").append(t.gettX()).append(" ").append(t.gettY()).append("\n");
        }
        return sb.toString();
    }
    
    public void loadFromString(String source){
        //clear current configuration
        triangles.clear();
        
        source = source.trim();
        
        impl_loadFromString(source, source.startsWith("scale"));
    }
    
    private void impl_loadFromString(String source, boolean hasScale){
        //parse input
        Scanner s = new Scanner(source);
        
        if(hasScale){
            String scaleLine = s.nextLine();
            scale = Integer.parseInt(scaleLine.split(":")[1]);
        }
        
        if(!s.hasNextLine() || !s.nextLine().equals("x: -60 0")){
            throw new RuntimeException("Error in input");
        }
        if(!s.hasNextLine() || !s.nextLine().equals("y: 60 0")){
            throw new RuntimeException("Error in input");
        }
        
        int triangleCounter = 1;
        while(s.hasNextLine()){
            //read three lines
            String sLine = s.nextLine();
            if(!s.hasNextLine()){
                throw new RuntimeException("Error in input");
            }
            String uLine = s.nextLine();
            if(!s.hasNextLine()){
                throw new RuntimeException("Error in input");
            }
            String tLine = s.nextLine();
            
            //verify three lines
            if(!sLine.startsWith("s" + triangleCounter + ": ")){
                throw new RuntimeException("Error in input");
            }
            if(!uLine.startsWith("u" + triangleCounter + ": ")){
                throw new RuntimeException("Error in input");
            }
            if(!tLine.startsWith("t" + triangleCounter + ": ")){
                throw new RuntimeException("Error in input");
            }
            
            //parse three lines
            int sx, sy, ux, uy, tx, ty;
            sLine = sLine.split(": ")[1];
            uLine = uLine.split(": ")[1];
            tLine = tLine.split(": ")[1];
            String[] sValues = sLine.trim().split(" ");
            String[] uValues = uLine.trim().split(" ");
            String[] tValues = tLine.trim().split(" ");
            sx = Integer.parseInt(sValues[0]);
            sy = Integer.parseInt(sValues[1]);
            ux = Integer.parseInt(uValues[0]);
            uy = Integer.parseInt(uValues[1]);
            tx = Integer.parseInt(tValues[0]);
            ty = Integer.parseInt(tValues[1]);
            
            Triangle t = createNewTriangle();
            t.setsX(sx);
            t.setsY(sy);
            t.setuX(ux);
            t.setuY(uy);
            t.settX(tx);
            t.settY(ty);
            triangleCounter++;
        }
        
    }
    
    private static int getDistance2(int x1, int y1, int x2, int y2){
        return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
    }
}
