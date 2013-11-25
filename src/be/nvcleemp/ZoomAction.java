/*
 * Copyright (C) 2013 Nico Van Cleemput <nico.vancleemput@gmail.com>
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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Nico Van Cleemput <nico.vancleemput@gmail.com>
 */
public class ZoomAction extends AbstractAction {
    
    private final boolean zoomIn;
    private final ZesModel zesModel;
    private final ZesListener listener = new ZesListener() {

        @Override
        public void triangleAdded(Triangle triangle) {
        }

        @Override
        public void drawingChanged() {
            if(zoomIn){
                setEnabled(zesModel.getScale()!=1);
            }
        }

        @Override
        public void constraintsChanged() {
        }
    };

    public ZoomAction(boolean zoomIn, ZesModel zesModel) {
        super("Zoom " + (zoomIn ? "in" : "out"));
        this.zoomIn = zoomIn;
        this.zesModel = zesModel;
        zesModel.addListener(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(zoomIn){
            zesModel.decrementScale();
        } else {
            zesModel.incrementScale();
        }
    }
    
}
