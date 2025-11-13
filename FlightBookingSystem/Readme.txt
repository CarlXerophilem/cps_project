#configure this project first if it does not run normally.
#use path-to-javafx to define.
/cmd/

#testLines
...
# From the project root (where src/ is located). Adjust the JavaFX SDK path if different.

mkdir -p out

# collect all .java sources (works in Git Bash / WSL / macOS / Linux)

find src -name "*.java" > sources.txt

# compile with JavaFX module path

javac --module-path "C:\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml -d out @sources.txt

# run the app with the same JavaFX module path

java --module-path "C:\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp out app.Main



/............................../
WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::load has been called by com.sun.glass.utils.NativeLibLoader in module javafx.graphics (file:/C:/javafx-sdk-25.0.1/lib/javafx.graphics.jar)
WARNING: Use --enable-native-access=javafx.graphics to avoid a warning for callers in this module
WARNING: Restricted methods will be blocked in a future release unless native access is enabled
/............................./
#Expaination:
JavaFX is loading its native (C++) libraries using System.load(), and since Java 21 tightened security around this, 
the JVM warns you about it.


#catch @error: package does not exist
dir: out -> bin


