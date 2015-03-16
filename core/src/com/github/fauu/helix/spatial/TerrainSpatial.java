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

package com.github.fauu.helix.spatial;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
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
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.manager.GeometryManager;
import com.github.fauu.helix.shader.ShaderAttribute;
import com.github.fauu.helix.spatial.dto.PartialTileUpdateDTO;

// TODO: Refactor to use util.MeshUtil.mergeMeshes()?
public class TerrainSpatial extends Spatial implements RenderableProvider {
  
  private static final int NUM_POSITION_COMPONENTS = 3;

  private static final int NUM_TEXTURE_COMPONENTS = 2;
  
  private static final int NUM_COMPONENTS
      = NUM_POSITION_COMPONENTS + NUM_TEXTURE_COMPONENTS;
  
  // TODO: refine this
  private static final int NUM_VERTICES_PER_TILE = 6 * NUM_COMPONENTS;
  
  private GeometryManager geometryManager;
  
  private Mesh mesh;
  
  private int meshSize;
  
  private Renderable renderable;
  
  private TextureAtlas textureSet;

  private int numTiles;
  
  public TerrainSpatial(TextureAtlas textureSet) {
    super();

    this.textureSet = textureSet;
  }
  
  public void initialize(Array<Tile> tiles) {
    numTiles = tiles.size;

    constructMesh(tiles);

    updateRenderable();
    
    ready = true;
  }
  
  private void updateRenderable() {
    if (renderable == null) {
      renderable = createRenderable(mesh, meshSize, textureSet);
    } else {
      renderable.mesh = mesh;
      renderable.meshPartSize = meshSize;
    }
  }
  
  private void constructMesh(Array<Tile> tiles) {
    meshSize = NUM_VERTICES_PER_TILE * tiles.size;
    
    mesh = new Mesh(false,
                    meshSize, 
                    0,
                    new VertexAttribute(Usage.Position, 
                                        NUM_POSITION_COMPONENTS, 
                                        "a_position"),
                    new VertexAttribute(Usage.TextureCoordinates, 
                                        NUM_TEXTURE_COMPONENTS, 
                                        "a_texCoord0"));

    Texture texture = textureSet.getTextures().first();

    float[] vertices = new float[meshSize];

    int vertexCount = 0;

    for (Tile tile : tiles) {
      float[] tileVertices
          = getVertexArrayForTile(tile, new Vector2(texture.getWidth(),
                                                    texture.getHeight()));

      System.arraycopy(tileVertices, 
                       0, 
                       vertices, 
                       vertexCount, 
                       tileVertices.length);
      
      vertexCount += tileVertices.length;
    }
    
    mesh.setVertices(vertices, 0, vertexCount);
  }

  private void updateMesh(Array<Tile> tiles, Vector2 terrainDimensions) {
    Texture texture = textureSet.getTextures().first();

    for (Tile tile : tiles) {
      float[] tileVertices
          = getVertexArrayForTile(tile, new Vector2(texture.getWidth(),
                                                    texture.getHeight()));

      int tileIndex = (int) (tile.getPosition().x +
                             (terrainDimensions.x *
                              tile.getPosition().y));

      mesh.updateVertices(tileIndex * NUM_VERTICES_PER_TILE, tileVertices);
    }
  }

  private float[] getVertexArrayForTile(Tile tile, Vector2 setTextureSize) {
    // TODO: cache the result!
    TextureRegion textureRegion
        = textureSet.findRegion(tile.getTextureName());

    Matrix4 transformation
        = new Matrix4().translate(tile.getPosition())
        .translate(0.5f, 0.5f, 0)
        .rotate(0, 0, 1, tile.getOrientation().getAngle())
        .translate(-0.5f, -0.5f, 0);

    Vector2 textureCoords
        = new Vector2(textureRegion.getU(), textureRegion.getV());
    Vector2 textureScaling
        = new Vector2((float) textureRegion.getRegionWidth() /
                              setTextureSize.x,
                      (float) textureRegion.getRegionHeight() /
                              setTextureSize.y);

    Matrix3 uvTransformation
        = new Matrix3().translate(textureCoords)
                       .scale(textureScaling.x, -1 * textureScaling.y)
                       .translate(0, -1);

    Mesh geometry = geometryManager.getGeometry(tile.getGeometryName())
                                   .getMesh()
                                   .copy(true);

    geometry.transform(transformation);
    geometry.transformUV(uvTransformation);

    float[] tileVertices = new float[NUM_VERTICES_PER_TILE];

    geometry.getVertices(tileVertices);

    return tileVertices;
  }

  private Renderable createRenderable(Mesh mesh,
                                      int meshSize,
                                      TextureAtlas textureSet) {
    Renderable renderable = new Renderable();
    
    renderable.mesh = mesh;
    renderable.material = new Material(
        new TextureAttribute(TextureAttribute.Diffuse, 
                             textureSet.getTextures().first()),
        new ShaderAttribute(1));
    renderable.meshPartOffset = 0;
    renderable.meshPartSize = meshSize;
    renderable.primitiveType = GL20.GL_TRIANGLES;
    
    return renderable;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void update(Spatial.UpdateType type, Object value) {
    ready = false;

    switch (type) {
      case TILE_DATA:
        Array<Tile> tiles = (Array<Tile>) value;

        constructMesh(tiles);
        updateRenderable();
        break;
      case TILE_DATA_PARTIAL:
        PartialTileUpdateDTO updateDTO = (PartialTileUpdateDTO) value;

        updateMesh(updateDTO.getTileData(), updateDTO.getTerrainDimensions());
        updateRenderable();
        break;
      default: throw new UnsupportedOperationException();
    }

    ready = true;
  }
  
  public void setGeometryManager(GeometryManager manager) {
    this.geometryManager = manager;
  }

  @Override
  public void getRenderables(Array<Renderable> renderables, 
                             Pool<Renderable> pool) {
    renderables.add(renderable);
  }

}
