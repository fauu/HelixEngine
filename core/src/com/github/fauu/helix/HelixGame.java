/*
 * Copyright (C) 2014 Helix Engine Developers (http://github.com/fauu/HelixEngine)
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

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.components.PositionComponent;
import com.github.fauu.helix.components.SpatialFormComponent;
import com.github.fauu.helix.components.TileDataComponent;
import com.github.fauu.helix.datums.TileDatum;
import com.github.fauu.helix.loaders.MapRegionLoader;
import com.github.fauu.helix.systems.RenderingSystem;

public class HelixGame extends ApplicationAdapter {

  World world;
  PerspectiveCamera camera;
  AssetManager assetManager;
  CameraInputController cameraInputController;

	@Override
	public void create () {
    camera = new PerspectiveCamera(13, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.near = 0.1f;
    camera.far = 300f;
    camera.translate(0, -40, 40);
    camera.lookAt(0, 0, 0);

    cameraInputController = new CameraInputController(camera);
    Gdx.input.setInputProcessor(cameraInputController);

    world = new World();
    world.setSystem(new RenderingSystem(camera));
    world.initialize();

    assetManager = new AssetManager();
    assetManager.setLoader(Entity.class, ".hmr",
        new MapRegionLoader(new InternalFileHandleResolver()));

    MapRegionLoader.Parameters parameters = new MapRegionLoader.Parameters();
    parameters.world = world;
    assetManager.load("mapregions/0.hmr", Entity.class, parameters);

    assetManager.finishLoading();

    final Entity mapRegion = assetManager.get("mapregions/0.hmr");

    mapRegion.addToWorld();
	}

	@Override
	public void render () {
    cameraInputController.update();

    world.setDelta(Gdx.graphics.getDeltaTime());
    world.process();
	}

}
