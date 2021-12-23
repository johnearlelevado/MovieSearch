package com.example.challenge.ui;

import com.example.challenge.api.omdb.dto.Movie;
import com.example.challenge.api.omdb.dto.Search;

public interface ResultClickListener {
    void onResultItemClick(Search result);
}
