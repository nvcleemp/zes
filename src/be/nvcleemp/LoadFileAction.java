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

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

/**
 *
 * @author nvcleemp
 */
public class LoadFileAction extends AbstractAction {

    private final ZesModel model;
    
    private final JFileChooser fileChooser = new JFileChooser();
    
    public LoadFileAction(ZesModel model) {
        super("Load from file...");
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String s = br.readLine();
                while(s!=null){
                    sb.append(s).append("\n");
                    s = br.readLine();
                }
                model.loadFromString(sb.toString().trim());
            } catch (IOException ex) {
                Logger.getLogger(LoadFileAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
