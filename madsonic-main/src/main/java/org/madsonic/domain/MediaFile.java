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

import org.madsonic.util.FileUtil;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Date;
 
/**
 * A media file (audio, video or directory) with an assortment of its meta data.
 *
 * @author Sindre Mehus
 * @version $Id$
 */
public class MediaFile {

    private int id;
    private String path;
    private String folder;
    private MediaType mediaType;
    private boolean mediaTypeOverride;
    private String format;
    private String data;
    private String title;
    private String albumName;
    private String albumSetName;
    private String artist;
    private String albumArtist;
    private Integer discNumber;
    private Integer trackNumber;
    private Integer year;
    private String genre;
    private String mood;
    private Integer bitRate;
    private boolean variableBitRate;
    private Integer durationSeconds;
    private Long fileSize;
    private Integer width;
    private Integer height;
    private String coverArtPath;
    private String parentPath;
    private int playCount;
    private Date lastPlayed;
    private String comment;
    private Date created;
    private Date changed;
    private Date lastScanned;
    private Date firstScanned;
    private Date starredDate;
    private Date childrenLastUpdated;
    private boolean present;
    private int version;
    private int rank;
    
    private String artistPath;
    private boolean newAdded;

    public MediaFile(int id, String path, String folder, MediaType mediaType, boolean override, String format, String data, String title,
                     String albumName, String albumSetName, String artist, String albumArtist, Integer discNumber, Integer trackNumber, Integer year, String genre, String mood, Integer bitRate,
                     boolean variableBitRate, Integer durationSeconds, Long fileSize, Integer width, Integer height, String coverArtPath,
                     String parentPath, int playCount, Date lastPlayed, String comment, Date created, Date changed, Date lastScanned, Date firstScanned,
                     Date childrenLastUpdated, boolean present, int version, int rank) {
        this.id = id;
        this.path = path;
        this.folder = folder;
        this.mediaType = mediaType;
        this.mediaTypeOverride = override;        
        this.format = format;
        this.data = data;
        this.title = title;
        this.albumName = albumName;
        this.albumSetName = albumSetName;
        this.artist = artist;
        this.albumArtist = albumArtist;
        this.discNumber = discNumber;
        this.trackNumber = trackNumber;
        this.year = year;
        this.genre = genre;
        this.mood = mood;
        this.bitRate = bitRate;
        this.variableBitRate = variableBitRate;
        this.durationSeconds = durationSeconds;
        this.fileSize = fileSize;
        this.width = width;
        this.height = height;
        this.coverArtPath = coverArtPath;
        this.parentPath = parentPath;
        this.playCount = playCount;
        this.lastPlayed = lastPlayed;
        this.comment = comment;
        this.created = created;
        this.changed = changed;
        this.lastScanned = lastScanned;
        this.firstScanned = firstScanned;
        this.childrenLastUpdated = childrenLastUpdated;
        this.present = present;
        this.version = version;
        this.rank = rank;
    }

    public MediaFile() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public File getFile() {
        // TODO: Optimize
        return new File(path);
    }

    public boolean exists() {
        return FileUtil.exists(getFile());
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isUrl() {
        return mediaType == MediaType.URL;
    }
    
    public boolean isVideo() {
        return mediaType == MediaType.VIDEO || mediaType == MediaType.URL;
    }

    public boolean isAudio() {
        return mediaType == MediaType.MUSIC || mediaType == MediaType.AUDIOBOOK || mediaType == MediaType.PODCAST;
    }
	
	public boolean isSingleArtist() {
	    return mediaType == MediaType.ARTIST;
	 }

	 public boolean isMultiArtist() {
	    return mediaType == MediaType.MULTIARTIST;
	 }
	
    public boolean isAlbumSet() {
        return mediaType == MediaType.ALBUMSET;
    }	

    public boolean isVideoSet() {
        return mediaType == MediaType.VIDEOSET;
    }	    
    
    public boolean isAlbum() {
        return mediaType == MediaType.ALBUM || mediaType == MediaType.ALBUMSET;
    }
	
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isDirectory() {
 //     return !isFile();
        return mediaType == MediaType.DIRECTORY || mediaType == MediaType.ALBUMSET || mediaType == MediaType.ALBUM || mediaType == MediaType.VIDEOSET || mediaType == MediaType.ARTIST || mediaType == MediaType.MULTIARTIST;
    }

    public boolean isFile() {
 //   return mediaType != MediaType.DIRECTORY && mediaType != MediaType.ALBUM;
      return mediaType != MediaType.DIRECTORY && mediaType != MediaType.ALBUM && mediaType != MediaType.VIDEOSET && mediaType != MediaType.ALBUMSET && mediaType != MediaType.ARTIST && mediaType != MediaType.MULTIARTIST;
    }

	
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String album) {
        this.albumName = album;
    }

