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

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author nvcleemp
 */
public class ZesPanelMouseListener extends MouseInputAdapter {
    
    private final ZesModel zesModel;
    private final ZesPanel zesPanel;
    
    private Triangle currentTriangle = null;
    private POINT_TYPES currentType = null;

    public ZesPanelMouseListener(ZesModel zesModel, ZesPanel zesPanel) {
        this.zesModel = zesModel;
        this.zesPanel = zesPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton()!=MouseEvent.BUTTON1){
            System.out.println(zesModel.saveToString());
        } else if(e.getClickCount()>1){
            zesModel.createNewTriangle();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = (e.getX() - zesPanel.getWidth()/2)*zesModel.getScale()/5;
        int y = (e.getY() - zesPanel.getHeight()/2)*zesModel.getScale()/5;
        
        int distance2 = 100;
        Triangle nearest = null;
        POINT_TYPES type = null;
        //find nearest point
        for (Triangle t : zesModel.getTriangles()) {
            int sd2 = getDistance2(x, y, t.getsX(), t.getsY());
            if(sd2 < distance2){
                distance2 = sd2;
                nearest = t;
                type = POINT_TYPES.S;
            }
            int td2 = getDistance2(x, y, t.gettX(), t.gettY());
            if(td2 < distance2){
                distance2 = td2;
                nearest = t;
                type = POINT_TYPES.T;
            }
            int ud2 = getDistance2(x, y, t.getuX(), t.getuY());
            if(ud2 < distance2){
                distance2 = ud2;
                nearest = t;
                type = POINT_TYPES.U;
            }
        }
        currentTriangle = nearest;
        currentType = type;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentTriangle = null;
        currentType = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(currentTriangle!=null && currentType!=null){
            int x = (e.getX() - zesPanel.getWidth()/2)*zesModel.getScale()/5;
            int y = (e.getY() - zesPanel.getHeight()/2)*zesModel.getScale()/5;
            currentType.movePoint(x, y, currentTriangle, zesModel.getCircleRadius());
        }
    }

    private static enum POINT_TYPES {
        S {
            @Override
            public void movePoint(int x, int y, Triangle t, int radius){
                if(getDistance2(0, 0, x, y) < radius*radius){
                    double theta = Math.atan2(y, x);
                    t.setsX((int)(radius*Math.cos(theta)));
                    t.setsY((int)(radius*Math.sin(theta)));
                } else {
                    t.setsX(x);
                    t.setsY(y);
                }
            }
        },
        T{
            @Override
            public void movePoint(int x, int y, Triangle t, int radius){
                if(getDistance2(0, 0, x, y) < radius*radius){
                    double theta = Math.atan2(y, x);
                    t.settX((int)(radius*Math.cos(theta)));
                    t.settY((int)(radius*Math.sin(theta)));
                } else {
                    t.settX(x);
                    t.settY(y);
                }
            }
        },
        U{
            @Override
            public void movePoint(int x, int y, Triangle t, int radius){
                if(getDistance2(0, 0, x, y) > radius*radius){
                    double theta = Math.atan2(y, x);
                    t.setuX((int)(radius*Math.cos(theta)));
                    t.setuY((int)(radius*Math.sin(theta)));
                } else {
                    t.setuX(x);
                    t.setuY(y);
                }
            }
        };
        
        public abstract void movePoint(int x, int y, Triangle t, int radius);
    }
    
    private static int getDistance2(int x1, int y1, int x2, int y2){
        return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
    }
}
