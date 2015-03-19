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

package com.github.fauu.helix;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public enum TilePermission {

  OBSTACLE("Obstacle"),
  LEVEL0("Level 0"),
  LEVEL1("Level 1"),
  LEVEL2("Level 2"),
  LEVEL3("Level 3"),
  LEVEL4("Level 4"),
  LEVEL5("Level 5"),
  LEVEL6("Level 6"),
  LEVEL7("Level 7");

  private final String name;

  private static final Map<String, TilePermission> map
      = new HashMap<String, TilePermission>();

  static {
    for (TilePermission permission : TilePermission.values()) {
      map.put(permission.name, permission);
    }
  }

  private TilePermission(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static TilePermission fromString(String name) {
    if (map.containsKey(name)) {
      return map.get(name);
    }

    throw new NoSuchElementException(name + " not found");
  }

}
