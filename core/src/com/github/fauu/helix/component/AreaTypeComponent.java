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
import com.github.fauu.helix.AreaType;

public class AreaTypeComponent extends Component {

  private AreaType areaType;

  public AreaTypeComponent() { }

  public AreaTypeComponent(AreaType areaType) {
    this.areaType = areaType;
  }

  public AreaType get() {
    return areaType;
  }

  public void set(AreaType areaType) {
    this.areaType = areaType;
  }

}
