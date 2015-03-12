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

package com.github.fauu.helix.editor.screen;

import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.github.fauu.helix.editor.world.system.CameraControlSystem;
import com.github.fauu.helix.editor.world.system.TileMatchingSystem;
import com.github.fauu.helix.editor.world.system.TileSelectionSystem;
import com.github.fauu.helix.editor.world.system.WorldInputSystem;
import com.github.fauu.helix.manager.MapRegionManager;
import com.github.fauu.helix.manager.ObjectManager;
import com.github.fauu.helix.manager.GeometryManager;
import com.github.fauu.helix.system.SpatialUpdateSystem;
import com.github.fauu.helix.system.RenderingSystem;

public class MainScreen implements Screen {

  private World world;

  private PerspectiveCamera camera;

  private AssetManager assetManager;

  private InputProcessor worldInputProcessor;
  
  public MainScreen() {
    assetManager = new AssetManager();

    camera = new PerspectiveCamera(13, 
                                   Gdx.graphics.getWidth(), 
                                   Gdx.graphics.getHeight());
    camera.near = 0.1f;
    camera.far = 300f;
    camera.translate(0, -100, 40);
    camera.lookAt(0, 0, 0);
  
    WorldConfiguration worldConfiguration 
        = new WorldConfiguration().register(assetManager)
                                  .register(camera);
    world = new World(worldConfiguration);
    
    worldInputProcessor = new WorldInputSystem();

    world.setManager(new GeometryManager());
    world.setManager(new ObjectManager());
    world.setManager(new TagManager());
    world.setManager(new GroupManager());
    world.setManager(new MapRegionManager());
    
    world.setSystem((EntitySystem) worldInputProcessor);
    world.setSystem(new CameraControlSystem());
    world.setSystem(new TileMatchingSystem());
    world.setSystem(new TileSelectionSystem());
    world.setSystem(new SpatialUpdateSystem());
    world.setSystem(new RenderingSystem());

    world.initialize();
  }

  @Override
  public void render(float delta) {
    world.setDelta(delta);
    world.process();
  }

  @Override
  public void resize(int width, int height) { }

  @Override
  public void show() { }

  @Override
  public void hide() { }

  @Override
  public void pause() { }

  @Override
  public void resume() { }

  @Override
  public void dispose() {
    world.dispose();
    assetManager.dispose();
  }
  
  public World getWorld() {
    return world;
  }
  
  public InputProcessor getInputProcessor() {
    return worldInputProcessor;
  }

}
