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
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.datums.TileDatum;

public class TileDataComponent implements Component {

  private Array<TileDatum> tileData;

  public TileDataComponent() {
    this.tileData = null;
  }

  public Array<TileDatum> get() {
    return tileData;
  }

  public void set(Array<TileDatum> tileData) {
    this.tileData = tileData;
  }

  @Override
  public void reset() {}

}
