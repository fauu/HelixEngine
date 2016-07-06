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

package com.github.fauu.helix;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

// TODO: Rename to TileType or something
public enum TilePermission {

  OBSTACLE("Obstacle", -1),
  PASSAGE("Area Passage", -1),
  RAMP("Ramp", -1),
  LEVEL0("Level 0", 0),
  LEVEL1("Level 1", 1),
  LEVEL2("Level 2", 2),
  LEVEL3("Level 3", 3),
  LEVEL4("Level 4", 4),
  LEVEL5("Level 5", 5),
  LEVEL6("Level 6", 6),
  LEVEL7("Level 7", 7);

  private String name;

  private int elevation;

  private static final Map<String, TilePermission> nameToValue
      = new HashMap<String, TilePermission>();

  static {
    for (TilePermission permission : TilePermission.values()) {
      nameToValue.put(permission.name, permission);
    }
  }

  private TilePermission(String name, int elevation) {
    this.name = name;
    this.elevation = elevation;
  }

  public String getName() {
    return name;
  }

  public int getElevation() {
    return elevation;
  }

  public static TilePermission fromString(String name) {
    if (nameToValue.containsKey(name)) {
      return nameToValue.get(name);
    }

    throw new NoSuchElementException(name + " not found");
  }

}
