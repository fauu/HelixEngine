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
import com.badlogic.gdx.math.Vector3;

public class PositionComponent extends Component {

  private Vector3 position;
  
  private boolean consistentWithSpatial;
  
  public PositionComponent() {
    this.position = new Vector3();
  }

  public PositionComponent(Vector3 position) {
    this.position = position;
  }

  public Vector3 get() {
    return position;
  }

  public void set(Vector3 value) {
    if (!value.epsilonEquals(position, 0.1f)) {
      consistentWithSpatial = false;
    }

    this.position.set(value);
  }
  
  public boolean isConsistentWithSpatial() {
    return consistentWithSpatial;
  }

}
