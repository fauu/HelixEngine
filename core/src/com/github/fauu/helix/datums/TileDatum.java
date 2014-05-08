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

package com.github.fauu.helix.datums;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.Direction;

public class TileDatum {

  private Vector3 position;
  private Mesh geometry;
  private int textureId;
  private Direction orientation;

  public TileDatum() {
    position = new Vector3(0, 0, 0);
    geometry = null;
    textureId = 0;
    orientation = Direction.SOUTH;
  }

  public Vector3 getPosition() {
    return position;
  }

  public Mesh getGeometry() {
    return geometry;
  }

  public int getTextureId() {
    return textureId;
  }

  public Direction getOrientation() {
    return orientation;
  }

  public void setPosition(final Vector3 position) {
    this.position.set(position);
  }

  public void setGeometry(final Mesh geometry) {
    this.geometry = geometry;
  }

  public void setTextureId(final int textureId) {
    this.textureId = textureId;
  }

  public void setOrientation(final Direction orientation) {
    this.orientation = orientation;
  }
}
