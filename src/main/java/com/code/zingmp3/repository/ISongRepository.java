package com.code.zingmp3.repository;

import com.code.zingmp3.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISongRepository extends JpaRepository<Song, Long> {
    Page<Song> findAll(Pageable pageable);
}
