package com.intrasoft.moviemanager.mapper;

import com.intrasoft.moviemanager.dto.MovieDto;
import com.intrasoft.moviemanager.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    MovieDto movieToMovieDto(Movie movie);

    Movie movieDtoToMovie(MovieDto movieDto);

    List<Movie> movieDtoToMovie(List<MovieDto> movieDtoList);

    List<MovieDto> movieToMovieDto(List<Movie> movieList);
}
