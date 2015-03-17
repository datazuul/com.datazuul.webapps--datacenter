package com.datazuul.webapps.datacenter.backend.api;

import com.datazuul.webapps.datacenter.domain.music.MusicTrack;
import java.io.Serializable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author ralf
 * @param <T>
 * @param <ID>
 */
public interface MusicRepository<T extends MusicTrack, ID extends Serializable> extends PagingAndSortingRepository<T, ID>, FilterableRepository<T> {

    MusicTrack createMusicTrack();
}
