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

package com.github.fauu.helix.spatial.dto;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.datum.Tile;

public class PartialTileUpdateDTO {

  private Array<Tile> tileData;

  private Vector2 terrainDimensions;

  public PartialTileUpdateDTO(Array<Tile> tileData, Vector2 terrainDimensions) {
    this.tileData = tileData;
    this.terrainDimensions = terrainDimensions;
  }

  public Array<Tile> getTileData() {
    return tileData;
  }

  public void setTileData(Array<Tile> tileData) {
    this.tileData = tileData;
  }

  public Vector2 getTerrainDimensions() {
    return terrainDimensions;
  }

  public void setTerrainDimensions(Vector2 terrainDimensions) {
    this.terrainDimensions.set(terrainDimensions);
  }

}
