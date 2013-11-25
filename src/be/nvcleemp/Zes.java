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

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 *
 * @author nvcleemp
 */
public class Zes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Zes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GroupLayout layout = new GroupLayout(frame.getContentPane());
        frame.setLayout(layout);
        
        final ZesModel model = new ZesModel();
        ZesPanel panel = new ZesPanel(model);
        
        JLabel label = new JLabel("Not satisfied:");
        
        final JTextArea area = new JTextArea(0, 20);
        area.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(area, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
                .addGroup(
                        layout.createParallelGroup()
                                .addComponent(label)
                                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                ));

        layout.setVerticalGroup(layout.createParallelGroup()
                .addComponent(panel)
                .addGroup(
                        layout.createSequentialGroup()
                        .addComponent(label)
                        .addComponent(scrollPane)
                ));
        
        model.addListener(new ZesListener() {

            @Override
            public void triangleAdded(Triangle triangle) {
            }

            @Override
            public void drawingChanged() {
            }

            @Override
            public void constraintsChanged() {
                StringBuilder sb = new StringBuilder();
                for (String s : model.getNotSatisfied()) {
                    sb.append(s);
                    sb.append("\n");
                }
                area.setText(sb.toString());
            }
        });
        
        JMenu mFile = new JMenu("File");
        mFile.add(new SaveFileAction(model));
        mFile.add(new LoadFileAction(model));
        
        JMenu mZoom = new JMenu("Zoom");
        mZoom.add(new ZoomAction(true, model));
        mZoom.add(new ZoomAction(false, model));
        
        JMenuBar mb = new JMenuBar();
        mb.add(mFile);
        mb.add(mZoom);
        
        panel.getInputMap().put(KeyStroke.getKeyStroke('+'), "zoomin");
        panel.getInputMap().put(KeyStroke.getKeyStroke('-'), "zoomout");
        panel.getActionMap().put("zoomin", new ZoomAction(true, model));
        panel.getActionMap().put("zoomout", new ZoomAction(false, model));
        
        frame.setJMenuBar(mb);
        
        frame.pack();
        frame.setVisible(true);
    }
    
}
