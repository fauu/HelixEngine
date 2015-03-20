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
import com.github.fauu.helix.util.IntVector2;

public class DimensionsComponent extends Component {
  
  private IntVector2 dimensions;
  
  public DimensionsComponent() {
    this.dimensions = new IntVector2();
  }
  
  public DimensionsComponent(IntVector2 dimensions) {
    this();

    this.dimensions.set(dimensions);
  }
  
  public IntVector2 get() {
    return dimensions;
  }
  
  public void set(IntVector2 dimensions) {
    this.dimensions.set(dimensions);
  }

}
