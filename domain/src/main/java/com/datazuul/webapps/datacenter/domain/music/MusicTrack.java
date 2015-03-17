package com.datazuul.webapps.datacenter.domain.music;

/**
 * A music track on a CD, LP.
 *
 * @author ralf
 */
public interface MusicTrack {

    String getAlbum();

    String getArtist();

    String getFilepath();

    String getGenre();

    Long getId();

    long getLength();

    String getPubYear();

    String getTitle();

    long getTrackNumber();

    void setAlbum(String album);

    void setArtist(String artist);

    void setFilepath(String filepath);

    void setGenre(String genre);

    void setId(Long id);

    void setLength(long length);

    void setPubYear(String pubYear);

    void setTitle(String title);

    void setTrackNumber(long trackNumber);

}
