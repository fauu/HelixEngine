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

package com.github.fauu.helix.loaders;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.EntityFactory;
import com.github.fauu.helix.components.*;
import com.github.fauu.helix.datums.TileDatum;
import com.github.fauu.helix.spatials.ObjectSpatial;
import com.github.fauu.helix.spatials.Spatial;
import com.github.fauu.helix.spatials.TerrainSpatial;

import java.io.IOException;

public class MapRegionLoader
    extends AsynchronousAssetLoader<Entity, MapRegionLoader.Parameters> {

  public static class Parameters extends AssetLoaderParameters<Entity> {
    public World world;
  }

  protected Entity mapRegion;
  protected XmlReader xml = new XmlReader();
  protected XmlReader.Element root;

  public MapRegionLoader() {
    super(new InternalFileHandleResolver());
  }

  public MapRegionLoader(final FileHandleResolver resolver) {
    super(resolver);
  }

  @Override
  public void loadAsync(final AssetManager manager, final String fileName, final FileHandle hmrFile,
      final Parameters parameters) {}

  @Override
  public Entity loadSync(final AssetManager manager, final String fileName, FileHandle hmrFile,
      final Parameters parameters) {
    try {
      hmrFile = resolve(fileName);

      root = xml.parse(hmrFile);

      mapRegion = loadMapRegion(manager, root, hmrFile, parameters);

      return mapRegion;
    } catch (IOException e) {
      throw new GdxRuntimeException("Couldn't load map region '" + fileName + "'", e);
    }
  }

  protected Entity loadMapRegion(final AssetManager manager, final XmlReader.Element root,
      final FileHandle hmrFile, final Parameters parameters) {
    final Array<TileDatum> tileData = new Array<TileDatum>();
    final Array<Mesh> geometries = getGeometries(manager);

    final XmlReader.Element tilesElement = root.getChildByName("terrain");
    for (final XmlReader.Element element : tilesElement.getChildrenByName("tile")) {
      final Vector3 position = new Vector3(
          element.getIntAttribute("x", 0),
          element.getIntAttribute("y", 0),
          element.getIntAttribute("z", 0));
      final int geometryId = element.getIntAttribute("geometryid", 0);
      final int textureId = element.getIntAttribute("textureid", 0);
      final Direction orientation
          = Direction.valueOf(element.getAttribute("orientation", "south").toUpperCase());

      final TileDatum tileDatum = new TileDatum();
      tileDatum.setPosition(position);
      tileDatum.setGeometry(geometries.get(geometryId));
      tileDatum.setTextureId(textureId);
      tileDatum.setOrientation(orientation);

      tileData.add(tileDatum);
    }

    final int textureSetId = root.getIntAttribute("texturesetid", 0);
    final TextureAtlas textureSet = manager.get("texturesets/" + textureSetId + ".atlas");

    final Spatial terrainSpatial = new TerrainSpatial(tileData, textureSet);

    final Entity terrain = EntityFactory.createTerrain(parameters.world);
    terrain.getComponent(TileDataComponent.class).set(tileData);
    terrain.getComponent(SpatialFormComponent.class).setSpatial(terrainSpatial);
    terrain.addToWorld();

    final XmlReader.Element objectsElement = root.getChildByName("objects");
    for (XmlReader.Element element : objectsElement.getChildrenByName("object")) {
      final Vector3 position = new Vector3(
        element.getIntAttribute("x", 0),
        element.getIntAttribute("y", 0),
        element.getIntAttribute("z", 0));
      final String modelName = element.getAttribute("model");
      final Model model = manager.get("models/" + modelName + ".g3db");
      final Direction orientation
          = Direction.valueOf(element.getAttribute("orientation", "south").toUpperCase());

      final Matrix4 initialTransform = new Matrix4()
          .translate(position.x, position.y, position.z)
          .translate(0.5f, 0.5f, 0)
          .rotate(0, 0, 1, orientation.getAngle())
          .translate(-0.5f, -0.5f, 0);

      final Spatial spatial = new ObjectSpatial(model, initialTransform);

      final Entity object = EntityFactory.createObject(parameters.world);
      object.getComponent(PositionComponent.class).set(position);
      object.getComponent(OrientationComponent.class).set(orientation);
      object.getComponent(SpatialFormComponent.class).setSpatial(spatial);
      object.addToWorld();
    }

    final Entity mapRegion = EntityFactory.createMapRegion(parameters.world);
    mapRegion.getComponent(PositionComponent.class).set(new Vector3(0, 0, 0));

    return mapRegion;
  }

  @Override
  public Array<AssetDescriptor> getDependencies(final String fileName, final FileHandle hmrFile,
      final Parameters parameters) {
    final Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();

    try {
      root = xml.parse(hmrFile);

      final int textureSetId = root.getIntAttribute("texturesetid", 0);

      dependencies.add(new AssetDescriptor<TextureAtlas>(
          "texturesets/" + Integer.toString(textureSetId) + ".atlas", TextureAtlas.class));

      final FileHandle geometryDir = Gdx.files.internal("tilegeometries");
      final long geometryCount = geometryDir.list().length;

      for (int i = 0; i < geometryCount; i++) {
        dependencies.add(new AssetDescriptor<Model>("tilegeometries/" + i + ".obj", Model.class));
      }

      final XmlReader.Element objectsElement = root.getChildByName("objects");

      final Array<String> loadedModelNames = new Array<String>();
      for (final XmlReader.Element element : objectsElement.getChildrenByName("object")) {
        final String modelName = element.getAttribute("model");

        if (!loadedModelNames.contains(modelName, false)) {
          dependencies.add(new AssetDescriptor<Model>("models/" + modelName + ".g3db", Model.class));

          loadedModelNames.add(modelName);
        }
      }

      return dependencies;
    } catch (IOException e) {
      throw new GdxRuntimeException("Couldn't load map region '" + fileName + "'", e);
    }
  }

  private Array<Mesh> getGeometries(final AssetManager manager) {
    final Array<Mesh> geometries = new Array<Mesh>();

    final FileHandle geometryDir = Gdx.files.internal("tilegeometries");
    final long geometryCount = geometryDir.list().length;

    for (int i = 0; i < geometryCount; i++) {
      final Model geometryModel = manager.get("tilegeometries/" + i + ".obj");

      geometries.add(geometryModel.meshes.first());
    }

    return geometries;
  }

}