    public String getAlbumSetName() {
        return albumSetName;
    }

    public void setAlbumSetName(String album) {
        this.albumSetName = album;
    }
    
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getName() {
        if (isFile()) {
            return title != null ? title : FilenameUtils.getBaseName(path);
        }

        return FilenameUtils.getName(path);
    }

    public Integer getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(Integer discNumber) {
        this.discNumber = discNumber;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getBitRate() {
        return bitRate;
    }

    public void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }

    public boolean isVariableBitRate() {
        return variableBitRate;
    }

    public void setVariableBitRate(boolean variableBitRate) {
        this.variableBitRate = variableBitRate;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getDurationString() {
        if (durationSeconds == null) {
            return null;
        }

        StringBuilder result = new StringBuilder(8);

        int seconds = durationSeconds;

        int hours = seconds / 3600;
        seconds -= hours * 3600;

        int minutes = seconds / 60;
        seconds -= minutes * 60;

        if (hours > 0) {
            result.append(hours).append(':');
            if (minutes < 10) {
                result.append('0');
            }
        }

        result.append(minutes).append(':');
        if (seconds < 10) {
            result.append('0');
        }
        result.append(seconds);

        return result.toString();
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getCoverArtPath() {
        return coverArtPath;
    }

    public void setCoverArtPath(String coverArtPath) {
        this.coverArtPath = coverArtPath;
    }


    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public File getParentFile() {
        return getFile().getParentFile();
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public Date getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getChanged() {
        return changed;
    }

    public void setChanged(Date changed) {
        this.changed = changed;
    }

    public Date getLastScanned() {
        return lastScanned;
    }

    public void setLastScanned(Date lastScanned) {
        this.lastScanned = lastScanned;
    }

    public Date getStarredDate() {
        return starredDate;
    }

    public void setStarredDate(Date starredDate) {
        this.starredDate = starredDate;
    }

    /**
     * Returns when the children was last updated in the database.
     */
    public Date getChildrenLastUpdated() {
        return childrenLastUpdated;
    }

    public void setChildrenLastUpdated(Date childrenLastUpdated) {
        this.childrenLastUpdated = childrenLastUpdated;
    }

    public boolean isPresent() {
        return present;
    }

    public boolean isMediaTypeOverride() {
		if (mediaTypeOverride)
			{	return mediaTypeOverride; }
		else
			{	return false; }
    }
		
	public void setMediaTypeOverride(boolean mediaTypeOverride) {
		this.mediaTypeOverride = mediaTypeOverride;
	}

    public void setPresent(boolean present) {
        this.present = present;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MediaFile && ((MediaFile) o).path.equals(path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    public File getCoverArtFile() {
        // TODO: Optimize
        return coverArtPath == null ? null : new File(coverArtPath);
    }

    @Override
    public String toString() {
        return getName();
    }

	public boolean isNewAdded() {
		return newAdded;
	}

	public void setNewAdded(boolean b) {
		this.newAdded = b;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public Date getFirstScanned() {
		return firstScanned;
	}

	public void setFirstScanned(Date firstScanned) {
		this.firstScanned = firstScanned;
	}

	public String getArtistPath() {
		return artistPath;
	}

	public void setArtistPath(String artistPath) {
		this.artistPath = artistPath;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public static enum MediaType {
		
        DIRECTORY,   // folder
        MUSIC,		 // music files
        ALBUM,	     // artist album
        ALBUMSET,    // artist albumset
        ARTIST,		 // artist folder
		MULTIARTIST, // various artists
        AUDIOBOOK,   // audiobook files
        PODCAST,	 // podcast files
        VIDEO,		 // video files
        VIDEOSET,	 // videoset like folder / imdb link
        IMAGE,		 // image files
        IMAGESET,	 // imageset like holiday, private
        OTHER,		 // other files
        URL 		 // URL like youtube link
    }
}