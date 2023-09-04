#!/bin/bash

# Directories
SRC_DIR="src"
DIST_DIR="dist"

# Create the dist directory if it doesn't exist
mkdir -p "$DIST_DIR"

# Recursively find and compile all .java files under src directory
find "$SRC_DIR" -name "*.java" > sources.txt
javac -cp "$DIST_DIR" -d "$DIST_DIR" @sources.txt

# Check if the compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful!"

    # Run the Main class and pass all arguments to it
    java -cp "$DIST_DIR" Main "$@"
else
    echo "Compilation failed!"
fi

# Clean up the sources.txt file
rm sources.txt
