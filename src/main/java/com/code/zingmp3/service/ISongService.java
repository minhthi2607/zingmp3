package com.code.zingmp3.service;

import com.code.zingmp3.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISongService extends IGenerateService<Song> {
    Page<Song> findAll(Pageable pageable);
}
