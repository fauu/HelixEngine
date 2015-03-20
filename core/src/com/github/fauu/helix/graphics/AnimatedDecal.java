/** Copyright 2014 Robin Stumm (serverkorken@googlemail.com, http://dermetfan.net/)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

/* Modifications: Quick and dirty appropriation for use with Java 1.6;
                  animation parameter deleted, initialFrame parameter added in newAnimatedDecal() method - Helix Engine Developers */

package com.github.fauu.helix.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;

/** {@link #setTextureRegion(TextureRegion) sets the texture region} of its super type {@link Decal} to the correct one according to the {@link Animation} it holds
 * 	@author dermetfan */
public class AnimatedDecal extends Decal implements ManagedAnimation {

	/** the {@link Animation} to display */
	private Animation animation;

	/** the current time of the {@link Animation} */
	private float time;

	/** if the animation is playing */
	private boolean playing = false;

	/** if the size of the previous frame should be kept by the following frame */
	private boolean keepSize;

	@Override
	protected void update() {
		update(Gdx.graphics.getDeltaTime());
		super.update();
	}

	@Override
	public void update(float delta) {
		if(playing) {
			setTextureRegion(animation.getKeyFrame(time += delta));
			if(!keepSize) {
				TextureRegion region = getTextureRegion();
				setDimensions(region.getRegionWidth(), region.getRegionHeight());
			}
		}
	}

  @Override
  public void play() {
    setPlaying(true);
  }

  @Override
  public void pause() {
    setPlaying(false);
  }

  @Override
	public void setTime(float time) {
		this.time = time;
	}

	@Override
	public float getTime() {
		return time;
	}

	@Override
	public void setPlaying(boolean playing){
		this.playing = playing;
	}

	@Override
	public boolean isPlaying() {
		return playing;
	}

	@Override
	public Animation getAnimated() {
		return animation;
	}

	/** @param animation the {@link #animation} to set */
	public void setAnimated(Animation animation) {
		this.animation = animation;
	}

	/** @return the {@link #keepSize} */
	public boolean isKeepSize() {
		return keepSize;
	}

	/** @param keepSize the {@link #keepSize} to set */
	public void setKeepSize(boolean keepSize) {
		this.keepSize = keepSize;
	}

	/** @see Decal#newDecal(TextureRegion) */
	public static AnimatedDecal newAnimatedDecal(TextureRegion initialFrame) {
		return newAnimatedDecal(initialFrame.getRegionWidth(), initialFrame.getRegionHeight(), initialFrame, DecalMaterial.NO_BLEND, DecalMaterial.NO_BLEND);
	}

	/** @see Decal#newDecal(TextureRegion, boolean) */
	public static AnimatedDecal newDecal(TextureRegion initialFrame, boolean hasTransparency) {
		return newAnimatedDecal(initialFrame.getRegionWidth(), initialFrame.getRegionHeight(), initialFrame, hasTransparency ? GL20.GL_SRC_ALPHA : DecalMaterial.NO_BLEND, hasTransparency ? GL20.GL_ONE_MINUS_SRC_ALPHA : DecalMaterial.NO_BLEND);
	}

	/** @see Decal#newDecal(float, float, TextureRegion) */
	public static AnimatedDecal newAnimatedDecal(float width, float height, TextureRegion initialFrame) {
		return newAnimatedDecal(width, height, initialFrame, DecalMaterial.NO_BLEND, DecalMaterial.NO_BLEND);
	}

	/** @see Decal#newDecal(float, float, TextureRegion, boolean) */
	public static AnimatedDecal newAnimatedDecal(float width, float height, TextureRegion initialFrame, boolean hasTransparency) {
		return newAnimatedDecal(width, height, initialFrame, hasTransparency ? GL20.GL_SRC_ALPHA : DecalMaterial.NO_BLEND, hasTransparency ? GL20.GL_ONE_MINUS_SRC_ALPHA : DecalMaterial.NO_BLEND);
	}

	/** @see Decal#newDecal(float, float, TextureRegion, int, int) */
	public static AnimatedDecal newAnimatedDecal(float width, float height, TextureRegion initialFrame, int srcBlendFactor, int dstBlendFactor) {
		AnimatedDecal decal = new AnimatedDecal();
		decal.setTextureRegion(initialFrame);
		decal.setBlending(srcBlendFactor, dstBlendFactor);
		decal.dimensions.x = width;
		decal.dimensions.y = height;
		decal.setColor(1, 1, 1, 1);
		return decal;
	}

  @Override
  public void stop() {
    pause();
    setTime(0);
  }

  @Override
  public void rewind(float duration) {
    update(getTime() - duration);
  }

  @Override
  public void flipFrames(boolean flipX, boolean flipY) {
    flipFrames(flipX, flipY, false);
  }

  @Override
  public void flipFrames(boolean flipX, boolean flipY, boolean set) {
    flipFrames(0, getAnimated().getAnimationDuration(), flipX, flipY, set);
  }

  @Override
  public void flipFrames(float startTime, float endTime, boolean flipX, boolean flipY) {
    flipFrames(startTime, endTime, flipX, flipY, false);
  }

  @Override
  public void flipFrames(float startTime, float endTime, boolean flipX, boolean flipY, boolean set) {
    Animation animation = getAnimated();

		for(float t = startTime; t < endTime; t += animation.getFrameDuration()) {
			TextureRegion frame = animation.getKeyFrame(t);
			frame.flip(flipX && (!set || !frame.isFlipX()), flipY && (!set || !frame.isFlipY()));
		}
  }

  @Override
  public boolean isAnimationFinished() {
    return getAnimated().isAnimationFinished(getTime());
  }

}
