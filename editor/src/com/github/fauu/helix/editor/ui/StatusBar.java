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

package com.github.fauu.helix.editor.ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class StatusBar extends JPanel {

  public static final String MATCHED_TILE_INFO_PLACEHOLDER = " ";

  JLabel matchedTileInfo;

  public StatusBar() {
    setLayout(new MigLayout("rtl"));

    matchedTileInfo = new JLabel(MATCHED_TILE_INFO_PLACEHOLDER);

    add(matchedTileInfo);
  }

  public void setMatchedTileInfoText(final String text) {
    matchedTileInfo.setText(text);
  }

  public void resetMatchedTileInfoText() {
    matchedTileInfo.setText(MATCHED_TILE_INFO_PLACEHOLDER);
  }

}
