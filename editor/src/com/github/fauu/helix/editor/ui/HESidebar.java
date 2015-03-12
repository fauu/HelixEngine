/*
 * Copyright (C) 2014, 2015 Helix Engine Developers 
 * (http://github.com/fauu/HelixEngine)
 *
 * This software is licensed under the GNU General Public License
 * (version 3 or later). See the COPYING file in this distribution.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this software. If not, see <http://www.gnu.org/licenses/>.
 *
 * Authored by: Piotr Grabowski <fau999@gmail.com>
 */

package com.github.fauu.helix.editor.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kotcrab.vis.ui.VisTable;
import com.kotcrab.vis.ui.VisUI;

public class HESidebar extends VisTable {
  
  public HESidebar() {
    super(true);
    
    row();
    
    addSeparator();

    row();
    
    // TODO: Dis hacky
    setBackground(
        (new Image(VisUI.getSkin().getRegion("menu-bg")).getDrawable()));

    pack();
  }

}
