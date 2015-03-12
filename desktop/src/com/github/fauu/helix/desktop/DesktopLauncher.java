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

package com.github.fauu.helix.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.fauu.helix.HelixGame;

public class DesktopLauncher {

  public static void main (String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

    config.title = "helix";
    config.width = 800;
    config.height = 480;

    new LwjglApplication(new HelixGame(), config);
  }

}
