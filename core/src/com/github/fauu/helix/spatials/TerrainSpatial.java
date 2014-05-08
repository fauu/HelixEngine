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

package com.github.fauu.helix.spatials;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.github.fauu.helix.datums.TileDatum;

public class TerrainSpatial extends Spatial implements RenderableProvider {

  public static final int NUM_POSITION_COMPONENTS = 3;
  public static final int NUM_TEXTURE_COMPONENTS = 2;
  public static final int NUM_COLOR_COMPONENTS = 1;
  public static final int NUM_COMPONENTS
      = NUM_POSITION_COMPONENTS + NUM_TEXTURE_COMPONENTS + NUM_COLOR_COMPONENTS;
  public static final int NUM_MAX_VERTICES_PER_TILE = 128 * NUM_COMPONENTS;

  private Array<TileDatum> tileData;
  private Mesh mesh;
  private Renderable renderable;
  private TextureAtlas textureSet;

  private int meshSize;

  public TerrainSpatial(final Array<TileDatum> tileData, final TextureAtlas textureSet) {
    this.tileData = tileData;
    this.textureSet = textureSet;
    ready = false;

    meshSize = NUM_MAX_VERTICES_PER_TILE * tileData.size;

    mesh = new Mesh(true, meshSize, 0,
        new VertexAttribute(Usage.Position, NUM_POSITION_COMPONENTS, "a_position"),
        new VertexAttribute(Usage.TextureCoordinates, NUM_TEXTURE_COMPONENTS, "a_texCoord0"),
        new VertexAttribute(Usage.ColorPacked, 4, "a_color"));

    updateMesh();

    renderable = new Renderable();
    renderable.mesh = mesh;
    renderable.material = new Material(
        new TextureAttribute(TextureAttribute.Diffuse, textureSet.getTextures().first()));
    renderable.meshPartOffset = 0;
    renderable.meshPartSize = meshSize;
    renderable.primitiveType = GL20.GL_TRIANGLES;
  }

  @Override
  public void getRenderables(final Array<Renderable> renderables, final Pool<Renderable> pool) {
    renderables.add(renderable);
  }

  private void updateMesh() {
    final float[] vertices = new float[meshSize];

    int vertexCount = 0;
    for (final TileDatum tileDatum : tileData) {
      final Texture texture = textureSet.getTextures().first();
      final TextureRegion textureRegion = textureSet.getRegions().get(tileDatum.getTextureId());

      final Vector2 textureCoords = new Vector2(textureRegion.getU(), textureRegion.getV());
      final Vector2 textureScaling = new Vector2(
          (float) textureRegion.getRegionWidth() / texture.getWidth(),
          (float) textureRegion.getRegionHeight() / texture.getHeight());

      final Matrix3 transformationMatrixUV = new Matrix3()
          .translate(textureCoords)
          .scale(textureScaling.x, -1 * textureScaling.y)
          .translate(0, -1);

      final Matrix4 transformationMatrix = new Matrix4()
          .translate(
              tileDatum.getPosition().x,
              tileDatum.getPosition().y,
              tileDatum.getPosition().z)
          .translate(0.5f, 0.5f, 0)
          .rotate(0, 0, 1, tileDatum.getOrientation().getAngle())
          .translate(-0.5f, -0.5f, 0);

      final Mesh geometry = tileDatum.getGeometry().copy(true);
      final float[] nonColorVertices
          = new float[(NUM_COMPONENTS - NUM_COLOR_COMPONENTS) * geometry.getNumVertices()];

      geometry.transform(transformationMatrix);
      geometry.transformUV(transformationMatrixUV);
      geometry.getVertices(nonColorVertices);

      final float[] allVertices = new float[NUM_COMPONENTS * geometry.getNumVertices()];

      final Color tileColor = new Color(1, 1, 1, 1);

      for (int i = 0, j = 0; i < allVertices.length; i++) {
        if ((i + 1) % NUM_COMPONENTS != 0) {
          allVertices[i] = nonColorVertices[j++];
        } else {
          allVertices[i] = tileColor.toFloatBits();
        }
      }

      System.arraycopy(allVertices, 0, vertices, vertexCount, allVertices.length);

      vertexCount += allVertices.length;
    }

    mesh.setVertices(vertices, 0, vertexCount);

    ready = true;
  }

}
