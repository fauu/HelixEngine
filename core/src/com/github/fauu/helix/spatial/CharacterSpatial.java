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

import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.graphics.AnimatedDecal;
import com.github.fauu.helix.graphics.AnimationSet;
import com.github.fauu.helix.graphics.AnimationType;
import com.github.fauu.helix.spatial.update.dto.AnimationUpdateDTO;
import com.github.fauu.helix.util.IntVector3;

public class CharacterSpatial extends DecalSpatial {

  protected AnimationSet animations;

  public CharacterSpatial(IntVector3 ownerPosition, String animationSetName) {
    super(ownerPosition);

    animations = new AnimationSet(animationSetName);

    AnimatedDecal decal
        = AnimatedDecal.newAnimatedDecal(DEFAULT_DIMENSIONS.x,
                                         DEFAULT_DIMENSIONS.y,
                                         animations.getDefault(),
                                         true);
    decal.setKeepSize(true);
    decal.setPosition(ownerPosition.toVector3());
    decal.getPosition().add(DEFAULT_DISPLACEMENT);
    decal.rotateX(DEFAULT_ROTATION);

    setDecal(decal);
  }

  @Override
  public void update(UpdateType type, Object value) {
    AnimatedDecal decal = ((AnimatedDecal) this.decal);

    switch (type) {
      case ORIENTATION:
        decal.setAnimated(
            animations.get(AnimationType.IDLE, (Direction) value));
        decal.play();
        break;
      case POSITION:
        decal.translate((Vector3) value);
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
