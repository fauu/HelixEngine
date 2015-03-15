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

package com.github.fauu.helix.editor.datum;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TextureEntry {
  
  private String name;
  
  private Drawable preview;
  
  private boolean selected;
  
  public TextureEntry() { }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Drawable getPreview() {
    return preview;
  }

  public void setPreview(Drawable preview) {
    this.preview = preview;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

}
