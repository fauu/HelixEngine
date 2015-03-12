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

import com.badlogic.gdx.graphics.Mesh;

public class Geometry {
  
  private String name; 
  
  private Mesh mesh;
  
  public Geometry() { }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Mesh getMesh() {
    return mesh;
  }

  public void setMesh(Mesh mesh) {
    this.mesh = mesh.copy(true);
  }

}
