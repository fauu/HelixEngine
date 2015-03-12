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

package com.github.fauu.helix.shader;

import com.badlogic.gdx.graphics.g3d.Attribute;

public class ShaderAttribute extends Attribute {

  public static final String ALIAS = "Shader";

  public static final long ID = register(ALIAS);  
  
  public int value;

  public ShaderAttribute(int value) {
    super(ID);
    
    this.value = value;
  }

  @Override
  public Attribute copy() {
    return new ShaderAttribute(value);
  }
  
  @Override
  protected boolean equals(Attribute other) {
    return ((ShaderAttribute) other).value == value;
  }
  
  public int getValue() {
    return value;
  }

}
