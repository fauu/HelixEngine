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

import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.graphics.AnimatedDecal;
import com.github.fauu.helix.graphics.AnimationSet;
import com.github.fauu.helix.graphics.AnimationType;
import com.github.fauu.helix.spatial.update.dto.AnimationUpdateDTO;
import com.github.fauu.helix.util.IntVector3;

public class CharacterSpatial extends DecalSpatial {

  @Wire
  private AssetManager assetManager;

  protected AnimationSet animations;

  public CharacterSpatial(IntVector3 ownerPosition,
                          String animationSetName,
                          TextureRegion shadowTexture) {
    super(ownerPosition);

    animations = new AnimationSet(animationSetName);


    AnimatedDecal decal
        = AnimatedDecal.newAnimatedDecal(DEFAULT_DIMENSIONS.x,
                                         DEFAULT_DIMENSIONS.y,
                                         animations.getDefault(),
                                         true);

    decal.setKeepSize(true);
    decal.setPosition(ownerPosition.toVector3());
    decal.translate(DEFAULT_DISPLACEMENT);
    decal.rotateX(DEFAULT_ROTATION);

    setMainDecal(decal);

    Decal shadow = new Decal();

    shadow.setPosition(ownerPosition.toVector3());
    shadow.translate(DEFAULT_SHADOW_DISPLACEMENT);
    shadow.setDimensions(DEFAULT_SHADOW_DIMENSIONS.x,
        DEFAULT_SHADOW_DIMENSIONS.y);
    shadow.setColor(1, 1, 1, .35f);
    shadow.setTextureRegion(shadowTexture);
    shadow.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    setShadowDecal(shadow);
  }

  @Override
  public void update(UpdateType type, Object value) {
    AnimatedDecal decal = ((AnimatedDecal) this.mainDecal);

    switch (type) {
      case ORIENTATION:
        decal.setAnimated(
            animations.get(AnimationType.IDLE, (Direction) value));
        decal.play();
        break;
      case POSITION:
          mainDecal.translate((Vector3) value);
          shadowDecal.translate((Vector3) value);
        break;
      case ANIMATION:
        AnimationUpdateDTO updateDTO = (AnimationUpdateDTO) value;

        decal.stop();
        decal.setAnimated(animations.get(updateDTO.type,
                                         updateDTO.direction,
                                         updateDTO.duration));
        decal.play();
        break;
      default: throw new UnsupportedOperationException();
    }
  }

}
