package com.code.zingmp3.service.impl;

import com.code.zingmp3.model.Song;
import com.code.zingmp3.repository.ISongRepository;
import com.code.zingmp3.service.IGenerateService;
import com.code.zingmp3.service.ISongService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SongService implements ISongService {

    private final ISongRepository songRepository;

    public SongService(ISongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public Iterable<Song> findAll() {
        return songRepository.findAll();
    }

    @Override
    public void save(Song song) {
        songRepository.save(song);
    }

    @Override
    public Optional<Song> findById(Long id) {
        return songRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        songRepository.deleteById(id);
    }

    @Override
    public Page<Song> findAll(Pageable pageable) {
        return songRepository.findAll(pageable);
    }
}
