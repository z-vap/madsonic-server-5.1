/*
 This file is part of Subsonic.

 Subsonic is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Subsonic is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Subsonic.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2009 (C) Sindre Mehus
 */
package org.madsonic.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import org.madsonic.domain.Player;
import org.madsonic.domain.User;
import org.madsonic.domain.UserSettings;
import org.madsonic.service.PlayerService;
import org.madsonic.service.SecurityService;
import org.madsonic.service.SettingsService;

/**
 * Controller for the playlist frame.
 *
 * @author Sindre Mehus
 */
public class PlayQueueController extends ParameterizableViewController {

    private PlayerService playerService;
    private SecurityService securityService;
    private SettingsService settingsService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = securityService.getCurrentUser(request);
        UserSettings userSettings = settingsService.getUserSettings(user.getUsername());
        Player player = playerService.getPlayer(request, response);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);
        map.put("player", player);
        map.put("players", playerService.getPlayersForUserAndClientId(user.getUsername(), null));
        map.put("visibility", userSettings.getPlaylistVisibility());
        map.put("buttonVisibility", userSettings.getButtonVisibility()); 
        map.put("customScrollbar", userSettings.isCustomScrollbarEnabled()); 		
        map.put("partyMode", userSettings.isPartyModeEnabled());
        map.put("notify", userSettings.isSongNotificationEnabled());
        ModelAndView result = super.handleRequestInternal(request, response);
        result.addObject("model", map);
        return result;
    }

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
}