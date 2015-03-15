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

package com.github.fauu.helix.manager;

import java.util.HashMap;
import java.util.Map;

import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.github.fauu.helix.datum.Geometry;

public class GeometryManager extends Manager {
  
  protected static final String DIRECTORY_NAME = "tile-geometry";
  
  protected static final String EXTENSION = "obj";

  @Wire
  protected AssetManager assetManager;
  
  protected Map<String, Geometry> geometries;
  
  public GeometryManager() {
    geometries = new HashMap<String, Geometry>();
  }
  
  public Geometry load(String name) {
    String path = DIRECTORY_NAME + "/" + name + "." + EXTENSION;

    if (!assetManager.isLoaded(path)) {
      assetManager.load(path, Model.class);
      assetManager.finishLoading();
    }

    Model model = assetManager.get(path);
    
    Geometry geometry = new Geometry();
    
    geometry.setName(name);
    geometry.setMesh(model.meshes.first());
    
    geometries.put(name, geometry);
    
    return geometry;
  }
  
  public Geometry getGeometry(String name) {
    return geometries.get(name);
  }

}
