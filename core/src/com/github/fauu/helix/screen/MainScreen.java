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

package com.github.fauu.helix.screen;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.managers.UuidEntityManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.github.fauu.helix.manager.GeometryManager;
import com.github.fauu.helix.manager.MapRegionManager;
import com.github.fauu.helix.manager.ObjectManager;
import com.github.fauu.helix.manager.TextureManager;
import com.github.fauu.helix.system.RenderingSystem;
import com.github.fauu.helix.system.SpatialUpdateSystem;

public class MainScreen implements Screen {

  private World world;

  private PerspectiveCamera camera;

  private AssetManager assetManager;

  public MainScreen() {
    assetManager = new AssetManager();

    camera = new PerspectiveCamera(13, Gdx.graphics.getWidth(), 
                                       Gdx.graphics.getHeight());
    camera.near = 0.1f;
    camera.far = 300f;
    camera.translate(0, -40, 40);
    camera.lookAt(0, 0, 0);
    
    WorldConfiguration worldConfiguration 
        = new WorldConfiguration().register(assetManager)
                                  .register(camera);

    world = new World(worldConfiguration);

    world.setSystem(new SpatialUpdateSystem());
    world.setSystem(new RenderingSystem());

    world.setManager(new UuidEntityManager());
    world.setManager(new GeometryManager());
    world.setManager(new TextureManager());
    world.setManager(new ObjectManager());
    world.setManager(new GroupManager());
    world.setManager(new TagManager());
    world.setManager(new MapRegionManager());

    world.initialize();
    
    world.getManager(MapRegionManager.class).load("region1");
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
    assetManager.dispose();
  }

}
