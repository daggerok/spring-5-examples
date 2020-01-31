package daggerok.api;

import java.util.List;
import java.util.UUID;

public interface UserService {

  UUID saveUser(final User user);

  User getUser(final UUID uuid);

  List<User> getAllUsers();
}
