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

package com.github.fauu.helix.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.github.fauu.helix.datums.TileDatum;
import com.github.fauu.helix.editor.ui.Sidebar;
import com.github.fauu.helix.editor.ui.StatusBar;

import javax.swing.*;
import java.awt.*;

public class HelixEditor extends JFrame {

  private StatusBar statusBar;

  public HelixEditor() {
    setTitle("helix");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    final Container mainContainer = getContentPane();
    mainContainer.setLayout(new BorderLayout());

    final Sidebar sidebar = new Sidebar();
    mainContainer.add(sidebar, BorderLayout.LINE_START);

    final LwjglCanvas worldCanvas = new LwjglCanvas(new EditorWorld(this));
    worldCanvas.getCanvas().setPreferredSize(new Dimension(800, 480));
    mainContainer.add(worldCanvas.getCanvas(), BorderLayout.CENTER);

    statusBar = new StatusBar();
    mainContainer.add(statusBar, BorderLayout.PAGE_END);

    pack();
    setVisible(true);
  }

  public void setMatchedTileInfo(final TileDatum datum) {
    if (datum != null) {
      statusBar.setMatchedTileInfoText("Position: " + datum.getPosition());
    } else {
      statusBar.resetMatchedTileInfoText();
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new HelixEditor();
      }
    });
  }

}