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

import com.github.fauu.helix.TilePermission;

public class Tile {

  private TilePermission permissions;

  public Tile() {
    setPermissions(TilePermission.LEVEL0);
  }
  public TilePermission getPermissions() {
    return permissions;
  }

  public void setPermissions(TilePermission permissions) {
    this.permissions = permissions;
  }

}
