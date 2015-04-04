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

package com.github.fauu.helix.editor.spatial;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.spatial.ModelSpatial;

import java.util.HashMap;
import java.util.Map;

public class TilePermissionsGridSpatial extends ModelSpatial {

  private static final float Z_OFFSET = -0.02f;

  private TextureAtlas atlas;

  private ModelInstance instance;

  public TilePermissionsGridSpatial(Tile[][] tiles, TextureAtlas atlas) {
    this.atlas = atlas;

    MeshBuilder meshBuilder = new MeshBuilder();

    meshBuilder.begin(VertexAttributes.Usage.Position |
                      VertexAttributes.Usage.TextureCoordinates,
                      GL20.GL_TRIANGLES);

    for (int y = 0; y < tiles.length; y++) {
      for (int x = 0; x < tiles[y].length; x++) {
        // TODO: Cache regions
        meshBuilder.setUVRange(
            atlas.findRegion(tiles[x][y].getPermissions().name()));

        meshBuilder.rect(x,     y,     0,
                         x + 1, y,     0,
                         x + 1, y + 1, 0,
                         x,     y + 1, 0,
                         0,     0,     1);
      }
    }

    Mesh mesh = meshBuilder.end();

    ModelBuilder modelBuilder = new ModelBuilder();
    modelBuilder.begin();
    modelBuilder.part("grid",
                      mesh,
                      GL20.GL_TRIANGLES,
                      new Material(
                          new TextureAttribute(
                              TextureAttribute.createDiffuse(atlas.getTextures()
                                                                  .first()))));

    instance = new ModelInstance(modelBuilder.end());
    instance.transform.translate(0, 0, Z_OFFSET);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void update(UpdateType type, Object value) {
    switch (type) {
      case TILES_PARTIAL:
        HashMap<Integer, Tile> tilesWithIndex = (HashMap<Integer, Tile>) value;

        Mesh mesh = instance.model.meshes.first();

        // TODO: Get rid of magic numbers
        for (Map.Entry<Integer, Tile> tileWithIndex :
             tilesWithIndex.entrySet()) {
          int index = tileWithIndex.getKey();
          Tile tile = tileWithIndex.getValue();

          float[] vertices = new float[4 * 5];

          mesh.getVertices(index * 4 * 5, 4 * 5, vertices);

          TextureRegion region = atlas.findRegion(tile.getPermissions().name());

          float[] uv = new float[] { region.getU() , region.getV2(),
                                     region.getU2(), region.getV2(),
                                     region.getU2(), region.getV(),
                                     region.getU() , region.getV() };

          for (int i = 0; i < 4; i++) {
            vertices[i * 5 + 3] = uv[i * 2];
            vertices[i * 5 + 4] = uv[i * 2 + 1];
          }

          mesh.updateVertices(index * 4 * 5, vertices);
        }
        break;
      default: throw new UnsupportedOperationException();
    }
  }

  @Override
  public void getRenderables(Array<Renderable> renderables,
                             Pool<Renderable> pool) {
    instance.getRenderables(renderables, pool);
  }
}
