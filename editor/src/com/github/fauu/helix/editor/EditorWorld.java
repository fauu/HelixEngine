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

package com.github.fauu.helix.editor;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.systems.event.BasicEventDeliverySystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.github.fauu.helix.loaders.MapRegionLoader;
import com.github.fauu.helix.systems.RenderingSystem;
import com.github.fauu.helix.systems.editor.EditorInputProcessingSystem;
import com.github.fauu.helix.systems.editor.EditorTileMatchingSystem;

public class EditorWorld extends ApplicationAdapter {

  World world;
  HelixEditor editor;
  PerspectiveCamera camera;
  AssetManager assetManager;
  CameraInputController cameraInputController;

  public EditorWorld(final HelixEditor editor) {
    this.editor = editor;
  }

  @Override
  public void create() {
    camera = new PerspectiveCamera(13, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.near = 0.1f;
    camera.far = 300f;
    camera.translate(0, -40, 40);
    camera.lookAt(0, 0, 0);

    cameraInputController = new CameraInputController(camera);
    Gdx.input.setInputProcessor(cameraInputController);

    world = new World();
    world.setEventDeliverySystem(new BasicEventDeliverySystem());
    world.setSystem(new RenderingSystem(camera));
    world.setSystem(new EditorInputProcessingSystem());
    world.setSystem(new EditorTileMatchingSystem(editor, camera));
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

  public void create(HelixEditor editor) {

  }

  @Override
  public void render() {
    cameraInputController.update();

    world.setDelta(Gdx.graphics.getDeltaTime());
    world.process();
  }

}
