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

import com.kotcrab.vis.ui.VisTable;
import com.kotcrab.vis.ui.VisUI;

public class HESidebar extends VisTable {
  
  public HESidebar() {
    super(true);
    
    top();

    setBackground((VisUI.getSkin().getDrawable("menu-bg")));

    addSeparator().padTop(-0.5f);

    add(new HEToolbox()).fill().expandX().padBottom(10);

    row();

    addSeparator().padTop(-0.5f);

    add(new HETilePermissionPanel()).fill().expandX().padTop(-5);

    row();

    pack();
  }

}
