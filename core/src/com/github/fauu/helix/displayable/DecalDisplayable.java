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

package com.github.fauu.helix.displayable;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.graphics.AnimationType;
import com.github.fauu.helix.util.IntVector3;

public abstract class DecalDisplayable extends Displayable {

  protected static final Vector2 DEFAULT_DIMENSIONS;

  protected static final int DEFAULT_ROTATION;

  protected static final Vector3 DEFAULT_DISPLACEMENT;

  protected static final Vector2 DEFAULT_SHADOW_DIMENSIONS;

  protected static final Vector3 DEFAULT_SHADOW_DISPLACEMENT;

  protected Vector3 position;

  protected Decal mainDecal;

  protected Decal shadowDecal;

  private Decal[] decals;

  static {
    DEFAULT_DIMENSIONS = new Vector2(1.95f, 1.95f);
    DEFAULT_ROTATION = 45;
    DEFAULT_DISPLACEMENT = new Vector3(.52f, .5f, 1.01f);

    DEFAULT_SHADOW_DIMENSIONS = new Vector2(1, .8f);
    DEFAULT_SHADOW_DISPLACEMENT = new Vector3(.03f, -.33f, -.96f);
  }

  public DecalDisplayable(Vector3 position) {
    decals = new Decal[2];

    this.position = position;
  }

  public Decal[] getDecals() {
    return decals;
  }

  public void setMainDecal(Decal decal) {
    mainDecal = decal;

    decals[0] = mainDecal;
  }

  public void setShadowDecal(Decal decal) {
    shadowDecal = decal;

    decals[1] = shadowDecal;
  }

  // TODO: Move this into AnimatedDecalDisplayable / make all decals animated / use composition?
  public abstract void animate(AnimationType type,
                               Direction direction,
                               float duration);

  public abstract void orientate(Direction direction);

  public void move(Vector3 translation) {
    mainDecal.translate(translation);
    shadowDecal.translate(translation);

    position.add(translation);
  }

  private void moveTo(Vector3 position) {
    Vector3 translation = position.sub(this.position);

    move(translation);
  }

  public void updatePosition(IntVector3 entityPosition) {
    moveTo(entityPosition.toVector3().add(DEFAULT_DISPLACEMENT));
  }

}
