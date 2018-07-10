package com.sinnguyen.service;

import com.sinnguyen.entities.Song;
import com.sinnguyen.model.ResponseModel;

public interface SongService {
	ResponseModel add(Song song);
}
