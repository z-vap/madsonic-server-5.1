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
package org.madsonic.domain;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import org.madsonic.domain.PlayQueue.SortOrder;
import org.madsonic.domain.PlayQueue.Status;

/**
 * Unit test of {@link PlayQueue}.
 *
 * @author Sindre Mehus
 */
public class PlayQueueTestCase extends TestCase {

    public void testEmpty() {
        PlayQueue playQueue = new PlayQueue();
        assertEquals(0, playQueue.size());
        assertTrue(playQueue.isEmpty());
        assertEquals(0, playQueue.getFiles().size());
        assertNull(playQueue.getCurrentFile());
    }

    public void testStatus() throws Exception {
        PlayQueue playQueue = new PlayQueue();
        assertEquals(Status.PLAYING, playQueue.getStatus());

        playQueue.setStatus(Status.STOPPED);
        assertEquals(Status.STOPPED, playQueue.getStatus());

        playQueue.addFiles(true, new TestMediaFile());
        assertEquals(Status.PLAYING, playQueue.getStatus());

        playQueue.clear();
        assertEquals(Status.PLAYING, playQueue.getStatus());
    }

    public void testMoveUp() throws Exception {
        PlayQueue playQueue = createPlaylist(0, "A", "B", "C", "D");
        playQueue.moveUp(0);
        assertPlaylistEquals(playQueue, 0, "A", "B", "C", "D");

        playQueue = createPlaylist(0, "A", "B", "C", "D");
        playQueue.moveUp(9999);
        assertPlaylistEquals(playQueue, 0, "A", "B", "C", "D");

        playQueue = createPlaylist(1, "A", "B", "C", "D");
        playQueue.moveUp(1);
        assertPlaylistEquals(playQueue, 0, "B", "A", "C", "D");

        playQueue = createPlaylist(3, "A", "B", "C", "D");
        playQueue.moveUp(3);
        assertPlaylistEquals(playQueue, 2, "A", "B", "D", "C");
    }

    public void testMoveDown() throws Exception {
        PlayQueue playQueue = createPlaylist(0, "A", "B", "C", "D");
        playQueue.moveDown(0);
        assertPlaylistEquals(playQueue, 1, "B", "A", "C", "D");

        playQueue = createPlaylist(0, "A", "B", "C", "D");
        playQueue.moveDown(9999);
        assertPlaylistEquals(playQueue, 0, "A", "B", "C", "D");

        playQueue = createPlaylist(1, "A", "B", "C", "D");
        playQueue.moveDown(1);
        assertPlaylistEquals(playQueue, 2, "A", "C", "B", "D");

        playQueue = createPlaylist(3, "A", "B", "C", "D");
        playQueue.moveDown(3);
        assertPlaylistEquals(playQueue, 3, "A", "B", "C", "D");
    }

    public void testRemove() throws Exception {
        PlayQueue playQueue = createPlaylist(0, "A", "B", "C", "D");
        playQueue.removeFileAt(0);
        assertPlaylistEquals(playQueue, 0, "B", "C", "D");

        playQueue = createPlaylist(1, "A", "B", "C", "D");
        playQueue.removeFileAt(0);
        assertPlaylistEquals(playQueue, 0, "B", "C", "D");

        playQueue = createPlaylist(0, "A", "B", "C", "D");
        playQueue.removeFileAt(3);
        assertPlaylistEquals(playQueue, 0, "A", "B", "C");

        playQueue = createPlaylist(1, "A", "B", "C", "D");
        playQueue.removeFileAt(1);
        assertPlaylistEquals(playQueue, 1, "A", "C", "D");

        playQueue = createPlaylist(3, "A", "B", "C", "D");
        playQueue.removeFileAt(3);
        assertPlaylistEquals(playQueue, 2, "A", "B", "C");

        playQueue = createPlaylist(0, "A");
        playQueue.removeFileAt(0);
        assertPlaylistEquals(playQueue, -1);
    }

    public void testNext() throws Exception {
        PlayQueue playQueue = createPlaylist(0, "A", "B", "C");
        assertFalse(playQueue.isRepeatEnabled());
        playQueue.next();
        assertPlaylistEquals(playQueue, 1, "A", "B", "C");
        playQueue.next();
        assertPlaylistEquals(playQueue, 2, "A", "B", "C");
        playQueue.next();
        assertPlaylistEquals(playQueue, -1, "A", "B", "C");

        playQueue = createPlaylist(0, "A", "B", "C");
        playQueue.setRepeatEnabled(true);
        assertTrue(playQueue.isRepeatEnabled());
        playQueue.next();
        assertPlaylistEquals(playQueue, 1, "A", "B", "C");
        playQueue.next();
        assertPlaylistEquals(playQueue, 2, "A", "B", "C");
        playQueue.next();
        assertPlaylistEquals(playQueue, 0, "A", "B", "C");
    }

    public void testPlayAfterEndReached() throws Exception {
        PlayQueue playQueue = createPlaylist(2, "A", "B", "C");
        playQueue.setStatus(Status.PLAYING);
        playQueue.next();
        assertNull(playQueue.getCurrentFile());
        assertEquals(Status.STOPPED, playQueue.getStatus());

        playQueue.setStatus(Status.PLAYING);
        assertEquals(Status.PLAYING, playQueue.getStatus());
        assertEquals(0, playQueue.getIndex());
        assertEquals("A", playQueue.getCurrentFile().getName());
    }

