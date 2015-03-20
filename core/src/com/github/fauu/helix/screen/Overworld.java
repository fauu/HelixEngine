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

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.managers.UuidEntityManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.component.*;
import com.github.fauu.helix.manager.AreaManager;
import com.github.fauu.helix.manager.TextureManager;
import com.github.fauu.helix.spatial.PlayerSpatial;
import com.github.fauu.helix.system.PlayerMovementSystem;
import com.github.fauu.helix.system.RenderingSystem;
import com.github.fauu.helix.system.SpatialUpdateSystem;
import com.github.fauu.helix.util.IntVector3;

public class Overworld implements Screen {

  private World world;

  private PerspectiveCamera camera;

  private AssetManager assetManager;

  public Overworld() {
    assetManager = new AssetManager();

    camera = new PerspectiveCamera(30, Gdx.graphics.getWidth(),
                                          Gdx.graphics.getHeight());

    camera.near = 0.1f;
    camera.far = 300f;
    camera.translate(0, -14, 17);
    camera.lookAt(0, 0, 0);

    camera.translate(13 + 0.5f, 20 + 0.6f, 0);
    
    WorldConfiguration worldConfiguration 
        = new WorldConfiguration().register(assetManager)
                                  .register(camera);

    world = new World(worldConfiguration);

    world.setSystem(new PlayerMovementSystem());
    world.setSystem(new SpatialUpdateSystem());
    world.setSystem(new RenderingSystem());

    world.setManager(new UuidEntityManager());
    world.setManager(new TextureManager());
    world.setManager(new GroupManager());
    world.setManager(new TagManager());
    world.setManager(new AreaManager());

    IntVector3 playerPosition = new IntVector3(13, 20, 0);
    Direction playerOrientation = Direction.SOUTH;
    float playerMovementSpeed = 4.5f;

    world.initialize();
    
    world.getManager(AreaManager.class).load("area1");

    Entity player
        = world.createEntity()
        .edit()
        .add(new OrientationComponent(playerOrientation))
        .add(new MovementSpeedComponent(playerMovementSpeed))
        .add(new PositionComponent(playerPosition))
        .add(new SpatialFormComponent(
            new PlayerSpatial(playerPosition,
                              playerOrientation,
                              playerMovementSpeed)))
        .add(new VisibilityComponent())
        .getEntity();
    world.getManager(TagManager.class).register("player", player);

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
