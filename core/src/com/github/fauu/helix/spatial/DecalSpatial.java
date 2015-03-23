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

  protected static final Vector2 DEFAULT_SHADOW_DIMENSIONS;

  protected static final Vector3 DEFAULT_SHADOW_DISPLACEMENT;

  protected Decal mainDecal;

  protected Decal shadowDecal;

  private Decal[] decals;

  static {
    DEFAULT_DIMENSIONS = new Vector2(1.95f, 1.95f);
    DEFAULT_ROTATION = 45;
    DEFAULT_DISPLACEMENT = new Vector3(0.52f, 0.5f, 1.01f);

    DEFAULT_SHADOW_DIMENSIONS = new Vector2(1, .8f);
    DEFAULT_SHADOW_DISPLACEMENT = new Vector3(.55f, .17f, .05f);
  }

  public DecalSpatial(IntVector3 position) {
    decals = new Decal[2];
  }

  public Decal[] getDecals() {
    return decals;
  }

  public Decal getMainDecal() {
    return mainDecal;
  }

  public void setMainDecal(Decal decal) {
    mainDecal = decal;

    decals[0] = mainDecal;
  }

  public Decal getShadowDecal() {
    return shadowDecal;
  }

  public void setShadowDecal(Decal decal) {
    shadowDecal = decal;

    decals[1] = shadowDecal;
  }

}
