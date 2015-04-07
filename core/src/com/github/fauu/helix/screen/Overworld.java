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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.graphics.HelixCamera;
import com.github.fauu.helix.manager.AreaManager;
import com.github.fauu.helix.manager.PlayerManager;
import com.github.fauu.helix.manager.TextureManager;
import com.github.fauu.helix.manager.WeatherMan;
import com.github.fauu.helix.system.*;

public class Overworld implements Screen {

  private World world;

  private HelixCamera camera;

  private AssetManager assetManager;

  public Overworld() {
    assetManager = new AssetManager();

    camera = new HelixCamera(30, new Vector3(0, -13, 17), .1f, 35);

    camera.translate(16 + 0.5f, 16 + 0.6f, 0);
    
    WorldConfiguration worldConfiguration 
        = new WorldConfiguration().register(assetManager)
                                  .register(camera);

    world = new World(worldConfiguration);

    world.setSystem(new PlayerMovementSystem());
    world.setSystem(new CameraClientsUpdateSystem());
    world.setSystem(new DisplayableUpdateSystem());
    world.setSystem(new DisplayableAnimationUpdateSystem());
    world.setSystem(new RenderingSystem());
    world.setSystem(new ScreenFadingSystem());

    world.setManager(new UuidEntityManager());
    world.setManager(new TextureManager());
    world.setManager(new GroupManager());
    world.setManager(new TagManager());
    world.setManager(new AreaManager());
    world.setManager(new PlayerManager());
    world.setManager(new WeatherMan());
    world.initialize();

    world.getManager(AreaManager.class).load("area1");

    world.getManager(WeatherMan.class)
         .setType(WeatherMan.WeatherType.OVERCAST);
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
