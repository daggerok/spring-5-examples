Stuff - a REST, CQRS, EventSourcing, DCI, functional example
============================================================

Doing all the things mentioned in the title is hard, requires tons of libraries, and is too complicated for most devs
to get right. Or is it? This sample app is an attempt at exploring if it's possible to do all of that while still
keeping the code reasonably simple, and with minimal amount of libraries to help out.

What it is
----------
The domain I have chosen is that of a to-do list manager, hence the name "Stuff". Usecases have been ripped from various
GTD apps that already exist. It is simple enough that most people know how it works, yet complex enough that it can showcase
some interesting design issues.

Design
------
The app is packaged as a WAR and implemented with Restlet for the REST support. REST resources will call DCI functions
that encapsulate usecases, which in turn calls domain. Domain generates events which are distributed to all view models,
which then can be viewed through REST calls. And that's about it.

Installation notes
------------------
* Install Java 8 Lambda edition (that's the functional part)
* Add the following (with your correct path) to your .m2/settings.xml file for Maven to find Java 8 (do "View Raw" for easy copy&paste):
```
<settings>
  <profiles>
    <profile>
      <id>compiler</id>
        <properties>
          <JAVA_1_8_HOME>/Library/Java/JavaVirtualMachines/jdk1.8.0</JAVA_1_8_HOME>
        </properties>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>compiler</activeProfile>
  </activeProfiles>
</settings>
```
* Clone from GitHub
* Run "mvn install"
* Run the WAR in your favourite Container, or do "mvn jetty:run"
* View http://localhost:8080/stuff/

General notes
-------------
Apart from playing around with the above design and architectural patterns I'm also testing a bit of JavaScript and CSS.
The idea is to make the app not a completely server-side generated website, and also not a single-page-app, but somewhere in between,
with JavaScript and CSS pulling resources from a proper REST API into what you get on the root. This should make the whole thing
a human-viewable webapp AND a proper REST API at the same time.

I have no idea what I'm doing CSS-wise, so it looks like hell. Any pull requests to make it pretty will most likely be accepted :-)

REST notes
----------
Unfortunately the notion of what REST is has become quite muddled. By "REST" I mean it in the ROCA sense: http://roca-style.org/.
The general idea is that the resources exposed represent use cases in the application. This is different from the usual expose-the-domain
design, which I consider to be an anti-pattern. For REST clients, the rule of thumb should be that they do largely two things:
* Follow links
* Submit forms
Hypermedia will tell the client what links there are, what forms there are, and what URLs and methods to use for submission. No need to hardcode
any of that in the client. In essence, the REST API is therefore basically an ugly website.
