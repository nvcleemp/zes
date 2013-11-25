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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class ZesPanel extends JPanel{
    
    private static final BasicStroke DASHED = new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, new float[] {10.0f}, 0.0f);
    
    private static final int POINT_RADIUS = 5;
    
    private final ZesModel zesModel;

    public ZesPanel() {
        this(new ZesModel());
    }
    
    public ZesPanel(ZesModel zesModel) {
        this.zesModel = zesModel;
        ZesPanelMouseListener l = new ZesPanelMouseListener(zesModel, this);
        addMouseListener(l);
        addMouseMotionListener(l);
        zesModel.addListener(new ZesListener() {

            @Override
            public void triangleAdded(Triangle triangle) {
            }

            @Override
            public void drawingChanged() {
                repaint();
            }

            @Override
            public void constraintsChanged() {
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        paintComponentImpl(g, getWidth(), getHeight());
    }
    
    private void paintComponentImpl(Graphics g, int width, int height) {
        g = g.create();
        g.translate(width/2, height/2);
        ((Graphics2D)g).scale(5.0/zesModel.getScale(), 5.0/zesModel.getScale());
        paintCircle(g);
        for (Triangle triangle : zesModel.getTriangles()) {
            paintTriangle(g, triangle);
        }
    }
    
    private void paintCircle(Graphics g){
        Graphics2D g2 = (Graphics2D)(g.create());
        Stroke s = g2.getStroke();
        g2.setStroke(DASHED);
        g2.drawOval(-zesModel.getCircleRadius(), -zesModel.getCircleRadius(), 2*zesModel.getCircleRadius(), 2*zesModel.getCircleRadius());
        g2.setStroke(s);
        g2.drawLine(-zesModel.getCircleRadius(), 0, zesModel.getCircleRadius(), 0);
        paintPoint(g, -zesModel.getCircleRadius(), 0, Color.WHITE, "x");
        paintPoint(g, zesModel.getCircleRadius(), 0, Color.WHITE, "y");
    }
    
    private void paintPoint(Graphics g, int x, int y, Color color, String name){
        Graphics2D g2 = (Graphics2D)(g.create());
        g2.setColor(color);
        g2.fillOval(x-POINT_RADIUS, y-POINT_RADIUS, 2*POINT_RADIUS, 2*POINT_RADIUS);
        g2.setColor(Color.BLACK);
        g2.drawOval(x-POINT_RADIUS, y-POINT_RADIUS, 2*POINT_RADIUS, 2*POINT_RADIUS);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD).deriveFont(AffineTransform.getScaleInstance(zesModel.getScale()/5.0, zesModel.getScale()/5.0)));
        g2.drawString(name, x + 2*POINT_RADIUS, y - 2*POINT_RADIUS);
    }
    
    private void paintTriangle(Graphics g, Triangle t){
        g.drawLine(t.getsX(), t.getsY(), t.getuX(), t.getuY());
        g.drawLine(t.getuX(), t.getuY(), t.gettX(), t.gettY());
        paintPoint(g, t.getsX(), t.getsY(), Color.ORANGE, "s" + t.getNumber());
        paintPoint(g, t.gettX(), t.gettY(), Color.BLUE, "t" + t.getNumber());
        paintPoint(g, t.getuX(), t.getuY(), Color.YELLOW, "u" + t.getNumber());
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        } else {
            return new Dimension(500,500);
        }
    }
}
