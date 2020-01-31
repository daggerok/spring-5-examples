package daggerok;

import daggerok.api.User;
import daggerok.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
public class RemoteServiceSpringApplication {

  @Service
  public static class UserServiceImpl implements UserService {

    final static Map<UUID, User> users = new ConcurrentHashMap<>();

    @Override public UUID saveUser(final User user) {

      log.info("saveUser execution: {}", user);

      final UUID uuid = Optional.ofNullable(user.getUuid())
                                .orElse(UUID.randomUUID());

      users.put(uuid, user.setUuid(uuid));
      return uuid;
    }

    @Override public User getUser(final UUID uuid) {
      log.info("getUser execution: {}", uuid);
      return users.getOrDefault(uuid, null);
    }

    @Override public List<User> getAllUsers() {
      return new ArrayList<>(users.values());
    }
  }

  @Bean public RmiServiceExporter rmiServiceExporter(final UserService userService) {
    final RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
    rmiServiceExporter.setServiceName("UserService");
    rmiServiceExporter.setService(userService);
    rmiServiceExporter.setServiceInterface(UserService.class);
    rmiServiceExporter.setRegistryPort(1199);
    return rmiServiceExporter;
  }

  public static void main(String[] args) {
    SpringApplication.run(RemoteServiceSpringApplication.class, args);
  }
}
