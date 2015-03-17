package com.datazuul.webapps.datacenter.business;

import com.datazuul.webapps.datacenter.domain.music.MusicTrack;
import java.nio.file.Path;

/**
 * @author ralf
 */
public interface MusicService {

    MusicTrack createMusicTrack();

    Iterable<MusicTrack> getAll();

    Iterable<MusicTrack> importFromFilesystem(Path sourceDirectory);
}
