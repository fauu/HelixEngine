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

package com.github.fauu.helix.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class SizeComponent extends Component {
  
  private Vector2 size;
  
  public SizeComponent() {
    this.size = new Vector2();
  }
  
  public SizeComponent(Vector2 size) {
    this.size = size;
  }
  
  public Vector2 get() {
    return this.size;
  }
  
  public void set(Vector2 size) {
    this.size.set(size);
  }

}
