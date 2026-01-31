package org.example.katalog.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@DiscriminatorValue("movie")
public class Movie extends Item {

  @NotBlank(message = "Director must not be empty")
  private String director;

  @Positive(message = "Duration must be a positive number")
  private int duration;

  public Movie() {}

  public Movie(String title, int year, String genre, String director, int duration) {
    super(title, year, genre);
    this.director = director;
    this.duration = duration;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }
}
