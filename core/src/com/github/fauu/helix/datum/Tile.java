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

package com.github.fauu.helix.datum;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.Direction;

public class Tile {
  
  private Vector3 position;
  
  private Vector2 flatPosition;

  private Direction orientation;

  private String geometryName;

  private String textureName;
  
  public Tile() {
    this.position = new Vector3();
    this.flatPosition = new Vector2();
  }
  
  public Vector3 getPosition() {
    return position;
  }

  public Direction getOrientation() {
    return orientation;
  }
  
  public String getGeometryName() {
    return geometryName;
  }
  
  public String getTextureName() {
    return textureName;
  }
  
  public void setPosition(Vector3 position) {
    this.position.set(position);

    this.flatPosition.x = position.x;
    this.flatPosition.y = position.y;
  }
  
  public void setOrientation(Direction orientation) {
    this.orientation = orientation;
  }
  
  public void setGeometryName(String geometryName) {
    this.geometryName = geometryName;
  }
  
  public void setTextureName(String textureName) {
    this.textureName = textureName;
  }
  
  public Vector2 getFlatPosition() {
    return flatPosition;
  }

}
