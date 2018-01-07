package daggerok.reservations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Document
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation implements Serializable {

  private static final long serialVersionUID = -221084014830948927L;

  @Id String id;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  LocalDate checkIn, checkOut;
}
