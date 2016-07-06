/*
 * Copyright (C) 2014-2016 Helix Engine Developers 
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
import com.github.fauu.helix.util.IntVector3;

public class PositionComponent extends Component {

  private IntVector3 position;

  public PositionComponent() {
    this.position = new IntVector3();
  }

  public PositionComponent(IntVector3 position) {
    this();

    set(position);
  }

  public IntVector3 get() {
    return position;
  }

  public void set(IntVector3 value) {
    this.position.set(value);
  }

}
