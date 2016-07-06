/*
 * Copyright (C) 2014-2016 Helix Engine Developers
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

package com.github.fauu.helix.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.fauu.helix.Direction;

import java.util.EnumMap;

public class AnimationSet {

  private static final String DIRECTORY_NAME;

  private static final String EXTENSION;

  private static final String TEXTURE_EXTENSION;

  private static final Direction[] DIRECTIONS;

  EnumMap<AnimationType, EnumMap<Direction, Animation>> animations;

  static {
    DIRECTORY_NAME = "animation-set";
    EXTENSION = "anim";
    TEXTURE_EXTENSION = "png";

    DIRECTIONS = new Direction[4];
    DIRECTIONS[0] = Direction.NORTH;
    DIRECTIONS[1] = Direction.EAST;
    DIRECTIONS[2] = Direction.SOUTH;
    DIRECTIONS[3] = Direction.WEST;
  }

  public AnimationSet(String name) {
    animations = new EnumMap<AnimationType, EnumMap<Direction, Animation>>(
        AnimationType.class);

    for (AnimationType at : AnimationType.values()) {
      animations.put(at, new EnumMap<Direction, Animation>(Direction.class));
    }

    load(name);
  }

  private void load(String name) {
    FileHandle file
        = Gdx.files.internal(DIRECTORY_NAME + "/" + name + "." + EXTENSION);

    FileHandle textureFile
        = Gdx.files.internal(
            DIRECTORY_NAME + "/" + name + "." + TEXTURE_EXTENSION);

    TextureRegion[][] frameSheet
        = TextureRegion.split(new Texture(textureFile), 32, 32);

    String[] entries = file.readString().split("[\\r\\n]+");
    for (String entry : entries) {
      String[] segments = entry.split(":");
      String[] frameIndices = segments[1].split(",");

      AnimationType type = AnimationType.valueOf(segments[0]);

      for (int i = 0; i < DIRECTIONS.length; i++) {
        TextureRegion[] frames = new TextureRegion[frameIndices.length];

        for (int j = 0; j < frameIndices.length; j++) {
          frames[j] = frameSheet[i][Integer.valueOf(frameIndices[j])];
        }

        Animation animation = new Animation(1, frames);
        animation.setPlayMode(Animation.PlayMode.NORMAL);

        add(type, DIRECTIONS[i], animation);
      }
    }
  }

  private void add(AnimationType type,
                   Direction direction,
                   Animation animation) {
    animations.get(type).put(direction, animation);
  }

  public Animation get(AnimationType type, Direction direction) {
    return get(type, direction, 1);
  }

  // TODO: Duration shouldn't be set here
  public Animation get(AnimationType type, Direction direction, float duration) {
    Animation animation = animations.get(type).get(direction);

    animation.setFrameDuration(duration / animation.getKeyFrames().length);

    return animation;
  }

  public Animation getDefault() {
    return animations.get(AnimationType.IDLE).get(Direction.SOUTH);
  }

}
