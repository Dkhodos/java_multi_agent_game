#!/bin/bash

SRC_DIR="src"
OUTPUT_FILE="concatenated.txt"

# Use find to locate all *.java files under SRC_DIR and concatenate them
find "$SRC_DIR" -name "*.java" -exec cat {} + > "$OUTPUT_FILE"

echo "All Java files from $SRC_DIR have been concatenated into $OUTPUT_FILE"
