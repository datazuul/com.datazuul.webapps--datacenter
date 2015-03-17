package com.datazuul.webapps.datacenter.backend.impl.jpa.entities;

import com.datazuul.webapps.datacenter.domain.music.MusicTrack;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A music track (from a CD, LP)
 *
 * @author ralf
 */
@Entity
@Table(name = "musictracks")
public class MusicTrackJpaEntity implements MusicTrack {

    @Id
    @GeneratedValue
    private Long id;

    String artist;
    String album;
    String filepath;
    String genre;
    long length;
    String title;
    long trackNumber;
    String pubYear;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String getFilepath() {
        return filepath;
    }

    @Override
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public String getGenre() {
        return genre;
    }

    @Override
    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public long getTrackNumber() {
        return trackNumber;
    }

    @Override
    public void setTrackNumber(long trackNumber) {
        this.trackNumber = trackNumber;
    }

    @Override
    public String getAlbum() {
        return album;
    }

    @Override
    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public String getPubYear() {
        return pubYear;
    }

    @Override
    public void setPubYear(String pubYear) {
        this.pubYear = pubYear;
    }

    @Override
    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public long getLength() {
        return length;
    }
}