    public void testAppend() throws Exception {
        PlayQueue playQueue = createPlaylist(1, "A", "B", "C");

        playQueue.addFiles(true, new TestMediaFile("D"));
        assertPlaylistEquals(playQueue, 1, "A", "B", "C", "D");

        playQueue.addFiles(false, new TestMediaFile("E"));
        assertPlaylistEquals(playQueue, 0, "E");
    }

    public void testUndo() throws Exception {
        PlayQueue playQueue = createPlaylist(0, "A", "B", "C");
        playQueue.setIndex(2);
        playQueue.undo();
        assertPlaylistEquals(playQueue, 0, "A", "B", "C");

        playQueue.removeFileAt(2);
        playQueue.undo();
        assertPlaylistEquals(playQueue, 0, "A", "B", "C");

        playQueue.clear();
        playQueue.undo();
        assertPlaylistEquals(playQueue, 0, "A", "B", "C");

        playQueue.addFiles(true, new TestMediaFile());
        playQueue.undo();
        assertPlaylistEquals(playQueue, 0, "A", "B", "C");

        playQueue.moveDown(1);
        playQueue.undo();
        assertPlaylistEquals(playQueue, 0, "A", "B", "C");

        playQueue.moveUp(1);
        playQueue.undo();
        assertPlaylistEquals(playQueue, 0, "A", "B", "C");
    }

    public void testOrder() throws IOException {
        PlayQueue playQueue = new PlayQueue();
        playQueue.addFiles(true, new TestMediaFile(2, "Artist A", "Album B"));
        playQueue.addFiles(true, new TestMediaFile(1, "Artist C", "Album C"));
        playQueue.addFiles(true, new TestMediaFile(3, "Artist B", "Album A"));
        playQueue.addFiles(true, new TestMediaFile(null, "Artist D", "Album D"));
        playQueue.setIndex(2);
        assertEquals("Error in sort.", new Integer(3), playQueue.getCurrentFile().getTrackNumber());

        // Order by track.
        playQueue.sort(SortOrder.TRACK);
        assertEquals("Error in sort().", null, playQueue.getFile(0).getTrackNumber());
        assertEquals("Error in sort().", new Integer(1), playQueue.getFile(1).getTrackNumber());
        assertEquals("Error in sort().", new Integer(2), playQueue.getFile(2).getTrackNumber());
        assertEquals("Error in sort().", new Integer(3), playQueue.getFile(3).getTrackNumber());
        assertEquals("Error in sort().", new Integer(3), playQueue.getCurrentFile().getTrackNumber());

        // Order by artist.
        playQueue.sort(SortOrder.ARTIST);
        assertEquals("Error in sort().", "Artist A", playQueue.getFile(0).getArtist());
        assertEquals("Error in sort().", "Artist B", playQueue.getFile(1).getArtist());
        assertEquals("Error in sort().", "Artist C", playQueue.getFile(2).getArtist());
        assertEquals("Error in sort().", "Artist D", playQueue.getFile(3).getArtist());
        assertEquals("Error in sort().", new Integer(3), playQueue.getCurrentFile().getTrackNumber());

        // Order by album.
        playQueue.sort(SortOrder.ALBUM);
        assertEquals("Error in sort().", "Album A", playQueue.getFile(0).getAlbumName());
        assertEquals("Error in sort().", "Album B", playQueue.getFile(1).getAlbumName());
        assertEquals("Error in sort().", "Album C", playQueue.getFile(2).getAlbumName());
        assertEquals("Error in sort().", "Album D", playQueue.getFile(3).getAlbumName());
        assertEquals("Error in sort().", new Integer(3), playQueue.getCurrentFile().getTrackNumber());
    }

    private void assertPlaylistEquals(PlayQueue playQueue, int index, String... songs) {
        assertEquals(songs.length, playQueue.size());
        for (int i = 0; i < songs.length; i++) {
            assertEquals(songs[i], playQueue.getFiles().get(i).getName());
        }

        if (index == -1) {
            assertNull(playQueue.getCurrentFile());
        } else {
            assertEquals(songs[index], playQueue.getCurrentFile().getName());
        }
    }

    private PlayQueue createPlaylist(int index, String... songs) throws Exception {
        PlayQueue playQueue = new PlayQueue();
        for (String song : songs) {
            playQueue.addFiles(true, new TestMediaFile(song));
        }
        playQueue.setIndex(index);
        return playQueue;
    }

    private static class TestMediaFile extends MediaFile {

        private String name;
        private Integer track;
        private String album;
        private String artist;

        TestMediaFile() {
        }

        TestMediaFile(String name) {
            this.name = name;
        }

        TestMediaFile(Integer track, String artist, String album) {
            this.track = track;
            this.album = album;
            this.artist = artist;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isFile() {
            return true;
        }

        @Override
        public Integer getTrackNumber() {
            return track;
        }

        @Override
        public String getArtist() {
            return artist;
        }

        @Override
        public String getAlbumName() {
            return album;
        }
        
        @Override
        public File getFile() {
            return new File(name);
        }

        @Override
        public boolean exists() {
            return true;
        }

        @Override
        public boolean isDirectory() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            return this == o;
        }
    }
}