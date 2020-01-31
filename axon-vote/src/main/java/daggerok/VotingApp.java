package daggerok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VotingApp {

  /*

  mongo
  use axon
  show collections
  db.events.find().pretty()
  db.snapshots.find().pretty()
  db.dropDatabase()

   */

  public static void main(String[] args) {
    SpringApplication.run(VotingApp.class, args);
  }
}
