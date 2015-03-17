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
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.spatial.Spatial;
import com.github.fauu.helix.util.IntVector2;
import com.github.fauu.helix.util.TileUtil;

public class TilePermissionsGridSpatial extends Spatial {

  private static final float Z_OFFSET = -0.01f;

  private ModelInstance instance;

  public TilePermissionsGridSpatial(Array<Tile> tiles,
                                    IntVector2 areaDimensions,
                                    TextureAtlas atlas) {
    MeshBuilder meshBuilder = new MeshBuilder();

    meshBuilder.begin(VertexAttributes.Usage.Position |
                      VertexAttributes.Usage.TextureCoordinates,
                      GL20.GL_TRIANGLES);

    int i = 0;
    for (Tile tile : tiles) {
      IntVector2 position = TileUtil.calculatePosition(i, areaDimensions);

      meshBuilder.setUVRange(atlas.findRegion(tile.getPermissions().name()));

      meshBuilder.rect(position.x, position.y, Z_OFFSET,
                       position.x + 1, position.y, Z_OFFSET,
                       position.x + 1, position.y + 1, Z_OFFSET,
                       position.x, position.y + 1, Z_OFFSET,
                       0, 0, 1);

      i++;
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

    ready = true;
  }

  @Override
  public void update(UpdateType type, Object value) { }

  @Override
  public void getRenderables(Array<Renderable> renderables,
                             Pool<Renderable> pool) {
    instance.getRenderables(renderables, pool);
  }
}
