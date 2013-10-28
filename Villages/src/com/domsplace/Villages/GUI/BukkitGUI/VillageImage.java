/*
 * Copyright 2013 Dominic.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.domsplace.Villages.GUI.BukkitGUI;

import com.domsplace.Villages.Bases.Base;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * @author      Dominic
 * @since       14/10/2013
 */
public class VillageImage extends JLabel {
    private InputStream is;
    private Image image;
    private ImageIcon icon;
    
    public VillageImage(String dir) throws IOException {
        super();
        this.is = Base.getPlugin().getResource(dir);
        this.image = ImageIO.read(this.is);
        this.icon = new ImageIcon(this.image);
        this.setIcon(this.icon);
        this.setVisible(true);
    }
    
    public InputStream getFileInputStream() {return this.is;}
    public Image getImage() {return this.image;}
    public ImageIcon getImageIcon() {return this.icon;}
}
