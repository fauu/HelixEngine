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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.graphics.AnimatedDecal;
import com.github.fauu.helix.util.IntVector3;

import java.util.HashMap;

// TODO: Refactor this a bit
public class PlayerSpatial extends DecalSpatial {

  private Direction orientation;

  private HashMap<Direction, TextureRegion> idleFrames;

  private HashMap<Direction, Animation[]> walkAnimations;

  private int animationCycleCounter;

  public PlayerSpatial(IntVector3 position,
                       Direction orientation,
                       float movementSpeed) {
    super(position);

    this.orientation = orientation;

    Texture texture = new Texture(Gdx.files.internal("texture-atlas/player.png"));

    TextureRegion[][] frameSheet = TextureRegion.split(texture, 32, 32);

    idleFrames = new HashMap<Direction, TextureRegion>();

    walkAnimations = new HashMap<Direction, Animation[]>();

    Direction[] directions = new Direction[] { Direction.SOUTH,
                                               Direction.EAST,
                                               Direction.WEST,
                                               Direction.NORTH };

    for (int i = 0; i < frameSheet.length; i++) {
      idleFrames.put(directions[i], frameSheet[i][1]);

      Animation[] animations = new Animation[2];
      for (int j = 0; j < 2; j++) {
        TextureRegion[] frames = new TextureRegion[2];

        frames[0] = j == 0 ? frameSheet[i][0] : frameSheet[i][2];
        frames[1] = frameSheet[i][1];

        Animation animation = new Animation(1 / movementSpeed - 0.1f, frames);
        animation.setPlayMode(Animation.PlayMode.NORMAL);

        animations[j] = animation;
      }

      walkAnimations.put(directions[i], animations);
    }

    AnimatedDecal decal
        = AnimatedDecal.newAnimatedDecal(DEFAULT_DIMENSIONS.x,
                                         DEFAULT_DIMENSIONS.y,
                                         idleFrames.get(orientation),
                                         true);

    decal.setKeepSize(true);
    decal.setPosition(position.toVector3());
    decal.getPosition().add(DEFAULT_DISPLACEMENT);
    decal.rotateX(45);
    decal.setTextureRegion(idleFrames.get(orientation));

    setDecal(decal);
  }

  @Override
  public void update(UpdateType type, Object value) {
    switch (type) {
      case ORIENTATION:
        decal.setTextureRegion(idleFrames.get(value));

        orientation = (Direction) value;
        break;
      case POSITION:
        decal.translate((Vector3) value);
        break;
      case PLAY_ANIMATION:
        ((AnimatedDecal) decal).setAnimated(walkAnimations.get(value)[animationCycleCounter]);
        ((AnimatedDecal) decal).play();

        animationCycleCounter = animationCycleCounter == 1 ? 0 : 1;
        break;
      case STOP_ANIMATION:
        ((AnimatedDecal) decal).stop();

        break;
      case IDLE:
        ((AnimatedDecal) decal).stop();

        decal.setTextureRegion(idleFrames.get(value));
        break;
      default: throw new UnsupportedOperationException();
    }
  }

}
