## private cps_project
wku cps 2231 final project - simple_flight_booking_system.
Aimed to offer the cheapest ticket price by comparing amongst different global-airline prices (dynamic) via URL scanning.
you can review the class-method tree in tree.md

## configure this project first if it does not run normally, i.e., displaying "module for javafx not found".

# Check your dependencies on Apache Maven and MySQL first. Check path-to-javafx as well. Usually in this format: javafx-sdk-<version>\bin

For windows 11 user, right click on the FlightBookingSystem\, Open terminal within. Try the following code:

```console
dir
mvn
```

You should see bin in the directory. If you cannot execute command "mvn", add <path-to-apache_maven\bin> to your system path in the environment varable settings, as fullfilled as possible. Let your PC remember that. 
Then run the following command to build the project:

```console
mvn compile
mvn clean
```

If no failure, continue to run:

```console
mvn javafx:run
```
A typical [WARNNING] will be called because of the poor arrangement of java-plugin version. About to be fixed.




# ~~Execute Main.java without maven (depricated).~~

```console
mkdir -p out
```
 collect all .java sources (works in Git Bash / WSL / macOS / Linux)
 
```console
find src -name "*.java" > sources.txt
```
 compile with JavaFX module path

```console
javac --module-path "C:\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml -d out @sources.txt
```

 run the app with the same JavaFX module path

```console
java --module-path "C:\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp out app.Main
```

 If you see:
```

WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::load has been called by com.sun.glass.utils.NativeLibLoader in module javafx.graphics (file:/C:/javafx-sdk-25.0.1/lib/javafx.graphics.jar)
WARNING: Use --enable-native-access=javafx.graphics to avoid a warning for callers in this module
WARNING: Restricted methods will be blocked in a future release unless native access is enabled

```
. Expaination:
JavaFX is loading its native (C++) libraries using System.load(), and since Java 21 tightened security around this, 
the JVM warns you about it.


If catch error: package does not exist
change dir: out -> bin
