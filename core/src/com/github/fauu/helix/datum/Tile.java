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

import com.github.fauu.helix.TilePermissions;

public class Tile {
  
  private TilePermissions permissions;

  public Tile() {
    permissions = TilePermissions.LEVEL0;
  }

  public TilePermissions getPermissions() {
    return permissions;
  }

  public void setPermissions(TilePermissions permissions) {
    this.permissions = permissions;
  }

}
