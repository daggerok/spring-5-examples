package daggerok.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface RemoteUserService extends Remote {

  UUID saveUser(final User user) throws RemoteException;

  User getUser(final UUID uuid) throws RemoteException;

  List<User> getAllUsers() throws RemoteException;
}
