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

import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.github.fauu.helix.component.OrientationComponent;
import com.github.fauu.helix.component.PositionComponent;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.component.VisibilityComponent;
import com.github.fauu.helix.spatial.ObjectSpatial;

public class ObjectManager extends Manager {

  @Wire
  private AssetManager assetManager;
  
  public ObjectManager() { }
  
  public Entity load(String modelName) {
    String path = "model/" + modelName + ".g3db";
    
    if (!assetManager.isLoaded(path)) {
      assetManager.load(path, Model.class);
      assetManager.finishLoading();
    }
    
    Model model =  assetManager.get(path);
    
    Entity object = world.createEntity()
                         .edit()
                         .add(new PositionComponent())
                         .add(new OrientationComponent())
                         .add(new SpatialFormComponent(
                             new ObjectSpatial(model)))
                         .add(new VisibilityComponent())
                         .getEntity();
    
    return object;
  }

}
