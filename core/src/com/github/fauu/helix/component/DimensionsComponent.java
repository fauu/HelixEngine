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

public class DimensionsComponent extends Component {
  
  private Vector2 dimensions;
  
  public DimensionsComponent() {
    this.dimensions = new Vector2();
  }
  
  public DimensionsComponent(Vector2 dimensions) {
    this.dimensions = dimensions.cpy();
  }
  
  public Vector2 get() {
    return this.dimensions;
  }
  
  public void set(Vector2 size) {
    this.dimensions.set(size);
  }

}
