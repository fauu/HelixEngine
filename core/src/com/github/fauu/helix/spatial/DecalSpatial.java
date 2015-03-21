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

package com.github.fauu.helix.spatial;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.util.IntVector3;

public abstract class DecalSpatial extends Spatial {

  protected static final Vector2 DEFAULT_DIMENSIONS;

  protected static final int DEFAULT_ROTATION;

  protected static final Vector3 DEFAULT_DISPLACEMENT;

  static {
    DEFAULT_DIMENSIONS = new Vector2(1.95f, 1.95f);
    DEFAULT_ROTATION = 45;
    DEFAULT_DISPLACEMENT = new Vector3(0.52f, 0.5f, 1.01f);
  }

  protected Decal decal;

  public DecalSpatial(IntVector3 position) { }

  public Decal getDecal() {
    return decal;
  }

  public void setDecal(Decal decal) {
    this.decal = decal;
  }

}
