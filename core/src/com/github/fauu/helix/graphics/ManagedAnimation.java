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

/* Modifications: Quick and dirty appropriation for use with Java 1.6 - Helix Engine Developers */

package com.github.fauu.helix.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;

/**	a managed animation state machine
 *  @author dermetfan */
public interface ManagedAnimation extends Animated<Animation> {

	/** {@link #pause() pauses} and {@link #setTime(float) sets} the time to 0 */
	@Override
	void stop();

	/** flips all frames
	 *  @see #flipFrames(boolean, boolean, boolean) */
	void flipFrames(boolean flipX, boolean flipY);

  /** flips all frames
	 *  @see #flipFrames(float, float, boolean, boolean, boolean) */
	void flipFrames(boolean flipX, boolean flipY, boolean set);

  /** flips all frames
	 *  @see #flipFrames(float, float, boolean, boolean, boolean) */
	void flipFrames(float startTime, float endTime, boolean flipX, boolean flipY);

	void flipFrames(float startTime, float endTime, boolean flipX, boolean flipY, boolean set);

	boolean isAnimationFinished();

}