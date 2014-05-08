/*
 * Copyright (C) 2014 Helix Engine Developers (http://github.com/fauu/HelixEngine)
 *
 * This software is licensed under the GNU General Public License
 * (version 3 or later). See the COPYING file in this distribution.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this software. If not, see <http://www.gnu.org/licenses/>.
 *
 * Authored by: Piotr Grabowski <fau999@gmail.com>
 */

package com.github.fauu.helix.components;

import com.artemis.Component;
import com.github.fauu.helix.spatials.Spatial;

public class SpatialFormComponent implements Component {

  private Spatial spatial;
  private boolean visible;

  public SpatialFormComponent() {
    this.spatial = null;
    this.visible = true;
  }

  @Override
  public void reset() {
    this.visible = true;
  }

  public Spatial getSpatial() {
    return spatial;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(final boolean visible) {
    this.visible = visible;
  }

  public void setSpatial(final Spatial spatial) {
    this.spatial = spatial;
  }

}
